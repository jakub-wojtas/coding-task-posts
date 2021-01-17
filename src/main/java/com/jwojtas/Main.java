package com.jwojtas;

public class Main {

    private static final String ADDRESS = "https://jsonplaceholder.typicode.com/posts";
    private static final String OUTPUT_PATH = "output-files";
    private static final String NAMING_PROPERTY = "id";

    public static void main(String[] args) {
        JsonFetcherService jsonFetcherService = new JsonFetcherService();

        jsonFetcherService.saveApiResponseTo(ADDRESS, OUTPUT_PATH, NAMING_PROPERTY);
    }
}
