package com.itavgur.omul.schedule.exception

class TimeSlotNotFoundException(message: String) : RuntimeException(message) {

    companion object {

        @Throws
        fun throwWithBaseMessage(slotId: Long): Exception {

            return TimeSlotNotFoundException("timeslot with id $slotId is absent")
        }

    }
}