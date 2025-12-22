package com.aashray.hiremate.resume.repository;

import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long> {

    List<Resume> findAllByUser(User user, Pageable pageable);
    List<Resume> findAllByUserAndLabel(User user, ResumeLabel resumeLabel, Pageable pageable);

}
