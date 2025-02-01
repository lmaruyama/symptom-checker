# Symptom Checker API

This project is a **Symptom Checker** based on **Bayes' Theorem**, built using **Spring Boot 3** and **DynamoDB**. The API allows users to register, log in, and complete a medical assessment by answering symptom-related questions, leading to a probabilistic diagnosis.

## Features
- User Authentication (Register & Login)
- Symptom Assessment using **Bayes' Theorem**
- Stores assessments and user data in **DynamoDB**
- Generates dynamic follow-up questions based on the provided symptoms

---

## Prerequisites
- **Java 17**
- **Maven**
- **Docker & Docker Compose** (for local DynamoDB instance)

## Setting Up DynamoDB Locally
Run the following commands to create the required tables in a **local DynamoDB instance**:

### **Assessment Table**
```sh
aws dynamodb create-table \
    --table-name Assessment \
    --attribute-definitions \
        AttributeName=assessment_id,AttributeType=S \
        AttributeName=user_id,AttributeType=S \
    --key-schema \
        AttributeName=assessment_id,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=assessment_user_index, \
        KeySchema=[{AttributeName=user_id,KeyType=HASH}], \
        Projection={ProjectionType=ALL}, \
        ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}" \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000
```

### **User Table**
```sh
aws dynamodb create-table \
    --table-name User \
    --attribute-definitions \
        AttributeName=user_id,AttributeType=S \
        AttributeName=email,AttributeType=S \
    --key-schema \
        AttributeName=user_id,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=user_email_index, \
        KeySchema=[{AttributeName=email,KeyType=HASH}], \
        Projection={ProjectionType=ALL}, \
        ProvisionedThroughput={ReadCapacityUnits=1,WriteCapacityUnits=1}" \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --endpoint-url http://localhost:8000
```

---

## API Endpoints
### **Authentication**
#### **Register User**
`POST /auth/register`

Registers a new user.
##### Request Body:
```json
{
  "email": "user@example.com",
  "password": "securepassword",
  "age": 30,
  "gender": "MALE"
}
```
##### Response:
```json
User registered successfully.
```

#### **User Login**
`POST /auth/login`

Authenticates a user and returns a session token.
##### Request Body:
```json
{
  "email": "user@example.com",
  "password": "securepassword"
}
```
##### Response:
```json
{
  "user_id": "12345"
}
```

---

### **Assessment Workflow**
#### **Start Assessment**
`POST /assessment/start`

Starts a new medical assessment.
##### Request Body:
```json
{
  "user_id": "12345",
  "initial_symptoms": ["FEVER", "COUGH"]
}
```
##### Response:
```json
{
  "assessment_id": "67890",
  "next_question_id": "Shortness of breath"
}
```

#### **Answer a Question**
`POST /assessment/{assessment_id}/answer`

Submits an answer to a symptom question.
##### Request Body:
```json
{
  "question_id": "Shortness of breath",
  "response": "YES"
}
```
##### Response:
```json
{
  "next_question_id": "Chest Pain"
}
```
*Returns `null` if no further questions are required.*

#### **Get Assessment Result**
`GET /assessment/{assessment_id}/result`

Retrieves the final diagnosis based on the assessment.
##### Response:
```json
{
  "condition": "COVID-19",
  "probabilities": {
    "COVID-19": 0.75,
    "Common Cold": 0.15,
    "Hay Fever": 0.10
  }
}
```

---

## Running the Application
### **Start the Application with Docker Compose**
Run the following command to start the application and DynamoDB:
```sh
docker compose up --build
```

### **Access the API**
After starting, the API will be available at:
```
http://localhost:8080/auth/register
```
