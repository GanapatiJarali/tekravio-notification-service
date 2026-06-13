package com.tekravio.notification.service;

import com.tekravio.notification.service.entity.*;
import com.tekravio.notification.service.entity.repo.*;
import com.tekravio.notification.service.exception.ValidationException;
import com.tekravio.notification.service.util.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationRecipientRepository notificationRecipientRepository;

    @Mock
    private NotificationTemplateRepository templateRepository;

    @Mock
    private NotificationTemplateRepository notificationTemplateRepository;

    @Mock
    private NotificationTemplateVariantRepo notificationTemplateVariantRepo;

    @InjectMocks
    private CommonService commonService;

    private NotificationRecipient recipient;
    private Notification notification;
    private NotificationTemplate template;

    @BeforeEach
    void setUp() {
        recipient = new NotificationRecipient();
        recipient.setId(1L);

        notification = new Notification();
        notification.setNotificationId("N001");

        template = new NotificationTemplate();
        template.setId(10L);
        template.setTemplateCode("TEMP001");
    }



    @Test
    void testFetchByNotificationRecipient_success() {
        when(notificationRecipientRepository.findById(1L))
                .thenReturn(Optional.of(recipient));

        NotificationRecipient result =
                commonService.fetchByNotificationRecipient(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFetchByNotificationRecipient_notFound() {
        when(notificationRecipientRepository.findById(1L))
                .thenReturn(Optional.empty());

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> commonService.fetchByNotificationRecipient(1L)
        );

        assertEquals(200, ex.getErrorCode());
    }



    @Test
    void testFetchNotificationById() {
        when(notificationRepository.findByNotificationId("N001"))
                .thenReturn(Optional.of(notification));

        Optional<Notification> result =
                commonService.fetchNotificationById("N001");

        assertTrue(result.isPresent());
    }

    // ---------------- findByTemplate ----------------

    @Test
    void testFindByTemplate() {
        when(templateRepository.findByTemplateCode("TEMP001"))
                .thenReturn(Optional.of(template));

        Optional<NotificationTemplate> result =
                commonService.findByTemplate("TEMP001");

        assertTrue(result.isPresent());
    }

    // ---------------- saveNotification ----------------

    @Test
    void testSaveNotification() {
        commonService.saveNotification(notification);

        verify(notificationRepository, times(1))
                .save(notification);
    }



    @Test
    void testSaveTemplate() {
        when(templateRepository.save(template)).thenReturn(template);

        NotificationTemplate result =
                commonService.saveTemplate(template);

        assertNotNull(result);
        verify(templateRepository, times(1)).save(template);
    }


    @Test
    void testSaveTemplateVariantAll() {
        List<NotificationTemplateVariant> list = new ArrayList<>();

        commonService.saveTemplateVariantAll(list);

        verify(notificationTemplateVariantRepo, times(1))
                .saveAll(list);
    }



    @Test
    void testGetTemplateEntity_success() {
        when(notificationTemplateRepository.findById(10L))
                .thenReturn(Optional.of(template));

        NotificationTemplate result =
                commonService.getTemplateEntity(10L);

        assertNotNull(result);
    }

    @Test
    void testGetTemplateEntity_notFound() {
        when(notificationTemplateRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ValidationException.class,
                () -> commonService.getTemplateEntity(10L)
        );
    }



    @Test
    void testGetTemplateVariants() {
        List<NotificationTemplateVariant> variants = List.of(
                new NotificationTemplateVariant()
        );

        when(notificationTemplateVariantRepo.findByTemplate(template))
                .thenReturn(variants);

        List<NotificationTemplateVariant> result =
                commonService.getTemplateVariants(template);

        assertEquals(1, result.size());
    }
}