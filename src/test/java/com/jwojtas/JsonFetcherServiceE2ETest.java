package com.jwojtas;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JsonFetcherService")
class JsonFetcherServiceE2ETest {

    @TempDir
    Path temporaryFolder;

    @Test
    @DisplayName("is saving correct amount of posts in temporary output folder")
    void postsAreSavedInSeparateFiles() throws IOException {
        //Given
        String url = "https://jsonplaceholder.typicode.com/posts";
        String outputPath = temporaryFolder.toString();
        String namingProperty = "id";

        //When
        JsonFetcherService.saveApiResponseToDisk(url, outputPath, namingProperty);

        //Then
        assertEquals(100L, Files.list(temporaryFolder).count());
    }

    @Test
    @DisplayName("post are saved correctly and are can be parsed")
    void postsCanBeParsed() throws IOException {
        //Given
        String url = "https://jsonplaceholder.typicode.com/posts";
        String outputPath = temporaryFolder.toString();
        String namingProperty = "id";
        JSONParser jsonParser = new JSONParser();

        //When
        JsonFetcherService.saveApiResponseToDisk(url, outputPath, namingProperty);

        ArrayList<Object> resultList = new ArrayList<>();

        Files.list(temporaryFolder).forEach(file -> {
            try (FileReader reader = new FileReader(file.toString())) {
                resultList.add(jsonParser.parse(reader));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        });

        //Then
        assertEquals(100, resultList.size());
    }
}