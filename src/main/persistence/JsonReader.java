package persistence;


import model.Account;
import model.MyStock;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Account read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private Account parseAccount(JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        int authenticationNumber = jsonObject.getInt("authenticationNumber");

        List<MyStock> currentHoldings = parseCurrentHoldings(jsonObject);

        double balance = jsonObject.getDouble("balance");
        int wrongAttempts = jsonObject.getInt("wrongAttempts");

        Account acc = new Account(username, password, authenticationNumber, currentHoldings, balance, wrongAttempts);
        return acc;
    }


    // MODIFIES: this
    // EFFECTS: the function recreates the user portfolio (List<MyStock>) by parsing the JSON object
    private List<MyStock> parseCurrentHoldings(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("currentHoldings");

        List<MyStock> currentHoldings = new ArrayList<>();

        for (Object json : jsonArray) {
            JSONObject myStock = (JSONObject) json;
            currentHoldings.add(parseMyStock(myStock));
        }
        return currentHoldings;
    }

    // MODIFIES: this
    // EFFECTS: creates MyStock object from the passed JSON object to construct the object
    private MyStock parseMyStock(JSONObject myStockJsonObject) {
        String name = myStockJsonObject.getString("name");
        double initialPrice = myStockJsonObject.getDouble("initialPrice");
        int quantity = myStockJsonObject.getInt("quantity");
        double purchasePrice = myStockJsonObject.getDouble("purchasePrice");
        String ticker = myStockJsonObject.getString("ticker");
        double volatility = myStockJsonObject.getDouble("volatility");

        MyStock myStock = new MyStock(name, initialPrice, quantity, purchasePrice, ticker, volatility);
        return myStock;
    }

}

