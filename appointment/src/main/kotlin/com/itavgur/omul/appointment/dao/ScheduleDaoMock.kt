package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.config.TransportScheduleMockConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnBean(TransportScheduleMockConfig::class)
class ScheduleDaoMock : ScheduleDao {

    /**
     * return TRUE for even timeSlotId, FALSE for odd
     */
    override fun checkTimeSlotIsFree(timeSlotId: Long): Boolean {

        return timeSlotId % 2 == 0L
    }

    /**
     * return slotId for even timeSlotId, FALSE for odd
     */
    override fun reserveTimeSlot(timeSlotId: Long, customerId: Int): Boolean {
        return true
    }

    override fun releaseTimeSlot(timeSlotId: Long): Boolean {
        return true
    }
}