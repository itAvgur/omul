package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.auth.JwtService
import com.itavgur.omul.appointment.config.TransportScheduleRestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestClient

@Primary
@Repository
@ConditionalOnBean(TransportScheduleRestConfig::class)
class ScheduleDaoRest(
    @Qualifier("scheduleRestClient") @Autowired var restClient: RestClient,
    @Autowired private var jwtService: JwtService
) : ScheduleDao {

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val GET_TIMESLOT_BY_ID = "/v1/reserve"
        const val RESERVE_TIME_SLOT = "/v1/reserve"
        const val RELEASE_TIME_SLOT = "/v1/reserve/release"
    }

    override fun checkTimeSlotIsFree(timeSlotId: Long): Boolean {
        var result = true
        val restClient = restClient.get().uri("${GET_TIMESLOT_BY_ID}/${timeSlotId}?status=FREE")
        jwtService.getCurrentJwt()?.let {
            restClient.header(AUTH_HEADER, "Bearer ${jwtService.getCurrentJwt()}")
        }
        restClient.retrieve().onStatus(HttpStatusCode::is4xxClientError) { _, _ -> result = false }
            .body(String::class.java)
        return result
    }

    override fun reserveTimeSlot(timeSlotId: Long, customerId: Int): Boolean {

        var result = true
        val restClient = restClient.post().uri(RESERVE_TIME_SLOT).contentType(MediaType.APPLICATION_JSON)
            .body(ReserveTimeSlotRequest(customerId, timeSlotId))
        jwtService.getCurrentJwt()?.let {
            restClient.header(AUTH_HEADER, "Bearer ${jwtService.getCurrentJwt()}")
        }
        restClient.retrieve().onStatus(HttpStatusCode::is4xxClientError) { _, _ -> result = false }
            .body(ReserveTimeSlotResponse::class.java)

        return result
    }

    override fun releaseTimeSlot(timeSlotId: Long): Boolean {
        var result = true
        val restClient = restClient.patch().uri(RELEASE_TIME_SLOT).contentType(MediaType.APPLICATION_JSON)
            .body(ReleaseTimeSlotRequest(timeSlotId))
        jwtService.getCurrentJwt()?.let {
            restClient.header(AUTH_HEADER, "Bearer ${jwtService.getCurrentJwt()}")
        }
        restClient.retrieve().onStatus(HttpStatusCode::is4xxClientError) { _, _ -> result = false }
            .body(ReleaseTimeSlotResponse::class.java)

        return result
    }

    data class ReserveTimeSlotRequest(
        val customerId: Int, val slotId: Long
    )

    data class ReserveTimeSlotResponse(
        val customerId: Int,
        val slotId: Long,

        )

    data class ReleaseTimeSlotRequest(
        val slotId: Long
    )

    data class ReleaseTimeSlotResponse(
        val slotId: Long, val status: String
    )
}