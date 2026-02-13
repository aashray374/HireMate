package com.squirtle.hiremate.contacts.service;

import com.squirtle.hiremate.contacts.entity.Contact;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContactService {
    void upload(MultipartFile file) throws Exception;

    List<Contact> getContactsFromCompany(String company);

}
