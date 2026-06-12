package com.tekravio.notification.service.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PreviewResponse {

    private Map<String, ChannelPreview> channels;
}