package in.jtechy.EDIParseTool.controller;

import in.jtechy.EDIParseTool.storage.StorageFileNotFoundException;
import in.jtechy.EDIParseTool.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/edifact_inbound_result")
    public String getEdifactInboundResults(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "edifact_inbound_result";
    }

    @GetMapping("/edifact_outbound_result")
    public String getEdifactOutboundResults(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "edifact_outbound_result";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile[] file,
                                   Model model) throws IOException {
        storageService.store(file);
        model.addAttribute("message",
                "You successfully uploaded " + file.toString() + "!");

        return "home";
    }

    @GetMapping("/edifact_ib_input")
    public String getIbInput() {
        return "edifact_ib_input";
    }

    @GetMapping("/edifact_ob_input")
    public String getObInput() {
        return "edifact_ob_input";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
