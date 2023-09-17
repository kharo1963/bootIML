package com.example.bootIML.controller;

import java.util.Base64;
import com.example.bootIML.dto.InterpretRequest;
import com.example.bootIML.dto.InterpretResponse;
import com.example.bootIML.interpretator.SourceProgram;
import com.example.bootIML.service.InterpretatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InterpretatorController {
    private final InterpretatorService interpretatorService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/")
    public ResponseEntity<InterpretResponse> interpret(@RequestBody InterpretRequest interpretRequest){
        log.info("interpretRequest {}", interpretRequest.getSourceText());
        SourceProgram sourceProgram = interpretatorService.invokeInterpretator(interpretRequest.getSourceText());
        log.info("sourceProgram.resultText {}", sourceProgram.resultText);
        log.info("resultText.indexOf(spinCube)" + sourceProgram.resultText.indexOf("spinCube"));
        String resultVideo64 = "";
        if (sourceProgram.resultText.indexOf("spinCube") >= 0) {
            String resultFile64  = Base64.getEncoder().encodeToString(sourceProgram.fileContent);
            resultVideo64 = "data:video/mp4;base64," + resultFile64;
            //model.addAttribute("resultVideo64", resultVideo64);
            //model.addAttribute("videoOperator", "Результат выполнения оператора spinCube");
        }
        return ResponseEntity.ok(new InterpretResponse (sourceProgram.resultText, resultVideo64));
    }

}

