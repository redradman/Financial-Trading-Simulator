package ui;

import model.Account;
import model.MyStock;
import model.Stock;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TradingApp {
    private Account currentUser;
    private List<Account> accounts = new ArrayList<>();
    private List<Stock> stocks = new ArrayList<>();
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/account.json";

    // EFFECTS: constructs the testnet trading app
    public TradingApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runTradingApp();
    }


    // MODIFIES: this
    // EFFECTS: processes user input
    public void runTradingApp() {
        boolean keepRunningApp = true;
        String command;

        // INITIALIZING VALUES AND ADDING STOCKS TO BE BOUGHT AND MAKING AN ACCOUNT AND SIGNING IN
        init();

        // WELCOME MESSAGE
        System.out.println("Hello and Welcome to Testnet trading program. \n" + "A place to learn how to trade "
                + "with the peace of mind");

        // PROCESSING
        while (keepRunningApp) {
            displayMenu();  // the options that the user has
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("quit")) {
                keepRunningApp = false;
            } else {
                processCommand(command);
            }
        }
        // GOODBYE MESSAGE
        System.out.println("\n Thank you for using our testnet trading program");
    }

    // MODIFIES: this
    // EFFECTS: Asks if the user wants to load data and if there are no accounts the user is prompted to make
    //          a new account
    // NOTE: the user is ONLY able to load information here (this function is executed once)
    public void init() {
        createStocks(); // creating some stocks that can be purchased in the testnet
        System.out.println("Do you want to load account data? Please note that you can only do this once");
        System.out.println("\tload -> Load account information");
        System.out.println("\tno -> do NOT load any information");
        String response = input.next();
        if (response.equals("load")) {
            loadAccount();
        }

        if (accounts.size() == 0) {
            System.out.println("Hi. You are being directed to make a new account.");
            makeAccount();
        }

        signIn(); // for user to sign in
    }

    // EFFECTS: creates the stocks that can be traded on the platform
    public void createStocks() {
        Stock bananaCorp;
        Stock evilCorp;
        Stock angelCorp;
        Stock exoPlanetCorp;
        bananaCorp = new Stock("Banana Corp", "BNNA", 1000, 4);
        evilCorp = new Stock("Evil Corp", "EVIL", 300, 6);
        angelCorp = new Stock("Angel Corp", "ANGL", 100, 5);
        exoPlanetCorp = new Stock("ExoPlanet", "EXPL", 20000, 3);

        stocks.add(bananaCorp);
        stocks.add(evilCorp);
        stocks.add(angelCorp);
        stocks.add(exoPlanetCorp);
    }

    // MODIFIES: this
    // EFFECTS: is used for already existing users to login
    public void signIn() {
        System.out.println("Please choose one of the options below: ");
        System.out.println("\tlogin");
        System.out.println("\tnewAccount");
        String loginOrNewAccount = input.next();
        if (loginOrNewAccount.equals("newAccount")) {
            makeAccount();
        } else {
            System.out.println("Please enter your username: ");
            String enteredUsername = input.next();
            Account userAboutToSignIn;
            System.out.println("Please enter your password: ");
            String enteredPassword = input.next();
            for (Account account : accounts) {
                if (enteredPassword.equals(account.getPassword()) && enteredUsername.equals(account.getUsername())) {
                    userAboutToSignIn = account;
                    currentUser = userAboutToSignIn;
                } else {
                    System.out.println("Incorrect username or password");
                    signIn();
                }
            }
        }
    }

    // REQUIRED: username cannot be an already existing value. Both username and password
    //           are non-empty strings.
    // MODIFIES: this
    // EFFECTS: creates a new account and appends it to the list containing the accounts
    public void makeAccount() {
        System.out.println("Enter a username: ");
        String enteredUsername = input.next();
        System.out.println("Enter a password: ");
        String enteredPassoword = input.next();

        Account newUser = new Account(enteredUsername, enteredPassoword);
        accounts.add(newUser);
        System.out.println("Congrats. A new account has been made.");
        System.out.println("Please write down your authentication number below (it is used for all password resets) ");
        System.out.println(newUser.getAuthenticationNumber());
    }





    // EFFECTS: simply returns the commands that can be used for working with the program
    public void displayMenu() {
        System.out.println("Please choose the action you would like to take from the list below: \n");
        // commands for interacting with the program
        System.out.println("\tbuy -> Buy Stock");
        System.out.println("\tsell -> Sell Stock");
        System.out.println("\treset -> Reset Password");
        System.out.println("\tholdings ->  Current Holdings");
        System.out.println("\tauth -> Authentication Number");
        System.out.println("\tsave -> Save the account information");

        // command for exiting / terminating the program
        System.out.println("\tquit -> Terminate the program");
    }


    // MODIFIES: this
    // EFFECTS: processes user command
    public void processCommand(String command) {
        if (command.equals("buy")) {
            buyStock();
        } else if (command.equals("sell")) {
            sellStock();
        } else if (command.equals("reset")) {
            reset();
        } else if (command.equals("holdings")) {
            allHoldings();
        } else if (command.equals("save")) {
            saveAccount();
        } else if (command.equals("auth")) {
            System.out.println("Your authentication number is: " + currentUser.getAuthenticationNumber());
        }
    }

    ///////////////////////////////////////////////////
    // Buy stock and its dependencies

    // REQUIRED: Purchasing amount cannot be higher than user's balance and
    // enteredQuantity cannot be negative, selectedStock must be from the listOfStock
    // MODIFIES: this
    // EFFECTS: buys stock using money from the balance and adds it to holdings
    public void buyStock() {
        System.out.println("You are about to buy a stock");
        listOfStocks();
        Stock stockToBePurchased = selectedStock();
        int enteredQuantity = quantityOfStock();
        currentUser.buyStock(enteredQuantity, stockToBePurchased);
    }

    // EFFECTS: shows all the stocks that can be purchased in the market at the time of the request
    public void listOfStocks() {
        System.out.println("Please select one of the stocks below:");
        for (Stock stock: stocks) {
            System.out.println("\t" + stock.getTicker() + " -> " + stock.getName() + " Price: "
                    + stock.getCurrentPrice());
        }
    }

    // REQUIRED: selectedStock must be a stock in the stocks list
    // EFFECTS: identifies and returns the stock object associated with the user's entry
    public Stock selectedStock() {
        System.out.println("Enter the ticker of the stock you want to buy. ENTER THE TICKER IN ALL CAPS: ");
        String stockTicker = input.next();
        Stock selectedStock;
        System.out.println(stockTicker + " has been selected.");
        for (Stock stock: stocks) {
            if (stockTicker.equals(stock.getTicker())) {
                selectedStock = stock;
                return selectedStock;
            }
        }
        return null;
    }

    // REQUIRES: the returned value must be a positive integer
    // EFFECTS: returns the number of stocks to be purchased / sold
    public int quantityOfStock() {
        System.out.println("Available balance is: " + currentUser.getBalance());
        System.out.println("Please enter the quantity: ");
        int enteredQuantity = input.nextInt();
        return enteredQuantity;
    }

    ///////////////////////////////////////////////////
    // Sell stock and its dependencies

    // REQUIRES: the stock about to be sold must be in holdings and quantity to be sold cannot exceed the held quantity
    // MODIFIES: this
    // EFFECTS: this function is used by the user to sell some or all the quantity of a stock they have
    // in their holdings
    public void sellStock() {
        System.out.println("You are about to sell a stock");
        allHoldings();
        System.out.println("Please select one of the stocks above that you hold"
                + ". ENTER THE TICKER IN ALL CAPS: ");
        MyStock stockToBeSold = chosenStock();
        int enteredQuantity = quantityOfStock();
        currentUser.sellStock(enteredQuantity, stockToBeSold);
    }

    // REQUIRED: selectedStock must be a stock in the stocks list
    // EFFECTS: identifies and returns the stock object associated with the user's entry
    public MyStock chosenStock() {
        String stockTicker = input.next();
        MyStock chosenStock;
        System.out.println(stockTicker + " has been selected.");
        for (MyStock myStock: currentUser.getCurrentHoldings()) {
            if (stockTicker.equals(myStock.getTicker())) {
                chosenStock = myStock;
                return chosenStock;
            }
        }
        return null;
    }

    // EFFECTS: shows all the stocks in current holdings in human-readable format
    public void allHoldings() {
        if (currentUser.getCurrentHoldings().size() > 0) {
            for (MyStock myStock : currentUser.getCurrentHoldings()) {
                System.out.println("ticker: " + myStock.getTicker() + "\tStock name: " + myStock.getName()
                        + "\tquantity: " + myStock.getQuantity());
            }
        } else {
            System.out.println("You do not have any holdings at this time");
        }
    }

    // REQUIRES: newPassword must be a non-empty string
    // MODIFIES: this
    // EFFECTS: attempts to reset the password using the information provided by the user
    public void reset() {
        System.out.println("Please enter your username: ");
        String enteredUsername = input.next();
        System.out.println("Please enter the new password: ");
        String newPassword = input.next();
        System.out.println("Enter your authentication number: ");

        int authKey = input.nextInt();
        currentUser.resetPassword(enteredUsername,newPassword, authKey);
    }

    // EFFECTS: saves the account of currentUser to file
    public void saveAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(currentUser);
            jsonWriter.close();
            System.out.println(currentUser.getUsername() + " your account has been added to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads account from file
    public void loadAccount() {
        try {
            currentUser = jsonReader.read();
            accounts.add(currentUser);
            System.out.println("Hi " + currentUser.getUsername()
                    + "!, your account information has been successfully imported from  " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}
