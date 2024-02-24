package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.config.PostgresDBConfig
import com.itavgur.omul.appointment.domain.AppointmentEvent
import com.itavgur.omul.appointment.domain.AppointmentStatus
import com.itavgur.omul.appointment.util.DBUtil.Companion.getDateTimeValue
import com.itavgur.omul.appointment.util.DBUtil.Companion.getIntValue
import com.itavgur.omul.appointment.util.DBUtil.Companion.getStringValue
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Primary
@Repository
@ConditionalOnBean(PostgresDBConfig::class)
class AppointmentEventDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : AppointmentEventDao {

    companion object {

        const val QUERY_GET_EVENT_BY_ID =
            """SELECT ap.appointment_event_id ,ap.appointment_id, ap.status, ap.description, ap.created 
                FROM appointment_events ap
                WHERE ap.appointment_event_id = :appointmentId"""

        const val QUERY_INSERT_EVENT =
            """INSERT INTO appointment_events (appointment_id, status, description, created)
                VALUES (:appointmentId, :status, :description, :created)"""

        const val QUERY_GET_LAST_EVENT =
            """SELECT ap.appointment_event_id ,ap.appointment_id, ap.status, 
                ap.description, ap.created, status.order_priority
                FROM appointment_events ap
                JOIN appointment_event_status status ON ap.status = status.name
                WHERE ap.appointment_id = :appointmentId
                ORDER BY order_priority DESC LIMIT 1 """
    }

    override fun addEvent(event: AppointmentEvent): AppointmentEvent? {
        val map = MapSqlParameterSource(
            mapOf(
                "eventId" to event.appointmentEventId,
                "appointmentId" to event.appointmentId,
                "status" to event.status.toString(),
                "description" to event.description,
                "created" to event.created
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_EVENT, map, generatedKeyHolder)
        event.appointmentEventId = generatedKeyHolder.keyList.first()["appointment_event_id"] as Int?

        return event
    }

    override fun getEventById(eventId: Int): AppointmentEvent? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_EVENT_BY_ID,
                MapSqlParameterSource("eventId", eventId),
                AppointmentEventRowMapper()
            )
        return result.firstOrNull()
    }

    override fun getLastEvent(appointmentId: Int): AppointmentEvent {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_LAST_EVENT,
                MapSqlParameterSource("appointmentId", appointmentId),
                AppointmentEventRowMapper()
            )
        return result.first()
    }

    private class AppointmentEventRowMapper : RowMapper<AppointmentEvent> {
        override fun mapRow(rs: ResultSet, rowNum: Int): AppointmentEvent {

            return AppointmentEvent(
                appointmentEventId = getIntValue("appointment_event_id", rs),
                appointmentId = getIntValue("appointment_id", rs)!!,
                status = AppointmentStatus.valueOf(getStringValue("status", rs)!!),
                description = getStringValue("description", rs),
                created = getDateTimeValue("created", rs),
            )
        }
    }
}