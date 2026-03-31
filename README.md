
## Overview

This repository contains two complementary **Selenium + Cucumber BDD automation frameworks** developed using Java that work seamlessly together:

### **Cucumber Framework** (BDD-Focused)
- Behavior-Driven Development approach using Gherkin syntax
- Scenario-based testing with step definitions
- PicoContainer for dependency injection
- Custom and Extent report generation
- Parallel test execution (3 threads)

### **Automation UI Framework** (Page Object Model)
- Traditional Page Object Model design pattern
- Java 17+ implementation with modern practices
- JSON-based test data management
- Lombok annotations for cleaner code
- Comprehensive logging and reporting

Both frameworks follow industry best practices with modular, reusable components and enterprise-level architecture.

---

## Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 17+ |
| **Automation Tool** | Selenium WebDriver 4.18+ |
| **BDD Framework** | Cucumber 7.34.2 |
| **Test Framework** | TestNG 7.9.0 |
| **Build Tool** | Maven 3.6+ |
| **Reporting** | Extent Reports, Custom HTML Reports |
| **Logging** | Log4j2, SLF4J |
| **CI/CD** | Jenkins |
| **Driver Management** | WebDriverManager |
| **Utilities** | Lombok, Jackson, Commons IO |

---

## Framework Features

✅ **BDD Implementation**
- Cucumber Gherkin syntax for business-readable test scenarios
- Modular and reusable step definitions
- Scenario context sharing with Cucumber PicoContainer

✅ **Design Patterns**
- Page Object Model (POM) for maintainable UI interaction code
- Singleton pattern for WebDriver management
- Factory pattern for driver instantiation

✅ **Execution Capabilities**
- Parallel test execution (3 concurrent threads)
- Cross-browser testing (Chrome, Firefox, Edge, Safari)
- Configurable test environments (DEV, QA, STAGING, PROD)

✅ **Advanced Features**
- Hooks for setup and teardown operations
- Screenshot capture on test failures
- Automatic WebDriver binary management
- Data-driven testing with JSON files
- Custom HTML reporting

✅ **Logging & Reporting**
- Comprehensive logging with Log4j2
- Extent Reports integration with detailed insights
- Custom HTML report generation
- TestNG native reports
- Test execution metrics and analytics

✅ **Code Quality**
- Modular and reusable components
- Centralized configuration management
- Exception handling and error reporting
- Code cleanup with Lombok annotations
- Consistent naming conventions

---

## Project Structure

```
.
├── automation-framework/                    # BDD Testing Framework
│   ├── pom.xml                           # Maven configuration
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/                     # Framework utilities
│   │   │   └── resources/
│   │   └── test/
│   │       ├── java/com/test/framework/
│   │       │   ├── constants/            # Test constants
│   │       │   ├── context/              # Scenario context
│   │       │   ├── hooks/                # @Before, @After hooks
│   │       │   ├── manager/              # WebDriver manager
│   │       │   ├── pages/                # Page Object classes
│   │       │   ├── runner/               # Test runner class
│   │       │   ├── stepdefinitions/      # Cucumber step definitions
│   │       │   └── utils/                # Utility classes
│   │       └── resources/
│   │           ├── features/             # .feature files (Gherkin)
│   │           ├── config.properties     # Environment config
│   │           └── log4j2.xml           # Logging configuration
│   ├── target/
│   │   ├── cucumber-reports.html        # Cucumber HTML report
│   │   ├── ExtentReports/              # Extent HTML report
│   │   ├── CustomReports/              # Custom HTML report
│   │   └── screenshots/                # Failure screenshots
│   └── test-output/                    # TestNG output
│
└── 

---

## Running Tests

### Cucumber Framework

#### Run All Tests
```bash
cd cucumber-framework
mvn clean test
```

#### Run Specific Environment
```bash
mvn clean test -Denv=qa
# Environments: dev, qa, staging, prod
```

#### Run Specific Browser
```bash
mvn clean test -Dbrowser=chrome
# Browsers: chrome, firefox, edge, safari
```

#### Run Specific Feature File
```bash
mvn test -Dcucumber.filter.name="Feature Name"
```

#### Run Tests with Tags
```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@regression"
mvn test -Dcucumber.filter.tags="@smoke and @regression"
```

#### Run in Headless Mode
```bash
mvn clean test -Dheadless=true
```

#### Run with Parallel Execution (Default: 3 threads)
```bash
mvn clean test
# Modify thread count in pom.xml if needed
```

## CI/CD Integration

### Jenkins Pipeline

This framework integrates seamlessly with Jenkins for continuous integration and continuous deployment.

#### Example Jenkins Pipeline Flow:

```
GitHub Repository Push
    ↓
Jenkins Webhook Trigger
    ↓
Git Clone/Pull
    ↓
Maven Build (mvn clean install)
    ↓
Run Tests (mvn test)
    ↓
Generate Reports
    ↓
Publish Reports to Jenkins
    ↓
Email Notification to Team
```


## Author
**Gunaseelan**
SDET (Software Development Engineer in Test)
+91 7538808959
