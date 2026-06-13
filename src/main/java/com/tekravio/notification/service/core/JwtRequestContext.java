package com.tekravio.notification.service.core;

import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ToString
@RequestScope
public class JwtRequestContext {
    private final Map<String, Object> requestVariables = new HashMap<>();

    public void set(String key, Object value) {
        requestVariables.put(key, value);
    }

    public Object get(String key) {
        return requestVariables.get(key);
    }

    public String getEmail() {
        return (String) requestVariables.get("role");
    }

    public List<String> getRoles() {
        List<String> list = (List<String>) requestVariables.get("role");
        return list;
    }
}