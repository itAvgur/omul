package com.itavgur.omul.schedule.service

import com.itavgur.omul.schedule.auth.JwtService
import com.itavgur.omul.schedule.auth.JwtValidated
import com.itavgur.omul.schedule.dao.timeslot.TimeSlotDao
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import com.itavgur.omul.schedule.exception.InvalidRequestException
import com.itavgur.omul.schedule.exception.TimeSlotNotFoundException
import com.itavgur.omul.schedule.util.logger
import com.itavgur.omul.schedule.web.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReserveService(
    @Autowired private val timeSlotDao: TimeSlotDao,
    @Autowired private val personnelService: PersonnelService,
    @Autowired private val jwtService: JwtService
) {

    companion object {
        val LOG by logger()
    }

    fun getFreeSlots(dateFrom: LocalDateTime?, dateTo: LocalDateTime?, doctorId: Int?): CalendarTimeSlots {
        val freeTimeSlots = timeSlotDao.getTimeSlotsFiltered(
            doctorId = doctorId, dateFrom = dateFrom, dateTo = dateTo, status = TimeSlotStatus.FREE
        ).map {
            FreeTimeSlotInfo.from(it)
        }

        freeTimeSlots.forEach {
            enrichWithPersonnelInfo(it)
        }
        return CalendarTimeSlots(doctorId = doctorId, dateFrom = dateFrom, dateTo = dateTo, timeSlots = freeTimeSlots)
    }

    private fun enrichWithPersonnelInfo(timeSlot: FreeTimeSlotInfo) {
        val personnelInfo = personnelService.getPersonnelInfo(timeSlot.doctorId)
        timeSlot.doctorFullName = personnelInfo.fullName
        timeSlot.doctorQualification = personnelInfo.qualification
    }

    @JwtValidated
    fun getTimeSlotById(slotId: Long, status: TimeSlotStatus?): TimeSlotResponse {
        timeSlotDao.getTimeSlotById(slotId, status)?.let {

            val result = TimeSlotResponse.from(it)
            enrichWithPersonnelInfo(result)

            if (result.status != TimeSlotStatus.FREE) {
                jwtService.validateIdWithJwt(result.customerId)
            }

            return result
        }
        throw TimeSlotNotFoundException("timeslot with id $slotId is absent")
    }

    private fun enrichWithPersonnelInfo(timeSlot: TimeSlotResponse) {
        timeSlot.doctorId ?: return

        val personnelInfo = personnelService.getPersonnelInfo(timeSlot.doctorId)
        timeSlot.doctorFullName = personnelInfo.fullName
        timeSlot.doctorQualification = personnelInfo.qualification
    }

    @JwtValidated
    fun reserveTimeSlot(request: ReserveTimeSlotRequest): TimeSlotResponse {

        timeSlotDao.getTimeSlotById(request.slotId)?.let {

            jwtService.validateIdWithJwt(request.customerId)

            if (it.status != TimeSlotStatus.FREE) throw InvalidRequestException("timeslot with id ${request.slotId} isn't free")

            LOG.debug("reserve slot ${request.slotId} for customer ${request.customerId}")

            it.customerId = request.customerId
            it.status = TimeSlotStatus.RESERVED

            timeSlotDao.patchTimeSlot(request.slotId, it.customerId!!, it.status)

            val result = TimeSlotResponse.from(it)
            enrichWithPersonnelInfo(result)

            return result
        }
        throw TimeSlotNotFoundException("timeslot with id ${request.slotId} is absent")
    }

    @JwtValidated
    fun releaseTimeSlot(request: ReleaseTimeSlotRequest): TimeSlotResponse {

        LOG.debug("release slot ${request.slotId}")

        timeSlotDao.getTimeSlotById(request.slotId)?.let {

            jwtService.validateIdWithJwt(it.customerId)

            if (it.status != TimeSlotStatus.RESERVED
                && it.status != TimeSlotStatus.CONFIRMED
            ) {
                throw InvalidRequestException("timeslot with id ${request.slotId} isn't reserved")
            }
            it.customerId = null
            it.status = TimeSlotStatus.FREE

            timeSlotDao.patchTimeSlot(request.slotId, null, it.status)

            val result = TimeSlotResponse.from(it)
            enrichWithPersonnelInfo(result)

            return result
        }
        throw TimeSlotNotFoundException("timeslot with id ${request.slotId} is absent")
    }

}