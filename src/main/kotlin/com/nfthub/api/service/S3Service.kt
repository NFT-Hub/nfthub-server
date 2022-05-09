package com.nfthub.api.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.nfthub.api.controller.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.net.URLEncoder


@Service
class S3Service(
    private val amazonS3: AmazonS3,
    @Value("\${nozzle.aws.s3.bucketName}")
    private val bucketName: String
) {

    fun upload(uploadFile: MultipartFile, dirName: String): String {
        val originalFilename = uploadFile.originalFilename
        if (originalFilename.isNullOrEmpty()) {
            throw BadRequestException("Upload file name is empty")
        }

        val fileName = "$dirName/${originalFilename.encodeUtf8()}"
        return putS3(uploadFile, fileName)       // 업로드 된 파일의 s3 URL 주소 반환
    }

    fun delete(fileUrl: String) {
        if (fileUrl.isEmpty()) {
            return
        }

        val fileName = URI(fileUrl).path.removePrefix("/")
        amazonS3.deleteObject(DeleteObjectRequest(bucketName, fileName))
    }

    private fun putS3(uploadFile: MultipartFile, fileName: String): String {
        val metadata = ObjectMetadata().apply {
            contentType = uploadFile.contentType
            contentLength = uploadFile.size
        }

        val putObjectRequest = PutObjectRequest(bucketName, fileName, uploadFile.inputStream, metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead)

        amazonS3.putObject(putObjectRequest)

        return amazonS3.getUrl(bucketName, fileName).toString()
    }

    // 파일 이름은 공백이나 URL로 쓰일수 없는 문자가 들어갈수 있기 때문에, UTF8로 인코딩
    private fun String.encodeUtf8() = URLEncoder.encode(this, Charsets.UTF_8)

}
