package it.getinsight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, "v1/requests/**", "v1/configurations/**", "v1/roles/**", "v1/clients/**", "v1/notifications/emails/**", "v1/storages/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "v1/requests/**", "v1/configurations/**", "v1/roles/**", "v1/clients/**", "v1/notifications/emails/**", "v1/storages/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "v1/requests/**", "v1/configurations/**", "v1/roles/**", "v1/clients/**", "v1/notifications/emails/**", "v1/storages/**").authenticated()
                .requestMatchers(HttpMethod.GET, "v1/requests/**", "v1/configurations/**", "v1/roles/**", "v1/clients/**", "v1/notifications/emails/**", "v1/storages/**").permitAll()
                .requestMatchers( "/actuator/**", "/v3/api-docs", "/v3/api-docs/**", "/configuration/ui", "/swagger-ui/**", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
            ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
