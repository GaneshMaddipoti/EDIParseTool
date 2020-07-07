package in.jtechy.EDIParseTool.storage;

import in.jtechy.EDIParseTool.processor.EDIFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class StorageService {

    @Autowired
    private EDIFileParser ediFileParser;

    public void init() {

    }

    public void store(MultipartFile file) {
        ediFileParser.parse(file);
    }

    public Stream<Path> loadAll() {
        return null;
    }

    public Path load(String filename) {
        return null;
    }

    public Resource loadAsResource(String filename) {
        return null;
    }

    public void deleteAll() {

    }

}
