package it.getinsight.core.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class WithSecurityContextAspect {

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Around("@annotation(it.getinsight.core.security.WithSecurityContext)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        WithSecurityContext annotation = method.getAnnotation(WithSecurityContext.class);

        String userId = extractUserId(joinPoint, annotation.userIdExpression());

        log.debug("Setting SecurityContext with userId: {}", userId);
        SecurityContextHelper.setSecurityContext(userId);

        registerCleanupAfterTransaction();

        return joinPoint.proceed();
    }

    private String extractUserId(ProceedingJoinPoint joinPoint, String expression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        Object result = expressionParser.parseExpression(expression).getValue(context);

        if (result == null) {
            throw new IllegalArgumentException("SpEL expression '" + expression + "' returned null userId");
        }

        return result.toString();
    }

    private void registerCleanupAfterTransaction() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    log.debug("Clearing SecurityContext after transaction completion (status={})", status);
                    SecurityContextHelper.clearSecurityContext();
                }
            });
        } else {
            log.warn("Transaction synchronization not active, SecurityContext will not be cleared automatically");
        }
    }
}
