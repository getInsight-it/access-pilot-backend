package it.getinsight.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class SpringMailConfig implements ApplicationContextAware, EnvironmentAware {


    private ApplicationContext applicationContext;
    private Environment environment;

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(textTemplateResolver());
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.addTemplateResolver(stringTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }


    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mail/MailMessages");
        return messageSource;
    }

        @Bean
        public ITemplateResolver emailTemplateResolver() {
            final var templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("mail/templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setOrder(1);
            return templateResolver;
        }

        @Bean
        public ITemplateResolver textTemplateResolver() {
            final var templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("mail/templates/");
            templateResolver.setSuffix(".txt");
            templateResolver.setTemplateMode(TemplateMode.TEXT);
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setOrder(2);
            return templateResolver;
        }

        @Bean
        public ITemplateResolver stringTemplateResolver() {
            final var templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setTemplateMode("LEGACYHTML5");
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setOrder(3);
            return templateResolver;
        }

        @Bean
        public ITemplateResolver htmlTemplateResolver() {
            final var templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setOrder(4);
            return templateResolver;
        }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}


