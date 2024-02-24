package com.itavgur.omul.schedule.domain

import java.time.LocalDateTime

data class TimeSlot(
    var slotId: Long? = null,
    var customerId: Int? = null,
    val doctorId: Int,
    val dateTimeStart: LocalDateTime,
    val dateTimeEnd: LocalDateTime,
    var status: TimeSlotStatus
) : Cloneable {

    public override fun clone(): TimeSlot = TimeSlot(
        slotId = slotId,
        customerId = customerId,
        doctorId = doctorId,
        dateTimeStart = dateTimeStart,
        dateTimeEnd = dateTimeEnd,
        status = status
    )
}