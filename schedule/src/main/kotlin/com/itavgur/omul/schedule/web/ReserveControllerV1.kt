package com.itavgur.omul.schedule.web

import com.itavgur.omul.schedule.domain.TimeSlotStatus
import com.itavgur.omul.schedule.service.ReserveService
import com.itavgur.omul.schedule.web.dto.CalendarTimeSlots
import com.itavgur.omul.schedule.web.dto.ReleaseTimeSlotRequest
import com.itavgur.omul.schedule.web.dto.ReserveTimeSlotRequest
import com.itavgur.omul.schedule.web.dto.TimeSlotResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/reserve")
class ReserveControllerV1(
    @Autowired
    val reserveService: ReserveService
) {

    @Operation(summary = "Get free time slots", tags = ["reserveSlots"])
    @GetMapping("/free")
    fun getFreeTimeSlotsV1(
        @RequestParam(name = "dateFrom") dateFrom: LocalDateTime? = null,
        @RequestParam(name = "dateTo") dateTo: LocalDateTime? = null,
        @RequestParam(name = "doctorId") doctorId: Int? = null,
    ): CalendarTimeSlots = reserveService.getFreeSlots(dateFrom, dateTo, doctorId)

    @Operation(summary = "Get time slot by ID", tags = ["customerInfo"])
    @GetMapping("/{slotId}")
    fun getReservedTimeSlotV1(
        @PathVariable(name = "slotId", required = true) slotId: Long,
        @RequestParam(name = "status") status: TimeSlotStatus? = null,
    ): TimeSlotResponse = reserveService.getTimeSlotById(slotId, status)

    @Operation(summary = "Reserve time slot", tags = ["reserveSlots"])
    @PostMapping
    fun reserveFreeTimeSlotV1(
        @Valid @RequestBody request: ReserveTimeSlotRequest,
    ): TimeSlotResponse {
        return reserveService.reserveTimeSlot(request)
    }

    @Operation(summary = "Release time slot", tags = ["reserveSlots"])
    @PatchMapping("/release")
    fun releaseFreeTimeSlotV1(
        @Valid @RequestBody request: ReleaseTimeSlotRequest,
    ): TimeSlotResponse {
        return reserveService.releaseTimeSlot(request)
    }

}