package com.itavgur.omul.schedule.service

import com.itavgur.omul.schedule.dao.timeslot.TimeSlotDao
import com.itavgur.omul.schedule.domain.TimeSlot
import com.itavgur.omul.schedule.exception.TimeSlotNotFoundException
import com.itavgur.omul.schedule.web.dto.ManageTimeSlotRequest
import com.itavgur.omul.schedule.web.dto.TimeSlotResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ManageService(
    @Autowired private val timeSlotDao: TimeSlotDao
) {

    fun getSlots(
        doctorId: Int?,
        customerId: Int?,
        dateFrom: LocalDateTime?,
        dateTo: LocalDateTime?
    ): List<TimeSlotResponse> {
        return timeSlotDao.getTimeSlotsFiltered(doctorId, customerId, dateFrom, dateTo)
            .map { TimeSlotResponse.from(it) }
    }

    fun getSlotById(slotId: Long): TimeSlotResponse {
        timeSlotDao.getTimeSlotById(slotId)?.let {
            return TimeSlotResponse.from(it)

        }
        throw TimeSlotNotFoundException("timeslot with id $slotId is absent")
    }

    fun manageTimeSlot(request: List<ManageTimeSlotRequest>): Int {

        if (request.isEmpty()) return 0

        var countRecordDone = 0
        val newTimeSlots: MutableList<TimeSlot> = mutableListOf()
        val updateTimeSlots: MutableList<TimeSlot> = mutableListOf()

        request.forEach {

            if (it.slotId == null) {
                newTimeSlots.add(
                    TimeSlot(
                        customerId = it.customerId,
                        doctorId = it.doctorId,
                        dateTimeStart = it.dateTimeStart,
                        dateTimeEnd = it.dateTimeEnd,
                        status = it.status
                    )
                )
            } else {
                updateTimeSlots.add(
                    TimeSlot(
                        slotId = it.slotId,
                        customerId = it.customerId,
                        doctorId = it.doctorId,
                        dateTimeStart = it.dateTimeStart,
                        dateTimeEnd = it.dateTimeEnd,
                        status = it.status
                    )
                )
            }
        }

        countRecordDone += timeSlotDao.addTimeSlots(newTimeSlots)
        countRecordDone += timeSlotDao.updateTimeSlots(updateTimeSlots)

        return countRecordDone
    }
}