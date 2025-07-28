package com.dialogdata.main.service;

import com.dialogdata.main.dto.DocumentDto;
import com.dialogdata.main.entity.Document;
import com.dialogdata.main.entity.Product;
import com.dialogdata.main.mapper.DocumentMapper;
import com.dialogdata.main.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    public DocumentDto createDocument(MultipartFile file, Product product) throws IOException {

        Document document = new Document();

        document.setFileName(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setData(file.getBytes());
        document.setProduct(product);

        Document savedDocument = documentRepository.save(document);

        return documentMapper.toDto(savedDocument);
    }

    public DocumentDto getDocumentByProductId(Integer productId) {

        Document document = documentRepository.findByProductId(productId);

        if (document == null) {
            return null;
        }

        return documentMapper.toDto(document);
    }

    public DocumentDto getDocumentById(Integer id) {
        return documentRepository.findById(id)
                .map(documentMapper::toDto)
                .orElse(null);
    }

    public void deleteDocumentById(Integer id) {
        documentRepository.deleteById(id);
    }

    public void deleteDocumentByProductId(Integer productId) {

        Document document = documentRepository.findByProductId(productId);

        if (document != null) {
            documentRepository.delete(document);
        }
    }
}
