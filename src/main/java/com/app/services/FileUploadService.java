package com.app.services;

import com.app.dao.FileUploadDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileUploadService {
    @Autowired
    private FileUploadDao uploadDao;

    private final String ROOT_LOCATION = "c://uploads";

    public void store(MultipartFile file) throws IOException {
        Path location = Paths.get(ROOT_LOCATION);

        String randomName = RandomStringUtils.randomAlphanumeric(15);
        String extension = file.getOriginalFilename().split("\\.")[1];

        Path destinationFile = location.resolve(Paths.get(randomName + "." + extension)).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);
        uploadDao.storeFileData(randomName + "." + extension);
    }

    public Stream<Path> getAllFiles() throws IOException {
//        return Files.walk(Paths.get(ROOT_LOCATION), 1)
//                .filter(path -> !path.equals(Paths.get(ROOT_LOCATION)))
//                .map(Paths.get(ROOT_LOCATION)::relativize);

        return uploadDao.getAllFiles()
                .stream()
                .map(s -> Paths.get(ROOT_LOCATION).resolve(s));
    }

    public List<String> getFilesFromDB() {
        List<String> links = new ArrayList<>();

        for (String name : uploadDao.getAllFiles()) {
            links.add("http://localhost:8090/files/" + name);
        }

        return links;
    }

    public Resource getResource(String filename) throws MalformedURLException {
        Path file = Paths.get(ROOT_LOCATION).resolve(filename);
        return new UrlResource(file.toUri());
    }

    public String getPath() {
        return ROOT_LOCATION;
    }
}
