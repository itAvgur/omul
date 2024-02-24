package com.itavgur.omul.customer.dao.procedure

import com.itavgur.omul.customer.domain.MedicalProcedure

interface MedicalProcedureDao {

    fun getProceduresByCustomerId(procedureId: Int): List<MedicalProcedure>

    fun addProcedure(procedure: MedicalProcedure): MedicalProcedure

}