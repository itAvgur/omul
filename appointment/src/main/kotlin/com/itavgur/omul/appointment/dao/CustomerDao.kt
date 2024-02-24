package com.itavgur.omul.appointment.dao

interface CustomerDao {

    fun checkCustomerExist(customerId: Int): Boolean

    fun createTemporaryUser(phone: String?, email: String?): Int?
}