package com.itavgur.omul.schedule.dao.personnelDao

import com.itavgur.omul.schedule.domain.PersonnelInfo

interface PersonnelDao {

    fun getPersonnelInfoById(id: Int): PersonnelInfo?

}