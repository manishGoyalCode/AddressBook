# 📒 Modular Address Book Service

A Spring Boot-based modular address book service that supports in-memory contact creation, updating, deletion, and searching. Designed to scale to millions of records with efficient ~O(1) operations and strict modularity.

---

## 🚀 Features

- ✅ Create, update, delete, and search contacts
- ✅ In-memory storage using `ConcurrentHashMap`
- ✅ Token-based indexing for O(1) search by name
- ✅ Follows strict API contract
- ✅ Modular design (Controller, Service, Repository, Model)
- ✅ Easily extendable for email/phone search
- ✅ Built-in UUID generation on create

---

## 🧑‍💻 Tech Stack

- Java 17+
- Spring Boot 3.x
- Maven
- Lombok
- JUnit 5 + MockMvc (for testing)

---

## ⚙️ Setup & Run

### 📦 Prerequisites

- Java 17 or higher
- Maven installed

### 🛠️ How to Run

```bash
git clone https://github.com/manishGoyalCode/AddressBook.git
cd AddressBook
mvn clean install
mvn spring-boot:run

``` 

Runs at:
📍 http://localhost:5000

## 📘 API Contract
All APIs accept and return JSON. Fields in responses must follow this strict order:
id, name, phone, email

### 1. Create Contact(s)
POST /create

Request:
```
[
  {
    "name": "Alice Smith",
    "phone": "1234567890",
    "email": "alice@example.com"
  },
  {
    "name": "Bob Jones",
    "phone": "2345678901",
    "email": "bob@example.com"
  }
]
```
Response:

```
[
  {
    "id": "generated-uuid-1",
    "name": "Alice Smith",
    "phone": "1234567890",
    "email": "alice@example.com"
  },
  {
    "id": "generated-uuid-2",
    "name": "Bob Jones",
    "phone": "2345678901",
    "email": "bob@example.com"
  }
]
```
### 2. Update Contact(s)
PUT /update

Request:
```
[
  {
    "id": "generated-uuid-1",
    "phone": "9999999999"
  },
  {
    "id": "generated-uuid-2",
    "email": "newbob@example.com"
  }
]
``` 
Response:

```
[
  {
    "id": "generated-uuid-1",
    "name": "Alice Smith",
    "phone": "9999999999",
    "email": "alice@example.com"
  },
  {
    "id": "generated-uuid-2",
    "name": "Bob Jones",
    "phone": "2345678901",
    "email": "newbob@example.com"
  }
]
``` 
### 3. Delete Contact(s)
DELETE /delete

Request:

```
[
  "generated-uuid-1",
  "generated-uuid-2"
]
```
Response:

```
{
  "deleted": 2
}
```

### 4. Search Contact(s)
POST /search

Request:

```
{
  "query": "Smith"
}
```
Response:
```
[
  {
    "id": "uuid-of-alice",
    "name": "Alice Smith",
    "phone": "1234567890",
    "email": "alice@example.com"
  },
  {
    "id": "uuid-of-charlie",
    "name": "Charlie Smith",
    "phone": "3456789012",
    "email": "charlie@example.com"
  }
]
``` 
### 5. Suggest Duplicates (Exact Match)
GET /suggest-duplicates
Returns groups of contacts that have identical values for name, phone, and email (UUIDs may differ).

Response:

```
[
  [
    {
      "id": "uuid1",
      "name": "John Doe",
      "phone": "1234567890",
      "email": "john@example.com"
    },
    {
      "id": "uuid2",
      "name": "John Doe",
      "phone": "1234567890",
      "email": "john@example.com"
    }
  ],
  [
    {
      "id": "uuid3",
      "name": "Alice Smith",
      "phone": "1112223333",
      "email": "alice@example.com"
    },
    {
      "id": "uuid4",
      "name": "Alice Smith",
      "phone": "1112223333",
      "email": "alice@example.com"
    }
  ]
]
```
#### 🔍 Notes
- Only returns groups of contacts where all three fields (name, phone, email) match 
- Useful to identify and clean exact duplicates in the address book 
- Returns empty array [] if no duplicates are found

## 🧪 Sample Curl Requests
Create Contacts
```
curl -X POST http://localhost:5000/create \
  -H "Content-Type: application/json" \
  -d '[{"name":"Alice Smith","phone":"1234567890","email":"alice@example.com"}]'
```
Search Contacts
```
curl -X POST http://localhost:5000/search \
  -H "Content-Type: application/json" \
  -d '{"query":"Smith"}'
```
Update Contacts
```
curl -X PUT http://localhost:5000/update \
  -H "Content-Type: application/json" \
  -d '[{"id":"<uuid>","phone":"9999999999"}]'
```

Delete Contacts
```
curl -X DELETE http://localhost:5000/delete \
  -H "Content-Type: application/json" \
  -d '["<uuid>"]'
```
## 📂 Project Structure
```
src/main/java/com/example/addressbook
├── model/         # POJOs (Contact, SearchRequest)
├── controller/    # REST controllers
├── service/       # Business logic
├── repository/    # In-memory storage & indexing
└── AddressBookApplication.java
```

## 🎁 Optional Enhancements
Index on email and phone

Suggest duplicates via /suggest-duplicates

## 👨‍💻 Author
Manish Goyal
