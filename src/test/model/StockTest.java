package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {
    private Stock stocktest1;
    private Stock stocktest2;
    private Stock stocktest3;

    @BeforeEach
    void runBefore() {
        stocktest1 = new Stock("Banana", "BNNA", 100, 4);
        stocktest2 = new Stock("Orange", "ORGN", 1000, 8);
        stocktest3 = new Stock("Apple", "APPL", 10, 10);
    }

    @Test
    void getCurrentPriceTest() {
        assertTrue(stocktest1.getCurrentPrice() >= 60 && stocktest1.getCurrentPrice() <= 140);
        assertTrue(stocktest2.getCurrentPrice() >= 200 && stocktest2.getCurrentPrice() <= 1800);
        assertFalse(stocktest1.getCurrentPrice() < 0);
        assertFalse(stocktest2.getCurrentPrice() < 0);
    }


}
