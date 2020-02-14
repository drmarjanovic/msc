# Updates

A part of microservices-based infrastructure that provides managing updates. 
Currently, it is in charge for creating new update and retrieving data for it through `proxy` service.

### Specification

Every route of the service provides JSON responses respecting [JSON API][www:json-api] specification.

Following routes are exposed:

##### Creates an update

- `POST   api/users/:userId/updates`
  It requires JSON body in format given below:

  ```json
  {
	  "data": {
        "type": "updates",
        "attributes": {
          "message": "example",
          "type": "vip"
        }
      }
  }
  ```

##### Retrieving updates
- `GET    api/users/:userId/updates`
  - `page[limit]`     represents limit in offset-based pagination strategy
  - `page[offset]`    represents offset in offset-based pagination strategy


### How to run?

Service requires [PostgreSQL][www:postgresql] database as part of its infrastructure. We've already provided that for you in `docker-compose.yml` file. 

Use following command to provide service infrastructure:

```bash
docker-compose up -d
```

It should run database named `messages` available on port `5444`.

Beside that, it requires following application environment variables:

| Variable           | Description                                                                   | Required |
|--------------------|-------------------------------------------------------------------------------|----------|
| HTTP_HOST          | Host of the service (e.g. **0.0.0.0**).                                       | YES      |
| HTTP_PORT          | Port of the service (e.g. **9003**).                                          | YES      |
| DATABASE_URL       | Database URL (e.g. **postgres://postgres:postgres@localhost:5445/postgres**). | YES      |
| DATABASE_USER      | Database username (e.g. **postgres**).                                        | YES      |
| DATABASE_PASSWORD  | Database password (e.g. **postgres**).                                        | YES      |
| TRACER_HOST        | Host of the [Jaeger][www:jaeger] tracer tool (e.g. **0.0.0.0:9411**).         | NO       |

To setup environment variables in application provide `.env` file in the root of the project.

After configuring, application can be started via command:

```
sbt run
```

Application should be running at the following [link](http://0.0.0.0:9003).

[www:jaeger]: https://www.jaegertracing.io/
[www:json-api]: https://jsonapi.org/
[www:postgresql]: https://www.postgresql.org/
