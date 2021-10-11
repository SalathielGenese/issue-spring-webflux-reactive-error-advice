package name.genese.salathiel.issuespringwebfluxreactiveerroradvice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Account
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Oct 11, 2021 @t 21:02:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @NotNull
    private String name;
}
