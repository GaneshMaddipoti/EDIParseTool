package in.jtechy.EDIParseTool.storage;

import in.jtechy.EDIParseTool.processor.EDIFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path ediIbUploadLocation;
	private final Path ediObUploadLocation;
	private final Path ediIbDownloadLocation;
	private final Path ediObDownloadLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.ediIbUploadLocation = Paths.get("edi-ib-upload-dir");
		this.ediObUploadLocation = Paths.get("edi-ob-upload-dir");
		this.ediIbDownloadLocation = Paths.get("edi-ib-download-dir");
		this.ediObDownloadLocation = Paths.get("edi-ob-download-dir");
	}

	@Override
	public void ediIbUpload(MultipartFile[] file) throws IOException {
		for(MultipartFile multipartFile : file) {
			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Files.copy(inputStream, this.ediIbUploadLocation.resolve(filename),
						StandardCopyOption.REPLACE_EXISTING);
			}
			EDIFileParser.parse(ediIbUploadLocation.resolve(filename), ediIbDownloadLocation);
		}
	}

	@Override
	public void ediObUpload(MultipartFile[] file) throws IOException {
		for(MultipartFile multipartFile : file) {
			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Files.copy(inputStream, this.ediObUploadLocation.resolve(filename),
						StandardCopyOption.REPLACE_EXISTING);
			}
			EDIFileParser.parse(ediObUploadLocation.resolve(filename), ediObDownloadLocation);
		}
	}

	@Override
	public Stream<Path> loadIbFiles() {
		try {
			return Files.walk(this.ediIbDownloadLocation, 1)
					.filter(path -> !path.equals(this.ediIbDownloadLocation))
					.map(this.ediIbDownloadLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public Stream<Path> loadObFiles() {
		try {
			return Files.walk(this.ediObDownloadLocation, 1)
				.filter(path -> !path.equals(this.ediObDownloadLocation))
				.map(this.ediObDownloadLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public Path loadIb(String filename) {
		return ediIbDownloadLocation.resolve(filename);
	}

	@Override
	public Path loadOb(String filename) {
		return ediObDownloadLocation.resolve(filename);
	}

	@Override
	public Resource loadIbResource(String filename) {
		try {
			Path file = loadIb(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public Resource loadObResource(String filename) {
		try {
			Path file = loadOb(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		//FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(ediIbUploadLocation);
			Files.createDirectories(ediObUploadLocation);
			Files.createDirectories(ediIbDownloadLocation);
			Files.createDirectories(ediObDownloadLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
