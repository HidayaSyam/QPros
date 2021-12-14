package sample;

import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PetController {

    static String apiUrl="https://petstore.swagger.io/v2/pet";
    static List<Pet> allPetData;

    public static List<Pet> GetAllData(String query) {
        try {
            HttpURLConnection connection = getConnection("GET", query);
            int status = connection.getResponseCode();
            if (status == 200) {
                String data = ConReadAllJsonData(connection);
                allPetData = ConvertAllToPets(data);

            } else {
                System.out.println("Something wrong...! ");
            }

        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        return allPetData;
    }

    public static Pet GetOnePet(String query) {
        Pet pet = null;
        try {
            HttpURLConnection connection = getConnection("GETId", query);
            int status = connection.getResponseCode();
            if (status == 200) {
                String data = ConReadAllJsonData(connection);
                pet = ConvertOneToPet(data);
            } else {
                System.out.println("Something wrong...! ");
            }

        } catch (Exception e) {
            System.out.println("Error Get One : " + e);
        }
        return pet;
    }

    public static int DeletePet(String query ) throws IOException {
        HttpURLConnection connection =getConnection("DELETE",query);
        int statusCode = connection.getResponseCode();
        return statusCode;

    }

    public static int AddOrUpdatePet( String method, Pet p) throws MalformedURLException {

        int statusCode = 0;
        try {
            HttpURLConnection connection = getConnection(method, "");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            String jsonInputString = "{\"id\":" + p.getId() + ",\"category\": {\"id\": 0, \"name\": \"string\"  }, \"name\":\"" + p.getName() + "\",  \"photoUrls\": [  \"string\"  ],\"tags\": [  {  \"id\": 0,   \"name\": \"string\"  }  ], \"status\": \"" + p.getStatus() + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            statusCode = connection.getResponseCode();

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
            HttpURLConnection connection = getConnection("GET", arr[i]);
            String statusData = ConReadAllJsonData(connection);
            Status status = new Status(ConvertAllToPets(statusData).size(), arr[i]);
            statusList.add(status);
        }
        return statusList;
    }

    public static void GenerateUrl(String method,String query){
        switch (method){
            case "GET":
                if (query=="All")
                    apiUrl= "https://petstore.swagger.io/v2/pet/findByStatus?status=available,sold,pending";
                else
                    apiUrl= "https://petstore.swagger.io/v2/pet/findByStatus?status="+query;
                break;
            case "DELETE":
            case "GETId":
                apiUrl= "https://petstore.swagger.io/v2/pet/"+Integer.parseInt(query);
                break;
            case "POST":
            case "PUT":
                apiUrl= "https://petstore.swagger.io/v2/pet";
                break;

        }
    }

    public static HttpURLConnection getConnection( String method ,String query) {
        HttpURLConnection connection = null;

        GenerateUrl(method,query);

        if (method=="GETId"){
            method="GET";
        }

        try {
            URL url= new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String ConReadAllJsonData(HttpURLConnection connection) throws IOException {
        String inline = "";
        BufferedReader br = new BufferedReader(new InputStreamReader (connection.getInputStream()));
        String i;
        while ((i = br.readLine()) != null)
        {
            inline+=i;
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