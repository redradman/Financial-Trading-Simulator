package persistence;

import model.Account;
import model.MyStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.json.*;

public class JsonReaderTest extends JsonTest{


    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Account acc = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderExistingFile() {
        JsonReader reader = new JsonReader("./data/testWriterAccountWithHoldings.json");
        try {
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
            Account account = reader.read();
            String username = "username";
            String password = "password";


            checkAccountConstructor2(account, "username", "password", 987654321,
                    holdings, 10000, 2);
            assertEquals("username", account.getUsername());
            assertEquals("password", account.getPassword());


            // further tests
            assertEquals(3, account.getCurrentHoldings().size());
            assertEquals(3,account.getCurrentHoldings().get(0).getQuantity());
            assertEquals(20,account.getCurrentHoldings().get(1).getQuantity());
            assertEquals(3,account.getCurrentHoldings().get(2).getQuantity());
            assertEquals("APPL",account.getCurrentHoldings().get(0).getTicker());
            assertEquals("ORNG",account.getCurrentHoldings().get(1).getTicker());
            assertEquals("BNNA",account.getCurrentHoldings().get(2).getTicker());

        } catch (IOException e) {
            fail("Could not read from file.");
        }
    }







}
