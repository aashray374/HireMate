package com.squirtle.hiremate.contacts.service;

import com.squirtle.hiremate.contacts.entity.Contact;
import com.squirtle.hiremate.contacts.repository.ContactRepository;
import com.squirtle.hiremate.common.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final JdbcTemplate jdbcTemplate;
    private final ContactRepository contactRepository;

    private static final int BATCH_SIZE = 1000;
    private static final List<String> REQUIRED_HEADERS = List.of("email", "name", "company");

    @Override
    @Transactional
    public void upload(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")) {
            throw new BadRequestException("Only CSV files are allowed");
        }

        try (
                Reader reader = new InputStreamReader(file.getInputStream());
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.builder()
                                .setHeader()
                                .setSkipHeaderRecord(true)
                                .build())
        ) {

            validateHeaders(csvParser);

            List<Object[]> batchArgs = new ArrayList<>(BATCH_SIZE);

            for (CSVRecord record : csvParser) {

                String email = record.get("email");
                String name = record.get("name");
                String company = record.get("company");

                if (email == null || name == null || company == null) {
                    continue; // skip bad row
                }

                batchArgs.add(new Object[]{
                        email.trim(),
                        name.trim(),
                        company.trim().toLowerCase()
                });

                if (batchArgs.size() == BATCH_SIZE) {
                    insertBatch(batchArgs);
                    batchArgs.clear();
                }
            }

            if (!batchArgs.isEmpty()) {
                insertBatch(batchArgs);
            }

        } catch (Exception e) {
            log.error("CSV upload failed", e);
            throw new BadRequestException("Invalid CSV format or upload failed");
        }
    }

    @Override
    public List<Contact> getContactsFromCompany(String company) {
        return contactRepository.findByCompanyIgnoreCase(company);
    }

    private void validateHeaders(CSVParser parser) {
        Set<String> headers = parser.getHeaderMap().keySet();

        if (!headers.containsAll(REQUIRED_HEADERS)) {
            throw new BadRequestException("CSV must contain headers: email, name, company");
        }
    }

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