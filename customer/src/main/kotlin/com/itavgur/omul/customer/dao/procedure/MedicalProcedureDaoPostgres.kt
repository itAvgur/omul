package com.itavgur.omul.customer.dao.procedure

import com.itavgur.omul.customer.config.PostgresDBConfig
import com.itavgur.omul.customer.domain.MedicalProcedure
import com.itavgur.omul.customer.util.DBUtil
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
class MedicalProcedureDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : MedicalProcedureDao {

    companion object {

        const val QUERY_GET_MEDICAL_PROCEDURE_BY_ID =
            """SELECT proc.procedure_id, proc.customer_id ,proc.summary, proc.description, proc.date
                FROM medical_procedures proc
                WHERE proc.customer_id = :customerId"""

        const val QUERY_INSERT_MEDICAL_PROCEDURE =
            """INSERT INTO medical_procedures (customer_id, summary, description, date)
                VALUES (:customerId, :summary, :description, :date)"""

    }

    override fun getProceduresByCustomerId(procedureId: Int): List<MedicalProcedure> {
        return namedParameterJdbcTemplate.query(
            QUERY_GET_MEDICAL_PROCEDURE_BY_ID,
            MapSqlParameterSource("customerId", procedureId),
            MedicalProcedureRowMapper()
        )
    }

    override fun addProcedure(procedure: MedicalProcedure): MedicalProcedure {
        val map = MapSqlParameterSource(
            mapOf(
                "customerId" to procedure.customerId,
                "summary" to procedure.summary,
                "description" to procedure.description,
                "date" to procedure.date
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_MEDICAL_PROCEDURE, map, generatedKeyHolder)
        procedure.procedureId = generatedKeyHolder.keyList.first()["procedure_id"] as Int?

        return procedure
    }

    private class MedicalProcedureRowMapper : RowMapper<MedicalProcedure> {
        override fun mapRow(rs: ResultSet, rowNum: Int): MedicalProcedure {

            return MedicalProcedure(
                procedureId = DBUtil.getIntValue("procedure_id", rs),
                customerId = DBUtil.getIntValue("customer_id", rs)!!,
                summary = DBUtil.getStringValue("summary", rs).toString(),
                description = DBUtil.getStringValue("description", rs),
                date = DBUtil.getDateTimeValue("date", rs)
            )

        }
    }
}