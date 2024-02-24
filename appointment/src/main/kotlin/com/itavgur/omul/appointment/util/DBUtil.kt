package com.itavgur.omul.appointment.util

import org.apache.commons.lang3.StringUtils
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class DBUtil {

    companion object {
        fun getStringValue(column: String, rs: ResultSet): String? {
            return if (rs.getObject(column) != null)
                StringUtils.trimToEmpty(rs.getString(column))
            else
                null
        }

        fun getUUIDValue(column: String, rs: ResultSet): UUID? {
            return if (rs.getObject(column) != null)
                UUID.fromString(StringUtils.trimToEmpty(rs.getString(column)))
            else
                null
        }

        fun getIntValue(column: String, rs: ResultSet): Int? {
            return if (rs.getObject(column) != null)
                rs.getInt(column)
            else
                null
        }

        fun getLongValue(column: String, rs: ResultSet): Long? {
            return if (rs.getObject(column) != null)
                rs.getLong(column)
            else
                null
        }

        fun getDateValue(column: String, rs: ResultSet): LocalDate? {
            return if (rs.getObject(column) != null)
                rs.getDate(column).toLocalDate()
            else
                null
        }

        fun getBooleanValue(column: String, rs: ResultSet): Boolean? {
            return if (rs.getObject(column) != null)
                rs.getBoolean(column)
            else
                null
        }

        fun getDateTimeValue(column: String, rs: ResultSet): LocalDateTime {
            return rs.getTimestamp(column).toLocalDateTime()
        }

        fun getJSONBValue(column: String, rs: ResultSet): String {
            return rs.getBlob(column).toString()
        }
    }
}