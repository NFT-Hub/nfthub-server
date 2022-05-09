package com.nfthub.api

import com.nfthub.api.auth.AUTHORIZATION_HEADER
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@OpenAPIDefinition(
    servers = [Server(url = "/")]
)
@SecurityScheme(
    name = AUTHORIZATION_HEADER,
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.HEADER
)
@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean::class)
@EntityScan("com.nfthub.api")
@ComponentScan(value = ["com.nfthub.api"])
class CoreScanConfig