package com.nhathuy.week5.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IStorageService {

    void init();

    String store(MultipartFile file, String storeFilename);

    void delete(String storeFilename);

    Path load(String filename);

    Resource loadAsResource(String filename);

    String getStorageFilename(MultipartFile file, String id);
}
