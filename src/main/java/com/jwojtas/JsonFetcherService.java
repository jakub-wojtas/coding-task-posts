package com.jwojtas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public class JsonFetcherService {
    public static void saveApiResponseToDisk(String address, String outputPath, String namingProperty) {
        try {
            URL url = connectToApi(address);

            JSONArray responseArray = getJsonArrayFromUrl(url);

            writeObjectsToDisk(responseArray, outputPath, namingProperty);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static URL connectToApi(String address) throws IOException {
        URL url = new URL(address);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Http response code: " + connection.getResponseCode());
        } else {
            return url;
        }
    }

    private static JSONArray getJsonArrayFromUrl(URL url) throws Exception {
        StringBuilder responseStringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            responseStringBuilder.append(scanner.nextLine());
        }

        scanner.close();

        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(responseStringBuilder.toString());
    }

    private static void writeObjectsToDisk(JSONArray array, String outputPath, String namingProperty) throws IOException {
        for (Object o : array) {
            JSONObject object = (JSONObject) o;

            if(Objects.isNull(object.get(namingProperty))) {
                throw new RuntimeException(namingProperty + " property not found in the object.");
            }

            FileWriter writer = new FileWriter(new File(outputPath, object.get(namingProperty) + ".json"));
            writer.write(object.toJSONString());
            writer.close();
        }
    }
}
