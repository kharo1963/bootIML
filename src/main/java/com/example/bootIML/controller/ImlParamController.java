package com.example.bootIML.controller;

import java.util.List;

import com.example.bootIML.model.Client;
import com.example.bootIML.model.ImlParam;
import com.example.bootIML.service.ImlParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bootIML.service.StorageFileNotFoundException;
import com.example.bootIML.service.StorageService;
import com.example.bootIML.interpretator.Interpretator;

import com.example.bootIML.service.ArrayFilFiles;


@Controller
public class ImlParamController {

    private final ImlParamService imlParamService;

    @Autowired
    public ImlParamController(ImlParamService imlParamService) {
        this.imlParamService = imlParamService;
    }

    @PostMapping(value = "/imlParam")
    public ResponseEntity<?> create(@RequestBody ImlParam imlParam) {
        imlParamService.create(imlParam);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/imlParam")
    public ResponseEntity<List<ImlParam>> read() {
        final List<ImlParam> imlParam = imlParamService.readAll();

        return imlParam != null &&  !imlParam.isEmpty()
                ? new ResponseEntity<>(imlParam, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/imlParam/{id}")
    public ResponseEntity<ImlParam> read(@PathVariable(name = "id") int id) {
        final ImlParam imlParam = imlParamService.read(id);

        System.out.println("imlParamController.read:");
        System.out.println(imlParam.getParamName());

        return imlParam != null
                ? new ResponseEntity<>(imlParam, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/imlParam/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody ImlParam imlParam) {
        final boolean updated = imlParamService.update(imlParam, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/imlParam/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = imlParamService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}

