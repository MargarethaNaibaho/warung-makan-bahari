package com.enigmacamp.springbootwmbreview.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Warung Makan Bahari",
        version = "1.0",
        contact = @Contact(
                name = "Margaretha",
                url = "https://www.margaretha.com",
                email = "margarethanaibaho@gmail.com"))) // ini bisa tambahkan dokumentasi tentang API kita
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
) //ini gunanya supaya bisa login dengan swagger
public class OpenAPIConfiguration {

}
