package com.backend_gestion_biblioteca.repository;

import com.backend_gestion_biblioteca.domain.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    @Query("SELECT b FROM Libro b WHERE (:titulo IS NULL OR LOWER(b.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) "
            + "AND (:autor IS NULL OR LOWER(b.autor) LIKE LOWER(CONCAT('%', :autor, '%'))) "
            + "AND (:categoria IS NULL OR LOWER(b.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) "
            + "AND (:anio IS NULL OR b.anioPublicacion = :anio)")
    List<Libro> searchBooks(@Param("titulo") String titulo,
                           @Param("autor") String autor,
                           @Param("categoria") String categoria,
                           @Param("anio") Year anioPublicacion);
}
