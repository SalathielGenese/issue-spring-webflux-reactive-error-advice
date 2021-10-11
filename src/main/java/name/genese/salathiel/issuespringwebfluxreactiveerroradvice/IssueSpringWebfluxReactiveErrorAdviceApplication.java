package name.genese.salathiel.issuespringwebfluxreactiveerroradvice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.ResponseEntity.status;
import static reactor.core.publisher.Mono.just;

@SpringBootApplication
@EnableAspectJAutoProxy
public class IssueSpringWebfluxReactiveErrorAdviceApplication {
    public static void main(String[] args) {
        run(IssueSpringWebfluxReactiveErrorAdviceApplication.class, args);
    }

    /**
     * Remap {@link ConstraintViolationException} around reactive web handler to
     * <br>
     * <br>
     * <pre>
     * HTTP 422
     *
     * [ ...{ String[] paths; String template; } ]
     */
    @Aspect
    @Component
    static class AroundReactiveWebHandler {
        @Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
        public Object aroundPostMapping(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            // Catch both sync and reactive errors
            try {
                final Object proceed = proceedingJoinPoint.proceed();

                if (proceed instanceof Mono) {
                    try {
                        return just(((Mono<?>) proceed).toFuture().get());
                    } catch (ExecutionException executionException) {
                        throw executionException.getCause();
                    }
                }

                return proceed;
            } catch (ConstraintViolationException constraintViolationException) {
                final List<Violation> violations = constraintViolationException.getConstraintViolations().stream()
                        .map(violation -> new Violation(
                                violation.getMessageTemplate(),
                                stream(violation.getPropertyPath().spliterator(), false)
                                        .map(Node::getName)
                                        .collect(Collectors.toList())
                        )).collect(Collectors.toList());

                // NOTE: java.lang.ClassCastException: class org.springframework.http.ResponseEntity cannot be cast to class reactor.core.publisher.Mono
                //       But if wrapped in a Mono ```return just( status(UNPROCESSABLE_ENTITY).body(violations) );```, the response becomes
                //
                //      < 200 OK OK
                //      < Content-Type: [application/json]
                //      < Content-Length: [159]
                //
                //      {"headers":{},"body":[{"template":"{javax.validation.constraints.NotNull.message}","path":["name"]}],"statusCode":"UNPROCESSABLE_ENTITY","statusCodeValue":422}
                return status(UNPROCESSABLE_ENTITY).body(violations);
            }
        }

        @Getter
        @RequiredArgsConstructor
        static class Violation {
            private final String template;
            private final List<String> path;
        }
    }
}
