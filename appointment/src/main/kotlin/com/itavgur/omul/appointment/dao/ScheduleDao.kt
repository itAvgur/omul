package com.itavgur.omul.appointment.dao

interface ScheduleDao {

    fun checkTimeSlotIsFree(timeSlotId: Long): Boolean

    fun reserveTimeSlot(timeSlotId: Long, customerId: Int): Boolean

    fun releaseTimeSlot(timeSlotId: Long): Boolean

}