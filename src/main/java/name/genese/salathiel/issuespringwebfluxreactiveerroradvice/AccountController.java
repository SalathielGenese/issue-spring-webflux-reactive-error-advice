package name.genese.salathiel.issuespringwebfluxreactiveerroradvice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * AccountController
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Oct 11, 2021 @t 21:10:07
 */
@RestController
@RequiredArgsConstructor
public class AccountController {
    public static final String PATH_PREFIX = "/welcome";

    private final AccountService accountService;

    @PostMapping(PATH_PREFIX)
    public Mono<Message> welcome(@RequestBody Account account) {
        return accountService.welcome(account);
    }
}
