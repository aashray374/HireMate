package com.aashray.hiremate.resume.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadUtil {

    public final Path rootLocation;

    public FileUploadUtil(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        init();
    }

    private void init(){
        try{
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initiate storage location",e);
        }
    }

    public void saveFile(MultipartFile file, String subDir, String fileName)throws IOException {
        Path destinationDir = this.rootLocation.resolve(subDir).normalize();

        if(!Files.exists(destinationDir)){
            Files.createDirectories(destinationDir);
        }

        Path destinationFile = destinationDir.resolve(fileName);
        Files.copy(file.getInputStream(),destinationFile, StandardCopyOption.REPLACE_EXISTING);

    }

    public void deleteFile(String filePath) throws IOException {
        if(filePath == null || filePath.isEmpty())  return;
        Path path = this.rootLocation.resolve(filePath).normalize();
        Files.deleteIfExists(path);
    }
}
