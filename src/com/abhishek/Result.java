package com.abhishek;

import java.util.HashMap;
import java.util.List;

public class Result {

    private Document document;
    private double accuracy;

    private HashMap<String, Double> term2tfIdf = new HashMap<>();

    public Result(Document document) {
        this.document = document;
    }

    public void addMatch(String term, double tfIdf) {
        term2tfIdf.put(term, tfIdf);
        accuracy += tfIdf;
    }

    public Document getDocument() {
        return document;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public static void normalise(List<Result> results, double maxAccuracy) {
        for (Result result : results) {
            if(maxAccuracy > 0) {
                double accuracy = (result.getAccuracy() / maxAccuracy);
                result.setAccuracy(accuracy);
            } else {
                result.setAccuracy(1);
            }
        }
    }
}
