# java-test-automation-framework
A scalable test automation framework developed using Java, Selenium WebDriver, Cucumber BDD, and TestNG. The framework supports parallel execution, cross-browser testing,and CI/CD integration with Jenkins. Designed following industry best practices for enterprise-level automation testing.


## Overview
This repository contains a **Selenium + Cucumber BDD automation framework** developed using Java.
The framework follows industry-standard design patterns like **Page Object Model (POM)** and supports scalable automated testing.
This project demonstrates how modern SDET frameworks are designed with maintainability, modularity, and reporting capabilities.
---
## Tech Stack
* Java
* Selenium WebDriver
* Cucumber (BDD)
* TestNG
* Maven
* Jenkins (CI/CD Integration)
---
## Framework Features

* BDD implementation using Cucumber
* Page Object Model design pattern
* Centralized WebDriver management
* Custom HTML reporting
* Logging using Log4j
* Configurable environments
* Scenario context sharing
* Hooks for setup and teardown
* Modular and reusable step definitions
---
## Project Structure
src
 ├── test/java
 │    ├── constants
 │    ├── context
 │    ├── hooks
 │    ├── manager
 │    ├── pages
 │    ├── runner
 │    ├── stepdefinitions
 │    └── utils
 │
 └── test/resources
      ├── features
      ├── log4j2.xml
      └── config.properties
```

## How to Run Tests
### Run from Maven

```
mvn clean test
```

### Run Specific Environment

```
mvn test -Denv=qa
```
### Run Specific Browser
```
mvn test -Dbrowser=chrome
```
---
## Reporting

After execution, reports are generated under:

```
target/
```

Reports include:

* Cucumber HTML Report
* Custom HTML Report
* Execution Logs

---
## CI/CD Integration

This framework can be executed through **Jenkins pipelines** with source code pulled from Git repositories.

Example pipeline flow:

```
GitHub → Jenkins → Build → Execute Tests → Generate Reports
```
---

## Author
Gunaseelan
SDET,
Java | Selenium | Cucumber | TestNG | Jenkins
