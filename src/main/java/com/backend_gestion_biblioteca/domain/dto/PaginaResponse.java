package com.backend_gestion_biblioteca.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginaResponse<T> {

    private List<T> contenido;
    private long totalElementos;
    private int totalPaginas;
    private int numeroPagina;
    private int tamanioPagina;

    public PaginaResponse() {
    }

    public PaginaResponse(List<T> contenido, long totalElementos, int totalPaginas, int numeroPagina, int tamanioPagina) {
        this.contenido = contenido;
        this.totalElementos = totalElementos;
        this.totalPaginas = totalPaginas;
        this.numeroPagina = numeroPagina;
        this.tamanioPagina = tamanioPagina;
    }
}
