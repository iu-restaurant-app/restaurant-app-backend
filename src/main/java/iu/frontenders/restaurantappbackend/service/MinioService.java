package iu.frontenders.restaurantappbackend.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {

    private MinioClient minioClient;
    private final String bucketName = "meal-images";

    @Value("${minio.base-url}")
    private String baseURL;

    @Value("${minio.username}")
    private String username;

    @Value("${minio.password}")
    private String password;

    private void authorize() {
        minioClient = MinioClient.builder()
                .endpoint(baseURL)
                .credentials(username, password)
                .build();
    }

    public String addImage(String name, byte[] image) throws ErrorResponseException, InsufficientDataException, InternalException, IOException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {

        authorize();
        return minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(name)
                .stream(new ByteArrayInputStream(image), -1, 10485760)
                .build()
        ).object();
    }

    public byte[] getImage(String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        authorize();
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(name)
                .build()
        ).readAllBytes();
    }

    public void deleteImage(String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        authorize();
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(name)
                .build()
        );
    }
}
