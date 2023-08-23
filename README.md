# PersonManagement

This is a sample application made for a case for an application process.
It is based on Spring JPA and contains a simple h2 database with Persons in it. Persons can have different relations to one another.

The idea of this repository is to show my personal capabilities with Java, and Spring Boot & Spring JPA in particular.
This application is purely for demo purposes and has specific requirements.

# How to Run
Run the following commands
```agsl
gradle build
```

```agsl
gradle bootRun
```

By default, the application will run on localhost at port 8080.
http://localhost:8080/

# How to Use

The application has multiple endpoints that fit with the requirements provided in the case.
How to use the Endpoints will be described here

### Get All Persons Sorted

Retrieve a list of all persons, sorted based on the given criteria.

- **URL:** `/persons/all`
- **Method:** GET
- **Query Parameters:**
    - `sortBy`: Optional parameter to specify sorting criteria (default: `id`)

Example
```agsl
Request: http://localhost:8080/persons/all?sortBy=name
Response:
[
    {
        "id": 5,
        "name": "Alice",
        "birth_date": "1980-01-01",
        "parent1": {
            "id": 7,
            "name": "Robert",
            "birth_date": "2023-08-24"
        },
        "parent2": {
            "id": 8,
            "name": "Emily",
            "birth_date": "2023-08-24"
        },
        "partner": {
            "id": 1,
            "name": "William",
            "birth_date": "2023-08-24"
        }
    },
    {
        "id": 3,
        "name": "Ella",
        "birth_date": "1980-01-01",
        "parent1": {
            "id": 7,
            "name": "Robert",
            "birth_date": "2023-08-24"
        },
        "parent2": {
            "id": 8,
            "name": "Emily",
            "birth_date": "2023-08-24"
        },
        "partner": {
            "id": 4,
            "name": "Oliver",
            "birth_date": "2015-01-01"
        }
    }
    ]
```
### Get Persons with Partner and Children

Retrieve t a list of all persons who have a partner and three children with that partner and one
of the children has an age below 18.

- **URL:** `/persons/filter`
- **Method:** GET

### Add Person

Add a new person using the provided JSON data.

- **URL:** `/persons/addPerson`
- **Method:** POST
- **Request Body:** PersonRequest data in JSON format


```agsl
{
    "name": "Tom",
    "birthDate": "1996-06-22",
    "parent1Id": 2,
    "parent2Id": 3,
    "childrenIds": [4,5,6],
    "partnerId": 7
}
```

### Delete Person

Delete a person by their ID, considering possible cascading issues.

- **URL:** `/persons/delete/{id}`
- **Method:** DELETE
- **Path Variable:** `id` (ID of the person to be deleted)

### Update Person

Update a person's information by their ID using the provided JSON data.
This overwrites the whole person object.

- **URL:** `/persons/update/{personId}`
- **Method:** PUT
- **Path Variable:** `personId` (ID of the person to be updated)
- **Request Body:** Updated person data in JSON format

```agsl
{
    "name": "Tom",
    "birthDate": "1996-06-22",
    "parent1Id": 2,
    "parent2Id": 3,
    "childrenIds": [4,5,6],
    "partnerId": 7
}
```
