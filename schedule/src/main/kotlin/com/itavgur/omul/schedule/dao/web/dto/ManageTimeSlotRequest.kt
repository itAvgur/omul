package com.itavgur.omul.schedule.dao.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import java.time.LocalDateTime

data class ManageTimeSlotRequest(

    val slotId: Long? = null,
    val customerId: Int? = null,
    val doctorId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTimeStart: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTimeEnd: LocalDateTime,
    val status: TimeSlotStatus

)