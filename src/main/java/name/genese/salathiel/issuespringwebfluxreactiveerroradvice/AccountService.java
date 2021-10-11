package name.genese.salathiel.issuespringwebfluxreactiveerroradvice;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static reactor.core.publisher.Mono.just;

/**
 * AccountService
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Oct 11, 2021 @t 21:04:19
 */
@Service
public class AccountService {
    Mono<Message> welcome(Account account) {
        return just(new Message(
                format("Willkommen, %s!", account.getName())
        ));
    }
}
