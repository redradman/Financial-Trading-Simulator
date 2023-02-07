package model;

import static java.lang.Math.max;

public class Stock {
    private final String name;      // name of the company
    private double currentPrice;    // price at the moment
    private double initialPrice;    // price that was set at first
    private final String ticker;    // ticker of the equity
    private final double volatility;   // the higher the value the more volatile the stock will be


    // REQUIRES: volatility is an integer between or equal to 1 to 10
    // The higher the volatility causes the stock to move more
    public Stock(String name, String ticker, double initialPrice, double volatility) {
        this.name = name;
        this.initialPrice = initialPrice;
        this.ticker = ticker;
        this.volatility = volatility;
        this.currentPrice = getCurrentPrice();
    }


    // MODIFIES: this
    // EFFECTS: sets the new / current price and returns it
    // The price modeling is using the random walk theory that suggests that the movement of
    // the stock is random. The volatility and the price create a range from this range a random value is picked
    // this random value is set as the current / new price.
    public double getCurrentPrice() {
        double movementRange = initialPrice * volatility / 100;
        double min = initialPrice - movementRange;
        double max = initialPrice + movementRange;
        double newPrice = Math.random() * (max - min) + min;
        currentPrice = max(newPrice, 1);    // max function is used to ensure that the newPrice is not lower than 1
        return currentPrice;
    }


    // the getter methods

    public String getName() {
        return name;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public double getVolatility() {
        return volatility;
    }
}
