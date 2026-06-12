package com.tekravio.notification.service.service;

import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.dto.TemplateRequest;
import com.tekravio.notification.service.dto.TemplateResponse;
import com.tekravio.notification.service.dto.VariantsRequest;
import org.springframework.web.bind.annotation.PathVariable;


public interface TemplateService {
    BaseResponse<TemplateResponse> createTemplate(TemplateRequest request);

    TemplateResponse getTemplate(Long id);


    BaseResponse deleteTemplate(@PathVariable("id") Long id);
}
