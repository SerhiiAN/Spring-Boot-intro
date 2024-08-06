# BookStore Online

## 📚 Project Overview
Welcome to the documentation of the "BookStore Online" project built with Java Spring.

The Online Book Store API provides a RESTful interface for managing online bookstores. It offers a comprehensive set of endpoints essential for building a fully functional online bookshop.

This API allows you to perform a variety of operations, such as:

- Adding new books to the inventory
- Updating book details such as title, author, and price
- Retrieving detailed information about books in the store
- Managing shopping carts by adding books and updating quantities
- Placing orders for books

Additionally, the API features robust user authentication and authorization mechanisms, allowing you to control access to specific endpoints based on user roles, ensuring secure and controlled interactions with the system.

## 🛠️ Technologies Used
- **Java**: The primary programming language for the entire application.
- **Spring Boot**: Framework enabling rapid and efficient development of Spring-based applications.
- **Spring Security**: Provides authentication and authorization mechanisms in the application.
- **JWT (JSON Web Token)**: Standard for securely transmitting information between parties as JSON objects.
- **Spring Data JPA**: Simplifies data access layer by providing repository support and object-relational mapping.
- **Mapstruct**: Tool for automatic mapping between DTOs (Data Transfer Objects) and entities.
- **Lombok**: Library to reduce boilerplate code in Java classes.
- **Maven**: Build automation tool for managing dependencies and building the project.
- **Liquibase**: Manages database schema changes over time.
- **MySQL**: Relational database for storing application data.
- **Docker**: Platform for containerizing applications to ensure consistency between development and production environments.
- **JUnit 5**: Framework for writing unit tests.
- **Mockito**: Framework for creating mocks and stubs in tests.
- **Swagger**: Tool for generating interactive API documentation.

## 🚀 Running the Project
To run the project, follow these steps:

1. **Install Required Tools**:
   Download and install Docker, Maven, and JDK Development Kit.

2. **Clone the Project Repository**:
   Clone the project repository using Git.

3. **Configure Environment Variables**:
   In the .env file, provide the necessary environment variables related to the database and Docker. Below is an example configuration:

   ```plaintext
   MYSQL_USER=admin
   MYSQL_PASSWORD=hdnctwbd6Tvswkk
   MYSQL_DATABASE=bookstore
   MYSQL_LOCAL_PORT=3307
   MYSQL_DOCKER_PORT=3306
   MYSQL_ENDPOINTS=bookstore-instance.cl66w0qu6d4i.us-east-1.rds.amazonaws.com
   SPRING_LOCAL_PORT=8088
   SPRING_DOCKER_PORT=8080
   DEBUG_PORT=5005
   Build the Application:
   Run the command mvn clean package to build the application.

Run the Docker Container:
Execute the command docker-compose build to build the Docker container.
Then, use docker-compose up to start the Docker container.

Accessing the Application:
The application will be available locally at: http://localhost:8081/api.
You can test the application using tools such as Postman or Swagger. For Postman, remember to pass the authentication (Bearer Token) received after logging in.

Testing Admin and Standard User Features:
To test admin features, use the following login credentials:

Email: admin@example.com
Password: 123123123
For testing standard user features, use:

Email: user@example.com
Password: 123123123
📊 Entity Structure and Relations Diagram
Below is the entity structure and relations diagram for the "Bookstore Online" project, illustrating connections between various entities and their fields.


📚 Project Structure
Controllers
AuthenticationController: Handles login and registration requests for users.
BookController: Manages operations related to books, such as adding, updating, and deleting.
CategoryController: Manages operations related to book categories, such as adding, updating, and deleting.
OrderController: Handles operations related to orders, such as placing new orders and updating order status.
ShoppingCartController: Manages operations on shopping carts, such as adding and removing items from the cart.
DTOs (Data Transfer Objects)
All DTOs, such as BookDto, CategoryDto, OrderDto, etc., used for transferring data between controllers and services.
Mappers
Mappers, such as BookMapper, CategoryMapper, etc., responsible for mapping DTO objects to entities and vice versa.
Services
Services, such as BookService, CategoryService, OrderService, ShoppingCartService, containing business logic and used by controllers.
Repositories
Repositories, such as BookRepository, CategoryRepository, OrderRepository, ShoppingCartRepository, responsible for communication with the database.
Exceptions
Exceptions, such as CustomGlobalExceptionHandler, EntityNotFoundException, handling custom errors and exceptions in the application.
Security
Security components, such as JwtAuthenticationFilter, CustomUserDetailsService, JwtUtil, SecurityConfig, handling user authentication and authorization.
Configuration
Application configurations, such as application.properties, liquibase.properties, containing application settings.
Database Scripts
Initialization and update scripts for the database used by Liquibase, such as .yaml and .sql files.
Tests
Unit and integration tests, such as BookControllerTest, BookServiceTest, BookRepositoryTest, ensuring the correctness of individual components of the application.
Infrastructure
Docker configurations, such as Dockerfile, docker-compose.yml, used for running the application in Docker containers.
Other
Miscellaneous files and components, such as CoverImageValidator, EmailValidator, etc., which are part of the application.
⭐ Features Overview
Authentication Management Endpoints
Available for Everybody:
🌐 POST: /api/auth/registration - registers a new user.
🌐 POST: /api/auth/login - sign in for an existing user.
Book Management Endpoints
Administrator Available:
🔑 POST: /api/books - creates a new book.
🔑 PUT: /api/books/{id} - updates an existing book.
🔑 DELETE: /api/books/{id} - deletes a book.
User Available:
👤 GET: /api/books - retrieves all books.
👤 GET: /api/books/{id} - retrieves a certain book.
👤 GET: /api/books/search - search for a book with parameters.
Category Management Endpoints
Administrator Available:
🔑 POST: /api/categories - creates a new category.
🔑 PUT: /api/categories/{id} - updates an existing category.
🔑 DELETE: /api/categories/{id} - deletes a category.
User Available:
👤 GET: /api/categories - retrieves all categories.
👤 GET: /api/categories/{id} - retrieves a certain category.
👤 GET: /api/categories/{id}/books - provides books by category id.
Shopping Cart Management Endpoints
User Available:
👤 POST: /api/cart - puts a book into the shopping cart.
👤 GET: /api/cart/ - retrieves the shopping cart.
👤 PUT: /api/cart/cart-items/{cartItemId} - updates the quantity of books.
👤 DELETE: /api/cart/cart-items/{cartItemId} - deletes a book from the cart.
Order Management Endpoints
Administrator Available:
🔑 PATCH: /api/orders/{id} - updates the status of the order.
User Available:
👤 POST: /api/orders - places an order.
👤 GET: /api/orders - retrieves the user's order history.
👤 GET: /api/orders/{orderId}/items - provides a list of items contained in the order.
👤 GET: /api/orders/{orderId}/items/{itemId} - provides an order item relying on its ID.
📸 Screenshots
Here are screenshots illustrating the functionality of select sample endpoints from the "Bookstore Online" project. These are just a few instances, with a comprehensive video showcasing all available endpoints further below.

User Registration:

User Login:

Add New Book:

Add New Category:

View Shopping Cart:

View Order Details:

🎥 Video Presentation of Endpoint Functionality
Watch the video below to learn about all the available endpoints in the "Bookstore Online" application and see how they work in action:

Click to watch video

Description:
In this video, I showcase all the available endpoints in our "Bookstore Online" application and explain how they work and what they're used for. You'll see how to register new users, log in, add and update books and categories, manage the shopping cart, place orders, and much more!

🛠 Challenges Faced During Project Development
Docker
🐳 One of the primary goals of the project was to utilize containerization with Docker to facilitate environment management and ensure consistency between developer and production environments. However, during the configuration and execution of Docker containers, I encountered several issues related to environment setup and dependency management. The main causes of these problems were the lack of experience working with Docker and the need to tailor the configuration to the project's specifics.

Integration Tests
🧪 Implementing integration tests was another challenge during the project. While these tests are a crucial element in ensuring code quality and application functionality, creating them required a significant amount of effort. I faced difficulties, especially in configuring tests, creating realistic test scenarios, and ensuring proper isolation of tests from the production environment.