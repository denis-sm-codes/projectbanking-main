package com.petprojects.projectbanking.security.date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateCondit extends JsonSerializer<LocalDateTime> {

    private static final DateTimeFormatter FULL_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter SHORT_FORMAT = DateTimeFormatter.ofPattern("MM.yyyy");

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrSupport = auth != null &&
                auth.getAuthorities().stream()
                        .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()) ||
                                "ROLE_SUPPORT".equals(a.getAuthority()));

        if (isAdminOrSupport) {
            gen.writeString(value.format(FULL_FORMAT));
        } else {
            gen.writeString(value.format(SHORT_FORMAT));
        }
    }
}