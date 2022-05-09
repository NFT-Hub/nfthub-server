package com.nfthub.api.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Configuration(
    private val context: ApplicationContext
) {
    @Bean
    open fun awsS3Client(
        @Value("\$cloud.aws.credentials.access-key") accessKey: String,
        @Value("\$cloud.aws.credentials.access-key") secretKey: String,
        @Value("\$cloud.aws.credentials.access-key") region: String
    ): AmazonS3 {

        return AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
            .withRegion(region)
            .build()
    }
}