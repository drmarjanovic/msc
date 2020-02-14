# Messages

A part of microservicese-based infrastracture that provides managing messages. 
Currently, it is in charge for sending message through `updates` service and retrieving data through `proxy` service.

### Specification

Every route of the service provides JSON responses respecting [JSON API][www:json-api] specification.

Following routes are exposed:

##### Sends a message to specified contact

- `POST   api/users/:userId/contacts/:contactId/messages`
  It requires JSON body in format given below:

  ```json
  {
	  "data": {
        "type": "messages",
        "attributes": {
          "body": "example"
        }
      }
  }
  ```

##### Retrieving messages for specified contact
- `GET    api/users/:userId/contacts/:contactId/messages`


### How to run?

Service requires [PostgreSQL][www:postgresql] database as part of its infrastructure. We've already provided that for you in `docker-compose.yml` file. 

Use following command to provide service infrastructure:

```bash
docker-compose up -d
```

It should run database named `messages` available on port `5445`.

Beside that, it requires following application environment variables:

| Variable           | Description                                                                   | Required |
|--------------------|-------------------------------------------------------------------------------|----------|
| HTTP_HOST          | Host of the service (e.g. **0.0.0.0**).                                       | YES      |
| HTTP_PORT          | Port of the service (e.g. **9004**).                                          | YES      |
| DATABASE_URL       | Database URL (e.g. **postgres://postgres:postgres@localhost:5445/postgres**). | YES      |
| DATABASE_USER      | Database username (e.g. **postgres**).                                        | YES      |
| DATABASE_PASSWORD  | Database password (e.g. **postgres**).                                        | YES      |
| TRACER_HOST        | Host of the [Jaeger][www:jaeger] tracer tool (e.g. **0.0.0.0:9411**).         | NO       |

To setup environment variables in application provide `.env` file in the root of the project.

After configuring, application can be started via command:

```
sbt run
```

Application should be running at the following [link](http://0.0.0.0:9004).

[www:jaeger]: https://www.jaegertracing.io/
[www:json-api]: https://jsonapi.org/
[www:postgresql]: https://www.postgresql.org/
