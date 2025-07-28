package com.dialogdata.main.repository;

import com.dialogdata.main.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {


    Document findByProductId(Integer productId);
}
