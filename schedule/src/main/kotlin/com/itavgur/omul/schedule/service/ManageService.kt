package com.itavgur.omul.schedule.service

import com.itavgur.omul.schedule.dao.timeslot.TimeSlotDao
import com.itavgur.omul.schedule.dao.web.dto.ManageTimeSlotRequest
import com.itavgur.omul.schedule.domain.TimeSlot
import com.itavgur.omul.schedule.exception.TimeSlotNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ManageService(
    private val timeSlotDao: TimeSlotDao
) {

    fun getSlots(
        doctorId: Int?,
        customerId: Int?,
        dateFrom: LocalDateTime?,
        dateTo: LocalDateTime?
    ): List<TimeSlot> {
        return timeSlotDao.getTimeSlotsFiltered(doctorId, customerId, dateFrom, dateTo)
    }

    fun getSlotById(slotId: Long): TimeSlot {
        timeSlotDao.getTimeSlotById(slotId)?.let {
            return it
        }

        throw TimeSlotNotFoundException.throwWithBaseMessage(slotId)
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