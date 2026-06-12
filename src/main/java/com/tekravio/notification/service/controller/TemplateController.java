package com.tekravio.notification.service.controller;

import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.dto.TemplatePreviewRequest;
import com.tekravio.notification.service.dto.TemplateRequest;
import com.tekravio.notification.service.dto.TemplateResponse;
import com.tekravio.notification.service.dto.TemplateVariantResponse;
import com.tekravio.notification.service.service.TemplateService;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping("/templates")
    public ResponseEntity<BaseResponse<TemplateResponse>> createTemplate(@RequestBody TemplateRequest request) {
        return new ResponseEntity<>(templateService.createTemplate(request), HttpStatus.CREATED);
    }

    @GetMapping("/templates/{id}")
    public ResponseEntity<TemplateResponse> getTemplate(@PathVariable("id") Long id) {

        return new ResponseEntity<>(templateService.getTemplate(id), HttpStatus.OK);
    }


    @DeleteMapping("/templates/{id}")
    public ResponseEntity<BaseResponse> deleteTemplate(@PathVariable("id") Long id) {
        return new ResponseEntity<>(templateService.deleteTemplate(id), HttpStatus.OK);
    }

    @PostMapping("/templates/{id}/preview")
    public ResponseEntity<BaseResponse<List<TemplateVariantResponse>>> previewApi(@PathVariable Long id, @RequestBody TemplatePreviewRequest request) {
        return new ResponseEntity<>(templateService.previewApi(id, request), HttpStatus.OK);
    }
}
