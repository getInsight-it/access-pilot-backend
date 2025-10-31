package it.getinsight.module.user.service;

import java.util.Optional;
import java.util.regex.Pattern;

public record ScopeRef(Long clientId, Long roleId, Long levelId, String codeItem) {
    private static final Pattern P = Pattern.compile("^(\\d+):(\\d+):(\\d+):([^:]+)$");

    public static Optional<ScopeRef> parse(String raw) {
        if (raw == null || raw.isBlank()) return Optional.empty();
        var m = P.matcher(raw.trim());
        if (!m.matches()) return Optional.empty();
        Long clientId = toLong(m.group(1));
        Long roleId = toLong(m.group(2));
        Long levelId = toLong(m.group(3));
        String item = m.group(4);
        return Optional.of(new ScopeRef(clientId, roleId, levelId, item));
    }

    private static Long toLong(String s) {
        try {
            return s == null ? null : Long.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


