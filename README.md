# Backend - Sistema de Gestión de Biblioteca

Este proyecto es un prototipo funcional del backend para la gestión de una biblioteca construido con **Spring Boot 3**, **Java 21**, **Spring Data JPA**, **Spring Security** y **JWT**. Incluye documentación OpenAPI/Swagger y un script SQL listo para PostgreSQL.

## Arquitectura y buenas prácticas

- **Capas**: la solución está organizada en capas de controladores, servicios, repositorios y utilidades.
- **DTOs y mapeadores**: cada endpoint expone DTOs específicos y se evita exponer entidades directamente.
- **Estructuras de datos lineales**: se emplean listas enlazadas, pilas y colas para gestionar el historial de operaciones, listas de espera y registros recientes dentro de los servicios (`LinkedList`, `ArrayDeque`).
- **Seguridad**: el backend expone autenticación vía JWT, protegida por Spring Security y filtros personalizados.
- **Documentación**: Swagger UI disponible en `/api/swagger-ui`.

## Endpoints principales

| Recurso | Método | Ruta | Descripción |
|---------|--------|------|-------------|
| Autenticación | POST | `/api/auth/register` | Registra un usuario. |
| Autenticación | POST | `/api/auth/login` | Genera token JWT. |
| Libros | POST | `/api/books` | Crea un libro. |
| Libros | GET | `/api/books` | Lista paginada de libros. |
| Libros | GET | `/api/books/search` | Búsqueda con filtros. |
| Libros | PUT | `/api/books/{id}` | Actualiza un libro. |
| Libros | DELETE | `/api/books/{id}` | Elimina un libro. |
| Usuarios | GET | `/api/users` | Lista usuarios (rol ADMIN). |
| Usuarios | GET | `/api/users/{id}` | Detalle de usuario. |
| Usuarios | PUT | `/api/users/{id}/deactivate` | Desactiva usuario. |
| Préstamos | POST | `/api/loans` | Crea préstamo (ADMIN/LIBRARIAN). |
| Préstamos | PUT | `/api/loans/{id}/return` | Registra devolución. |
| Préstamos | GET | `/api/loans` | Lista préstamos. |

## Configuración

1. Configura PostgreSQL con la base de datos y credenciales en `src/main/resources/application.yml`.
2. Ejecuta el script `src/main/resources/db/scripts/schema.sql` para crear las tablas.
3. Ejecuta la aplicación:

```bash
./mvnw spring-boot:run
```

La documentación interactiva estará disponible en `http://localhost:8080/api/swagger-ui`.

## Pruebas

Se incluyen pruebas de servicios (`BookServiceImplTest`, `LoanServiceImplTest`) con base de datos H2 en memoria.

## Notas de diseño

- Los préstamos controlan listas de espera con colas (`ArrayDeque`) y generan préstamos automáticos cuando se devuelven ejemplares.
- Se controlan los límites de préstamos activos y se manejan errores de negocio mediante excepciones personalizadas.
- El seguimiento de operaciones y errores recientes utiliza listas enlazadas y pilas, ofreciendo evidencia del uso de estructuras lineales.
