package it.getinsight.utilitario;
import feign.FeignException;
import it.getinsight.core.exception.InfraException;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class RetryUtils {

    private RetryUtils() {}

    public static <T> T retryOn404(int maxAttempts, long delayMs, Supplier<T> operation) {
        return retry(maxAttempts, delayMs, operation, e ->
            e instanceof FeignException fe && fe.status() == 404
        );
    }

    public static <T> T retry(int maxAttempts, long delayMs, Supplier<T> operation, Predicate<Exception> retryCondition) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.get();
            } catch (Exception e) {
                if (attempt == maxAttempts || !retryCondition.test(e)) {
                    throw e;
                }
                sleepSafely(delayMs);
            }
        }
        throw new IllegalStateException("Retry falhou de forma inesperada.");
    }

    private static void sleepSafely(long delayMs) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new InfraException("Thread interrompida durante o retry", ie);
        }
    }
}
