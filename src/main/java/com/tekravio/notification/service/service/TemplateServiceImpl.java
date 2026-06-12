package com.tekravio.notification.service.service;

import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.dto.*;
import com.tekravio.notification.service.entity.NotificationRecipient;
import com.tekravio.notification.service.entity.NotificationTemplate;
import com.tekravio.notification.service.entity.NotificationTemplateVariant;
import com.tekravio.notification.service.entity.repo.NotificationTemplateRepository;
import com.tekravio.notification.service.entity.repo.NotificationTemplateVariantRepo;
import com.tekravio.notification.service.exception.ValidationException;
import com.tekravio.notification.service.util.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final CommonService commonService;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationTemplateVariantRepo notificationTemplateVariantRepo;

    @Autowired
    public TemplateServiceImpl(CommonService commonService, NotificationTemplateRepository notificationTemplateRepository, NotificationTemplateVariantRepo notificationTemplateVariantRepo) {
        this.commonService = commonService;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationTemplateVariantRepo = notificationTemplateVariantRepo;
    }

    @Override
    public BaseResponse<TemplateResponse> createTemplate(TemplateRequest request) {
        Optional<NotificationTemplate> notificationTemplate = commonService.findByTemplate(request.getTemplateCode());
        if (notificationTemplate.isPresent()) {
            throw new ValidationException(300, "Template already Present", "Tempalate already present");
        }
        TemplateResponse templateResponse = new TemplateResponse();
        templateResponse.setTemplateCode(request.getTemplateCode());
        templateResponse.setVersion(1);
        List<TemplateVariantResponse> variants = new ArrayList<>();
        NotificationTemplate notificationTemp = new NotificationTemplate();
        notificationTemp.setTemplateCode(request.getTemplateCode());
        notificationTemp.setVersion(1);
        notificationTemp.setCreatedAt(LocalDateTime.now());
        notificationTemp.setActive(true);
        NotificationTemplate notificationTempSaved = commonService.saveTemplate(notificationTemp);
        List<NotificationTemplateVariant> notificationTemplateVariants = new ArrayList<>();
        request.getVariantsRequests().stream().forEach(res -> {
            NotificationTemplateVariant notificationTemplateVariant = new NotificationTemplateVariant();
            notificationTemplateVariant.setBody(res.getBody());
            notificationTemplateVariant.setChannel(res.getChannel());
            notificationTemplateVariant.setSubject(res.getSubject());
            notificationTemplateVariant.setTemplate(notificationTempSaved);
            notificationTemplateVariant.setTitle(res.getTitle());
            notificationTemplateVariants.add(notificationTemplateVariant);
            TemplateVariantResponse templateVariantResponse = new TemplateVariantResponse();
            templateVariantResponse.setBody(res.getBody());
            templateVariantResponse.setChannel(res.getChannel());
            templateVariantResponse.setSubject(res.getSubject());
            templateVariantResponse.setTitle(res.getTitle());
            variants.add(templateVariantResponse);
        });
        commonService.saveTemplateVariantAll(notificationTemplateVariants);
        return BaseResponse.success(templateResponse);
    }

    @Override
    public TemplateResponse getTemplate(Long id) {
        //Done @cacheable
        NotificationTemplate notificationTemplate = commonService.getTemplateEntity(id);
        List<NotificationTemplateVariant> notificationRecipients = commonService.getTemplateVariants(notificationTemplate);

        TemplateResponse templateResponse = new TemplateResponse();
        templateResponse.setTemplateCode(notificationTemplate.getTemplateCode());
        templateResponse.setVersion(1);
        List<TemplateVariantResponse> notificationTemplateVariants = new ArrayList<>();
        notificationRecipients.forEach(res -> {
            TemplateVariantResponse notificationTemplateVariant = new TemplateVariantResponse();
            notificationTemplateVariant.setTitle(res.getTitle());
            notificationTemplateVariant.setSubject(res.getSubject());
            notificationTemplateVariant.setChannel(res.getChannel());
            notificationTemplateVariant.setBody(res.getBody());
            notificationTemplateVariants.add(notificationTemplateVariant);
        });
        templateResponse.setVariants(notificationTemplateVariants);
        return templateResponse;
    }


    @Override
    @CacheEvict(value = "notificationTemplate", key = "#id")
    public BaseResponse deleteTemplate(Long id) {
        NotificationTemplate notificationTemplate = notificationTemplateRepository.findById(id).orElseThrow(() -> new ValidationException(2020, "Template id not found ", "Template id not found"));
        notificationTemplate.setActive(false);
        notificationTemplateRepository.save(notificationTemplate);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse<List<TemplateVariantResponse>> previewApi(Long id, TemplatePreviewRequest request) {

        NotificationTemplate template = notificationTemplateRepository.findById(id).orElseThrow(() -> new ValidationException(
                1014,
                "Template not found",
                "Template not found"));
        List<NotificationTemplateVariant> variants =
                notificationTemplateVariantRepo.findByTemplate(template);

        List<TemplateVariantResponse> responses = new ArrayList<>();

        for (NotificationTemplateVariant variant : variants) {

            TemplateVariantResponse response = new TemplateVariantResponse();

            response.setChannel(variant.getChannel());

            response.setSubject(render(variant.getSubject(), request.getTemplateVariables()));

            response.setTitle(render(variant.getTitle(), request.getTemplateVariables()));

            response.setBody(render(variant.getBody(), request.getTemplateVariables()));
            responses.add(response);
        }
        return BaseResponse.success(responses);
    }

    private String render(String template, Map<String, Object> variables) {
        if (template == null) {
            return null;
        }
        String result = template;

        for (Map.Entry<String, Object> entry :
                variables.entrySet()) {

            result = result.replace(
                    "{{" + entry.getKey() + "}}",
                    String.valueOf(entry.getValue()));
        }
        return result;
    }
}
