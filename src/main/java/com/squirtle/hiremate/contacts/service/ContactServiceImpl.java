package com.squirtle.hiremate.contacts.service;

import com.squirtle.hiremate.contacts.entity.Contact;
import com.squirtle.hiremate.contacts.repository.ContactRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService{

    private final JdbcTemplate jdbcTemplate;
    private final ContactRepository contactRepository;

    private static final int BATCH_SIZE = 1000;

    public ContactServiceImpl(JdbcTemplate jdbcTemplate, ContactRepository contactRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.contactRepository = contactRepository;
    }

    @Override
    @Transactional
    public void upload(MultipartFile file) throws Exception {

        //noinspection deprecation
        try (
                Reader reader = new InputStreamReader(file.getInputStream());
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {

            List<Object[]> batchArgs = new ArrayList<>(BATCH_SIZE);

            for (CSVRecord record : csvParser) {

                batchArgs.add(new Object[]{
                        record.get("email"),
                        record.get("name"),
                        record.get("company").toLowerCase()
                });

                if (batchArgs.size() == BATCH_SIZE) {
                    insertBatch(batchArgs);
                    batchArgs.clear();
                }
            }

            if (!batchArgs.isEmpty()) {
                insertBatch(batchArgs);
            }
        }
    }

    @Override
    public List<Contact> getContactsFromCompany(String company) {
        return contactRepository.getContactsFromCompany(company);
    }

//    private void insertBatch(List<Object[]> batchArgs) {
//
//        jdbcTemplate.batchUpdate(
//                """
//                INSERT INTO contact(email, name, company)
//                VALUES (?, ?, ?)
//                ON CONFLICT (email)
//                DO UPDATE SET
//                    name = EXCLUDED.name,
//                    company = EXCLUDED.company
//                """,
//                batchArgs
//        );
//    }

    private void insertBatch(List<Object[]> batchArgs) {

        jdbcTemplate.batchUpdate(
                """
                MERGE INTO contact (email, name, company)
                KEY(email)
                VALUES (?, ?, ?)
                """,
                batchArgs
        );
    }


}
