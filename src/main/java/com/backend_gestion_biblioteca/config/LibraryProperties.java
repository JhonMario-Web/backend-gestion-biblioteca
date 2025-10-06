package com.backend_gestion_biblioteca.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.prestamos")
public class LibraryProperties {

        private int maxActive;
        private int diasPrestamo;

        public int getMaxActive() {
                return maxActive;
        }

        public void setMaxActive(int maxActive) {
                this.maxActive = maxActive;
        }

        public int getDiasPrestamo() {
                return diasPrestamo;
        }

        public void setDiasPrestamo(int dias) {
                this.diasPrestamo = diasPrestamo;
        }
}
