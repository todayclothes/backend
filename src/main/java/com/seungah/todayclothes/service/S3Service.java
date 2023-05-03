package com.seungah.todayclothes.service;

import static com.seungah.todayclothes.common.exception.ErrorCode.FAILED_FILE_UPLOAD;
import static com.seungah.todayclothes.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.seungah.todayclothes.common.exception.ErrorCode.INVALID_IMAGE_EXTENSION;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.seungah.todayclothes.common.exception.CustomException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Service {

	private static final Set<String> AVAILABLE_IMAGE_EXTENSION
		= new HashSet<>(
		Arrays.asList(
			"gif", "png", "jpg", "jpeg", "bmp", "webp",
			"GIF", "PNG", "JPG", "JPEG", "BMP", "WebP", "WEBP"
		)
	);

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	// TODO 디렉토리 어떻게 받을지
//	@Value("${cloud.aws.s3.directory}")
//	private String directory;

	/**
	 * (방법1) 단일로 image upload하는 메서드
	 */
	public String uploadImage(MultipartFile multipartFile, String directory) {
		validateImageFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
		String s3FileName = createS3FileName(multipartFile.getOriginalFilename(), directory);

		try (InputStream inputStream = multipartFile.getInputStream()) {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(inputStream.available());
			objectMetadata.setContentType(multipartFile.getContentType());

			amazonS3.putObject(
				new PutObjectRequest(bucket, s3FileName, inputStream, objectMetadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new CustomException(FAILED_FILE_UPLOAD);
		}

		return amazonS3.getUrl(bucket, s3FileName).toString();
	}

	/**
	 * (방법2) 리스트로 받아서 image upload하는 메서드
	 */
	public List<String> uploadImages(List<MultipartFile> multipartFiles, String directory) {
		if (CollectionUtils.isEmpty(multipartFiles)) {
			return Collections.emptyList();
		}

		List<String> fileUrlList = new ArrayList<>();

		for (MultipartFile file : multipartFiles) {
			validateImageFileName(Objects.requireNonNull(file.getOriginalFilename()));
			String s3FileName = createS3FileName(file.getOriginalFilename(), directory);

			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.getSize());
			objectMetadata.setContentType(file.getContentType());

			try (InputStream inputStream = file.getInputStream()) {
				amazonS3.putObject(
					new PutObjectRequest(bucket, s3FileName, inputStream, objectMetadata)
						.withCannedAcl(CannedAccessControlList.PublicRead));
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new CustomException(FAILED_FILE_UPLOAD);
			}

			fileUrlList.add(amazonS3.getUrl(bucket, s3FileName).toString());
		}

		return fileUrlList;
	}

	// image 삭제
	public void deleteImage(String imageUrl, String directory) {
		try {
			String decodingUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8); // URL 디코딩
			int startIdx = decodingUrl.indexOf(directory); // directory 경로가 처음 나온 부분부터 Object Key에 해당
			String objectKey = decodingUrl.substring(startIdx);
			amazonS3.deleteObject(bucket, objectKey);
		} catch (AmazonServiceException e) {
			log.error(e.getMessage());
			throw new CustomException(INTERNAL_SERVER_ERROR);
		}
	}

	private void validateImageFileName(String fileName) {
		String[] splitElements = fileName.split("\\.");
		String fileExtension = splitElements[splitElements.length - 1];

		if (!AVAILABLE_IMAGE_EXTENSION.contains(fileExtension)) {
			throw new CustomException(INVALID_IMAGE_EXTENSION);
		}
	}
	
	private String createS3FileName(String fileName, String directory) {
		return directory + "/" + UUID.randomUUID() + fileName;
	}
}
