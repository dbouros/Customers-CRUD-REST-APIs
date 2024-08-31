# Customers CRUD REST APIs

## Description

The "Customers CRUD REST APIs" project is a Java-based application that provides a set of RESTful APIs for managing customer data. The application allows users to create, read, update, and delete customer records stored in a PostgreSQL database running inside a Docker container. The project showcases a complete CRUD (Create, Read, Update, Delete) functionality and provides a comprehensive demonstration of building RESTful APIs with Java and Spring Boot as well as integrating them with the PostgreSQL database running in Docker.

## Features

- **RESTful APIs**: Implements a full set of CRUD operations through RESTful APIs.
- **Database Integration**: Utilizes PostgreSQL with JPA (Jakarta Persistence API) and Hibernate for data persistence, while running in a Docker container.
- **Terminal API Consumption**: Detailed instructions on how to interact with the APIs using terminal commands.

## Prerequisites

- **Java 11** or later
- **Maven**: For managing dependencies and building the project.
- **Docker**: To run the PostgreSQL database container.
- **PostgreSQL**: The database where all customer data is stored.
- **curl**: To make HTTP requests from the terminal.

## Project Structure

The project contains the following key files:

- `Main.java`: Contains the primary application logic, including the RESTful APIs for managing customers.
- `Customer.java`: Defines the `Customer` entity with attributes such as ID, name, and email.
- `CustomerRepository.java`: Provides the repository interface for `Customer` entities, enabling database operations.

## Running the Project

1. **Clone the repository**:

```bash
    git clone https://github.com/dbouros/Customers-CRUD-REST-APIs.git
```

2. **Set Up the Database**:
    - Make sure Docker is running.
    - Use the provided `docker-compose.yml` file or manually run a PostgreSQL container using:

    ```bash
        docker run --name postgres-container -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres
    ```

    - Ensure the database is accessible and configured correctly in the `application.yml` file.

3. **Build and Run the Application**:
    - Navigate to the project directory and build the project using Maven:

    ```bash
        mvn clean install
    ```
    - **Start the application**:
    ```bash
        mvn spring-boot:run
    ```

4. **Interacting with the APIs**:
    - Use the following terminal commands to interact with the APIs.

## API Endpoints and Usage

### 1. Retrieve All Customers

- **Endpoint**: `GET /api/v1/customers`
- **Description**: Retrieves all customer records.
- **Terminal Command**:

    ```bash
        curl -X GET http://localhost:8080/api/v1/customers
    ```

    - `-X GET`: Specifies the GET method.

### 2. Create a New Customer

- **Endpoint**: `POST /api/v1/customers`
- **Description**: Creates a new customer.
- **Terminal Command**:
    ```bash
        curl -X POST -H "Content-Type: application/json" -d '{"name": "John Doe", "email": "john.doe@example.com", "age": 32}' http://localhost:8080/api/v1/customers
    ```

    - `-X POST`: Specifies the POST method.
    - `-H "Content-Type: application/json"`: Sets the content type to JSON.
    - `-d`: Passes the data to be sent with the request.

### 3. Delete a Customer

- **Endpoint**: DELETE /api/v1/customers/delete_resource/{customerID}
- **Description**: Deletes a customer by their ID.
- **Terminal Command**:

    ```bash
        curl -X DELETE http://localhost:8080/api/v1/customers/delete_resource/1
    ```

    - `{customerID}`: A customer's identifier into the database. Here the customer's identifier is equal to '1'. This means that the customer that has a `customerID` equal to '1', will be deleted.
    - `-X DELETE`: Specifies the DELETE method.

### 4. Update a Customer

- **Endpoint**: PUT /api/v1/customers/update_resource/{customerID}
- **Description**: Updates an existing customer's information.
- **Terminal Command**:

    ```bash
        curl -X PUT -H "Content-Type: application/json" -d '{"email": "jane.doe@example.com"}' http://localhost:8080/api/v1/customers/update_resource/2
    ```

    - `{customerID}`: A customer's identifier into the database. Here the customer's identifier is equal to '2'. This means that the customer that has a `customerID` equal to '2', will now have a different email.
    - `-X PUT`: Specifies the PUT method.
    - `-H "Content-Type: application/json"`: Sets the content type to JSON.
    - `-d`: Passes the data to be sent with the request.

You can also use a file after the `-d` flag to pass larger JSON data for more extensive database updates. For example, you can use `@data.json` to specify a file containing your JSON payload:

```bash
    curl -X POST -H "Content-Type: application/json" -d @data.json http://localhost:8080/api/v1/customers
```

In this example, `data.json` should be a file in your directory containing the appropriate JSON data to create or update customers in the database. This approach is especially useful when dealing with larger datasets.

**For more detailed information about the CRUD APIs and how to set up the project , please refer to the "Notes and Images" directory located in the "resources" folder (`spring-boot-example\src\main\resources`).**

This directory contains valuable insights and visual aids to help you better understand the project's implementation and functionality.
