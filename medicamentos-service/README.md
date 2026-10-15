# medicamentos-service  (puerto 8082)

Microservicio de **medicamentos, categorías y lotes** del sistema Farmacia y Medicamentos.
Spring Boot 4.1 · Java 21 · MySQL · Thymeleaf · Hibernate/JPA · AOP.

## Ejecutar
1. MySQL levantado en `localhost:3306` (la BD `farmacia_db` se crea sola).
2. `./mvnw spring-boot:run`  → http://localhost:8082
   (credenciales por defecto: `root` sin clave; cambiar con `DB_USERNAME` / `DB_PASSWORD` / `DB_URL`).

## Qué cubre de la Evaluación 02
| Pregunta | Dónde |
|---|---|
| P1 Relaciones JPA | `Medicamento` ⟷ `CategoriaMedicamento` (@ManyToOne/@OneToMany) y `Medicamento` ⟷ `LoteMedicamento` (@OneToMany/@ManyToOne). CRUD completo de las tres. |
| P2 Auditoría | `audit/` (@Auditable + AuditoriaAspect con AOP), entidad `AuditoriaLog` → tabla `auditoria`. Pantalla `/auditoria` y API `/api/auditoria`. |
| P3–P5 Usuarios, roles, frontend, control de acceso | Pertenecen a **auth-core-service**. Aquí el usuario de la auditoría sale de `audit/UsuarioActual` (se cambia por el usuario autenticado al integrar). |

## Endpoints
- Vistas: `/medicamentos`, `/categorias`, `/lotes`, `/auditoria`
- REST: `/api/medicamentos`, `/api/categorias`, `/api/lotes`, `/api/auditoria?entidad=LoteMedicamento`
- Para probar la auditoría por API envía el header `X-Usuario: jmeneses`.
