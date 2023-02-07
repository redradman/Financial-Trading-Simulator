package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;



// represents an account using the trading system
// the account has: Username, Password, List of current holdings, balance that can be used for purchase,
// the authentication number is random 9-digit number used for changing password and the number of wrong
// attempts when changing the password that can lock the account
public class Account implements Writable {
    private static final double feeRate = 0.001;    // the fee multiplied by the purchase price payable to broker
    private static final int min = 100000000;       // used for generation of 9 digit authenticationNumber
    private static final int max = 999999999;       // used for generation of 9 digit authenticationNumber
    private static double initialBalance = 1000;    // initial balance that is given to a user
    private static final int maxWrongAttempts = 3;
    private int wrongAttempts;                      // number of wrong password reset attempts
    private final String username;                  // the username that the user will use for login
    private String password;                        // the password used for authentication
    private final int authenticationNumber;         // an id is generated randomly to be used for password reset
    private List<MyStock> currentHoldings;          // shows the holdings of the user
    private double balance;                         // the remaining balance of the user that is cash that can be used


    // REQUIRES: username, password should be non-empty strings
    // EFFECTS:  username and password are set to the passed strings
    // current holdings is a list of the held stocks by the account and is set to empty list initially
    // wrongAttempts is the number fo times that attempts for password change have failed and set to 0 initially
    // the authentication number is random number that can be used for password reset
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.currentHoldings = new ArrayList<>();
        this.balance = initialBalance;
        this.wrongAttempts = 0;
        this.authenticationNumber = (int)(Math.random() * (max - min + 1) + min);
        // random function is used to generate a numeric value between 0 and 1 and is then scaled and converted
        // to a 9 digit int
    }


    public Account(String username, String password, int authenticationNumber,
                   List<MyStock> currentHoldings, double balance, int wrongAttempts) {
        this.username = username;
        this.password = password;
        this.currentHoldings = currentHoldings;
        this.balance = balance;
        this.wrongAttempts = wrongAttempts;
        this.authenticationNumber = authenticationNumber;
    }

    // the username is required for security purposes
    // REQUIRES: username and newPassword have to be non-empty strings
    // MODIFIES: this
    // EFFECTS: Changes the password of the user to the new password if the passed authentication number is accurate
    // along with the correct username and the max wrong attempts has not been reached the number of wrong password
    // reset attempts are below or equal to the max wrong attempts
    public boolean resetPassword(String username, String newPassword, int num) {
        if (canPasswordBeChanged(num, username)) {
            this.password = newPassword;
            resetWrongAttempts();
            return true;
        } else {
            wrongAttempts += 1;
            return false;
        }
    }

    // EFFECTS: returns true if the conditions for changing password are met
    //          1 - passedAuthNumber must match the generated one held in account
    //          2 - passedUsername must match the account's username
    //          3 - maxWrongAttempts should not have been exceeded
    public boolean canPasswordBeChanged(int passedAuthNumber, String passedUsername) {
        return doAuthenticationNumbersMatch(passedAuthNumber) && wrongAttemptsNotExceeded()
                && doUsernamesMatch(passedUsername);
    }

    // EFFECTS: returns true if the passedAuthNumber matches the generated one held in account and false otherwise
    public boolean doAuthenticationNumbersMatch(int passedAuthNumber) {
        return authenticationNumber == passedAuthNumber;
    }

    // EFFECTS: returns true if the number of wrong attempts is not exceeded
    //          NOTE: if the number is exceeded the user cannot change the password
    public boolean wrongAttemptsNotExceeded() {
        return wrongAttempts <= maxWrongAttempts;
    }

    // EFFECTS: returns true if the passed username matches the real username and false otherwise
    public boolean doUsernamesMatch(String passedUsername) {
        return username.equals(passedUsername);
    }


    // REQUIRES: amount is >= 0
    // MODIFIES: this
    // EFFECTS: adds amount to the user's balance
    protected void addBalance(double amount) {
        this.balance += amount;
    }




    // REQUIRES: quantity has to be an integer
    // MODIFIES: this
    // EFFECTS: buys the stock if the user has enough balance and is added to the holdings
    public boolean buyStock(int quantity, Stock stock) {
        double currentPrice = stock.getCurrentPrice();
        double cost = quantity * stock.getCurrentPrice();
        MyStock mystock = new MyStock(stock.getName(), stock.getInitialPrice(),
                quantity, currentPrice, stock.getTicker(),
                stock.getVolatility());
        double fee = calculateFee(cost);

        if (sufficientBalance(cost)) {
            // TODO add event log here
            EventLog.getInstance().logEvent(new
                    Event(this.username + ", bought " + Integer.toString(quantity)
                    + " shares of " + stock.getName() + " with the ticker " + stock.getTicker()));
            balance -= cost;
            balance -= fee;
            addToHoldings(mystock);
            return true;
        } else {
            return false;
        }
    }



    // REQUIRES: the stockTicker must be the ticker of a stock in the current holdings
    // MODIFIES: this
    // EFFECTS: sells stocks that are in the current holdings of the account
    public void sellStock(int quantity, MyStock mystock) {
        double cost = quantity * mystock.getCurrentPrice();

        // TODO add event log here
        EventLog.getInstance().logEvent(new
                Event(this.username + ", sold " + Integer.toString(quantity)
                + " shares of " + mystock.getName() + " with the ticker " + mystock.getTicker()));

        if (mystock.getQuantity() > quantity) {
            mystock.decreaseQuantity(quantity);
        } else if (mystock.getQuantity() == quantity) {
            removeFromHoldings(mystock);
        }

        balance -= calculateFee(cost);
        balance += cost;

    }

    // REQUIRES: cost cannot be negative
    // EFFECTS: returns true only if there is sufficient balance for the transaction and the fees associated with it
    protected boolean sufficientBalance(double cost) {
        double transactionFee = calculateFee(cost);
        double minimumAmount = cost + transactionFee;
        if (balance >= minimumAmount) {
            // TODO add event log here
            EventLog.getInstance().logEvent(new
                    Event(this.username + ", " + "has sufficient balance"));
            return true;
        } else {
            // TODO add event log here
            EventLog.getInstance().logEvent(new
                    Event(this.username + ", " + "does NOT" + "have sufficient balance"));
            return false;
        }
    }



    // EFFECTS: converts an account such that it can be represented as a JSON Object and returns the object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        json.put("authenticationNumber",authenticationNumber);
        json.put("currentHoldings", currentHoldingsToJson());
        json.put("balance", balance);
        json.put("wrongAttempts", wrongAttempts);
        return json;
    }

    // EFFECTS: returns the stocks held by user(type: MyStock) in current holdings as a JSON array
    public JSONArray currentHoldingsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (MyStock ms: currentHoldings) {
            jsonArray.put(ms.toJson());
        }

        return jsonArray;
    }













    // REQUIRES: price cannot be negative
    // EFFECTS: calculate and returns the fees associated with the trade
    public double calculateFee(double price) {
        return price * feeRate;
    }

    // MODIFIES: this
    // EFFECTS: resets the number of wrong attempts
    public void resetWrongAttempts() {
        wrongAttempts = 0;
    }

    protected void addToHoldings(MyStock stock) {
        currentHoldings.add(stock);
    }

    protected void removeFromHoldings(MyStock stock) {
        currentHoldings.remove(stock);
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public List<MyStock> getCurrentHoldings() {
        return currentHoldings;
    }

    public int getAuthenticationNumber() {
        return authenticationNumber;
    }

    public String getPassword() {
        return password;
    }

    public int getWrongAttempts() {
        return wrongAttempts;
    }

    public double getfeeRate() {
        return feeRate;
    }
}
