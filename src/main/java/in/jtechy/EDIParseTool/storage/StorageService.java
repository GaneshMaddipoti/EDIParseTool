package in.jtechy.EDIParseTool.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void ediIbUpload(MultipartFile[] file) throws IOException;

	void ediObUpload(MultipartFile[] file) throws IOException;

	Stream<Path> loadIbFiles();

	Stream<Path> loadObFiles();

	Path loadIb(String filename);
	Path loadOb(String filename);

	Resource loadIbResource(String filename);
	Resource loadObResource(String filename);

	void deleteAll();

}
