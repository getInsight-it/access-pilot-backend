package it.getinsight.core.test;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@Slf4j
@Getter
public class TestcontainersHolder {

    private static TestcontainersHolder instance;

    private final PostgreSQLContainer<?> postgres;
    private final GenericContainer<?> redis;
    private final GenericContainer<?> mailhog;
    private final RabbitMQContainer rabbitmq;
    private final KeycloakContainer keycloak;

    private TestcontainersHolder() {
        log.info("Initializing Testcontainers...");

        Network network = Network.SHARED;

        log.info("Starting PostgreSQL container...");
        postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
            .withDatabaseName("accesspilot")
            .withUsername("postgres")
            .withPassword("postgres")
            .withNetwork(network)
            .withNetworkAliases("postgres")
            .withReuse(true);
        postgres.start();
        log.info("PostgreSQL started at {}", postgres.getJdbcUrl());

        log.info("Starting Redis container...");
        redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379)
            .withNetwork(network)
            .withNetworkAliases("redis")
            .withReuse(true);
        redis.start();
        log.info("Redis started at {}:{}", redis.getHost(), redis.getMappedPort(6379));

        log.info("Starting Mailhog container...");
        mailhog = new GenericContainer<>(DockerImageName.parse("mailhog/mailhog:latest"))
            .withExposedPorts(1025, 8025)
            .withNetwork(network)
            .withNetworkAliases("mailhog")
            .withReuse(true);
        mailhog.start();
        log.info("Mailhog started - SMTP: {}:{}, UI: {}:{}",
            mailhog.getHost(), mailhog.getMappedPort(1025),
            mailhog.getHost(), mailhog.getMappedPort(8025));

        log.info("Starting RabbitMQ container...");
        rabbitmq = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management-alpine"))
            .withNetwork(network)
            .withNetworkAliases("rabbitmq")
            .withReuse(true);
        rabbitmq.start();
        log.info("RabbitMQ started at {}", rabbitmq.getAmqpUrl());

        log.info("Starting Keycloak container...");
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.4")
            .withNetwork(network)
            .withNetworkAliases("keycloak")
            .withRealmImportFile("access-pilot-realm.json")
            .withStartupTimeout(Duration.ofMinutes(3))
            .withReuse(true);
        keycloak.start();
        log.info("Keycloak started at {}", keycloak.getAuthServerUrl());

        log.info("Testcontainers initialization completed!");
    }

    public static TestcontainersHolder getInstance() {
        if (instance == null) {
            synchronized (TestcontainersHolder.class) {
                if (instance == null) {
                    instance = new TestcontainersHolder();
                }
            }
        }
        return instance;
    }

    public void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));

        registry.add("spring.mail.host", mailhog::getHost);
        registry.add("spring.mail.port", () -> mailhog.getMappedPort(1025));

        registry.add("cali.queue.connection.url", rabbitmq::getAmqpUrl);
        registry.add("cali.queue.connection.credential.username", rabbitmq::getAdminUsername);
        registry.add("cali.queue.connection.credential.password", rabbitmq::getAdminPassword);

        String keycloakUrl = keycloak.getAuthServerUrl();
        String realmUrl = keycloakUrl + "/realms/access-pilot";

        registry.add("provider.keycloak.url", () -> keycloakUrl);
        registry.add("provider.keycloak.realm", () -> "access-pilot");
        registry.add("provider.keycloak.client-id", () -> "accesspilot-backend");
        registry.add("provider.keycloak.client-secret", () -> "test-secret");

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> realmUrl);
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
            () -> realmUrl + "/protocol/openid-connect/certs");

        registry.add("keycloak.url", () -> keycloakUrl);
        registry.add("keycloak.issuer-uri", () -> realmUrl);
        registry.add("test.oauth2.token-uri", () -> realmUrl + "/protocol/openid-connect/token");
        registry.add("test.oauth2.client-id", () -> "accesspilot-backend");
        registry.add("test.oauth2.client-secret", () -> "test-secret");
    }
}
