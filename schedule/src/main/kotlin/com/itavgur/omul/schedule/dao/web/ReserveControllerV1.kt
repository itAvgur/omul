package com.itavgur.omul.schedule.dao.web

import com.itavgur.omul.schedule.dao.web.dto.*
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import com.itavgur.omul.schedule.service.PersonnelService
import com.itavgur.omul.schedule.service.ReserveService
import com.itavgur.omul.schedule.util.ModelMapperUtils
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/reserve")
class ReserveControllerV1(
    val reserveService: ReserveService,
    val personnelService: PersonnelService
) {

    @Operation(summary = "Get free time slots", tags = ["reserveSlots"])
    @GetMapping("/free")
    fun getFreeTimeSlotsV1(
        @RequestParam(name = "dateFrom") dateFrom: LocalDateTime? = null,
        @RequestParam(name = "dateTo") dateTo: LocalDateTime? = null,
        @RequestParam(name = "doctorId") doctorId: Int? = null,
    ): CalendarTimeSlots {
        val freeSlotsInfo = ModelMapperUtils.mapList<FreeTimeSlotInfo>(
            reserveService.getFreeSlots(dateFrom, dateTo, doctorId)
        )
        freeSlotsInfo.forEach {
            enrichWithPersonnelInfo(it)
        }

        return CalendarTimeSlots(
            doctorId = doctorId, dateFrom = dateFrom, dateTo = dateTo,
            timeSlots = freeSlotsInfo
        )
    }

    @Operation(summary = "Get time slot by ID", tags = ["customerInfo"])
    @GetMapping("/{slotId}")
    fun getReservedTimeSlotV1(
        @PathVariable(name = "slotId", required = true) slotId: Long,
        @RequestParam(name = "status") status: TimeSlotStatus? = null,
    ): TimeSlotResponse {

        val timeSlot = reserveService.getTimeSlotById(slotId, status)
        val result = ModelMapperUtils.map<TimeSlotResponse>(timeSlot)
        enrichWithPersonnelInfo(result)

        return result

    }

    @Operation(summary = "Reserve time slot", tags = ["reserveSlots"])
    @PostMapping
    fun reserveFreeTimeSlotV1(
        @Valid @RequestBody request: ReserveTimeSlotRequest,
    ): TimeSlotResponse {

        val timeSlot = reserveService.reserveTimeSlot(request)

        val result = ModelMapperUtils.map<TimeSlotResponse>(timeSlot)
        enrichWithPersonnelInfo(result)

        return result
    }

    @Operation(summary = "Release time slot", tags = ["reserveSlots"])
    @PatchMapping("/release")
    fun releaseFreeTimeSlotV1(
        @Valid @RequestBody request: ReleaseTimeSlotRequest,
    ): TimeSlotResponse {
        val timeSlot = reserveService.releaseTimeSlot(request)

        val result = ModelMapperUtils.map<TimeSlotResponse>(timeSlot)
        enrichWithPersonnelInfo(result)

        return result
    }

    private fun enrichWithPersonnelInfo(timeSlot: TimeSlotResponse) {
        timeSlot.doctorId ?: return

        val personnelInfo = personnelService.getPersonnelInfo(timeSlot.doctorId)
        timeSlot.doctorFullName = personnelInfo.fullName
        timeSlot.doctorQualification = personnelInfo.qualification
    }

    private fun enrichWithPersonnelInfo(timeSlot: FreeTimeSlotInfo) {

        val personnelInfo = personnelService.getPersonnelInfo(timeSlot.doctorId)
        timeSlot.doctorFullName = personnelInfo.fullName
        timeSlot.doctorQualification = personnelInfo.qualification
    }

}