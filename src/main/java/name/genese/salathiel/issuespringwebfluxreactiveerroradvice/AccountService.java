package name.genese.salathiel.issuespringwebfluxreactiveerroradvice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

import static java.lang.String.format;
import static reactor.core.publisher.Mono.*;

/**
 * AccountService
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Oct 11, 2021 @t 21:04:19
 */
@Service
@RequiredArgsConstructor
public class AccountService {
    private final Validator validator;

    public Mono<Message> welcome(Account account) {
        return validate(account).map(__ -> new Message(
                format("Willkommen, %s!", account.getName())));
    }

    private Mono<Account> validate(Account account) {
        return defer(() -> {
            final Set<ConstraintViolation<Account>> violations = validator.validate(account);

            if (!violations.isEmpty()) {
                return error(new ConstraintViolationException(violations));
            }

            return just(account);
        });
    }
}
