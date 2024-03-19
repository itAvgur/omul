package com.itavgur.omul.schedule.service

import com.itavgur.omul.schedule.auth.JwtService
import com.itavgur.omul.schedule.dao.personnel.PersonnelDao
import com.itavgur.omul.schedule.domain.PersonnelInfo
import com.itavgur.omul.schedule.exception.ExternalDaoException
import org.springframework.stereotype.Service

@Service
class PersonnelService(
    private val personnelDao: PersonnelDao,
    private val jwtService: JwtService
) {

    fun getPersonnelInfo(id: Int): PersonnelInfo {
        jwtService.validateIdWithJwt(id)

        personnelDao.getPersonnelInfoById(id)
            ?.let {
                return it
            }
        throw ExternalDaoException("personnel info with id $id is absent")
    }

}