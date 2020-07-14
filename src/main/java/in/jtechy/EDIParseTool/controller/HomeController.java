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

        model.addAttribute("ibfiles", storageService.loadIbFiles().map(
                path -> MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                        "serveIbFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "edifact_inbound_result";
    }

    @GetMapping("/edifact_outbound_result")
    public String getEdifactOutboundResults(Model model) throws IOException {

        model.addAttribute("obfiles", storageService.loadObFiles().map(
                path -> MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                        "serveObFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "edifact_outbound_result";
    }

    @GetMapping("/ibfiles/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveIbFile(@PathVariable String filename) {

        Resource file = storageService.loadIbResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/obfiles/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveObFile(@PathVariable String filename) {

        Resource file = storageService.loadObResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/uploadEDIFactInbound")
    public String handleIbFileUpload(@RequestParam("file") MultipartFile[] file,
                                   Model model) throws IOException {
        storageService.ediIbUpload(file);
        model.addAttribute("message",
                "You successfully uploaded " + file.toString() + "!");

        return "edifact_ib_input";
    }

    @PostMapping("/uploadEDIFactOutbound")
    public String handleObFileUpload(@RequestParam("file") MultipartFile[] file,
                                   Model model) throws IOException {
        storageService.ediObUpload(file);
        model.addAttribute("message",
                "You successfully uploaded " + file.toString() + "!");

        return "edifact_ob_input";
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
