package com.jwojtas;

import static com.jwojtas.JsonFetcherService.saveApiResponseToDisk;

public class Main {

    public static void main(String[] args) {
        saveApiResponseToDisk("https://jsonplaceholder.typicode.com/posts", "output-files", "id");
    }
}
