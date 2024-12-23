package com.example.sesacsemi2.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.sesacsemi2.global.exception.ErrorCode;
import com.example.sesacsemi2.global.exception.GlobalException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service{

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String BUCKET;

	@Value("${servlet.multipart.max-file-size}")
	private String maxFileSize;

	private static final String UPLOAD_DIR = System.getProperty("user.dir");
	private static final String IMAGE_DIR = "board-images/";

	@Override
	public String upload(MultipartFile multipartFile) throws IOException {
		if(multipartFile.getSize() > parseSize(maxFileSize)) {
			throw new GlobalException(ErrorCode.FILE_UPLOAD_ERROR);
		}
		File uploadFile = convertToFile(multipartFile)
			.orElseThrow(() -> new GlobalException(ErrorCode.FILE_CONVERSION_ERROR));
		return uploadToS3(uploadFile);
	}

	private String uploadToS3(File uploadFile) {
		String fileName = IMAGE_DIR + UUID.randomUUID() + "-" + uploadFile.getName();
		putS3(uploadFile, fileName);
		return fileName;
	}

	private void putS3(File uploadFile, String fileName) {
		amazonS3.putObject(new PutObjectRequest(BUCKET, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	private Optional<File> convertToFile(MultipartFile multipartFile) throws IOException {
		String filePath = UPLOAD_DIR + "/" + multipartFile.getOriginalFilename();
		File uploadFile = new File(filePath);

		if (!uploadFile.exists() && uploadFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(uploadFile)) {
				fos.write(multipartFile.getBytes());
			} catch (IOException e) {
				throw new IOException("파일 저장 중 오류가 발생하였습니다.", e);
			}
			return Optional.of(uploadFile);
		}
		return Optional.empty();
	}

	private long parseSize(String requestFileSize) {
		String unit = requestFileSize.replaceAll("[^a-zA-Z]", "").toUpperCase();
		long size;

		try {
			size = Long.parseLong(requestFileSize.replaceAll("[^0-9]", ""));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("잘못된 크기 값입니다: " + requestFileSize);
		}

		return switch (unit) {
			case "GB" -> size * 1024 * 1024 * 1024;
			case "MB" -> size * 1024 * 1024;
			case "KB" -> size * 1024;
			case "" -> size; // 단위가 없으면 바이트 단위 그대로 반환
			default -> throw new IllegalArgumentException("지원하지 않는 단위입니다: " + unit);
		};
	}

}
