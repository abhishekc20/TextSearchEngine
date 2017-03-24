package com.abhishek;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            throw new IllegalArgumentException("No directory given to index.");
        }

        final File indexableDirectory = new File(args[0]);

        try {
            Catalog catalog = createCatalog(indexableDirectory);
            startSearchEngine(catalog);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void startSearchEngine(Catalog catalog) {
        Scanner keyboard = new Scanner(System.in);

        while (true) {
            System.out.print("search> ");
            final String line = keyboard.nextLine();

            List<Result> results = catalog.search(line);

            if (results.isEmpty()) {
                System.out.println("No results found");
            } else {
                printResults(results);
            }
        }
    }

    private static void printResults(List<Result> results) {
        for (int i = 0; i < results.size(); i++) {
            String resultStr = formatResult(results.get(i));
            System.out.println((i + 1) + ") " + resultStr);
        }
    }

    private static String formatResult(Result result) {
        Path p = Paths.get(result.getDocument().getFilename());
        String filename = p.getFileName().toString();

        double accuracy = result.getAccuracy() * 100;
        DecimalFormat df = new DecimalFormat("#.00");

        return filename + " : " + df.format(accuracy) + "%";
    }

    private static Catalog createCatalog(File indexableDirectory) throws Exception {
        ArrayList<Document> docs = new ArrayList<>();

        if (indexableDirectory == null) {
            throw new Exception();
        }

        List<File> directoryListing = getAllTextFiles(indexableDirectory);

        if (directoryListing.size() > 0) {
            for (File child : directoryListing) {
                Document doc = new Document(child.getAbsolutePath());
                docs.add(doc);
            }
        } else {
            throw new NoDocumentsFoundException("No txt files found. Exiting program.");
        }

        Catalog catalog = new Catalog();
        catalog.indexDocuments(docs);

        return catalog;
    }

    private static List<File> getAllTextFiles(File file) {

        List<File> results = new ArrayList<>();

        File[] files = file.listFiles();

        for (File subFile : files) {
            if (!subFile.isDirectory() && subFile.getName().endsWith(".txt")) {
                results.add(subFile);
            } else {
                results.addAll(getAllTextFiles(subFile));
            }
        }
        return results;
    }

}
