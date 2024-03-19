package com.itavgur.omul.schedule.service

import com.itavgur.omul.schedule.auth.JwtService
import com.itavgur.omul.schedule.auth.JwtValidated
import com.itavgur.omul.schedule.dao.timeslot.TimeSlotDao
import com.itavgur.omul.schedule.dao.web.dto.FreeTimeSlotInfo
import com.itavgur.omul.schedule.dao.web.dto.ReleaseTimeSlotRequest
import com.itavgur.omul.schedule.dao.web.dto.ReserveTimeSlotRequest
import com.itavgur.omul.schedule.domain.TimeSlot
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import com.itavgur.omul.schedule.exception.InvalidRequestException
import com.itavgur.omul.schedule.exception.TimeSlotNotFoundException
import com.itavgur.omul.schedule.util.logger
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReserveService(
    private val timeSlotDao: TimeSlotDao,
    private val personnelService: PersonnelService,
    private val jwtService: JwtService
) {

    companion object {
        val LOG by logger()
    }

    fun getFreeSlots(dateFrom: LocalDateTime?, dateTo: LocalDateTime?, doctorId: Int?): List<TimeSlot> =
        timeSlotDao.getTimeSlotsFiltered(
            doctorId = doctorId, dateFrom = dateFrom, dateTo = dateTo, status = TimeSlotStatus.FREE
        )

    private fun enrichWithPersonnelInfo(timeSlot: FreeTimeSlotInfo) {
        val personnelInfo = personnelService.getPersonnelInfo(timeSlot.doctorId)
        timeSlot.doctorFullName = personnelInfo.fullName
        timeSlot.doctorQualification = personnelInfo.qualification
    }

    @JwtValidated
    fun getTimeSlotById(slotId: Long, status: TimeSlotStatus?): TimeSlot {
        timeSlotDao.getTimeSlotById(slotId, status)?.let {

            if (it.status != TimeSlotStatus.FREE) {
                jwtService.validateIdWithJwt(it.customerId)
            }
            return it
        }
        throw TimeSlotNotFoundException.throwWithBaseMessage(slotId)
    }

    @JwtValidated
    fun reserveTimeSlot(request: ReserveTimeSlotRequest): TimeSlot {

        timeSlotDao.getTimeSlotById(request.slotId)?.let {

            jwtService.validateIdWithJwt(request.customerId)

            if (it.status != TimeSlotStatus.FREE) throw InvalidRequestException("timeslot with id ${request.slotId} isn't free")

            LOG.debug("reserve slot ${request.slotId} for customer ${request.customerId}")

            it.customerId = request.customerId
            it.status = TimeSlotStatus.RESERVED

            timeSlotDao.patchTimeSlot(request.slotId, it.customerId!!, it.status)

            return it
        }
        throw TimeSlotNotFoundException.throwWithBaseMessage(request.slotId)
    }

    @JwtValidated
    fun releaseTimeSlot(request: ReleaseTimeSlotRequest): TimeSlot {

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

            return it
        }
        throw TimeSlotNotFoundException.throwWithBaseMessage(request.slotId)
    }

}