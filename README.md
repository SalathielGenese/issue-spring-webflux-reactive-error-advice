# issue-spring-webflux-reactive-error-advice

A Minimal Code Viable Example for this StackOverflow question: https://stackoverflow.com/q/69519724/3748178

## QUESTION

How to advise (AOP) spring webflux web handlers to catch and transform reactive error?

## What's Inside ?

This repository feature a WebFlux REST endpoint to which we can `POST Account { String name }`
and it will respond `Message { String content }`.

> **NOTE:** I willfully preferred `Mono.error(...)` for validation over
>           Spring's & JSR 303's `@Validated`/`@Valid` in my service...
>           Nor `@Valid` on my controller web handler.

> **NOTE:** `@RestControllerAdvice` with `WebExchangeBindException` handler would work with
>           `@Validated`/`@Valid` or rather synchronous thrown exception.
>           **BUT I REALLy DON'T WANT THAT.**

## Getting Started

+ Clone the repository
+ Ensure you have Maven and JDK 8+ on your path
+ Run tests: `mvn test` - the class cast error has to do with my Aspect in entry class: check its comment

The failing test is my concern.

> Consider the SO question for more context.
