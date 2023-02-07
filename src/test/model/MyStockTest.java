package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyStockTest {
    private MyStock myStockTest1;
    private MyStock myStockTest2;


    @BeforeEach
    void runBefore() {
        myStockTest1 = new MyStock("Banana", 1000, 8, 888, "BNNA", 5);
        myStockTest2 = new MyStock("Orange", 100, 80, 100, "ORGN", 2);
    }


    @Test
    void increaseQuantityTest() {
        // for my stock test 1
        assertEquals(myStockTest1.getQuantity(), 8);  // prior to increase
        myStockTest1.increaseQuantity(2);
        assertEquals(myStockTest1.getQuantity(), 10); // after the increase

        // for my stock test 2
        assertEquals(myStockTest2.getQuantity(), 80);   // prior to increase
        myStockTest2.increaseQuantity(11);
        assertEquals(myStockTest2.getQuantity(), 91);   // after the increase

        myStockTest2.increaseQuantity(0);
        assertEquals(myStockTest2.getQuantity(), 91);


        myStockTest2.increaseQuantity(10);

    }

    @Test
    void decreaseQuantityTest() {
        assertEquals(myStockTest1.getQuantity(), 8);
        myStockTest1.decreaseQuantity(5);
        assertEquals(myStockTest1.getQuantity(), 3);

        assertEquals(myStockTest2.getQuantity(), 80);
        myStockTest2.decreaseQuantity(80);
        assertEquals(myStockTest2.getQuantity(), 0);

    }

    @Test
    void getQuantityTest() {
        assertEquals(myStockTest1.getQuantity(), 8);
        assertEquals(myStockTest2.getQuantity(), 80);
    }

    @Test
    void getPurchasePriceTest() {
        assertEquals(myStockTest1.getPurchasePrice(), 888);
        assertEquals(myStockTest2.getPurchasePrice(), 100);
    }

}
