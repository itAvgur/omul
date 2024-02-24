package com.itavgur.omul.schedule.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.itavgur.omul.schedule.domain.TimeSlot
import java.io.Serializable
import java.time.LocalDateTime

data class CalendarTimeSlots(

    @JsonInclude(Include.NON_EMPTY)
    val doctorId: Int?,
    @JsonInclude(Include.NON_EMPTY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateFrom: LocalDateTime?,
    @JsonInclude(Include.NON_EMPTY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTo: LocalDateTime?,
    val timeSlots: List<FreeTimeSlotInfo>

) : Serializable

data class FreeTimeSlotInfo(

    val slotId: Long,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTimeStart: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val dateTimeEnd: LocalDateTime,
    val doctorId: Int,
    var doctorFullName: String? = null,
    var doctorQualification: String? = null

) : Serializable {
    companion object {
        fun from(timeSlot: TimeSlot): FreeTimeSlotInfo {
            return FreeTimeSlotInfo(
                slotId = timeSlot.slotId!!,
                dateTimeStart = timeSlot.dateTimeStart,
                dateTimeEnd = timeSlot.dateTimeEnd,
                doctorId = timeSlot.doctorId
            )

        }
    }
}