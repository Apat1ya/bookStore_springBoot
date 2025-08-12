## 📚 Online Book Store Application

Developed as a pet project, Book Store showcases the power of Spring Boot in building robust RESTful services. 
It offers authentication, cart functionality, order tracking, and admin-level product management in a simple, 
maintainable structure.

---
## Technology Stack

| Layer                  | Tech Stack                     |
|------------------------|--------------------------------|
| **Language**           | Java 21                        |
| **Framework**          | Spring Boot                    |
| **Security**           | Spring Security + JWT          |
| **Data Access**        | Spring Data JPA + Hibernate    |
| **Database**           | MySQL (configurable)           |
| **API Documentation**  | Swagger (Springdoc OpenAPI)    |
| **Validation**         | Spring Validation              |
| **Testing**            | JUnit, Mockito                 |
| **Dev Tools**          | Lombok, MapStruct              |
| **Auth**               | JWT Token-based authentication |

---
## Features
### Authentication Controller
| Method | Endpoint                 | Description                | Access |
| ------ | ------------------------ |----------------------------| ------ |
| `POST` | `/api/auth/login`        | Authenticate and get token | Public |
| `POST` | `/api/auth/registration` | Register a new user        | Public |

### Book Controller
| Method   | Endpoint            | Description                                | Role    |
| -------- | ------------------- |--------------------------------------------|---------|
| `GET`    | `/api/books`        | View all books with pagination and sorting | USER/ADMIN    |
| `GET`    | `/api/books/{id}`   | Retrieve a book by ID                      | USER/ADMIN    |
| `POST`   | `/api/books`        | Create a book                              | ADMIN   |
| `PUT`    | `/api/books/{id}`   | Update a book                              | ADMIN   |
| `DELETE` | `/api/books/{id}`   | Delete a book                              | ADMIN   |
| `GET`    | `/api/books/search` | Search books by title and author.          | USER/ADMIN    |

### Category Controller
| Method   | Endpoint                     | Description                           | Role          |
| -------- | ---------------------------- |---------------------------------------|---------------|
| `POST`   | `/api/categories`            | Create a category                     | ADMIN         |
| `GET`    | `/api/categories`            | Get all categories (paginated)        | USER/ADMIN    |
| `GET`    | `/api/categories/{id}`       | Get a category by ID                  | USER/ADMIN    |
| `PUT`    | `/api/categories/{id}`       | Update a category                     | ADMIN         |
| `DELETE` | `/api/categories/{id}`       | Delete a category                     | ADMIN         |
| `GET`    | `/api/categories/{id}/books` | Get all books by category (paginated) | USER/ADMIN    |

### Order Controller
| Method  | Endpoint                           | Description                                     | Role           |
| ------- |------------------------------------|-------------------------------------------------|----------------|
| `GET`   | `/api/orders`                      | Get all orders for the current user (paginated) | USER/ADMIN     |
| `POST`  | `/api/orders`                      | Create a new order from cart                    | USER/ADMIN     |
| `PATCH` | `/api/orders/{id}`                 | Update order status                             | ADMIN          |
| `GET`   | `/api/orders/{orderId}`            | Get items of an order (paginated)               | USER/ADMIN     |
| `GET`   | `/api/orders/{orderId}/items/{id}` | Get a specific order item                       | USER/ADMIN     |

### Shopping Cart Controller
| Method   | Endpoint                       | Description                    | Role          |
| -------- | ------------------------------ |--------------------------------|---------------|
| `GET`    | `/api/cart`                    | Get the current user’s cart    | USER/ADMIN    |
| `POST`   | `/api/cart`                    | Add a book to cart             | USER/ADMIN    |
| `PUT`    | `/api/cart/items/{cartItemId}` | Update quantity of a cart item | USER/ADMIN    |
| `DELETE` | `/api/cart/items/{cartItemId}` | Remove a cart item             | USER/ADMIN    |

---
### Entities Structure
![Entities Structures](https://i.ibb.co/DgDQLkYK/2025-08-09-181328781.png)

---
### How to Run the Project
### Requirements
- **Java 17+**
- **Maven**
- **Docker**
### Step 1: Clone the repository

```bash
git clone https://github.com/Apat1ya/bookStore_springBoot.git
cd spring-book-store
```
### Step 2: Environment Configuration
Create a .env file in the project root (See `.env.example` for a sample.):
```bash
# === MySQL Configuration ===
MYSQLDB_DATABASE=database_name
MYSQLDB_USER=user_name
MYSQLDB_ROOT_PASSWORD=password
        
MYSQLDB_LOCAL_PORT=3305
MYSQLDB_DOCKER_PORT=3306

# === Spring Boot App Ports ===
SPRING_LOCAL_PORT=8081
SPRING_DOCKER_PORT=8080.

# === JWT Configuration ===
JWT_EXPIRATION=5
JWT_SECRET=secretKey
```
These variables are used in `docker-compose.yml` and the Spring Boot configuration.

### Step 3: Start Containers
```bash
docker-compose up --build
```
The app will be available at:
API: `http://localhost:8080`

### Step 4: Stop Containers
```bash
docker-compose down
```
---
## Postman Collections
This Postman collection contains a set of ready-made requests for testing the project's API.
 ### [Collection File](postman/Books_store.postman_collection.json)
> Import it into your Postman app and start exploring the API!
### How to use:
```bash
1. Open Postman.

2. Click Import and select the .json file from the repository.

3. Send a POST /auth/login request with your credentials to authenticate.

4. Copy the JWT token from the login response.

5. For all subsequent requests:
 Go to the Authorization tab.
 Select Bearer Token as the type.
 Paste the copied token.

You can now send authorized requests with the appropriate permissions.
```
