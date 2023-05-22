package com.example.bootIML.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import com.example.bootIML.interpretator.Ident;
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

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        StatD.TID = new Vector<Ident>();
        StatD.restArg = new Vector<String>();
        ArrayFilFiles.filFiles = new ArrayList();
        ArrayFilFiles.filFiles.add("Исходный код:");

        String srcCode = storageService.store(file);

        Path path = Paths.get(srcCode );
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for(String line : lines){
                ArrayFilFiles.filFiles.add(line + System.lineSeparator());
            }
         }
        catch (IOException e) {
            e.printStackTrace();
        }

        ArrayFilFiles.filFiles.add("Результат вычислений:");
        try {
            System.out.println("Hello");
            System.out.println(srcCode);
            Interpretator I = new Interpretator (srcCode);
            I.interpretation();
            redirectAttributes.addFlashAttribute("message",
                    "Вы успешно интерпретировали " + file.getOriginalFilename() + "!");
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("files", ArrayFilFiles.filFiles);
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}