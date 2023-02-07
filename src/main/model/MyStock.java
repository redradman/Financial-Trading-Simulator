package model;

import org.json.JSONObject;

public class MyStock extends Stock {
    private int quantity;
    private double purchasePrice;

    public MyStock(String name, double initialPrice, int quantity,
                   double purchasePrice, String ticker, double volatility) {
        super(name, ticker, initialPrice, volatility);
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }

    // REQUIRES: the amount is higher than 0
    // MODIFIES: this
    // EFFECTS: increases the number of held stocks (used in the event of a purchase)
    public void increaseQuantity(int amt) {
        quantity = quantity + amt;
    }



    // EFFECTS: convert a MyStock object into a JSON object so that it can be saved in JSON file
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("name", super.getName());
        json.put("initialPrice", super.getInitialPrice());
        json.put("quantity", quantity);
        json.put("purchasePrice", purchasePrice);
        json.put("ticker", super.getTicker());
        json.put("volatility", super.getVolatility());

        return json;
    }

    // REQUIRES: quantity must be higher than or equal to amt
    // MODIFIES: this
    // EFFECTS; reduce the number of held stocks (used in the event of a sale)
    public void decreaseQuantity(int amt) {
        quantity -= amt;
    }

    // the getter methods

    public int getQuantity() {
        return quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }
}
