package com.tefo.customerservice.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${keycloak.configServerUrl}")
    String authServerUrl;
    @Value("${keycloak.realm}")
    String realm;

    private static final String OPEN_ID_SCHEME_NAME = "openId";
    private static final String OPENID_CONFIG_FORMAT = "%s/realms/%s/.well-known/openid-configuration";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OPEN_ID_SCHEME_NAME, createOpenIdScheme()))
                .addSecurityItem(new SecurityRequirement().addList(OPEN_ID_SCHEME_NAME))
                .info(new Info()
                        .title("OpenAPI specification")
                        .description("OpenAPI v3 specification for customer service")
                        .version("v1.0")
                );
    }


    private io.swagger.v3.oas.models.security.SecurityScheme createOpenIdScheme() {
        String connectUrl = String.format(OPENID_CONFIG_FORMAT, authServerUrl, realm);

        return new io.swagger.v3.oas.models.security.SecurityScheme()
                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.OPENIDCONNECT)
                .scheme("bearer")
                .in(SecurityScheme.In.HEADER)
                .openIdConnectUrl(connectUrl);
    }
}
