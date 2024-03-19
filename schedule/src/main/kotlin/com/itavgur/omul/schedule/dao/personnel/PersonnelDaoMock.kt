package com.itavgur.omul.schedule.dao.personnel

import com.itavgur.omul.schedule.config.TransportPersonnelMockConfig
import com.itavgur.omul.schedule.domain.PersonnelInfo
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnBean(TransportPersonnelMockConfig::class)
class PersonnelDaoMock : PersonnelDao {

    override fun getPersonnelInfoById(id: Int): PersonnelInfo {

        return TransportPersonnelMockConfig.mockResponseFromPersonnelServer
    }

}