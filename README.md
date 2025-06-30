# Automated Testing Assignment

## Assignment Description

### Part 1: NHL Teams API Tests

Using the endpoint `https://qa-assignment.dev1.whalebone.io/api/teams`, implement the following tests:

1. Verify the response returns expected count of teams (32 in total)
2. Verify the oldest team is Montreal Canadiens
3. Verify there's a city with more than 1 team and verify names of those teams
4. Verify there are 8 teams in the Metropolitan division and verify them by their names
5. Open web browser and scrape roster of the oldest team and verify there are more Canadian players than players from USA

### Part 2: UI Testing Playground Tests

Using `http://uitestingplayground.com/`, implement the following tests:

1. From the Home page, navigate to the Sample App page and cover all functionalities
2. On the Home page, click on Load Delay and verify page loads within reasonable time
3. From the Home page, navigate to Progress Bar page and follow instructions in Scenario section

## Technical Requirements

- Language: Java
- Frameworks/Libraries:
  - TestNG (testing framework)
  - Playwright (browser automation)
  - RestAssured (API testing)
  - Spring Boot (dependency management)

## How to Run Tests

### Prerequisites

1. Java JDK 17+ installed
2. Maven installed

### Execution Steps

1. **Clone the repository:**

```
git clone git@github.com:wintrell523/whalebone_case_study.git
cd whalebone_case_study
```

2. **Install Playwright browsers:**

```
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

3. **Run all tests:**

```
mvn test
```

## Test Details

### API Tests (RestAssured)

- Verifies NHL teams endpoint responses
- Includes:
- Team count validation
- Oldest team verification
- Multi-team city check
- Division team validation
- Player nationality comparison

### UI Tests (Playwright)

- Automated browser tests for uitestingplayground.com
- Includes:
- Sample App functionality testing
- Load Delay performance check
- Progress Bar scenario implementation

## Dependencies

Managed through Maven (`pom.xml`):

- TestNG
- Playwright
- RestAssured
- Spring Boot Starter Test
