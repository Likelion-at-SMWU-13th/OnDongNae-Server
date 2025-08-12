package com.example.ondongnae.backend.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    private final AmazonS3 s3Client;

    public FileService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${AWS_BUCKET}")
    public String bucket;

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            s3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
            return s3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
