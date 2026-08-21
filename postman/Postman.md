# Device Management API - Postman Test Suite

This directory contains a comprehensive Postman test suite for the Device Management API.

## Files

- [`DeviceAPI.postman_collection.json`](DeviceAPI.postman_collection.json) - The Postman collection with all test cases
- [`DeviceAPI.postman_environment.json`](DeviceAPI.postman_environment.json) - Postman environment with variables

## Setup Instructions

### 1. Import the Collectireon

1. Open Postman
2. Click **Import** in the top-left corner
3. Select the `DeviceAPI.postman_collection.json` file
4. Click **Import**

### 2. Import the Environment

1. In Postman, click the **Environments** tab (eye icon in the top-right)
2. Click **Import**
3. Select the `DeviceAPI.postman_environment.json` file
4. Click **Import**
5. Select **Device API Environment** from the environment dropdown in the top-right

### 3. Configure the Base URL

Make sure the `baseUrl` variable in the environment matches your running application:

- Default: `http://localhost:8080`
- If your app runs on a different port, update the `baseUrl` variable

### 4. Start the Application

Ensure the Spring Boot application is running:

```bash
./mvnw spring-boot:run
```

Or if using Docker:

```bash
docker-compose up
```

## Test Suite Structure

The collection is organized into folders by endpoint:

### 1. Create Device (`POST /api/devices`)
- ✅ Create device with valid data (201)
- ❌ Missing name - validation error (400)
- ❌ Missing brand - validation error (400)
- ❌ Missing state - validation error (400)
- ❌ Invalid state value - validation error (400)

### 2. Get Device by ID (`GET /api/devices/{id}`)
- ✅ Get existing device (200)
- ❌ Get non-existing device - not found (404)

### 3. Get All Devices (`GET /api/devices`)
- ✅ Get all devices with pagination (200)
- ✅ Get devices with custom page size (200)

### 4. Get Devices by Brand (`GET /api/devices/brand/{brand}`)
- ✅ Get devices for existing brand with pagination (200)
- ✅ Get devices with custom page size (200)
- ✅ Get devices for non-existing brand - empty paged result (200)

### 5. Get Devices by State (`GET /api/devices/state/{state}`)
- ✅ Get devices with AVAILABLE state with pagination (200)
- ✅ Get devices with IN_USE state with pagination (200)
- ✅ Get devices with INACTIVE state with pagination (200)
- ✅ Get devices with custom page size (200)
- ✅ Get devices with no results - empty paged result (200)

### 6. Get All Distinct Brands (`GET /api/devices/brands`)
- ✅ Get all distinct brands (200)

### 7. Update Device (`PATCH /api/devices/{id}`)
- ✅ Full update - update all fields (200)
- ✅ Partial update - update name only (200)
- ✅ Partial update - update state only (200)
- ❌ Update non-existing device - not found (404)
- ❌ Update name of IN_USE device - conflict (409)
- ❌ Update brand of IN_USE device - conflict (409)
- ❌ Missing name - validation error (400)

### 8. Delete Device (`DELETE /api/devices/{id}`)
- ✅ Delete existing device (204)
- ❌ Delete non-existing device - not found (404)
- ❌ Delete IN_USE device - conflict (409)

## Pagination Support

The following endpoints now support pagination via query parameters:

- `GET /api/devices/brand/{brand}?page=0&size=20`
- `GET /api/devices/state/{state}?page=0&size=20`

**Query Parameters:**
- `page` (optional) - Page number (0-indexed), default: 0
- `size` (optional) - Number of items per page, default: 20

**Paged Response Format:**

```json
{
  "success": true,
  "data": {
    "content": [...],
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 100,
    "totalPages": 5,
    "lastPage": false
  }
}
```

## Running the Tests

### Run All Tests

1. Click the **Runner** button (play icon with three dots) in the top-right
2. Select **Device Management API - Test Suite**
3. Select **Device API Environment**
4. Click **Run**

### Run Individual Tests

1. Navigate to the desired request
2. Click **Send**
3. View the test results in the **Tests** tab

## Test Assertions

Each test includes assertions for:
- **Status code** - Verifies the HTTP response code
- **Response structure** - Verifies the JSON response structure
- **Response data** - Verifies the actual data values
- **Error messages** - Verifies error messages for failure cases
- **Pagination metadata** - Verifies page number, size, total elements, and total pages

## Notes

- Some tests depend on data created by previous tests (e.g., device IDs)
- The test suite assumes the database is empty or contains known test data
- For conflict tests (409), ensure devices with ID 2 exists and is in `IN_USE` state
- For delete success test, ensure a device with ID 3 exists and is not in `IN_USE` state

## API Response Format

All successful responses follow this format:

```json
{
  "success": true,
  "data": { ... },
  "message": "Operation successful"
}
```

All error responses follow this format:

```json
{
  "success": false,
  "error": "Error description",
  "message": null
}
```
