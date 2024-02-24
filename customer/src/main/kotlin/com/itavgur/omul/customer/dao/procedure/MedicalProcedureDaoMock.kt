package com.itavgur.omul.customer.dao.procedure

import com.itavgur.omul.customer.config.MockDBConfig
import com.itavgur.omul.customer.domain.MedicalProcedure
import com.itavgur.omul.customer.exception.DatabaseConstraintException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
@ConditionalOnBean(MockDBConfig::class)
class MedicalProcedureDaoMock : MedicalProcedureDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0
    }

    private val sequenceCounter: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableSet<MedicalProcedure> = mutableSetOf()
    override fun getProceduresByCustomerId(procedureId: Int): List<MedicalProcedure> =
        sqlTable.filter { it.customerId == procedureId }.toList()

    override fun addProcedure(procedure: MedicalProcedure): MedicalProcedure {
        checkConstraints(procedure)

        val newProcedure = procedure.clone()
        newProcedure.procedureId = sequenceCounter.getAndIncrement()
        sqlTable.add(newProcedure)
        return newProcedure
    }

    @Throws(DatabaseConstraintException::class)
    private fun checkConstraints(procedure: MedicalProcedure) {
        sqlTable.firstOrNull { e -> procedure.procedureId == e.procedureId }
            ?.let { throw DatabaseConstraintException("medical procedure with id ${procedure.procedureId} already exists") }
    }

}