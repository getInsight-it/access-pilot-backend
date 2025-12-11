package it.getinsight.core.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("asyncNotifyExecutor")
    public Executor asyncNotifyExecutor() {
        return new VirtualThreadSecurityContextExecutor();
    }

    private static class VirtualThreadSecurityContextExecutor implements Executor {
        private final Executor virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

        @Override
        public void execute(@NotNull Runnable command) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            SecurityContext contextCopy = null;

            if (securityContext != null) {
                contextCopy = SecurityContextHolder.createEmptyContext();
                contextCopy.setAuthentication(securityContext.getAuthentication());
            }
            final SecurityContext finalContext = contextCopy;

            virtualThreadExecutor.execute(() -> {
                try {
                    if (finalContext != null) {
                        SecurityContextHolder.setContext(finalContext);
                    }
                    command.run();
                } finally {
                    SecurityContextHolder.clearContext();
                }
            });
        }
    }
}


