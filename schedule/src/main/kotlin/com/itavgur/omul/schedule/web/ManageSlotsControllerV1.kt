package com.itavgur.omul.schedule.web

import com.itavgur.omul.schedule.service.ManageService
import com.itavgur.omul.schedule.web.dto.ManageTimeSlotRequest
import com.itavgur.omul.schedule.web.dto.TimeSlotResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/manage")
class ManageSlotsControllerV1(
    @Autowired
    val manageService: ManageService
) {

    @Operation(summary = "Get all time slots", tags = ["manageSlots"])
    @GetMapping("/slot")
    fun getAllTimeSlotsV1(
        @RequestParam(name = "doctorId") doctorId: Int?,
        @RequestParam(name = "customerId") customerId: Int?,
        @RequestParam(name = "dateFrom") dateFrom: LocalDateTime?,
        @RequestParam(name = "dateTo") dateTo: LocalDateTime?,
    ): List<TimeSlotResponse> = manageService.getSlots(doctorId, customerId, dateFrom, dateTo)

    @Operation(summary = "Get time slot by Id", tags = ["manageSlots"])
    @GetMapping("/slot/{slotId}")
    fun getTimeSlotV1(
        @PathVariable(name = "slotId", required = true) slotId: Long,
    ): TimeSlotResponse = manageService.getSlotById(slotId)

    @Operation(summary = "Add/change times slot information", tags = ["manageSlots"])
    @PutMapping("/slot")
    fun addTimeSlotsInfoV1(
        @Valid @RequestBody request: List<ManageTimeSlotRequest>,
    ): ResponseEntity<Any> {
        val recordsDone = manageService.manageTimeSlot(request)
        return ResponseEntity(recordsDone, HttpStatus.OK)
    }
}