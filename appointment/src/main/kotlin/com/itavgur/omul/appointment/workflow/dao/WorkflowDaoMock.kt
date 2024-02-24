package com.itavgur.omul.appointment.workflow.dao

import com.itavgur.omul.appointment.config.MockDBConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Repository
@ConditionalOnBean(MockDBConfig::class)
class WorkflowDaoMock : WorkflowDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0
    }

    private val sequenceCounter: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableSet<Workflow> = mutableSetOf()
    override fun getWorkflowsFiltered(status: WorkflowStatus, name: String, limit: Int?): List<Workflow> {
        limit?.let {
            return sqlTable.filter { it.status == status && it.name == name }.take(limit)
        }
        return sqlTable.filter { it.status == status && it.name == name }
    }

    override fun addWorkflow(workflow: Workflow): Workflow {
        val newOrder = workflow.clone()
        newOrder.id = sequenceCounter.getAndIncrement()
        sqlTable.add(newOrder)
        return newOrder
    }

    override fun updateWorkflow(workflow: Workflow): Workflow {
        sqlTable.firstOrNull { it.id == workflow.id }?.let {
            sqlTable.remove(it)
            sqlTable.add(workflow)
        }

        return workflow.clone()
    }

    override fun updateWorkflow(id: Int, status: WorkflowStatus, context: String): Boolean {
        sqlTable.firstOrNull { it.id == id }?.let {
            val name = it.name
            val correlationId = it.correlationId
            sqlTable.remove(it)
            sqlTable.add(Workflow(id, correlationId, name, status, context))
        }

        return true
    }

    override fun updateWorkflowStatus(id: Int, status: WorkflowStatus): Boolean {
        sqlTable.firstOrNull { it.id == id }?.let {
            it.status = status
            return true
        }
        return false
    }

    override fun updateWorkflowStatus(correlationId: UUID, status: WorkflowStatus): Boolean {
        sqlTable.firstOrNull { it.correlationId == correlationId }?.let {
            it.status = status
            return true
        }
        return false
    }
}