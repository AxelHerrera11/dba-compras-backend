package com.grupo2.dbacompras.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Habilita CORS para que el frontend (servido en otro puerto/origen, ej. Vite
 * en localhost:5173 o GitHub Pages) pueda consumir esta API sin ser bloqueado
 * por el navegador.
 *
 * Ajustar "allowedOrigins" cuando ya tengan la URL final del frontend desplegado.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*") // TODO: restringir al dominio real antes de la entrega final
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
