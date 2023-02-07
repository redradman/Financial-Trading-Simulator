package ui;

import model.Account;
import model.Event;
import model.EventLog;
import model.MyStock;
import model.Stock;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TradingPage extends JFrame implements ActionListener {
    // account elements
    private final Account currentUser;
    private JsonWriter jsonWriter;
    private java.util.List<Account> accounts;
    private List<Stock> stocks = new ArrayList<>();
    private static final String JSON_STORE =  "./data/account.json";
    Stock bananaCorp;
    Stock evilCorp;
    Stock angelCorp;
    Stock exoPlanetCorp;
    EventLog eventlog;


    // GUI elements
    int windowWidth = 1000;
    int windowHeight = 500;
    int scaledImageWidth = 30;
    int scaledImageHeight = 30;
    JButton buyButton = new JButton();
    JButton sellButton = new JButton();
    JButton resetPasswordButton = new JButton();
    JButton holdingsButton = new JButton();
    JButton authButton = new JButton();
    JButton saveButton = new JButton();
    JButton quitButton = new JButton();
    JLabel welcomeLabel = new JLabel();
    JPanel welcomePanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    // EFFECTS: constructs the trading page for the user that has logged in where the user can trade (buy and sell)
    //          stocks reset their password, look at their holdings and view their authentication key.
    //          In addition to creating the page, the user
    // MODIFIES: this
    public TradingPage(Account acc, List<Account>  passedAccounts) {
        jsonWriter = new JsonWriter(JSON_STORE);
        currentUser = acc;
        accounts = passedAccounts;
        createStocks();
        welcomeLabel.setText("Welcome " + currentUser.getUsername());
        init();
    }

    // EFFECTS: adjusts the trading page (this) appropriately by adding the necessary elements and altering it by
    //          adding the buttons, labels and the welcome label
    // MODIFIES: this
    private void init() {

        this.setTitle("Main Page");
        this.setVisible(true);
        this.setSize(windowWidth, windowHeight);

        // setting up the welcome label
        welcomeLabel.setText("Welcome " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        ImageIcon welcomeIcon = new ImageIcon("./src/images/chart.png");
        welcomeLabel.setIcon(scaleImage(welcomeIcon, 200,200));
        welcomeLabel.setHorizontalTextPosition(JLabel.CENTER);
        welcomeLabel.setVerticalTextPosition(JLabel.TOP);
        welcomePanel.add(welcomeLabel);

        makeButtons();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() { @Override
            public void windowClosing(WindowEvent e) {
                printEvents();
                e.getWindow().dispose();
            }
        });
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(welcomePanel);
        container.add(buttonsPanel);
        this.add(container);
    }



    // EFFECTS: creates all the buttons and adds them to the JPanel that contains them
    // MODIFIES: this
    private void makeButtons() {
        makeBuyButton();
        makeSellButton();
        makeResetButton();
        makeHoldingsButton();
        makeAuthButton();
        makeSaveButton();
        makeQuitButton();

        makeButtonsPanel();
    }

    // EFFECTS: adds a layout to buttons' JPanel and adds all the buttons to the JPanel
    // MODIFIES: this
    private void makeButtonsPanel() {
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(buyButton);
        buttonsPanel.add(sellButton);
        buttonsPanel.add(resetPasswordButton);
        buttonsPanel.add(holdingsButton);
        buttonsPanel.add(authButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(quitButton);
    }

    // EFFECTS: creates the buy button
    // MODIFIES: this
    private void makeBuyButton() {
        ImageIcon buyImage = new ImageIcon("./src/images/buy.png");
        buyButton.setText("Buy");
        buyButton.addActionListener(this);
        buyButton.setIcon(scaleImage(buyImage));
    }

    // EFFECTS: creates the reset password button
    // MODIFIES: this
    private void makeResetButton() {
        ImageIcon resetImage = new ImageIcon("./src/images/password.png");
        resetPasswordButton.setText("Reset Password");
        resetPasswordButton.setActionCommand("Reset");
        resetPasswordButton.addActionListener(this);
        resetPasswordButton.setIcon(scaleImage(resetImage));
    }

    // EFFECTS: creates the sell button
    // MODIFIES: this
    private void makeSellButton() {
        // making the Sell button
        ImageIcon sellImage = new ImageIcon("./src/images/sell.png");
        sellButton.setText("Sell");
        sellButton.addActionListener(this);
        sellButton.setIcon(scaleImage(sellImage));
    }

    // EFFECTS: creates the show holdings button
    // MODIFIES: this
    private void makeHoldingsButton() {
        ImageIcon holdingsImage = new ImageIcon("./src/images/briefcase.png");
        holdingsButton.setText("Holdings");
        holdingsButton.addActionListener(this);
        holdingsButton.setIcon(scaleImage(holdingsImage));
    }

    // EFFECTS: makes the show authorization number button
    // MODIFIES: this
    private void makeAuthButton() {
        ImageIcon authImage = new ImageIcon("./src/images/key.png");
        authButton.setText("Authentication Number");
        authButton.setActionCommand("Auth");
        authButton.addActionListener(this);
        authButton.setIcon(scaleImage(authImage));
    }

    // EFFECTS; makes the save button
    // MODIFIES: this
    private void makeSaveButton() {
        ImageIcon saveImage = new ImageIcon("./src/images/save.png");
        saveButton.setText("Save");
        saveButton.addActionListener(this);
        saveButton.setIcon(scaleImage(saveImage));
    }

    // EFFECTS: makes the quit button
    // MODIFIES: this
    private void makeQuitButton() {
        ImageIcon quitImage = new ImageIcon("./src/images/sign-out.png");
        quitButton.setText("Quit");
        quitButton.addActionListener(this);
        quitButton.setIcon(scaleImage(quitImage));
    }




    // EFFECTS: resizes the images for buttons so that their sizes are better looking in GUI
    private ImageIcon scaleImage(ImageIcon icon) {
        Image imageOfIcon = icon.getImage();
        Image rescaledIcon = imageOfIcon.getScaledInstance(scaledImageWidth, scaledImageHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(rescaledIcon);
    }

    // EFFECTS: resizes the images for buttons so that their sizes are better looking in GUI
    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image imageOfIcon = icon.getImage();
        Image rescaledIcon = imageOfIcon.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(rescaledIcon);
    }



    // MODIFIES: this
    // EFFECTS: creates the stocks that can be traded on the platform
    public void createStocks() {
        bananaCorp = new Stock("Banana Corp", "BNNA", 1000, 4);
        evilCorp = new Stock("Evil Corp", "EVIL", 300, 6);
        angelCorp = new Stock("Angel Corp", "ANGL", 100, 5);
        exoPlanetCorp = new Stock("ExoPlanet", "EXPL", 20000, 3);

        stocks.add(bananaCorp);
        stocks.add(evilCorp);
        stocks.add(angelCorp);
        stocks.add(exoPlanetCorp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Buy".equals(e.getActionCommand())) {
            buyStock();
        } else if ("Sell".equals(e.getActionCommand())) {
            sellStock();
        } else if ("Reset".equals(e.getActionCommand())) {
            // user story not implemented in this phase (As it is not required per the instructions)
            // System.out.println("Not implemented yet");
        } else if ("Holdings".equals(e.getActionCommand())) {
            showHoldings();
        } else if ("Save".equals(e.getActionCommand())) {
            saveAccount();
        } else if ("Auth".equals(e.getActionCommand())) {
            showAuthenticationNumber();
        } else if ("Quit".equals(e.getActionCommand())) {
            // TODO implement the process of printing eventLog before exiting
            printEvents();
            System.exit(0);
        }
    }

    // EFFECTS: prints the events in eventLog to console
    public void printEvents() {
        for (Event event: eventlog.getInstance()) {
            System.out.println(event.toString() + "\n");
        }
    }

    // REQUIRES: The entered ticker must be the ticker of a stock in the list of stocks,
    //           the entered quantity must in the option dialog must be type int and >0
    // MODIFIES: this
    // EFFECTS: allows the user to purchase a stock by specifying the quantity and ticker
    private void buyStock() {
        String ticker = JOptionPane.showInputDialog(null, "Enter the ticker stock you want to purchase");
        Stock selectedStock = findCorrespondingStock(ticker);
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the quantity"));
        currentUser.buyStock(quantity, selectedStock);
    }

    // REQUIRES: The entered ticker must be ticker of a stock in the list of stocks
    //           the entered ticker must be ticker of a MyStock in holdings of the user
    //           the entered quantity must be an int higher than 0
    //           but lower than or equal to the purchased quantity of MyStock
    // MODIFIES: this
    // EFFECTS: User will sell the stock by passing the ticker and the quantity to be sold
    private void sellStock() {
        String ticker = JOptionPane.showInputDialog(null, "Enter the ticker stock you want to sell");
        Stock selectedStock = findCorrespondingStock(ticker);
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the quantity"));
        currentUser.sellStock(quantity, findTheSelectedStockInHoldings(ticker));
    }

    // REQUIRES: the passed ticker must be ticker of a stock in current holdings
    // EFFECTS: returns the MyStock in holdings associated with the passed ticker
    private MyStock findTheSelectedStockInHoldings(String stockTicker) {
        for (MyStock myStock: currentUser.getCurrentHoldings()) {
            if (stockTicker.equals(myStock.getTicker())) {
                return myStock;
            }
        }
        return null;
    }

    // EFFECTS: returns the stock that has the passed string as its ticker (exoplanet-corp is the default choice)
    private Stock findCorrespondingStock(String choice) {
        if (choice.equals("BNNA")) {
            return bananaCorp;
        } else if (choice.equals("EVIL")) {
            return evilCorp;
        } else if (choice.equals("ANGL")) {
            return angelCorp;
        } else {
            return exoPlanetCorp;
        }
    }

//    private void resetPassword() {
//        resetPasswordWindow();
//
//
//    }

//    private void resetPasswordWindow() {
//        int resetWindowWidth = 300;
//        int resetWindowHeight = 300;
//        int resetWindowLabelWidth = 100;
//        int resetWindowLabelHeight = 30;
//        JFrame resetPasswordWindow = new JFrame();
//        resetPasswordWindow.setLayout(null);
//        resetPasswordWindow.setVisible(true);
//        resetPasswordWindow.setSize(new Dimension(resetWindowWidth,resetWindowHeight));
//
//        // making username label and field
//        JLabel usernameLabelResetPassword = new JLabel();
//        usernameLabelResetPassword.setText("Username");
//        usernameLabelResetPassword.setBounds(resetWindowWidth / 2, resetWindowHeight / 6,
//                resetWindowLabelWidth,resetWindowLabelHeight);
//
//        // making password label and field
//
//
//        // making auth-key label and field
//
//
//
//    }

    // EFFECTS: shows the user the stocks that they hold
    private void showHoldings() {
        if (currentUser.getCurrentHoldings().size() > 0) {
            String message = "List of Holdings: \n";
            for (MyStock myStock: currentUser.getCurrentHoldings()) {
                message += myStockToString(myStock);
            }
            JOptionPane.showMessageDialog(null, message, "List of Current Holdings",
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "You do not have any holdings",
                    "List of Current Holdings", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // EFFECTS; converts the data of stock that a user has already purchased to string
    private String myStockToString(MyStock myStock) {
        return myStock.getQuantity() + " shares of " + myStock.getName() + " " + "("
                + myStock.getTicker() + ") " + "purchased at " + Math.round(myStock.getPurchasePrice()) + "\n";
    }

    // EFFECTS: save the account data to JSON file
    private void saveAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(currentUser);
            jsonWriter.close();
            System.out.println(currentUser.getUsername() + " your account has been added to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: shows the authentication number of the user in a pop-up window
    private void showAuthenticationNumber() {
        String message = "DO NOT SHARE THIS NUMBER WITH ANYONE\n" + "Your authentication Number is: "
                + Integer.toString(currentUser.getAuthenticationNumber());
        JOptionPane.showMessageDialog(null, message, "Authentication Number", JOptionPane.PLAIN_MESSAGE);
    }

}
