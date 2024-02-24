package com.itavgur.omul.schedule.config

import com.itavgur.omul.schedule.domain.PersonnelInfo
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty("transport.personnel.type", havingValue = "mock", matchIfMissing = false)
class TransportPersonnelMockConfig {

    companion object {

        val mockResponseFromPersonnelServer = PersonnelInfo(
            personnelId = 11,
            firstName = "mockedFirstName",
            lastName = "mockedLastName",
            qualification = "mockedQualification"
        )

    }

}