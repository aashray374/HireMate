package com.squirtle.hiremate.contacts.repository;

import com.squirtle.hiremate.contacts.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ContactRepository extends JpaRepository<Contact,String> {
    @Query(value = "select email, name, company from contact where LOWER(company) = LOWER(:company)", nativeQuery = true)
    List<Contact> getContactsFromCompany(@Param("company") String company);
}
