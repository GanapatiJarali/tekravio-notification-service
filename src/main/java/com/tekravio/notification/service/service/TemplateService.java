package com.tekravio.notification.service.service;

import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.dto.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface TemplateService {
    BaseResponse<TemplateResponse> createTemplate(TemplateRequest request);

    TemplateResponse getTemplate(Long id);


    BaseResponse deleteTemplate(@PathVariable("id") Long id);

    BaseResponse<List<TemplateVariantResponse>> previewApi(Long id, TemplatePreviewRequest request);
}
