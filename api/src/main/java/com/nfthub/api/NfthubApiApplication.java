package com.nfthub.api;

import com.nfthub.core.CoreScanConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(
        CoreScanConfig.class
)
@SpringBootApplication
public class NfthubApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(NfthubApiApplication.class, args);
    }
}