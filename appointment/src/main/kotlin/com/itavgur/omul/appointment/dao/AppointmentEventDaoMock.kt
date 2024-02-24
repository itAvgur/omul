package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.config.MockDBConfig
import com.itavgur.omul.appointment.domain.AppointmentEvent
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
@ConditionalOnBean(MockDBConfig::class)
class AppointmentEventDaoMock : AppointmentEventDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0
    }

    private val sequenceCounter: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableSet<AppointmentEvent> = mutableSetOf()

    override fun addEvent(event: AppointmentEvent): AppointmentEvent? {

        val newEvent = event.clone()
        newEvent.appointmentEventId = sequenceCounter.getAndIncrement()
        sqlTable.add(newEvent)
        return newEvent
    }

    override fun getEventById(eventId: Int): AppointmentEvent? =
        sqlTable.firstOrNull { it.appointmentEventId == eventId }

    override fun getLastEvent(appointmentId: Int): AppointmentEvent {
        return sqlTable.filter { it.appointmentId == appointmentId }
            .sortedBy { it.status.order }.last()
    }
}