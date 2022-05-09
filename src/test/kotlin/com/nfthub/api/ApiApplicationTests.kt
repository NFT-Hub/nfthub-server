package com.nfthub.api


import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(
    CoreScanConfig::class
)
class ApiApplicationTests