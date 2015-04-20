package com.example.alekseyl.charrecognition;

import android.content.Context;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NNFacade {
    private Map<Integer, List<boolean[]>> trainingSet = new HashMap<>();
    public static final int NUMBERS_COUNT = 10;
    public static final int OUTPUT_SIZE = NUMBERS_COUNT;

    private static final int INPUT_SIZE = PaintView.imageSideSize * PaintView.imageSideSize;
    private MultiLayerPerceptron nn;

    private static final String FILE_NAME = "nn.data";

    public NNFacade() {
        resetNetwork();
    }

    public void addToTrainingSet(int number, boolean[] vector) {
        List<boolean[]> vectors = trainingSet.get(number);
        vectors.add(vector);
    }

    public void resetNetwork() {
        for (int i = 0; i < NUMBERS_COUNT; ++i) {
            trainingSet.put(i, new ArrayList<boolean[]>());
        }

        nn = new MultiLayerPerceptron(
                INPUT_SIZE,
                INPUT_SIZE,
                NUMBERS_COUNT);
    }

    public void train() {
        DataSet dataSet = new DataSet(INPUT_SIZE, OUTPUT_SIZE);

        for (Map.Entry<Integer, List<boolean[]>> pair: trainingSet.entrySet()) {
            double output[] = intToVector(pair.getKey());

            for (boolean[] vector: pair.getValue()) {
                double input[] = imageToVector(vector);
                dataSet.addRow(input, output);
            }
        }

        nn.learn(dataSet);
    }

    public int recognize(boolean[] vector) {
        double[] input = imageToVector(vector);

        nn.setInput(input);
        nn.calculate();
        double[] output = nn.getOutput();

        int result = outputVectorToInt(output);

        return result;
    }

    public double[] intToVector(int number) {
        double[] result = new double[OUTPUT_SIZE];
        result[number] = 1;

        return result;
    }

    public double[] imageToVector(boolean[] vector) {
        double[] result = new double[vector.length];

        for (int i = 0; i < vector.length; ++i) {
            result[i] = vector[i] ? 1 : 0;
        }

        return result;
    }

    public int outputVectorToInt(double[] output) {
        System.out.println("Result: " + Arrays.toString(output));

        int maxId = 0;
        double maxValue = 0;

        for (int i = 0; i < output.length; ++i) {
            if (output[i] > maxValue) {
                maxId = i;
                maxValue = output[i];
            }
        }

        return maxId;
    }

    public void save(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        nn.save(file.getAbsolutePath());
    }

    public void load(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        nn = (MultiLayerPerceptron) NeuralNetwork.createFromFile(file);
    }
}
