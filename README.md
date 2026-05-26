# BFHL API - Bajaj Finserv Health API Round

A Spring Boot REST API that processes an input data array and returns categorized results.

## Features

- **POST /bfhl** - Processes input data array and returns:
  - Even/Odd number classification
  - Alphabet extraction
  - Special character detection
  - Sum of all numbers
  - Concatenation string (reversed alphabets with alternating caps)
  
- **GET /bfhl** - Returns operation code (health check)

## Tech Stack

- Java 21
- Spring Boot 3.3.5
- Maven

## API Usage

### POST /bfhl

**Request:**
```json
{
  "data": ["M", "1", "334", "4", "R", "9"]
}
```

**Response:**
```json
{
  "is_success": true,
  "user_id": "john_doe_17091999",
  "email": "john@xyz.com",
  "roll_number": "ABCD123",
  "odd_numbers": ["1", "9"],
  "even_numbers": ["334", "4"],
  "alphabets": ["M", "R"],
  "special_characters": [],
  "sum": "348",
  "concat_string": "Rm"
}
```

## Running Locally

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Or run the JAR directly
java -jar target/bfhl-api-0.0.1-SNAPSHOT.jar
```

The API will be available at `http://localhost:8080/bfhl`

## Running Tests

```bash
./mvnw test
```

## Deployment

### Using Docker
```bash
docker build -t bfhl-api .
docker run -p 8080:8080 bfhl-api
```

### On Render
1. Push code to GitHub
2. Create a new Web Service on Render
3. Set build command: `./mvnw clean package -DskipTests`
4. Set start command: `java -jar target/bfhl-api-0.0.1-SNAPSHOT.jar`
5. Set environment variable: `PORT=8080`

## Configuration

Update your personal details in `src/main/resources/application.properties`:

```properties
app.user.full-name=your_name_here
app.user.email=your_email@example.com
app.user.roll-number=YOUR_ROLL_NUMBER
app.user.dob=DDMMYYYY
```
