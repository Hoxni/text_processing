package com.example.ds1.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
public class ExtractRequestModel {
    private MultipartFile file;
}
