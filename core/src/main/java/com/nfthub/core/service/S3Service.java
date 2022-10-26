package com.nfthub.core.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadImage(MultipartFile file, String dir);

    void deleteImage(String fileUrl);
}
