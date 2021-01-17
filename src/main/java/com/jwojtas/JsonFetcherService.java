package com.jwojtas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

import static java.nio.file.Files.createDirectories;

public class JsonFetcherService {
    public void saveApiResponseTo(String address, String outputPath, String namingProperty) {
        try {
            URL url = connectTo(address);

            JSONArray responseArray = getJsonArrayFrom(url);

            writeObjectsTo(responseArray, outputPath, namingProperty);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private URL connectTo(String address) throws IOException {
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

    private JSONArray getJsonArrayFrom(URL url) throws Exception {
        StringBuilder responseStringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            responseStringBuilder.append(scanner.nextLine());
        }

        scanner.close();

        return (JSONArray) new JSONParser().parse(responseStringBuilder.toString());
    }

    private void writeObjectsTo(JSONArray array, String outputPath, String namingProperty) throws IOException {
        createDirectories(Paths.get(outputPath));

        for (Object o : array) {
            JSONObject object = (JSONObject) o;

            if (Objects.isNull(object.get(namingProperty))) {
                throw new RuntimeException(namingProperty + " property not found in the object.");
            }

            FileWriter writer = new FileWriter(new File(outputPath, object.get(namingProperty) + ".json"));
            writer.write(object.toJSONString());
            writer.close();
        }
    }
}
