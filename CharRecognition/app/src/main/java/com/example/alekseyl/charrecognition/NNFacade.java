package com.example.alekseyl.charrecognition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NNFacade {
    private Map<Integer, List<boolean[]>> trainingSet = new HashMap<>();
    public static final int NUMBERS_COUNT = 2;

    public NNFacade() {
        resetTrainingSet();
    }

    public void addToTrainingSet(int number, boolean[] vector) {
        List<boolean[]> vectors = trainingSet.get(number);
        vectors.add(vector);
    }

    public void resetTrainingSet() {
        for (int i = 0; i < NUMBERS_COUNT; ++i) {
            trainingSet.put(i, new ArrayList<boolean[]>());
        }
    }

    public void train() {
        //TODO: train
    }

    public int recognize(boolean[] vector) {
        //TODO: recognize
        return 0;
    }
}
