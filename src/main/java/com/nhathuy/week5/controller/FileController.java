package com.nhathuy.week5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nhathuy.week5.exception.StorageFileNotFoundException;
import com.nhathuy.week5.service.IStorageService;

@Controller
public class FileController {

    @Autowired
    private IStorageService storageService;

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);
            
            // Determine content type based on file extension
            String contentType = "application/octet-stream";
            String filenameLower = filename.toLowerCase();
            
            if (filenameLower.endsWith(".jpg") || filenameLower.endsWith(".jpeg")) {
                contentType = MediaType.IMAGE_JPEG_VALUE;
            } else if (filenameLower.endsWith(".png")) {
                contentType = MediaType.IMAGE_PNG_VALUE;
            } else if (filenameLower.endsWith(".gif")) {
                contentType = MediaType.IMAGE_GIF_VALUE;
            } else if (filenameLower.endsWith(".webp")) {
                contentType = "image/webp";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (StorageFileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
