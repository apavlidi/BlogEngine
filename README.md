# Blog Engine  [![Codacy Badge](https://api.codacy.com/project/badge/Grade/a72f9fb7f3984263b423fd1e3634e55c)](https://www.codacy.com/app/apavlidi/BlogEngine?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=apavlidi/BlogEngine&amp;utm_campaign=Badge_Grade)
Blog Engine is a project that is completely built with Test Driven Development (TTD) and Pair Programming.
The Project uses Spring Boot with Spring Data MongoDB, Maven, and JUnit 5.

## Pair Programming
The project is build with my friend @TetsuyaGR using the Driver-Navigator technique.

## TDD
The project is build fully with TDD by following the three laws of TDD.
 * You can’t write any production code until you have first written a failing unit test.
 * You can’t write more of a unit test than is sufficient to fail, and not compiling is failing.
 * You can’t write more production code than is sufficient to pass the currently failing unit test.

### How to
The project has an embedded tomcat server so you don`t need any application server. However you will need a MongoDB server up and running.
You can download MongoDB here: https://www.mongodb.com/what-is-mongodb

### Hint
The application is currently in development and its not yet fully implemented.

### Run the Test Suite:
`mvn verify -Pfailsafe` 


### Deploy the Application
`mvn deploy`
