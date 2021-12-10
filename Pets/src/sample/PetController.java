package sample;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class PetController {

    public static int DeletePet(BigInteger petId) throws IOException {
        URL apiUrl = new URL("https://petstore.swagger.io/v2/pet/" + petId);
        System.out.println(apiUrl);
        HttpURLConnection connection = getConnection(apiUrl, "DELETE");
        int statusCode = connection.getResponseCode();
        System.out.println(statusCode);
        return statusCode;

    }

    public static int AddOrUpdatePet(Pet p, String method) throws MalformedURLException {

        URL apiUrl = new URL("https://petstore.swagger.io/v2/pet");
        HttpURLConnection connection = getConnection(apiUrl, method);
        int statusCode = 0;
        try {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            String jsonInputString = "{\"id\":" + p.getId() + ",\"category\": {\"id\": 0, \"name\": \"string\"  }, \"name\":\"" + p.getName() + "\",  \"photoUrls\": [  \"string\"  ],\"tags\": [  {  \"id\": 0,   \"name\": \"string\"  }  ], \"status\": \"" + p.getStatus() + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    System.out.println("After");
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
            } else {
                System.out.println("something went wrong");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statusCode;

    }

    public static ArrayList<Status> getDashboardData() throws IOException {
        String[] arr = {"available", "pending", "sold"};
        ArrayList<Status> statusList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            URL storeUrl = new URL("https://petstore.swagger.io/v2/pet/findByStatus?status=" + arr[i]);
            HttpURLConnection connection = getConnection(storeUrl, "GET");
            String statusData = ReadAllJsonData(storeUrl);
            Status status = new Status(ConvertAllToPets(statusData).size(), arr[i]);
            statusList.add(status);
        }
        return statusList;
    }

    public static HttpURLConnection getConnection(URL url, String method) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String ReadAllJsonData(URL url) throws IOException {
        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        return inline;
    }

    public static Pet ConvertOneToPet(String data) {
        Pet pet = null;
        try {
            Gson gson = new Gson();
            Type quotesListType = new TypeToken<Pet>() {
            }.getType();

            pet = gson.fromJson(data, quotesListType);

        } catch (Exception e) {
            System.out.println(e);
        }
        return pet;
    }

    public static ArrayList<Pet> ConvertAllToPets(String data) {
        ArrayList<Pet> pets = null;
        try {
            Gson gson = new Gson();
            Type quotesListType = new TypeToken<ArrayList<Pet>>() {
            }.getType();

            pets = gson.fromJson(data, quotesListType);

        } catch (Exception e) {
            System.out.println(e);
        }
        return pets;
    }
}