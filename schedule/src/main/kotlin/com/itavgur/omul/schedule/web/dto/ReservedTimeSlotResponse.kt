package com.itavgur.omul.schedule.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.itavgur.omul.schedule.domain.TimeSlot
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import java.io.Serializable
import java.time.LocalDateTime

data class ReservedTimeSlotResponse(

    val slotId: Long,
    @JsonInclude(NON_NULL)
    val customerId: Int? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTimeStart: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTimeEnd: LocalDateTime,
    val status: TimeSlotStatus,
    val doctorId: Int,
    @JsonInclude(NON_NULL)
    var doctorFullName: String? = null,
    @JsonInclude(NON_NULL)
    var doctorQualification: String? = null


) : Serializable {
    companion object {
        fun from(timeSlot: TimeSlot): ReservedTimeSlotResponse {
            return ReservedTimeSlotResponse(
                slotId = timeSlot.slotId!!,
                customerId = timeSlot.customerId,
                dateTimeStart = timeSlot.dateTimeStart,
                dateTimeEnd = timeSlot.dateTimeEnd,
                status = timeSlot.status,
                doctorId = timeSlot.doctorId
            )
        }
    }
}