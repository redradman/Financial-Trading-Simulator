package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account accountTest1;
    private Account accountTest2;
    private Account accountTest3;
    private Account accountTest4;
    private MyStock myStockTest1;
    private MyStock myStockTest2;
    private Stock stocktest1;
    private Stock stocktest2;


    @BeforeEach
    void runBefore() {
        accountTest1 = new Account("McKinnon", "AJ3012");


        List<MyStock> noHoldings = new ArrayList();
        double balance = 1000;
        accountTest2 = new Account("Alcazar", "UJQ1045",123456789,
                noHoldings, balance, 0);


        myStockTest1 = new MyStock("Banana", 1000,
                8, 888, "BNNA", 5);
        myStockTest2 = new MyStock("Orange", 100,
                80, 100, "ORGN", 2);
        stocktest1 = new Stock("Banana", "BNNA", 100, 4);
        stocktest2 = new Stock("Orange", "ORGN", 1000, 8);

        List<MyStock> myStockTest1Only = new ArrayList<>();
        myStockTest1Only.add(myStockTest1);

        accountTest3  = new Account("user3", "pass3", 123456789, myStockTest1Only, 1000, 0);
        accountTest4  = new Account("user4", "pass4", 123456789, noHoldings, 1000, 4);
    }

    @Test
    void AccountTest() {
        // for testing the constructor that is used by JSON

        List<MyStock> noHoldings = new ArrayList();
        double balance = 1000;
        Account accounttest1 = new Account("name", "pass",123456789,
                noHoldings, balance, 1);
        assertEquals(accounttest1.getUsername(), "name");
        assertEquals(accounttest1.getPassword(), "pass");
        assertEquals(accounttest1.getAuthenticationNumber(), 123456789);
        assertEquals(accounttest1.getCurrentHoldings(), new ArrayList<>());
        assertEquals(accounttest1.getWrongAttempts(), 1);
    }

    @Test
    void resetPasswordTest() {
        int auth1 = accountTest1.getAuthenticationNumber();
        assertTrue(accountTest1.resetPassword("McKinnon", "newpassword", auth1)); // passes
        assertFalse(accountTest1.resetPassword("fakeusername", "newpassword", auth1)); // fails username check
        assertEquals(accountTest1.getPassword(),"newpassword");

        int auth2 = accountTest2.getAuthenticationNumber();
        assertEquals(accountTest2.getWrongAttempts(),0);
        assertFalse(accountTest2.resetPassword("McKinnon", "newpassword", 1));
        assertEquals(accountTest2.getWrongAttempts(),1);
        assertFalse(accountTest2.resetPassword("McKinnon", "newpassword", 1));
        assertEquals(accountTest2.getWrongAttempts(),2);
        assertFalse(accountTest2.resetPassword("McKinnon", "newpassword", 1));
        assertFalse(accountTest2.resetPassword("McKinnon", "newpassword", auth2));
        assertFalse(accountTest2.resetPassword("McKinnon", "newpassword", auth1)); // maxWrongAttempts is exceeded
        assertEquals(accountTest2.getWrongAttempts(), 5);
        assertEquals(accountTest2.getPassword(),"UJQ1045");
    }

    @Test
    void canPasswordBeChangedTest() {
        // all conditions are true
        assertTrue(accountTest2.canPasswordBeChanged(123456789, "Alcazar"));

        // passed auth number is wrong
        assertFalse(accountTest2.canPasswordBeChanged(1, "Alcazar"));

        // passed username is wrong
        assertFalse(accountTest2.canPasswordBeChanged(123456789, "wrongusername"));

        // passed auth number and passed username are wrong
        assertFalse(accountTest2.canPasswordBeChanged(2, "wrongusername"));
    }

    @Test
    void doAuthenticationNumbersMatchTest() {
        assertTrue(accountTest2.doAuthenticationNumbersMatch(123456789));
        assertFalse(accountTest2.doAuthenticationNumbersMatch(3));
    }

    @Test
    void wrongAttemptsNotExceededTest() {
        assertTrue(accountTest2.wrongAttemptsNotExceeded());
        assertFalse(accountTest4.wrongAttemptsNotExceeded());
    }

    @Test
    void doUsernamesMatchTest() {
        assertTrue(accountTest2.doUsernamesMatch("Alcazar"));
        assertFalse(accountTest2.doUsernamesMatch("wrongUsername"));
    }

    @Test
    void addBalanceTest() {
        assertEquals(accountTest1.getBalance(),1000);   // prior to addition
        accountTest1.addBalance(1000);
        assertEquals(accountTest1.getBalance(),2000);   // after addition

        assertEquals(accountTest2.getBalance(),1000);   // prior to addition
        accountTest2.addBalance(1000);
        assertEquals(accountTest2.getBalance(),2000);   // after addition
    }

    @Test
    void buyStockTest() {
        assertTrue(accountTest1.buyStock(4, stocktest1));
        assertFalse(accountTest2.buyStock(120, stocktest1));

    }

    @Test
    void sellStockTest() {
        accountTest1.buyStock(4, stocktest1);
        MyStock myStockToSell = accountTest1.getCurrentHoldings().get(0);

        assertTrue(accountTest1.getCurrentHoldings().size() == 1);
        accountTest1.sellStock(4,myStockToSell);
        assertTrue(accountTest1.getCurrentHoldings().size() == 0);

        accountTest2.buyStock(4, myStockTest2);
        assertTrue(accountTest2.getCurrentHoldings().size() == 1);
        accountTest1.sellStock(2,myStockToSell);
        assertTrue(accountTest2.getCurrentHoldings().size() == 1);
        assertEquals(accountTest2.getCurrentHoldings().get(0).getQuantity(), 4);

        assertEquals(accountTest3.getCurrentHoldings().size(), 1);
        accountTest3.sellStock(8, myStockTest1);
        assertEquals(accountTest3.getCurrentHoldings().size(), 0);

    }

    @Test
    void sufficientBalanceTest() {
        assertFalse(accountTest1.sufficientBalance(1000));
        assertFalse(accountTest1.sufficientBalance(1100));
        assertTrue(accountTest1.sufficientBalance(900));

        assertFalse(accountTest2.sufficientBalance(1000));
        assertFalse(accountTest2.sufficientBalance(1100));
        assertTrue(accountTest2.sufficientBalance(900));

    }

    @Test
    void calculateFeeTest() {
        assertEquals(accountTest1.calculateFee(100), accountTest1.getfeeRate() * 100);
        assertEquals(accountTest2.calculateFee(999), accountTest2.getfeeRate() * 999);
    }

    @Test
    void resetWrongAttemptsTest() {
        assertEquals(accountTest2.getWrongAttempts(),0);
        assertFalse(accountTest2.resetPassword("McKinnon", "pwd", 1));
        assertEquals(accountTest2.getWrongAttempts(),1);
        assertFalse(accountTest2.resetPassword("McKinnon", "pwd", 1));
        assertEquals(accountTest2.getWrongAttempts(),2);

        accountTest2.resetWrongAttempts();
        assertTrue(accountTest2.getWrongAttempts() == 0);
    }


    @Test
    void addToHoldingsTest() {
        assertTrue(accountTest1.getCurrentHoldings().size() == 0);
        accountTest1.addToHoldings(myStockTest1);
        assertTrue(accountTest1.getCurrentHoldings().size() == 1);
    }

    @Test
    void removeFromHoldings() {
        assertTrue(accountTest2.getCurrentHoldings().size() == 0);
        accountTest2.addToHoldings(myStockTest1);
        assertTrue(accountTest2.getCurrentHoldings().size() == 1);
        accountTest2.removeFromHoldings(myStockTest1);
        assertTrue(accountTest2.getCurrentHoldings().size() == 0);


    }

    @Test
    void getUserNameTest() {
        assertEquals(accountTest1.getUsername(), "McKinnon");
    }


}
