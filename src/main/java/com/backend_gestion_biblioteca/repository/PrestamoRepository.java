package com.backend_gestion_biblioteca.repository;

import com.backend_gestion_biblioteca.domain.entity.Libro;
import com.backend_gestion_biblioteca.domain.entity.Prestamo;
import com.backend_gestion_biblioteca.domain.entity.Usuario;
import com.backend_gestion_biblioteca.domain.enums.EstadosPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    long countByUsuarioAndEstado(Usuario usuario, EstadosPrestamo estados);

    @Query("SELECT l FROM Prestamo l WHERE l.estado = :status AND l.fechaVencimiento < :now")
    List<Prestamo> findPrestamosTardios(@Param("status") EstadosPrestamo estado, @Param("now") LocalDate now);

    List<Prestamo> findByLibro(Libro libro);

}
