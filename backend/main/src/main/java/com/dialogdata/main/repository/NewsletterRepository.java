package com.dialogdata.main.repository;

import com.dialogdata.main.entity.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterRepository extends JpaRepository<Newsletter, Integer> {


}
