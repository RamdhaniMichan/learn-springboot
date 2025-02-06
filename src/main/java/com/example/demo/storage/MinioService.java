package com.example.demo.storage;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public void uploadFile(String objectName, MultipartFile file) throws Exception {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (MinioException e) {
            System.err.println("Error occurred: " + e);
            throw new Exception("Error uploading file to MinIO: " + e.getMessage());
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            System.err.println("Error occurred: " + e);
            throw new Exception("Error processing file: " + e.getMessage());
        }
    }

    public InputStream downloadFile(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }
}
