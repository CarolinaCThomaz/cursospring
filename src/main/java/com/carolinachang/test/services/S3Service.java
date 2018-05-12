package com.carolinachang.test.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.carolinachang.test.services.exception.FileException;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	public URI uploadFile(MultipartFile multipartFilter) throws URISyntaxException {
		try {
			String fileName = multipartFilter.getOriginalFilename();
			InputStream is = multipartFilter.getInputStream();
			String contentType = multipartFilter.getContentType();
			return uploadFile(is, fileName, contentType);
		} catch (IOException e) {
			throw new FileException("Erro de IO " + e.getMessage());
		}

	}

	public URI uploadFile(InputStream is, String fileName, String contentType) throws URISyntaxException {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, metadata);
			LOG.info("upload ok");
			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (AmazonServiceException e) {
			throw new FileException("Erro ao converter URL para URI");
		} catch (AmazonClientException e) {
			throw new FileException("Amazon Cliente Exception");
		}
	}
}
