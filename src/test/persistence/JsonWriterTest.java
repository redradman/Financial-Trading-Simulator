package persistence;



import model.Account;
import model.MyStock;
import model.MyStockTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Account account = new Account("user","pass");
            JsonWriter writer = new JsonWriter("./data/my\0SomeFakeNameOfAFileThatDoesNotExist.json");
            writer.open();
            fail("IOException had to be triggered");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterMakingAnAccount() {  // Testing account with no holdings
        try {
            Account account = new Account("user","pass");
            JsonWriter writer = new JsonWriter("./data/testWriterAccountWithNoHoldings.json");
            writer.open();
            writer.write(account);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterAccountWithNoHoldings.json");
            account = reader.read();
            checkAccountConstructor1(account, "user", "pass");

        } catch (IOException e) {
            fail("Exception was not expected and should not have been thrown");
        }
    }

    @Test
    void testWriterMakingAnAccountWithHoldings() {
        try {
            // making a MyStock
            MyStock mystock1 = new MyStock("Apple", 2000, 3,
                    300, "APPL", 5);
            MyStock mystock2 = new MyStock("Orange", 10, 20,
                    10, "ORNG", 2);
            MyStock mystock3 = new MyStock("Banana", 10000, 3,
                    1, "BNNA", 4);

            List<MyStock> holdings = new ArrayList<>();
            holdings.add(mystock1);
            holdings.add(mystock2);
            holdings.add(mystock3);

            Account account = new Account("username","password", 987654321, holdings,
                    10000, 2);
            JsonWriter writer = new JsonWriter("./data/testWriterAccountWithHoldings.json");
            writer.open();
            writer.write(account);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterAccountWithHoldings.json");
            account = reader.read();

            checkAccountConstructor2(account, "username", "password", 987654321,
                    holdings, 10000, 2);

            // additional tests via holdings and MyStock tests
            assertEquals(holdings.size(), account.getCurrentHoldings().size());
            assertEquals("Apple", account.getCurrentHoldings().get(0).getName());
            assertEquals("Orange", account.getCurrentHoldings().get(1).getName());
            assertEquals("Banana", account.getCurrentHoldings().get(2).getName());
            assertEquals(3, account.getCurrentHoldings().get(0).getQuantity());
            assertEquals("ORNG", account.getCurrentHoldings().get(1).getTicker());
            assertEquals(2000, account.getCurrentHoldings().get(0).getInitialPrice());

        } catch (IOException e) {
            fail("Exception was not expected and should not have been thrown");
        }
    }




}
