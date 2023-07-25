package com.assignment;
import java.util.Scanner;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApp {

    private static JsonObject getWeatherData() throws IOException {
        URL url = new URL("https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();
        return JsonParser.parseString(response.toString()).getAsJsonObject();
    }

    private static double getTemperatureForDate(JsonObject data, String targetDate) {
        for (JsonElement entry : data.getAsJsonArray("list")) {
            String dateTime = entry.getAsJsonObject().get("dt_txt").getAsString();
            if (dateTime.contains(targetDate)) {
                return entry.getAsJsonObject().getAsJsonObject("main").get("temp").getAsDouble();
            }
        }
        return Double.NaN;
    }

    private static double getWindSpeedForDate(JsonObject data, String targetDate) {
        for (JsonElement entry : data.getAsJsonArray("list")) {
            String dateTime = entry.getAsJsonObject().get("dt_txt").getAsString();
            if (dateTime.contains(targetDate)) {
                return entry.getAsJsonObject().getAsJsonObject("wind").get("speed").getAsDouble();
            }
        }
        return Double.NaN;
    }

    private static double getPressureForDate(JsonObject data, String targetDate) {
        for (JsonElement entry : data.getAsJsonArray("list")) {
            String dateTime = entry.getAsJsonObject().get("dt_txt").getAsString();
            if (dateTime.contains(targetDate)) {
                return entry.getAsJsonObject().getAsJsonObject("main").get("pressure").getAsDouble();
            }
        }
        return Double.NaN;
    }
    private static String getDate(Scanner scanner) {
        System.out.print("Enter the date (YYYY-MM-DD HH:mm:ss - 2019-03-27 18:00:00):  ");
        return scanner.nextLine().trim();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        JsonObject weatherData = getWeatherData();

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());

            if (choice == 1) {
                String targetDate = getDate(scanner);
                double temperature = getTemperatureForDate(weatherData, targetDate);
                if (!Double.isNaN(temperature)) {
                    System.out.println("Temperature on " + targetDate + ": " + temperature);
                } else {
                    System.out.println("Data not available for the specified date.");
                }
            } else if (choice == 2) {
                 String targetDate = getDate(scanner);
                double windSpeed = getWindSpeedForDate(weatherData, targetDate);
                if (!Double.isNaN(windSpeed)) {
                    System.out.println("Wind Speed on " + targetDate + ": " + windSpeed);
                } else {
                    System.out.println("Data not available for the specified date.");
                }
            } else if (choice == 3) {
                String targetDate = getDate(scanner);
                double pressure = getPressureForDate(weatherData, targetDate);
                if (!Double.isNaN(pressure)) {
                    System.out.println("Pressure on " + targetDate + ": " + pressure);
                } else {
                    System.out.println("Data not available for the specified date.");
                }
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
