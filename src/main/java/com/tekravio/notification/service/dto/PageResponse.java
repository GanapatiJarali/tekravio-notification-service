package com.tekravio.notification.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> data;
    int totalPages;
    long totalElements;
}
