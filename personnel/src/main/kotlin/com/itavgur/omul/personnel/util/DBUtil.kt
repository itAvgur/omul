package com.itavgur.omul.personnel.util

import org.apache.commons.lang3.StringUtils
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime

class DBUtil {

    companion object {
        fun getStringValue(column: String, rs: ResultSet): String? {
            return if (rs.getObject(column) != null)
                StringUtils.trimToEmpty(rs.getString(column))
            else
                null
        }

        fun getIntValue(column: String, rs: ResultSet): Int? {
            return if (rs.getObject(column) != null)
                rs.getInt(column)
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
    }
}