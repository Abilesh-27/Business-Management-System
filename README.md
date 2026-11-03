
Business Management System

A Spring Boot backend project for managing labour operations and weekly payment records for a small contracting business.

The system is intended to bring worker details, daily work records, advances, partial payments, and weekly salary settlements into one centralized application.

Project focus: Java backend development, REST API design, relational data modelling, persistence with JPA, and API security.

Overview

Small businesses that manage field workers often need to keep track of changing work assignments, wage rates, advances, and payments made during the week. This project aims to make those records easier to maintain and review.

Core Areas

Labour management — maintain worker details and wage-related information.

Work records — organize daily work entries for salary calculation and settlement tracking.

Advances and partial payments — record payments made before the weekly settlement.

Weekly salary settlements — bring work records, advances, and partial payments together for weekly settlement.

Authentication and security — secure backend access using Spring Security and JWT-based authentication.

Technology Stack

Area

Technology

Language

Java 21

Backend framework

Spring Boot 3.5.3

Web layer

Spring Web

Persistence

Spring Data JPA, Hibernate

Database

MySQL

Security

Spring Security, JSON Web Tokens (JWT)

Validation

Spring Boot Starter Validation

Build tool

Apache Maven

Containerization

Dockerfile included

The versions and dependencies above are based on the project's Maven configuration.

Application Architecture

The backend follows a layered structure so that HTTP handling, business rules, data access, and security responsibilities remain separated.

Client
  |
  v
REST Controllers
  |
  v
Service Layer
  |
  v
Repository Layer
  |
  v
MySQL Database

Controllers receive HTTP requests and return API responses.

Services contain application and business logic.

Repositories handle database operations through Spring Data JPA.

Entities represent relational data stored in MySQL.

Security components use Spring Security and JWT to protect API access.

Getting Started

Prerequisites

Install the following before running the application:

Java Development Kit (JDK) 21

Apache Maven

MySQL Server

Git

1. Clone the repository

git clone https://github.com/Abilesh-27/dad-business-backend.git
cd dad-business-backend

2. Create a MySQL database

Create a database in your local MySQL server. Use the database name expected by your local application configuration.

CREATE DATABASE your_database_name;

3. Configure the application

Set your local MySQL connection details and the JWT signing secret in the application's local configuration. Use your own values for the database URL, username, password, and JWT secret.

Do not commit real passwords, JWT secrets, API keys, or production credentials to GitHub. Keep local secrets in an untracked configuration file or environment variables.

4. Run the application

From the project root, run:

mvn spring-boot:run

5. Build the application

To create a packaged application, run:

mvn clean package

Docker

A Dockerfile is included in the repository to build and package the Spring Boot application as a container image. Configure the database connection and JWT secret for the container environment before running it.

Repository Structure

dad-business-backend/
├── src/
├── .gitignore
├── Dockerfile
└── pom.xml

The Java source code is organized under src/. The Maven configuration and Dockerfile are maintained at the repository root.

Security Notes

Never publish database passwords or JWT signing secrets.

Keep local configuration files out of version control.

Use separate credentials for local development and deployed environments.

If a secret has ever been committed to Git, rotate it; deleting the file in a later commit does not remove it from the repository history.

Author

Abilesh J V
GitHub: @Abilesh-27

This project is part of my learning and practical work in Java backend development with Spring Boot.
