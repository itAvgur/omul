package com.itavgur.omul.schedule.dao.timeslot

import com.itavgur.omul.schedule.config.DatabaseMockConfig
import com.itavgur.omul.schedule.domain.TimeSlot
import com.itavgur.omul.schedule.domain.TimeSlotStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

@Repository
@ConditionalOnBean(DatabaseMockConfig::class)
class TimeSlotDaoMock : TimeSlotDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0L
    }

    private val sequenceCounter: AtomicLong = AtomicLong(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableMap<Long, TimeSlot> = mutableMapOf()

    override fun getTimeSlotById(slotId: Long, status: TimeSlotStatus?): TimeSlot? {
        sqlTable[slotId]?.let {
            if (status != null && it.status != status) {
                return null
            }
            return it
        }
        return null
    }

    override fun getTimeSlotsFiltered(
        doctorId: Int?, customerId: Int?, dateFrom: LocalDateTime?, dateTo: LocalDateTime?, status: TimeSlotStatus?
    ): List<TimeSlot> {

        return sqlTable.values.filter {

            if (customerId != null && it.customerId != customerId) return@filter false
            if (doctorId != null && it.doctorId != doctorId) return@filter false
            if (dateFrom != null && it.dateTimeStart.isBefore(dateFrom)) return@filter false
            if (dateTo != null && it.dateTimeEnd.isAfter(dateTo)) return@filter false
            if (status != null && it.status != status) return@filter false
            true
        }.sortedBy { timeSlot -> timeSlot.dateTimeStart }
    }

    override fun addTimeSlots(timeSlots: List<TimeSlot>): Int {

        var count = 0
        timeSlots.filter { it.slotId == null }.forEach {
            it.slotId = sequenceCounter.get()
            sqlTable[sequenceCounter.get()] = it
            sequenceCounter.getAndIncrement()
            ++count
        }

        return count
    }

    override fun updateTimeSlots(timeSlots: List<TimeSlot>): Int {
        var count = 0
        timeSlots.filter { it.slotId != null }.forEach {
            if (sqlTable.contains(it.slotId)) {
                ++count
                sqlTable[it.slotId!!] = it
            }
        }
        return count
    }

    override fun patchTimeSlot(timeSlotId: Long, customerId: Int?, status: TimeSlotStatus): Int {

        val slotFound = sqlTable.get(timeSlotId)

        if (slotFound != null) {

            slotFound.customerId = customerId
            slotFound.status = status
            return 1
        }

        return 0
    }

}