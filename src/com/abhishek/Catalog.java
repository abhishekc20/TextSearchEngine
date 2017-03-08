package com.abhishek;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Catalog {

    private ArrayList<Document> documents = new ArrayList<>();

    // HashMap: word -> document -> frequency
    private HashMap<String, HashMap<Document, Integer>> word2Doc2Freq = new HashMap<>();

    private static final String DOC_REGEX = "[\\p{Punct}\\s]+";
    private static final int MAX_RESULTS = 10;

    public void indexDocuments(ArrayList<Document> documents) {

        this.documents.addAll(documents);

        for (Document doc : documents) {

            System.out.print("Processing: " + doc.getFilename() + "... ");

            try {
                indexDoc(doc);
            } catch (IOException e) {
                System.err.println(e);
            }
            System.out.println("COMPLETE.");
        }

    }

    public List<Result> search(String query) {

        String[] queryWords = query.split(DOC_REGEX);
        Set<String> queries = removeDuplicates(queryWords);
        HashMap<Document, Result> doc2result = new HashMap<>();

        for (String term : queries) {
            if (word2Doc2Freq.containsKey(term)) {
                assignScoresForTerm(doc2result, term);
            }
        }

        List<Result> results = new ArrayList<>(doc2result.values());

        if (!results.isEmpty()) {
            results = sortAndFilterResults(results);
        }
        return results;
    }

    private Set<String> removeDuplicates(String[] queryWords) {
        Set<String> queries = new HashSet<>();
        Collections.addAll(queries, queryWords);
        return queries;
    }

    private List<Result> sortAndFilterResults(List<Result> results) {
        Collections.sort(results, new ResultComparator());
        results = results.subList(0, Math.min(MAX_RESULTS, results.size()));
        final double maxAccuracy = results.get(0).getAccuracy();
        Result.normalise(results, maxAccuracy);
        return results;
    }

    private void assignScoresForTerm(HashMap<Document, Result> doc2result, String term) {

        Set<Document> docs = word2Doc2Freq.get(term).keySet();
        double idf = calculateIdf(term);

        for (Document doc : docs) {
            double tf = calculateTf(term, doc);
            double tfIdf = tf * idf;

            if (!doc2result.containsKey(doc)) {
                doc2result.put(doc, new Result(doc));
            }
            doc2result.get(doc).addMatch(term, tfIdf);
        }
    }

    private double calculateIdf(String term) {
        double N = documents.size();
        double dft = word2Doc2Freq.get(term).keySet().size();
        return Math.log(N / dft);
    }

    private double calculateTf(String term, Document document) {
        int tf = word2Doc2Freq.get(term).get(document);
        return (1 + Math.log(tf));
    }

    private void indexDoc(Document document) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(document.getFilename()))) {

            String line = br.readLine();

            while (line != null) {
                // Split words by punctuation.
                String[] words = line.split(DOC_REGEX);

                for (String word : words) {
                    word = sanitize(word);
                    if (word.length() > 0) {
                        updateWordDocFrequency(document, word);
                    }
                }
                line = br.readLine();
            }
        }
    }

    private void updateWordDocFrequency(Document document, String word) {
        if (word2Doc2Freq.containsKey(word)) {
            if (word2Doc2Freq.get(word).containsKey(document)) {
                int freq = word2Doc2Freq.get(word).get(document);
                word2Doc2Freq.get(word).put(document, freq + 1);
            } else {
                word2Doc2Freq.get(word).put(document, 1);
            }
        } else {
            word2Doc2Freq.put(word, new HashMap<>());
            word2Doc2Freq.get(word).put(document, 1);
        }
    }

    private String sanitize(String word) {
        // TODO: possible experiment: remove ending apostrophes, e.g. graham's -> graham.
        return word.toLowerCase();
    }
}