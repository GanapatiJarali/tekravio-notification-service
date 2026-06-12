package com.tekravio.notification.service.controller;

import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.dto.NotificationRequest;
import com.tekravio.notification.service.dto.NotificationResponse;
import com.tekravio.notification.service.dto.PageResponse;
import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Status;
import com.tekravio.notification.service.service.NotificationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class NotificationController {
    public final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/notifications")
    public ResponseEntity<BaseResponse> sendNotification(@RequestBody NotificationRequest request) {
        return new ResponseEntity<>(notificationService.sendNotification(request), HttpStatus.CREATED);
    }

    @GetMapping("/notifications/{id}/status")
    public ResponseEntity<BaseResponse<NotificationResponse>> fetchNotification(@PathVariable("{id}") String id) {
        return new ResponseEntity<>(notificationService.fetchNotification(id), HttpStatus.OK);
    }

    @PostMapping("/notifications/{id}/retry")
    public ResponseEntity<BaseResponse<NotificationResponse>> retryNotification(@PathVariable("{id}") String id) {
        return new ResponseEntity<>(notificationService.retryNotification(id), HttpStatus.OK);
    }

    @GetMapping("/notifications/history")
    public ResponseEntity<BaseResponse<PageResponse<NotificationResponse>>> notificationHistory(Long recipientId, Channel channel, Status status, @RequestParam(name = "page") @Min(value = 0, message = "Invalid page Number") int page, @RequestParam(name = "size") @Min(value = 1, message = "Page size at least 1") @Max(value = 100) int size) {
        return new ResponseEntity<>(notificationService.notificationHistory(recipientId, channel, status, page, size), HttpStatus.OK);
    }
}
