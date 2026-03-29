package com.squirtle.hiremate.utils;

import com.cloudinary.Cloudinary;
import com.squirtle.hiremate.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryUtil {

    private final Cloudinary cloudinary;

    public CloudinaryUtil(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, String> uploadFile(MultipartFile file, String folderName) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folderName);
            options.put("resource_type", "auto");

            Map<String, Object> uploadedFile =
                    cloudinary.uploader().upload(file.getBytes(), options);

            String publicId = (String) uploadedFile.get("public_id");

            String url = cloudinary.url().secure(true).generate(publicId);
            return Map.of(
                    "url", url,
                    "publicId", publicId
            );

        } catch (IOException e) {
            throw new BadRequestException("Error uploading file");
        }
    }


    public void deleteFile(String publicId) {
        try {
            Map<String, Object> result =
                    cloudinary.uploader().destroy(publicId, new HashMap<>());

            String status = (String) result.get("result");

            if (!"ok".equals(status)) {
                throw new BadRequestException("Error deleting file");
            }

        } catch (Exception e) {
            throw new BadRequestException("Error deleting file");
        }
    }
}
