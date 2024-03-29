/**
 *
 *  @author Dudek Krzysztof S25692
 *
 */

package zad2;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;
import java.util.Locale;

public class Service {

    private static final String OPENWEATHER_API_KEY = "********************************";
    private static final String OPENWEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String EXCHANGERATE_API_KEY = "********************************";
    private static final String EXCHANGERATE_URL = "http://api.exchangerate.host/convert";
    private static final String NBP_TABLES_URL = "http://api.nbp.pl/api/exchangerates/tables/";

    private String countryCode;
    private String currencyCode;

    private String city;
    private String weatherDescription;
    private double temperature;
    private double feelsLike;
    private String chosenCurrency;
    private double rateToChosenCurrency;
    private double rateToPLN;

    public Service(String country) {
        Locale locale = getLocaleByCountryName(country);
        if (locale != null) {
            this.countryCode = locale.getCountry();
            this.currencyCode = Currency.getInstance(locale).getCurrencyCode();
        }
    }

    private void runWebsite(JFXPanel jfxPanel, String url) {
        Platform.runLater(() -> {
            WebView webView = new WebView();
            jfxPanel.setScene(new Scene(webView));
            WebEngine webEngine = webView.getEngine();
            webEngine.load(url);
        });
    }

    private Locale getLocaleByCountryName(String countryName) {
        for (Locale locale : Locale.getAvailableLocales()) {
            String displayCountry = locale.getDisplayCountry(new Locale("en"));
            if (displayCountry.equalsIgnoreCase(countryName)) {
                return locale;
            }
        }
        return null;
    }

    public String getWeather(String city) {
        this.city = city;
        String request = OPENWEATHER_URL + "?q=" + city + "," + countryCode + "&appid=" + OPENWEATHER_API_KEY;
        String response = sendRequest(request);
        this.weatherDescription = extractValueFromJson(response, "description");
        this.temperature = response != null ? Double.parseDouble(extractValueFromJson(response, "temp")) - 273.15 : 0.0;
        this.feelsLike = response != null ? Double.parseDouble(extractValueFromJson(response, "feels_like")) - 273.15 : 0.0;
        this.temperature = Math.round(this.temperature * 100.0) / 100.0;
        this.feelsLike = Math.round(this.feelsLike * 100.0) / 100.0;
        return response;
    }

    public Double getRateFor(String chosenCurrency) {
        String request = EXCHANGERATE_URL + "?from=" + currencyCode + "&to=" + chosenCurrency + "&amount=1&access_key=" + EXCHANGERATE_API_KEY;
        String response = sendRequest(request);

        if (response == null) {
            return null;
        }

        this.chosenCurrency = chosenCurrency;
        this.rateToChosenCurrency = Double.parseDouble(extractValueFromJson(response, "result"));

        return this.rateToChosenCurrency;
    }

    public Double getNBPRate() {
        if (currencyCode.equals("PLN")) {
            this.rateToPLN = 1.0;
            return this.rateToPLN;
        }

        String response = sendRequest(NBP_TABLES_URL + "A/");
        String rate = extractNearestRate(response, currencyCode);

        if (rate == null) {
            response = sendRequest(NBP_TABLES_URL + "B/");
            rate = extractNearestRate(response, currencyCode);
        }

        if (rate == null) {
            return null;
        }

        this.rateToPLN = Double.parseDouble(rate);
        return this.rateToPLN;
    }

    private String extractNearestRate(String response, String currencyCode) {
        if (response == null) {
            return null;
        }

        String[] rates = response.split(",\\{");
        String rate = null;

        for (String r : rates) {
            if (r.contains(currencyCode)) {
                rate = extractValueFromJson(r, "mid");
                break;
            }
        }

        return rate;
    }

    private String sendRequest(String request) {
        String response;

        try {
            URLConnection connection = new URL(request).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            response = content.toString();
        } catch (IOException e) {
            return null;
        }

        return response;
    }

    private String extractValueFromJson(String json, String fieldName) {
        if (json == null) {
            return null;
        }

        String wrappedFieldName = "\"" + fieldName + "\":";

        int startIndex = json.indexOf(wrappedFieldName);
        if (startIndex == -1) {
            throw new RuntimeException("Result field not found in the response");
        }

        startIndex += wrappedFieldName.length();

        int endIndex = json.indexOf(',', startIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf('}', startIndex);
        }

        return json.substring(startIndex, endIndex).trim().replace("\"", "");
    }

    public void displayUserInterface() {
        String url = "https://en.wikipedia.org/wiki/" + city;

        JLabel weatherLabel = new JLabel("Weather: " + weatherDescription + ", Temperature: " + temperature + "°C, Feels like: " + feelsLike + "°C");
        weatherLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel currencyRateLabel = new JLabel("Currency rate to " + chosenCurrency + ": " + rateToChosenCurrency);
        currencyRateLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel exchangeRateLabel = new JLabel("Exchange rate to PLN: " + rateToPLN);
        exchangeRateLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JTextField countryInput = new JTextField();
        countryInput.setFont(new Font("Arial", Font.PLAIN, 18));
        JButton countryButton = new JButton("Choose Country");
        countryButton.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField cityInput = new JTextField();
        cityInput.setFont(new Font("Arial", Font.PLAIN, 18));
        JButton cityButton = new JButton("Choose City");
        cityButton.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField currencyInput = new JTextField();
        currencyInput.setFont(new Font("Arial", Font.PLAIN, 18));
        JButton currencyButton = new JButton("Choose Currency");
        currencyButton.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(weatherLabel, gbc);
        gbc.gridy = 1;
        panel.add(currencyRateLabel, gbc);
        gbc.gridy = 2;
        panel.add(exchangeRateLabel, gbc);
        gbc.gridy = 3;
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(1, 6));
        innerPanel.add(countryInput);
        innerPanel.add(countryButton);
        innerPanel.add(cityInput);
        innerPanel.add(cityButton);
        innerPanel.add(currencyInput);
        innerPanel.add(currencyButton);
        panel.add(innerPanel, gbc);

        JFXPanel jfxPanel = new JFXPanel();

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(jfxPanel, BorderLayout.CENTER);
        frame.setSize(1200, 900);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Platform.runLater(() -> jfxPanel.getScene().getRoot().resize(frame.getWidth(), frame.getHeight()));
            }
        });

        runWebsite(jfxPanel, url);

        countryButton.addActionListener((ActionEvent e) -> {
            String country = countryInput.getText();
            Locale locale = getLocaleByCountryName(country);
            if (locale != null) {
                this.countryCode = locale.getCountry();
                this.currencyCode = Currency.getInstance(locale).getCurrencyCode();
            }
            getNBPRate();
            exchangeRateLabel.setText("Exchange rate to PLN: " + rateToPLN);
        });

        cityButton.addActionListener((ActionEvent e) -> {
            String city = cityInput.getText();
            getWeather(city);
            weatherLabel.setText("Weather: " + weatherDescription + ", Temperature: " + temperature + "°C, Feels like: " + feelsLike + "°C");
            runWebsite(jfxPanel, "https://en.wikipedia.org/wiki/" + city);
        });

        currencyButton.addActionListener((ActionEvent e) -> {
            String currency = currencyInput.getText();
            getRateFor(currency);
            currencyRateLabel.setText("Currency rate to " + chosenCurrency + ": " + rateToChosenCurrency);
        });
    }
}
