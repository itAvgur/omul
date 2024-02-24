package com.itavgur.omul.schedule.service

import com.itavgur.omul.schedule.auth.JwtService
import com.itavgur.omul.schedule.dao.personnelDao.PersonnelDao
import com.itavgur.omul.schedule.domain.PersonnelInfo
import com.itavgur.omul.schedule.exception.ExternalCallException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonnelService(
    @Autowired private val personnelDao: PersonnelDao,
    @Autowired private val jwtService: JwtService
) {

    fun getPersonnelInfo(id: Int): PersonnelInfo {
        jwtService.validateIdWithJwt(id)

        personnelDao.getPersonnelInfoById(id)
            ?.let {
                return it
            }
        throw ExternalCallException("personnel info with id $id is absent")
    }

}