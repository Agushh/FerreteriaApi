# CasaRoma Hardware Store — REST API

**Backend API built with Java Spring Boot for managing product listings of CasaRoma, a family-owned hardware store.**  
This API allows uploading product lists from multiple distributors in Excel format, storing them in a MySQL database, applying price adjustments, and displaying them with pagination and optimized search.  
Access to modifying and uploading products is secured via JWT authentication using Spring Security.

---

## 📌 Features

- **Full CRUD operations** for products and distributors.
- **Excel file upload** with custom parsers for each distributor’s format.
- **JWT authentication** (Spring Security) for admin-only actions.
- **Optimized search** across product name, category, and distributor, handling thousands of records efficiently.
- **Pagination** for improved performance.
- **MVC architecture** for clean and maintainable code.
- **Password encryption** and token-based authentication.
- **Error handling** using `ResponseEntity` and custom exceptions.

---

## 🛠️ Technologies

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F)
![MySQL](https://img.shields.io/badge/MySQL-005C84?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)

---

## 📂 Project Structure

src/

├── main/

│ ├── java/ # Source code

│ │ ├── config # Config and security implementations

│ │ ├── controller # REST controllers (endpoints)

│ │ ├── service # Business logic

│ │ ├── exception # Exception handlers

│ │ ├── model # Entities

│ │ ├── repository # Data access layer

│ └── resources/ # Application properties

└── test/ # Test cases (none for now)


---

## 🔒 Authentication

- **Login endpoint:** `POST /auth/login`
- Returns a **JWT token** upon successful authentication.
- Token must be sent in the **Authorization** header as `Bearer <token>` for protected endpoints.
- Passwords are encrypted before being stored in the database.

---

## 🌐 Deployment

- **Hosted on:** [Render](https://render.com) (free tier).
- **Limitation:** The backend enters sleep mode after 15 minutes of inactivity; first request after waking takes ~2–3 minutes.
- **Frontend (connected to this API):** [https://casaromaapp.onrender.com](https://casaromaapp.onrender.com)
- **Public API link (GET requests only):** [https://ferreteriaapi.onrender.com](https://ferreteriaapi.onrender.com)

---

## 📖 API Endpoints

| Method | Endpoint                | Description                           | Auth Required |
|--------|--------------------------|---------------------------------------|---------------|
| GET    | `/api/productos`         | List all products (paginated)         | No            |
| GET    | `/api/distribuidores`    | List all distributors                 | No            |
| POST   | `/api/productos`         | Add a new product                     | Yes           |
| PUT    | `/api/productos/{id}`    | Update a product                      | Yes           |
| DELETE | `/api/productos/{id}`    | Delete a product                      | Yes           |
| POST   | `/api/productos/upload`  | Upload an Excel file                  | Yes           |
| POST   | `/auth/login`            | Authenticate and receive JWT token    | No            |

---

## ⚙️ Installation (Optional)

> **Note:** This project is not intended for local deployment by others, but the source code is provided.  
If needed, you can run it locally using Docker.

**Requirements:**
- Java 17+
- Maven
- Docker
- MySQL

---

## 👤 Author

**Agustin Zalazar**  
Full Stack Developer in training — passionate about solving real-world problems with code.

---

## 📜 License

Currently unlicensed — all rights reserved.  
Usage, reproduction, or distribution of this project without permission is prohibited.

---
