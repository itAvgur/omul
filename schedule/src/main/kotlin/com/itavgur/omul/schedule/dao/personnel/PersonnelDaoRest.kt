package com.itavgur.omul.schedule.dao.personnel

import com.itavgur.omul.schedule.auth.JwtService
import com.itavgur.omul.schedule.config.TransportPersonnelRestConfig
import com.itavgur.omul.schedule.domain.PersonnelInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestClient

@Primary
@Repository
@ConditionalOnBean(TransportPersonnelRestConfig::class)
class PersonnelDaoRest(
    @Qualifier("personnelRestClient") @Autowired var restClient: RestClient,
    @Autowired private val jwtService: JwtService
) : PersonnelDao {

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val GET_PERSONNEL_BY_ID_URI = "/v1/employee?employeeId"
    }

    override fun getPersonnelInfoById(id: Int): PersonnelInfo? {
        val restClient = restClient
            .get()
            .uri("$GET_PERSONNEL_BY_ID_URI=$id")
        jwtService.getCurrentJwt()?.let {
            restClient.header(AUTH_HEADER, "Bearer ${jwtService.getCurrentJwt()}")
        }

        return restClient.retrieve().body(PersonnelInfo::class.java)
    }

}