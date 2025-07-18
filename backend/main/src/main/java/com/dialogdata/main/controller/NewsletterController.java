package com.dialogdata.main.controller;

import com.dialogdata.main.dto.NewsletterDto;
import com.dialogdata.main.entity.Newsletter;
import com.dialogdata.main.mapper.NewsletterMapper;
import com.dialogdata.main.service.NewsletterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newsletter")
public class NewsletterController {

    private final NewsletterService newsletterService;
    private final NewsletterMapper newsletterMapper;

    @Operation(summary = "Create a new newsletter")
    @ApiResponse(responseCode = "201", description = "Newsletter created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid newsletter data")
    @PostMapping
    public ResponseEntity<NewsletterDto> createNewsletter(@Parameter(description = "Newsletter data", required = true)
                                                          @RequestBody NewsletterDto newsletterDto) {

        Newsletter createdNewsletter = newsletterService.create(newsletterMapper.toEntity(newsletterDto));

        if (createdNewsletter == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(201).body(newsletterMapper.toDto(createdNewsletter));
    }

    @Operation(summary = "Get all newsletters")
    @ApiResponse(responseCode = "200", description = "Newsletters retrieved successfully")
    @ApiResponse(responseCode = "204", description = "No newsletters found")
    @GetMapping("/all")
    public ResponseEntity<List<NewsletterDto>> getAllNewsletters() {

        List<Newsletter> newsletters = newsletterService.findAll();

        if (!newsletters.iterator().hasNext()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(newsletterMapper.toDtoList(newsletters));
    }

    @Operation(summary = "Delete a newsletter by ID")
    @ApiResponse(responseCode = "200", description = "Newsletter deleted successfully")
    @ApiResponse(responseCode = "404", description = "Newsletter not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsletter(@Parameter(description = "Newsletter ID", required = true)
                                                 @PathVariable Integer id) {

        if (newsletterService.delete(id)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
