package com.itavgur.omul.appointment.domain

enum class AppointmentStatus(val order: Int) {
    PENDING(0),
    CREATED(1),
    IN_PROGRESS(5),
    CONFIRMED(10),
    CANCELED(20),
    CLOSED(30)
}