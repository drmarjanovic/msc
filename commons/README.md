[![api-module][api-module]](api-module)
[![tracing-module][tracing-module]](tracing-module)

# Commons

This project contains two modules: `api` and `tracing`.
Each of this modules contain common code for other services. 

## api module

Contains common code for HTTP API. We have defined several entities respecting [JSON API][www:json-api] specification that are used in other services.

## tracing module

Contains common code for tracing. One of the most important parts is [Jaeger][www:jaeger] tracer setup. It is kept on single place, and changing tracing vendor is very simple. Beside that, it contains often used methods as `extractTracingHeaders`, etc. Also, it contains extensions for `Span` (`failed()` method) and handling failures for `Option` datatype.

[api-module]: https://img.shields.io/badge/api--module-1.0.2-blue
[tracing-module]: https://img.shields.io/badge/tracing--module-1.0.1-blue

[www:jaeger]: https://www.jaegertracing.io/
[www:json-api]: https://jsonapi.org/
