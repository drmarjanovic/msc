# Proxy

Service written in [Caliban][www:caliban], a functional [GraphQL][www:graphql] backend in Scala. 

Following **queries** are exposed:
- `me`
  
   Retrieving info for authenticated user.
   
- `contacts`

   Retrieving list of contacts for authenticated user. You can retrieve `messages` in nested query as well.
   
- `updates`

   Retrieving list of updates for authenticated user.

Following **mutations** are exposed:
- `auth`

   Authenticating user.
   
- `sendUpdate`

   Specifying new update scheduled to send.

### How to run?

Service requires following application environment variables:

| Variable              | Description                                                                   | Required |
|-----------------------|-------------------------------------------------------------------------------|----------|
| APPLICATION_SECRET    | Random string used when generating JWT token.                                 | YES      |
| HTTP_HOST             | Host of the service (e.g. **0.0.0.0**).                                       | YES      |
| HTTP_PORT             | Port of the service (e.g. **9000**).                                          | YES      |
| USERS_SERVICE_HOST    | Host of the `users` service (e.g. **0.0.0.0**).                               | YES      |
| USERS_SERVICE_PORT    | Port of the `users` service (e.g. **9001**).                                  | YES      |
| CONTACTS_SERVICE_HOST | Host of the `contacts` service (e.g. **0.0.0.0**).                            | YES      |
| CONTACTS_SERVICE_PORT | Port of the `contacts` service (e.g. **9002**).                               | YES      | 
| UPDATES_SERVICE_HOST  | Host of the `updates` service (e.g. **0.0.0.0**).                             | YES      |
| UPDATES_SERVICE_PORT  | Port of the `updates` service (e.g. **9003**).                                | YES      | 
| MESSAGES_SERVICE_HOST | Host of the `messages` service (e.g. **0.0.0.0**).                            | YES      |
| MESSAGES_SERVICE_PORT | Port of the `messages` service (e.g. **9004**).                               | YES      | 
| TRACER_HOST           | Host of the [Jaeger][www:jaeger] tracer tool (e.g. **0.0.0.0:9411**).         | NO       |

To setup environment variables in application provide `.env` file in the root of the project.

After configuring, application can be started via command:

```
sbt run
```

Application should be running at the following [link](http://0.0.0.0:9000).

[www:caliban]: https://ghostdogpr.github.io/caliban/
[www:graphql]: https://graphql.org/
