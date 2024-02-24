package com.itavgur.omul.schedule.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.itavgur.omul.schedule.domain.TimeSlot
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import java.io.Serializable
import java.time.LocalDateTime

data class TimeSlotResponse(

    val slotId: Long,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val customerId: Int?,
    val doctorId: Int?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTimeStart: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTimeEnd: LocalDateTime?,
    val status: TimeSlotStatus,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var doctorFullName: String? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var doctorQualification: String? = null

) : Serializable {
    companion object {
        fun from(timeSlot: TimeSlot): TimeSlotResponse {
            return TimeSlotResponse(
                slotId = timeSlot.slotId!!,
                customerId = timeSlot.customerId,
                doctorId = timeSlot.doctorId,
                dateTimeStart = timeSlot.dateTimeStart,
                dateTimeEnd = timeSlot.dateTimeEnd,
                status = timeSlot.status
            )

        }
    }
}