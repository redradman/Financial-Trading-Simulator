package ui;

import model.Account;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WelcomePage extends JFrame implements ActionListener {
    // object / account elements
    private Account currentUser;
    private List<Account> accounts = new ArrayList<>();
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/account.json";
    private String enteredUsername;
    private String enteredPassword;


    // GUI elements
    JLabel usernameLabel;
    JTextField usernameTextField;
    JLabel passwordLabel;
    JTextField passwordTextField;
    JButton signUpButton;
    JButton loginButton;
    int pageWidth = 800;
    int pageHeight = 400;
    int valueX = pageWidth / 2;
    int buttonAdjustment = pageWidth / 8;
    int labelWidth = 100;
    int labelHeight = 30;
    int fieldWidth = 120;
    int fieldHeight = 30;
    int buttonWidth = 100;
    int buttonHeight = 30;
    Dimension textFieldDimension = new Dimension(fieldWidth,fieldHeight);
    int choice;

    // EFFECTS: creates welcomePage as a JFrame object where the user can log in or sign up
    public WelcomePage() {
        init();
        wantsToLoadData();
    }

    // EFFECTS: setting up frame
    // MODIFIES: this
    private void init() {
        jsonReader = new JsonReader(JSON_STORE);
        setupUsername();
        setupPassword();
        setupButtons();
        setupFrame();
    }

    // EFFECTS: sets up the username label and text field in GUI
    // MODIFIES: this
    private void setupUsername() {
        // setting up username label
        usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        usernameLabel.setBounds(valueX - labelWidth / 2,50,labelWidth,labelHeight);
        usernameLabel.setVerticalAlignment(JLabel.CENTER);

        // setting up username field
        usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(textFieldDimension);
        usernameTextField.setBounds(valueX - fieldWidth / 2,85,fieldWidth,fieldHeight);
    }

    // EFFECTS: sets up the password label and text field in GUI
    // MODIFIES: this
    private void setupPassword() {
        // setting up password label
        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        passwordLabel.setBounds(valueX - labelWidth / 2, 140, labelWidth, labelHeight);

        // setting up password field
        passwordTextField = new JPasswordField();
        passwordTextField.setPreferredSize(textFieldDimension);
        passwordTextField.setBounds(valueX - fieldWidth / 2,175, fieldWidth,fieldHeight);
    }

    // EFFECTS: sets up the login and sign up buttons in GUI
    // MODIFIES: this
    private void setupButtons() {

        // setting up login button
        loginButton = new JButton();
        loginButton.setText("login");
        loginButton.addActionListener(this);
        loginButton.setBounds(valueX - buttonWidth / 2 - buttonAdjustment,200, buttonWidth, buttonHeight);

        // setting up sign-up button
        signUpButton = new JButton();
        signUpButton.setText("sign up");
        signUpButton.setBounds(valueX - buttonWidth / 2 + buttonAdjustment,200, buttonWidth, buttonHeight);
        signUpButton.addActionListener(this);
        signUpButton.setActionCommand("signup"); // converting it from sign up to signup
    }

    // EFFECTS; sets up the JFrame that contains JLabel and JTextField and JButtons
    // MODIFIES: this
    private void setupFrame() {
        this.setTitle("Welcome! Please log in or make an account to trade");
        setVisible(true);
        this.setLayout(null);
        this.setSize(pageWidth,pageHeight);
        this.add(usernameLabel);
        this.add(usernameTextField);
        this.add(passwordLabel);
        this.add(passwordTextField);
        this.add(signUpButton);
        this.add(loginButton);
    }

    // EFFECTS: determines which button is associated with the performed action
    @Override
    public void actionPerformed(ActionEvent e) {
        // FOR DEBUGGING

        // System.out.println("username: " + usernameTextField.getText());

        // System.out.println("password: " + passwordTextField.getText());

        // System.out.println(e.getActionCommand());
        setUsernameAndPassword();
        if ("login".equals(e.getActionCommand())) {
        //     System.out.println("login button was pushed");              // FOR DEBUGGING
            login();
        } else {
        //     System.out.println("sign up button was pushed");            // FOR DEBUGGING
            signup();
        }
        dispose();
        // System.out.println("Removed the JFrame for the welcome page");  // FOR DEBUGGING
        new TradingPage(currentUser, accounts);
        // System.out.println("Main page was made");                       // FOR DEBUGGING
    }

    // EFFECTS: checks if the entered username and password are valid, if so the user is logged in
    // MODIFIES: this
    private void login() {
        // TODO implement login
        for (Account account: accounts) {
            if (enteredPassword.equals(account.getPassword()) && enteredUsername.equals(account.getUsername())) {
                currentUser = account;
            }
        }
    }

    // REQUIRES: username cannot be an already existing value. Both username and password are non-empty strings
    // MODIFIES: this
    // EFFECTS: creates a new account for the user that is about to sign up
    private void signup() {
        currentUser = new Account(enteredUsername, enteredPassword);
        accounts.add(currentUser);
    }

    // REQUIRES: Text fields cannot be empty strings
    // EFFECTS: Values of enteredUsername and enteredPassword are set equal to the values in the text field
    // MODIFIES: this
    private void setUsernameAndPassword() {
        enteredUsername = usernameTextField.getText();
        enteredPassword = passwordTextField.getText();
    }



    // EFFECTS: spawns a window for the user to choose if they want to load the data
    private void wantsToLoadData() {
        ImageIcon loadImage = new ImageIcon("./src/images/history.png");
        Image imageOfLoadIcon = loadImage.getImage();
        Image rescaledLoadIcon = imageOfLoadIcon.getScaledInstance(80, 80, Image.SCALE_SMOOTH);

        choice = JOptionPane.showOptionDialog(null, "Do you want to load your saved data?", "Welcome to Testnet",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(rescaledLoadIcon), null, 0);
        if (choice == 0) { // 0 is YES and 1 is NO
            loadAccount();
        }

    }

    // EFFECTS: loads account that was saved in the JSON file
    // MODIFIES: this
    private void loadAccount() {
        try {
            currentUser = jsonReader.read();
            accounts.add(currentUser);
            System.out.println("Hi " + currentUser.getUsername()
                    + "!, your account information has been successfully imported from  " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    public Account getCurrentUser() {
        return currentUser;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}