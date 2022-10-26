package com.nfthub.core.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nfthub.core.exception.BadRequestException;
import com.nfthub.core.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 amazonS3;
    @Value("${nfthub.aws.s3.bucket}")
    private String bucketName;

    public String uploadImage(MultipartFile file, String dir) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("file name is empty or null");
        }
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String fileNameWithPath = dir + "/" + encodedFileName;
        return putS3(file, fileNameWithPath);
    }

    private String putS3(MultipartFile uploadFile, String fileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(uploadFile.getContentType());
        objectMetadata.setContentLength(uploadFile.getSize());
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, uploadFile
                    .getInputStream(), objectMetadata);
            amazonS3.putObject(putObjectRequest);
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            throw new BadRequestException("file upload error" + e.getMessage());
        }
    }

    public void deleteImage(String fileUrl) {
        if (StringUtils.equals(fileUrl, "")) {
            return;
        }
        try {
            URI url = new URI(fileUrl);
            // removePrefix /
            String urlString = url.getPath();
            if (urlString.startsWith("/")) {
                urlString = urlString.substring(1);
            }
            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, urlString);
            amazonS3.deleteObject(request);
        } catch (Exception e) {
            throw new BadRequestException("file delete error" + e.getMessage());
        }
    }

}
