# Shopping List Microservice

A Spring Boot microservice for managing product types and shopping lists.
I created this project as an assignment for an exam during my studies at university. The task was to implement a self-designed application in a team of 4 students. 

## Development and deployment environment

GitLab Repositories with CI/CD pipeline
- automated testing
- deployment to Kubernetes Cluster

## Architectural specifications

Frontend: Android App 
Backend: 
- API Gateway with Circuit-Breaker
- Identity Provider Microservice
- Functional microservices
- MySQL database
- RabbitMQ Messaging Server

## Microservice Architecture

Hexagonal Architekture with Dependency Inversion

## Quality aspects

- High coverage with unit tests (> 80%)
- System tests with Postman
- Measures for transactionality, resilience, monitoring and logging
- Comments with Javadoc
