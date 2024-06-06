## 📚 Book Store API

<p style="text-align: center;">
  <img src="src/img/title.png" alt="Book Store API" style="width: 512px;"/>
</p>

Welcome to the Book Store API! This project serves as the backbone for an online bookstore, offering essential functionalities for searching, browsing, and purchasing books. Inspired by the need for a robust and efficient solution, the Book Store API aims to tackle the challenges faced by online book retailers, providing a seamless experience for both users and administrators.

## 🧱 Structure
- [👉 Technologies Used](#-technologies-and-tools-used)
- [👉 Features](#-features-and-functionality)
- [👉 Installation](#-installation)
- [👉 Postman Collection](#-postman-collection)
- [👉 Video-overview](#-video-overview)

## 🛠️ Technologies and Tools Used

- **Spring Boot**: For building the application framework.
- **Spring Security**: To handle authentication and authorization.
- **Spring Data JPA**: For database interactions using JPA.
- **Spring Web**: For building web-based applications with Spring MVC.
- **Swagger**: For API documentation and testing.
- **Hibernate**: As the ORM tool.
- **MySQL**: For the relational database.
- **Liquibase**: For database schema versioning and management.
- **Docker**: For containerizing the application.
- **JUnit**: For unit testing.
- **Mockito**: For mocking in tests.
- **Testcontainers**: For providing lightweight, disposable containers for integration testing, ensuring consistent and isolated test environments.
- **Maven**: For project management and dependency management.

## 🚀 Features and Functionality

### Authentication

#### Register User
- **Endpoint:** `POST /api/auth/register`
- **Description:** Registers a new user in the application.
- **Request Body:**
  ```json
  {
    "email": "rick.sanchez@universe.com",
    "password": "superpass",
    "repeatPassword": "superpass",
    "firstName": "Rick",
    "lastName": "Sanchez",
    "shippingAddress": "Washington, USA, Earth"
  }            
  ```
#### Login User
- **Endpoint:** `POST /api/auth/login`
- **Description:** Logs in an existing user and returns a token.
- **Request Body:**
  ```json
  {
    "email": "mohinder@example.com",
    "password": "superpass"
  }
  ```
#### Login Admin
- **Endpoint:** `POST /api/auth/login`
- **Description:** Logs in an admin user and returns a token.
- **Request Body:**
  ```json
  {
    "email": "john.doe@example.com",
    "password": "superpass"
  }
  ```
#### Usage of Tokens

Once a user or admin logs in, they receive a token that must be used to access protected endpoints. There are two ways to use this token:

1. **Bearer Token Authentication:**
    - Include the token in the `Authorization` header of your requests.
    - Example:
      ```http
      GET /api/protected-endpoint
      Authorization: Bearer jwt_token_here
      ```

2. **Basic Authentication:**
    - Include the credentials in the `Authorization` header of your requests in the format `Basic base64(email:password)`.
    - Example:
      ```http
      GET /api/protected-endpoint
      Authorization: Basic base64_encoded_credentials
      ```

#### Testing Credentials
- **The credentials provided above can be used for testing purposes.**

### Books

- **List all books**: `GET /api/books`
- **Get book details**: `GET /api/books/{id}`
- **Add new book**: `POST /api/books` (Admin only)
- **Update book**: `PUT /api/books/{id}` (Admin only)
- **Delete book**: `DELETE /api/books/{id}` (Admin only)

### Categories

- **List all categories**: `GET /api/categories`
- **Get category by id**: `GET /api/categories/{id}`
- **Get all books by category**: `GET /api/categories/{id}/books`
- **Add new category**: `POST /api/categories` (Admin only)
- **Update category**: `PUT /api/categories/{id}` (Admin only)
- **Delete category**: `DELETE /api/categories/{id}` (Admin only)

### Shopping Cart

- **Get user's cart**: `GET /api/cart`
- **Add book to cart**: `POST /api/cart`
- **Remove item from cart**: `DELETE /api/cart/{itemId}`
- **Update book quantity in cart**: `PUT /api/cart/{itemId}`

### Orders

- **Get order history**: `GET /api/orders`
- **Place order**: `POST /api/orders`
- **Get all items from order**: `GET /api/orders/{id}/items`
- **Get specific item from order**: `GET /api/orders/{orderId}/items/{itemId}`
- **Update order status**: `PATCH /api/orders/{id}` (Admin only)

## ⏬ Installation

Follow these steps to run application:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/VolandevlodD/book-store
   ```

2. **Navigate to the project directory**:
   ```bash
   cd book-store
   ```
3. **Set Environment Variables**:

   Create a `.env` file in the project root directory and populate it with the following environment variables:
   ```env
   MYSQLDB_USER=your_db_username
   MYSQLDB_ROOT_PASSWORD=your_db_password
   MYSQLDB_DATABASE=your_db_name
   MYSQLDB_LOCAL_PORT=3307
   MYSQLDB_DOCKER_PORT=3306

   SPRING_LOCAL_PORT=8080
   SPRING_DOCKER_PORT=8080
   DEBUG_PORT=5050

   JWT_SECRET=your_jwt_secret
   JWT_EXPIRATION=your_jwt_expiration
   ```

4. **Build the project**:
   ```bash
   ./mvnw clean install
   ```

5. **Build and Run the Docker Containers**:

    - Make sure you have Docker installed on your machine. If not, please visit the [official Docker website](https://www.docker.com/products/docker-desktop/) and download it before proceeding.

   ```bash
   docker-compose up
   ```

6. **Access the Application**:

   Open your browser and go to http://localhost:8080/api/swagger-ui.html to access the Swagger API documentation.

## 📫 Postman Collection

You can fork and use the Postman collection by clicking the button below:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://elements.getpostman.com/redirect?entityId=34424490-af10ac7b-7259-4f1f-ba60-74feb53d9ea4&entityType=collection)

## 📺 Video Overview

Explore the features of our API in this video:

<a href="https://www.loom.com/share/6087c921354845a594cd3f153701817b?sid=e655d459-f47b-4f81-bd50-163275ea0bfe">
  <img src="src/img/Loom_logo_lockup_color_white.png" alt="Watch the Video" width="333">
</a>

## 🛜Connect with Me

Feel free to connect with me on LinkedIn:

[![Connect with me on LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/vladyslav-radko-51b334301/)

I'm always open to networking and discussing new opportunities!
