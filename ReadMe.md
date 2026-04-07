ProjectBanking API

ProjectBanking — это высокопроизводительное RESTful API для банковского сектора, разработанное на 
Java 21 и Spring Boot 3.5. Проект демонстрирует современную архитектуру работы с финансами, включая 
безопасность на базе JWT, управление транзакциями и автоматизированную документацию.


Технологический стек

 - Runtime: Java 21 (LTS)

 - Framework: Spring Boot 3.5.x

 - Security: Spring Security + JJWT (JSON Web Token)

 - Data: Spring Data JPA (Hibernate)

 - Database: H2 Database (In-memory для разработки) / Поддержка PostgreSQL

 - API Documentation: SpringDoc OpenAPI (Swagger UI)

 - Tooling: Lombok, Maven, Jakarta Validation


Ключевые особенности

 - Security 3.0: Реализация Stateless-аутентификации с использованием AccessToken и RefreshToken.

 - Domain Model: Продвинутая иерархия сущностей:

 -  User (с ролевой моделью: USER, SUPPORT, ADMIN).

 - Account (связь 1:N, поддержка нескольких счетов на одного пользователя).

 - Transaction (строгая фиксация всех финансовых потоков).

 - Safe API: Использование кастомных исключений и RestControllerAdvice для предоставления понятных 
   HTTP-ответов.

 - Auto-generated Data: Автоматическая генерация уникальных 7-значных номеров пользователей и 
   15-значных номеров счетов.


Документация API

   Проект интегрирован с Swagger UI. После запуска приложения документация доступна по адресу:
   http://localhost:8080/swagger-ui.html 
   Здесь можно протестировать все эндпоинты в интерактивном режиме.


Безопасность и Валидация

 - JWT Protection: Все финансовые операции требуют валидный токен в заголовке Authorization: Bearer <token>.

 - Data Validation: Использование jakarta.validation для проверки email, длины строк и обязательных полей на 
   уровне DTO и сущностей.

 - Access Control: Проверка владения счетом (AccessDeniedException) при попытке получить историю транзакций 
   или выполнить перевод.


Roadmap (Планы по развитию)

 - Контейнеризация приложения через Docker.

 - Переход на PostgreSQL в Docker-контейнере.

 - Покрытие сервисов Unit-тестами (JUnit 5 + Mockito).

 - Внедрение системы логирования транзакций (Audit Logging).


Структура проекта

   com.petprojects.projectbanking
   controller    # REST контроллеры (Auth, Accounts, Transactions)
   model         # JPA Сущности (User, Account, Transaction)
   repository    # Интерфейсы Spring Data JPA
   service       # Бизнес-логика системы
   security      # Конфигурация JWT, фильтры и AuthProvider
   dto           # Объекты передачи данных (Request/Response)
   exception     # Кастомная иерархия исключений (AccessDenied, NotFound и др.)


Разработчик

Денис Смирнов/Denis Smirnov(denis-sm-codes) — Java-backend разработчик