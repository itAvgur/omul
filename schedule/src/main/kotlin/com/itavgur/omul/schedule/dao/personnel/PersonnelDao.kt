package com.itavgur.omul.schedule.dao.personnel

import com.itavgur.omul.schedule.domain.PersonnelInfo

interface PersonnelDao {

    fun getPersonnelInfoById(id: Int): PersonnelInfo?

}