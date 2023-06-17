package com.example.bootIML.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.bootIML.service.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        String sourceText = "";

        Path path = Paths.get(System.getProperty("user.dir") + "\\ext-gcd.txt");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                sourceText += line + System.lineSeparator();            }
        } catch (IOException e) {
            e.printStackTrace();
            sourceText = "program"
                       + System.lineSeparator()
                       + "   var x,y : int ;"
                       + System.lineSeparator()
                       + "begin"
                       + System.lineSeparator()
                       + "   x := y := 1 ;"
                       + System.lineSeparator()
                       + "   write (x); write (y);"
                       + System.lineSeparator()
                       + "end @";
        }

        return "uploadForm";
    }

    @GetMapping("/result")
    public String resultUploaded(Model model) throws IOException {

        String image = "data:image/png;base64,"
                     + "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4"
                     + "//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
        model.addAttribute("image", image);

        return "resultForm";
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

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}