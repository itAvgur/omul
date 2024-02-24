package com.itavgur.omul.schedule.dao.timeslot

import com.itavgur.omul.schedule.config.DatabasePostgresConfig
import com.itavgur.omul.schedule.domain.TimeSlot
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import com.itavgur.omul.schedule.util.DBUtil.Companion.getDateTimeValue
import com.itavgur.omul.schedule.util.DBUtil.Companion.getIntValue
import com.itavgur.omul.schedule.util.DBUtil.Companion.getLongValue
import com.itavgur.omul.schedule.util.DBUtil.Companion.getStringValue
import com.itavgur.omul.schedule.util.logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

@Primary
@Repository
@ConditionalOnBean(DatabasePostgresConfig::class)
class TimeSlotDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : TimeSlotDao {

    companion object {

        val LOG by logger()

        const val QUERY_GET_TIMESLOT_BY_ID =
            """SELECT ts.slot_id ,ts.customer_id, ts.doctor_id,
                ts.date_time_start, ts.date_time_end, ts.status
                FROM time_slots ts
                WHERE ts.slot_id = :slotId"""

        const val QUERY_GET_TIMESLOT_BY_ID_AND_STATUS =
            """SELECT ts.slot_id ,ts.customer_id, ts.doctor_id,
                ts.date_time_start, ts.date_time_end, ts.status
                FROM time_slots ts
                WHERE ts.slot_id = :slotId and ts.status = :status"""

        const val QUERY_GET_TIMESLOT_FILTERED_BASE =
            """SELECT ts.slot_id ,ts.customer_id, ts.doctor_id,
                ts.date_time_start, ts.date_time_end, ts.status
                FROM time_slots ts 
            """

        const val QUERY_INSERT_TIMESLOT =
            """INSERT INTO time_slots (customer_id, doctor_id, date_time_start, date_time_end, status)
                VALUES ( :customerId, :doctorId, :dateFrom, :dateTo, :status)"""

        const val QUERY_UPDATE_TIMESLOT =
            """UPDATE time_slots
                SET customer_id = :customerId, doctor_id = :doctorId,
                date_time_start = :dateFrom, date_time_end = :dateTo, status = :status
                WHERE time_slots.slot_id = :slotId"""

        const val QUERY_PATCH_TIMESLOT =
            """UPDATE time_slots
                SET customer_id = :customerId, status = :status
                WHERE time_slots.slot_id = :slotId"""
    }

    override fun getTimeSlotById(slotId: Long, status: TimeSlotStatus?): TimeSlot? {


        return if (status == null) {
            val map = MapSqlParameterSource(mapOf("slotId" to slotId))
            namedParameterJdbcTemplate.query(
                QUERY_GET_TIMESLOT_BY_ID, map,
                TimeSlotRowMapper()
            ).firstOrNull()
        } else {
            val map = MapSqlParameterSource(mapOf("slotId" to slotId, "status" to status.name))
            namedParameterJdbcTemplate.query(
                QUERY_GET_TIMESLOT_BY_ID_AND_STATUS, map,
                TimeSlotRowMapper()
            ).firstOrNull()
        }
    }

    override fun getTimeSlotsFiltered(
        doctorId: Int?,
        customerId: Int?,
        dateFrom: LocalDateTime?,
        dateTo: LocalDateTime?,
        status: TimeSlotStatus?
    ): List<TimeSlot> {

        val sb = StringBuilder()

        var isAnyConditions = false
        if (doctorId != null) {
            sb.append("   AND ts.doctor_id = :doctorId ")
            isAnyConditions = true
        }
        if (customerId != null) {
            sb.append("   AND ts.customer_id = :customerId ")
            isAnyConditions = true
        }
        if (dateFrom != null) {
            sb.append("   AND ts.date_time_start >= :dateFrom ")
            isAnyConditions = true
        }
        if (dateTo != null) {
            sb.append("   AND ts.date_time_end <= :dateTo ")
            isAnyConditions = true
        }
        if (status != null) {
            sb.append("   AND ts.status = :status ")
            isAnyConditions = true
        }

        if (isAnyConditions) {
            sb.replace(0, 6, "WHERE")
        }

        val map = MapSqlParameterSource(
            mapOf(
                "customerId" to customerId,
                "doctorId" to doctorId,
                "dateFrom" to dateFrom,
                "dateTo" to dateTo,
                "status" to status?.name
            )
        )

        return namedParameterJdbcTemplate.query(
            "$QUERY_GET_TIMESLOT_FILTERED_BASE $sb ORDER BY ts.slot_id", map, TimeSlotRowMapper()
        )
    }

    override fun addTimeSlots(timeSlots: List<TimeSlot>): Int {

        var recordedCounter = 0

        timeSlots.forEach {
            addTimeSlot(it)
            ++recordedCounter
        }

        return recordedCounter
    }

    fun addTimeSlot(timeSlot: TimeSlot): TimeSlot {
        val map = MapSqlParameterSource(
            mapOf(
                "customerId" to timeSlot.customerId,
                "doctorId" to timeSlot.doctorId,
                "dateFrom" to timeSlot.dateTimeStart,
                "dateTo" to timeSlot.dateTimeEnd,
                "status" to timeSlot.status.name
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_TIMESLOT, map, generatedKeyHolder)
        timeSlot.customerId = generatedKeyHolder.keyList.first()["customer_id"] as Int?

        return timeSlot
    }

    override fun updateTimeSlots(timeSlots: List<TimeSlot>): Int {
        var recordedCounter = 0

        timeSlots.forEach {
            try {
                updateTimeSlot(it)
                ++recordedCounter
            } catch (ex: Exception) {
                LOG.error("timeslot with id ${it.slotId} is absent")
            }

        }

        return recordedCounter
    }

    override fun patchTimeSlot(timeSlotId: Long, customerId: Int?, status: TimeSlotStatus): Int {
        val map = MapSqlParameterSource(
            mapOf(
                "slotId" to timeSlotId,
                "customerId" to customerId,
                "status" to status.name
            )
        )

        return namedParameterJdbcTemplate.update(QUERY_PATCH_TIMESLOT, map)
    }

    fun updateTimeSlot(timeSlot: TimeSlot): TimeSlot {
        val map = MapSqlParameterSource(
            mapOf(
                "slotId" to timeSlot.slotId,
                "doctorId" to timeSlot.doctorId,
                "customerId" to timeSlot.customerId,
                "dateFrom" to timeSlot.dateTimeStart,
                "dateTo" to timeSlot.dateTimeEnd,
                "status" to timeSlot.status.name
            )
        )

        namedParameterJdbcTemplate.update(QUERY_UPDATE_TIMESLOT, map)
        return timeSlot
    }

    private class TimeSlotRowMapper : RowMapper<TimeSlot> {
        override fun mapRow(rs: ResultSet, rowNum: Int): TimeSlot {

            return TimeSlot(
                slotId = getLongValue("slot_id", rs)!!,
                customerId = getIntValue("customer_id", rs),
                doctorId = getIntValue("doctor_id", rs)!!,
                dateTimeStart = getDateTimeValue("date_time_start", rs),
                dateTimeEnd = getDateTimeValue("date_time_end", rs),
                status = TimeSlotStatus.valueOf(
                    getStringValue("status", rs)!!
                )
            )


        }
    }
}