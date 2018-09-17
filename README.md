# Revolut Interview #
## Short description of used frameworks ##
- Java 1.8, Gradle, Guice, Rapidoid
- HikariCP, JDBI, H2, Flyway
- JUnit, Rest Assured

## Settings ##
Server port can be changed in config file *./src/main/resources/application.conf*

## Application build and startup ##
Run in console
*gradle clean build*
*java -jar ./build/libs/revolut-interview-all-1.0-SNAPSHOT.jar*


## Request/Response examples ##
Request

`http http://127.0.0.1:8888/api/users`

Response

```
[
    {
        "balance": 0,
        "id": 1,
        "name": "user1",
        "version": 1
    },
    {
        "balance": 100000,
        "id": 2,
        "name": "user2",
        "version": 2
    },
    {
        "balance": 500,
        "id": 3,
        "name": "user3",
        "version": 3
    },
    {
        "balance": 0,
        "id": 4,
        "name": "user4",
        "version": 4
    }
]
```

Request

`http PUT "http://do.w.devel:8888/api/users/2/transfer?to_id=4&money=5000"`

Response

```
{
    "status": "OK"
}
```
