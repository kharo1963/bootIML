package com.example.bootIML.controller;

import java.beans.Encoder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bootIML.service.StorageFileNotFoundException;
import com.example.bootIML.service.StorageService;
import com.example.bootIML.service.ArrayFilFiles;
import com.example.bootIML.interpretator.StatD;
import com.example.bootIML.interpretator.Interpretator;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {

        this.storageService = storageService;
    }

    @CrossOrigin
    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        return "uploadForm";
    }

    @GetMapping("/result")
    public String resultUploaded(Model model) throws IOException {

        String image = "data:image/png;base64,"
                     + "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4"
                     + "//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
        model.addAttribute("image", image);
        //model.addAttribute("resultVideo", System.getProperty("java.io.tmpdir") + "spincubevideo.mp4");

        URI resultURI = Path.of(System.getProperty("java.io.tmpdir") + "spincubevideo.mp4").toUri();
        //String stringURL = resultURI.toURL().toString();
        String stringURL = resultURI.toString();
        model.addAttribute("resultVideo", resultURI);

        byte[] fileContent = FileUtils.readFileToByteArray(new File(System.getProperty("java.io.tmpdir") + "spincubevideo.mp4"));
        String resultFile64 = Base64.getEncoder().encodeToString(fileContent);

        String resultVideo64 = "data:video/mp4;base64,"
                             + resultFile64;
        model.addAttribute("resultVideo64", resultVideo64);

        return "resultForm";

        //https://www.baeldung.com/java-base64-encode-and-decode
        //https://base64.guru/developers/html/video
        //https://paulcwarren.github.io/spring-content/
        //https://stackoverflow.com/questions/49608146/spring-boot-html-5-video-streaming?rq=3
        //https://habr.com/ru/articles/565056/ Реактивное программирование со Spring, часть 3 WebFlux
        //https://stackoverflow.com/questions/49608146/spring-boot-html-5-video-streaming
        //https://stackoverflow.com/questions/20634603/how-do-i-return-a-video-with-spring-mvc-so-that-it-can-be-navigated-using-the-ht
        //File MP4_FILE = new File("/home/ego/bbb_sunflower_1080p_60fps_normal.mp4");
        //return new FileSystemResource(System.getProperty("java.io.tmpdir") + "spincubevideo.mp4");
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @PostMapping("/addSample")
    public String handleAddSample(RedirectAttributes redirectAttributes) {

        String sourceText = "";

        System.out.println("addSample");

        Path path = Paths.get("ext-gcd.txt");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                sourceText += line + System.lineSeparator();            }
        } catch (IOException e) {
            e.printStackTrace();
            sourceText = "program var x,y : int ;  begin x := y := 1 ; write (x); write (y) end @";
        }
        redirectAttributes.addFlashAttribute("sourceText", sourceText);

        return "redirect:/";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("sourceText") String sourceText,
                                   RedirectAttributes redirectAttributes) {

        String srcCode;
        String resultText = "";
        Path path;

        StatD.TID = new ArrayList<>();
        StatD.restArg = new ArrayList<>();
        ArrayFilFiles.filFiles = new ArrayList();

        if (file.isEmpty()) {
            srcCode = System.getProperty("java.io.tmpdir") + "\\tmpEdit.txt";
            path = Paths.get(srcCode);
            try {
                Files.writeString(path, sourceText, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            sourceText = "";
            srcCode = storageService.store(file);
            path = Paths.get(srcCode);
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                for (String line : lines) {
                    sourceText += line + System.lineSeparator();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println("Start");
            System.out.println(srcCode);
            Interpretator I = new Interpretator(srcCode);
            I.interpretation();
            //redirectAttributes.addFlashAttribute("message", "Вы успешно интерпретировали " + file.getOriginalFilename() + "!");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        for (Object line : ArrayFilFiles.filFiles) {
            resultText += line + System.lineSeparator();
        }

        redirectAttributes.addFlashAttribute("resultText", resultText);
        redirectAttributes.addFlashAttribute("sourceText", sourceText);

        System.out.println("resultText.indexOf(spinCube)" + resultText.indexOf("spinCube"));

        if (resultText.indexOf("spinCube") >= 0) {
            return "redirect:/result";
        }
        return "redirect:/";

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}