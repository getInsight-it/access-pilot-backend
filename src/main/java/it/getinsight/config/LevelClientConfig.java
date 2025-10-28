package it.getinsight.config;

import it.getinsight.module.level.client.LevelClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LevelClientProperties.class)
public class LevelClientConfig {}
