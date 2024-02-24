package com.itavgur.omul.schedule.domain

data class PersonnelInfo(
    var personnelId: Int? = null,
    val firstName: String,
    val lastName: String,
    val qualification: String,

    ) {

    val fullName: String
        get() = "$firstName $lastName"

}