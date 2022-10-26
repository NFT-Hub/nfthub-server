package com.nfthub.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(
        CoreScanConfig.class
)
public class TestApplication {
}
