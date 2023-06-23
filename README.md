
# Schema Migration in MongoDB in Zero Downtime

The repository hosts the code that implements Schema Migration with Zero Downtime mentioned in the following blog - [Changing Database Schema with Zero Downtime](https://medium.com/@adityasrivastava_54942/changing-database-schema-with-zero-downtime-part-2-f4d0c1a419b9)

## Documentation

Part 1 - [Changing Database Schema with Zero Downtime — Part 1
](https://medium.com/@adityasrivastava_54942/changing-database-schema-with-zero-downtime-18ea49475512)



Part 2 - [Changing Database Schema with Zero Downtime — Part 2](https://medium.com/@adityasrivastava_54942/changing-database-schema-with-zero-downtime-part-2-f4d0c1a419b9)



## Installation

Install my-project with npm

```bash
  cd schema_migrator
  mvn clean install 
  mvn compile exec:java -Dexec.mainClass="com.schema_migrator.mongodb.App"
```
    
## Usage/Examples

*Sample Document*

```javascript
{
  "_id": {
    "$oid": "648a37b4d74e4d4f51053da4"
  },
  "age": 30,
  "createdDate": "2012-01-01",
  "emailAddress": "test@test.com",
  "firstName": "Addy",
  "lastName": "Srivastava",
  "phoneNumber": [
    "12355333"
  ],
  "preferences": "Veg:Non-Veg",
  "schemaVersion": 1,
  "tags": []
}
```

*Fetch latest user details*
```javascript
curl -X GET \
  'localhost:7070/user/648a37b4d74e4d4f51053da4' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)'
```




## API Reference

#### Get single user

```http
  GET /user/{userId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `string` | **Required**. Fetch a user with userid  |

#### Create one user

```http
  POST /api/user/
```

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `json`      | `string` | **Required**. To create a new user in the DB |





## Authors

- [AddySrivastava](https://github.com/AddySrivastava)

