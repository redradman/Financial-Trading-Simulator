package persistence;

import model.Account;
import model.MyStock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {

    // For checking the signature below
    // signature: Account(String username, String password)
    protected void checkAccountConstructor1(Account account, String username, String password) {
        assertEquals(username, account.getUsername());
        assertEquals(password, account.getPassword());
        assertEquals(0, account.getWrongAttempts());
    }

    // For checking the signature below
    // signature: Account(String username, String password, int authenticationNumber,
    //                   List<MyStock> currentHoldings, double balance, int wrongAttempts)
    protected void checkAccountConstructor2(Account account, String username,
                                            String password, int authenticationNumber,
                                            List<MyStock> currentHoldings, double balance, int wrongAttempts) {
        assertEquals(username, account.getUsername());
        assertEquals(password, account.getPassword());
        assertEquals(authenticationNumber, account.getAuthenticationNumber());
        assertEquals(currentHoldings.size(), account.getCurrentHoldings().size());
        assertEquals(balance, account.getBalance());
        assertEquals(wrongAttempts, account.getWrongAttempts());

    }
}
