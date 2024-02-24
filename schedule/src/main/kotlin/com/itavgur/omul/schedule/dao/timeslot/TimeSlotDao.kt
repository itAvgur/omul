package com.itavgur.omul.schedule.dao.timeslot

import com.itavgur.omul.schedule.domain.TimeSlot
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import java.time.LocalDateTime

interface TimeSlotDao {

    fun getTimeSlotById(slotId: Long, status: TimeSlotStatus? = null): TimeSlot?

    fun getTimeSlotsFiltered(
        doctorId: Int? = null,
        customerId: Int? = null,
        dateFrom: LocalDateTime? = null,
        dateTo: LocalDateTime? = null,
        status: TimeSlotStatus? = null
    ): List<TimeSlot>

    fun addTimeSlots(timeSlots: List<TimeSlot>): Int

    fun updateTimeSlots(timeSlots: List<TimeSlot>): Int

    fun patchTimeSlot(timeSlotId: Long, customerId: Int?, status: TimeSlotStatus): Int

}