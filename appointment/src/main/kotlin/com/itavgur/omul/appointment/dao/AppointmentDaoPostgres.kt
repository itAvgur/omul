package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.config.PostgresDBConfig
import com.itavgur.omul.appointment.domain.Appointment
import com.itavgur.omul.appointment.util.DBUtil.Companion.getDateTimeValue
import com.itavgur.omul.appointment.util.DBUtil.Companion.getIntValue
import com.itavgur.omul.appointment.util.DBUtil.Companion.getLongValue
import com.itavgur.omul.appointment.util.DBUtil.Companion.getStringValue
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*


@Primary
@Repository
@ConditionalOnBean(PostgresDBConfig::class)
class AppointmentDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : AppointmentDao {
    companion object {

        const val QUERY_GET_APPOINTMENT_BY_ID =
            """SELECT ap.appointment_id ,ap.correlation_id, ap.customer_id, ap.time_slot_id, ap.created 
                FROM appointments ap
                WHERE ap.appointment_id = :appointmentId"""

        const val QUERY_GET_APPOINTMENT_BY_UUID =
            """SELECT ap.appointment_id ,ap.correlation_id, ap.customer_id, ap.time_slot_id, ap.created 
                FROM appointments ap
                WHERE ap.correlation_id = :correlationId"""

        const val QUERY_INSERT_APPOINTMENT =
            """INSERT INTO appointments (correlation_id, customer_id, time_slot_id, created)
                VALUES (:correlationId, :customerId, :timeSlotId, :created)"""

        const val QUERY_UPDATE_APPOINTMENT =
            """UPDATE appointments
                SET correlation_id = :correlationId, customer_id = :customerId, time_slot_id = :timeSlotId, created = :created
                WHERE appointments.appointment_id = :appointments"""
    }

    override fun getAppointmentById(appointmentId: Int): Appointment? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_APPOINTMENT_BY_ID,
                MapSqlParameterSource("appointmentId", appointmentId),
                AppointmentRowMapper()
            )
        return result.firstOrNull()
    }

    override fun getAppointmentByUUID(uuid: UUID): Appointment? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_APPOINTMENT_BY_UUID,
                MapSqlParameterSource("correlationId", uuid.toString()),
                AppointmentRowMapper()
            )
        return result.firstOrNull()
    }

    override fun createAppointment(appointment: Appointment): Appointment {
        val map = MapSqlParameterSource(
            mapOf(
                "appointmentId" to appointment.appointmentId,
                "correlationId" to appointment.correlationId,
                "customerId" to appointment.customerId,
                "timeSlotId" to appointment.timeSlotId,
                "created" to appointment.created
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_APPOINTMENT, map, generatedKeyHolder)
        appointment.appointmentId = generatedKeyHolder.keyList.first()["appointment_id"] as Int?

        return appointment
    }

    override fun updateAppointment(appointment: Appointment): Appointment {
        val map = MapSqlParameterSource(
            mapOf(
                "appointmentId" to appointment.appointmentId,
                "correlationId" to appointment.correlationId,
                "customerId" to appointment.customerId,
                "timeSlotId" to appointment.timeSlotId,
                "created" to appointment.created
            )
        )
        namedParameterJdbcTemplate.update(QUERY_UPDATE_APPOINTMENT, map)

        return appointment
    }

    override fun deleteAppointment(appointmentId: Int): Int {
        TODO("Not yet implemented")
    }

    private class AppointmentRowMapper : RowMapper<Appointment> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Appointment {

            return Appointment(
                appointmentId = getIntValue("appointment_id", rs),
                correlationId = UUID.fromString(getStringValue("correlation_id", rs)!!),
                customerId = getIntValue("customer_id", rs),
                timeSlotId = getLongValue("time_slot_id", rs),
                created = getDateTimeValue("created", rs),
            )
        }
    }

}