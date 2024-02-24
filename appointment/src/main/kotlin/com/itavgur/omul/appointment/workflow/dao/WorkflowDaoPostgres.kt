package com.itavgur.omul.appointment.workflow.dao

import com.itavgur.omul.appointment.config.PostgresDBConfig
import com.itavgur.omul.appointment.util.DBUtil.Companion.getIntValue
import com.itavgur.omul.appointment.util.DBUtil.Companion.getStringValue
import com.itavgur.omul.appointment.util.DBUtil.Companion.getUUIDValue
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
class WorkflowDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : WorkflowDao {

    companion object {

        const val QUERY_GET_WORKFLOW_FILTERED =
            """SELECT wf.workflow_id, wf.correlation_id,wf.name, wf.status, wf.context
                FROM workflow wf
                WHERE wf.name = :name AND wf.status = :status LIMIT :limit"""

        const val QUERY_INSERT_WORKFLOW =
            """INSERT INTO workflow (name, correlation_id,status, context)
                VALUES (:name, :correlationId, :status, :context)"""

        const val QUERY_UPDATE_WORKFLOW =
            """UPDATE workflow
                SET name = :name, correlation_id = :correlationId, status = :status, context = :context
                WHERE workflow.workflow_id = :id"""

        const val QUERY_UPDATE_CONTEXT_STATUS =
            """UPDATE workflow
                SET status = :status, context = :context
                WHERE workflow.workflow_id = :id"""

        const val QUERY_UPDATE_STATUS =
            """UPDATE workflow
                SET status = :status
                WHERE workflow.workflow_id = :id"""

        const val QUERY_UPDATE_STATUS_CORR_ID =
            """UPDATE workflow
                SET status = :status
                WHERE workflow.correlation_id = :correlationId"""
    }

    override fun getWorkflowsFiltered(status: WorkflowStatus, name: String, limit: Int?): List<Workflow> {

        val map = MapSqlParameterSource(
            mapOf(
                "status" to status.toString(),
                "name" to name,
                "limit" to limit
            )
        )

        return namedParameterJdbcTemplate.query(QUERY_GET_WORKFLOW_FILTERED, map, WorkflowRowMapper())
    }

    override fun addWorkflow(workflow: Workflow): Workflow {
        val map = MapSqlParameterSource(
            mapOf(
                "correlationId" to workflow.correlationId,
                "status" to workflow.status.toString(),
                "name" to workflow.name,
                "context" to workflow.context
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_WORKFLOW, map, generatedKeyHolder)
        workflow.id = generatedKeyHolder.keyList.first()["workflow_id"] as Int?

        return workflow
    }

    override fun updateWorkflow(workflow: Workflow): Workflow {
        val map = MapSqlParameterSource(
            mapOf(
                "id" to workflow.id,
                "correlationId" to workflow.correlationId,
                "name" to workflow.name,
                "status" to workflow.status,
                "context" to workflow.context
            )
        )

        namedParameterJdbcTemplate.update(QUERY_UPDATE_WORKFLOW, map)
        return workflow
    }

    override fun updateWorkflow(id: Int, status: WorkflowStatus, context: String): Boolean {
        val map = MapSqlParameterSource(
            mapOf(
                "id" to id,
                "status" to status.toString(),
                "context" to context
            )
        )

        namedParameterJdbcTemplate.update(QUERY_UPDATE_CONTEXT_STATUS, map)
        return true
    }

    override fun updateWorkflowStatus(id: Int, status: WorkflowStatus): Boolean {
        val map = MapSqlParameterSource(
            mapOf(
                "id" to id,
                "status" to status.toString()
            )
        )
        namedParameterJdbcTemplate.update(QUERY_UPDATE_STATUS, map)
        return true
    }

    override fun updateWorkflowStatus(correlationId: UUID, status: WorkflowStatus): Boolean {
        val map = MapSqlParameterSource(
            mapOf(
                "correlationId" to correlationId.toString(),
                "status" to status.toString()
            )
        )
        namedParameterJdbcTemplate.update(QUERY_UPDATE_STATUS_CORR_ID, map)
        return true
    }

    private class WorkflowRowMapper : RowMapper<Workflow> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Workflow {

            return Workflow(
                id = getIntValue("workflow_id", rs),
                correlationId = getUUIDValue("correlation_id", rs),
                name = getStringValue("name", rs)!!,
                status = WorkflowStatus.valueOf(getStringValue("status", rs)!!),
                context = getStringValue("context", rs)!!
            )
        }
    }

}