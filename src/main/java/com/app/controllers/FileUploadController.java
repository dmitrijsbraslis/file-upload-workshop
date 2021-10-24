package com.app.controllers;

import com.app.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class FileUploadController {
    @Autowired
    private FileUploadService uploadService;

    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/upload")
    public String getUploadForm() {
        return "uploadForm";
    }

    @GetMapping("/images")
    public String getAllImagesPage(Model model) throws IOException {
        model.addAttribute("base", uploadService.getPath());

        List<String> files = uploadService.getAllFiles()
                .map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "getFile",
                        path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList());

        model.addAttribute("files", files);
//        model.addAttribute("files", uploadService.getFilesFromDB());
        return "images";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Resource file = uploadService.getResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        uploadService.store(file);
        return "redirect:/upload";
    }
}
