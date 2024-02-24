package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.config.MockDBConfig
import com.itavgur.omul.appointment.domain.Appointment
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Repository
@ConditionalOnBean(MockDBConfig::class)
class AppointmentDaoMock : AppointmentDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0
    }

    private val sequenceCounter: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableSet<Appointment> = mutableSetOf()

    override fun getAppointmentById(appointmentId: Int): Appointment? =
        sqlTable.firstOrNull { it.appointmentId == appointmentId }

    override fun getAppointmentByUUID(uuid: UUID): Appointment? =
        sqlTable.firstOrNull { it.correlationId == uuid }

    override fun createAppointment(appointment: Appointment): Appointment {

        val newAppointment = appointment.clone()
        newAppointment.appointmentId = sequenceCounter.getAndIncrement()
        sqlTable.add(newAppointment)
        return newAppointment
    }

    override fun updateAppointment(appointment: Appointment): Appointment {
        sqlTable.firstOrNull { it.appointmentId == appointment.appointmentId }
            ?.let {
                sqlTable.remove(it)
                sqlTable.add(appointment)
            }

        return appointment.clone()
    }

    override fun deleteAppointment(appointmentId: Int): Int {
        var deletedEntries = 0
        sqlTable
            .firstOrNull { it.appointmentId == appointmentId }
            ?.let {
                sqlTable.remove(it)
                ++deletedEntries
            }
        return deletedEntries
    }

}