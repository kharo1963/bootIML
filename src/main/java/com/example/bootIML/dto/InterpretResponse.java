package com.example.bootIML.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterpretResponse {
    private String resultText;
    private String resultVideo64;

    public InterpretResponse (String resultText, String resultVideo64) {
        this.resultText = resultText;
        this.resultVideo64 = resultVideo64;
    }
}

