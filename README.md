# Task Flow

Task Flow is a project and task management application with a React frontend and a Spring Boot backend that uses JWT authentication and PostgreSQL.

## Installation

1. Backend
   1. `cd spring-backend`
   2. Create a `.env` file with:
      1. `DB_URL=jdbc:postgresql://localhost:5432/taskflow`
      2. `DB_USERNAME=your_db_user`
      3. `DB_PASSWORD=your_db_password`
      4. `JWT_SECRET=your_long_secret_key`
   3. `./mvnw spring-boot:run`

2. Frontend
   1. `cd react-frontend`
   2. `npm install`
   3. `npm run dev`
   4. If you are running the backend locally, update `react-frontend/src/api.js` to use `http://localhost:8080`.
