package com.squirtle.hiremate.resume.repository;

import com.squirtle.hiremate.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
}
