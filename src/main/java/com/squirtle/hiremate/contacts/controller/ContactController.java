package com.squirtle.hiremate.contacts.controller;


import com.squirtle.hiremate.contacts.service.ContactService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") @NotNull MultipartFile file) throws Exception {
        contactService.upload(file);
        return ResponseEntity.ok("Contacts uploaded successfully");
    }
}
