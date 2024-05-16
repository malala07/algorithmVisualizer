package com.example.proj1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    private int[] numbers;
    private int[] originalNumbers;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private List<int[]> swaps = new ArrayList<>();
    private int currentSwapIndex = 0;
    private Timeline timeline = new Timeline();
    private Label complexityLabel = new Label();
    private Label timeLabel = new Label();
    private Slider speedSlider;
    private int delay = 100;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.setFillWidth(true);

        canvas = new Canvas(1500, 600);
        graphicsContext = canvas.getGraphicsContext2D();
        VBox.setVgrow(canvas, Priority.ALWAYS);

        ComboBox<String> algorithmSelection1 = new ComboBox<>();
        ComboBox<String> algorithmSelection2 = new ComboBox<>();
        ComboBox<String> algorithmSelection3 = new ComboBox<>();

        algorithmSelection1.getItems().addAll("Selection Sort", "Bubble Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Heap Sort");
        algorithmSelection2.getItems().addAll("None", "Selection Sort", "Bubble Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Heap Sort");
        algorithmSelection3.getItems().addAll("None", "Selection Sort", "Bubble Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Heap Sort");

        Button runButton = new Button("Run Algorithms");
        runButton.setOnAction(event -> runAlgorithms(algorithmSelection1.getValue(), algorithmSelection2.getValue(), algorithmSelection3.getValue()));

        Button generateButton = new Button("Generate New Array");
        generateButton.setOnAction(event -> generateNewArray(100));

        Button resetButton = new Button("Reset Array");
        resetButton.setOnAction(event -> resetArray());

        TextField inputField = new TextField();
        inputField.setPromptText("Enter integers to sort. Separate each one with a comma!");
        Button inputButton = new Button("Input Array");
        inputButton.setOnAction(event -> {
            String input = inputField.getText();
            if (!input.trim().isEmpty()) {
                String[] tokens = input.split(",");
                numbers = Arrays.stream(tokens).mapToInt(Integer::parseInt).toArray();
                originalNumbers = Arrays.copyOf(numbers, numbers.length);
                visualizeSort(numbers);
            }
        });

        speedSlider = new Slider(1, 500, 50); // Extended the range for faster options
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.valueProperty().addListener((obs, oldValue, newValue) -> delay = newValue.intValue());

        Label arraySizeLabel = new Label("Array Size:");
        ComboBox<Integer> arraySizeSelection = new ComboBox<>();
        arraySizeSelection.getItems().addAll(10, 100, 1000);
        arraySizeSelection.setValue(100);
        arraySizeSelection.setOnAction(event -> generateNewArray(arraySizeSelection.getValue()));

        root.getChildren().addAll(canvas, algorithmSelection1, algorithmSelection2, algorithmSelection3, runButton, complexityLabel, timeLabel, generateButton, resetButton, inputField, inputButton, speedSlider, arraySizeLabel, arraySizeSelection);
        VBox.setMargin(canvas, new Insets(10));

        Scene scene = new Scene(root, 1400, 750);
        stage.setTitle("Sorting Visualizer");
        stage.setScene(scene);
        stage.show();

        generateNewArray(arraySizeSelection.getValue());
    }

    public static void main(String[] args) {
        launch();
    }

    private void runAlgorithms(String algorithm1, String algorithm2, String algorithm3) {

        swaps.clear();
        currentSwapIndex = 0;
        timeline.stop();
        timeLabel.setText("");

        StringBuilder timeResults = new StringBuilder();

        // Copy the array to avoid modifying the original on each run
        int[] numbersCopy1 = Arrays.copyOf(numbers, numbers.length);
        int[] numbersCopy2 = Arrays.copyOf(numbers, numbers.length);
        int[] numbersCopy3 = Arrays.copyOf(numbers, numbers.length);

        // Sort and measure time for the first algorithm
        if (algorithm1 != null && !algorithm1.equals("None")) {
            long startTime = System.nanoTime();
            sortAndMeasureTime(numbersCopy1, algorithm1);
            long endTime = System.nanoTime();
            timeResults.append(algorithm1).append(" Time taken: ").append((endTime - startTime) / 1_000_000).append(" ms\n");
        }

        // Sort and measure time for the second algorithm
        if (algorithm2 != null && !algorithm2.equals("None")) {
            long startTime = System.nanoTime();
            sortAndMeasureTime(numbersCopy2, algorithm2);
            long endTime = System.nanoTime();
            timeResults.append(algorithm2).append(" Time taken: ").append((endTime - startTime) / 1_000_000).append(" ms\n");
        }

        // Sort and measure time for the third algorithm
        if (algorithm3 != null && !algorithm3.equals("None")) {
            long startTime = System.nanoTime();
            sortAndMeasureTime(numbersCopy3, algorithm3);
            long endTime = System.nanoTime();
            timeResults.append(algorithm3).append(" Time taken: ").append((endTime - startTime) / 1_000_000).append(" ms\n");
        }

        timeLabel.setText(timeResults.toString());
    }

    private void sortAndMeasureTime(int[] numbers, String algorithm) {
        switch (algorithm) {
            case "Bubble Sort":
                complexityLabel.setText("Bubble Sort: Average - O(n^2), Worst - O(n^2)");
                bubbleSort(numbers);
                break;
            case "Selection Sort":
                complexityLabel.setText("Selection Sort: Average - O(n^2), Worst - O(n^2)");
                selectionSort(numbers);
                break;
            case "Insertion Sort":
                complexityLabel.setText("Insertion Sort: Average - O(n^2), Worst - O(n^2)");
                insertionSort(numbers);
                break;
            case "Merge Sort":
                complexityLabel.setText("Merge Sort: Average - O(n log n), Worst - O(n log n)");
                mergeSort(numbers);
                break;
            case "Quick Sort":
                complexityLabel.setText("Quick Sort: Average - O(n log n), Worst - O(n^2)");
                quickSort(numbers, 0, numbers.length - 1);
                break;
            case "Heap Sort":
                complexityLabel.setText("Heap Sort: Average - O(n log n), Worst - O(n log n)");
                heapSort(numbers);
                break;
            default:
                break;
        }
    }

    private void generateNewArray(int size) {
        numbers = new Random().ints(size, 0, 100).toArray();
        originalNumbers = Arrays.copyOf(numbers, numbers.length);
        visualizeSort(numbers);
    }

    private void resetArray() {
        numbers = Arrays.copyOf(originalNumbers, originalNumbers.length);
        swaps.clear();
        currentSwapIndex = 0;
        timeline.stop();
        timeLabel.setText("");
        visualizeSort(numbers);
    }

    private void bubbleSort(int[] numbers) {
        swaps.clear();
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = 0; j < numbers.length - i - 1; j++) {
                if (numbers[j] > numbers[j + 1]) {
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
                swaps.add(Arrays.copyOf(numbers, numbers.length));
            }
        }
        visualizeSwaps();
    }

    private void selectionSort(int[] numbers) {
        swaps.clear();
        int n = numbers.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (numbers[j] < numbers[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = numbers[minIdx];
            numbers[minIdx] = numbers[i];
            numbers[i] = temp;
            swaps.add(Arrays.copyOf(numbers, numbers.length));
        }
        visualizeSwaps();
    }

    private void insertionSort(int[] numbers) {
        swaps.clear();
        int n = numbers.length;
        for (int i = 1; i < n; ++i) {
            int key = numbers[i];
            int j = i - 1;

            while (j >= 0 && numbers[j] > key) {
                numbers[j + 1] = numbers[j];
                j = j - 1;
                swaps.add(Arrays.copyOf(numbers, numbers.length));
            }
            numbers[j + 1] = key;
            swaps.add(Arrays.copyOf(numbers, numbers.length));
        }
        visualizeSwaps();
    }

    private void mergeSort(int[] numbers) {
        swaps.clear();
        mergeSortRecursive(numbers, 0, numbers.length - 1);
        visualizeSwaps();
    }

    private void mergeSortRecursive(int[] numbers, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSortRecursive(numbers, left, middle);
            mergeSortRecursive(numbers, middle + 1, right);
            merge(numbers, left, middle, right);
        }
    }

    private void merge(int[] numbers, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(numbers, left, leftArray, 0, n1);
        System.arraycopy(numbers, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                numbers[k] = leftArray[i];
                i++;
            } else {
                numbers[k] = rightArray[j];
                j++;
            }
            k++;
            swaps.add(Arrays.copyOf(numbers, numbers.length)); // Save the array state after each merge
        }

        while (i < n1) {
            numbers[k] = leftArray[i];
            i++;
            k++;
            swaps.add(Arrays.copyOf(numbers, numbers.length)); // Save the array state after each merge
        }

        while (j < n2) {
            numbers[k] = rightArray[j];
            j++;
            k++;
            swaps.add(Arrays.copyOf(numbers, numbers.length)); // Save the array state after each merge
        }
    }

    private void quickSort(int[] numbers, int low, int high) {
        swaps.clear();
        quickSortRecursive(numbers, low, high);
        visualizeSwaps();
    }

    private void quickSortRecursive(int[] numbers, int low, int high) {
        if (low < high) {
            int pi = partition(numbers, low, high);
            quickSortRecursive(numbers, low, pi - 1);
            quickSortRecursive(numbers, pi + 1, high);
        }
    }

    private int partition(int[] numbers, int low, int high) {
        int pivot = numbers[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (numbers[j] < pivot) {
                i++;
                int temp = numbers[i];
                numbers[i] = numbers[j];
                numbers[j] = temp;
                swaps.add(Arrays.copyOf(numbers, numbers.length)); // Save the array state after each swap
            }
        }

        int temp = numbers[i + 1];
        numbers[i + 1] = numbers[high];
        numbers[high] = temp;
        swaps.add(Arrays.copyOf(numbers, numbers.length)); // Save the array state after each swap

        return i + 1;
    }

    private void heapSort(int[] numbers) {
        swaps.clear();
        int n = numbers.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(numbers, n, i);
        }

        for (int i = n - 1; i >= 0; i--) {
            int temp = numbers[0];
            numbers[0] = numbers[i];
            numbers[i] = temp;
            swaps.add(Arrays.copyOf(numbers, numbers.length));
            heapify(numbers, i, 0);
        }
        visualizeSwaps();
    }

    private void heapify(int[] numbers, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && numbers[left] > numbers[largest]) {
            largest = left;
        }

        if (right < n && numbers[right] > numbers[largest]) {
            largest = right;
        }

        if (largest != i) {
            int swap = numbers[i];
            numbers[i] = numbers[largest];
            numbers[largest] = swap;
            swaps.add(Arrays.copyOf(numbers, numbers.length));
            heapify(numbers, n, largest);
        }
    }

    private void visualizeSwaps() {
        if (currentSwapIndex < swaps.size()) {
            visualizeSort(swaps.get(currentSwapIndex));
            currentSwapIndex++;
            timeline = new Timeline(new KeyFrame(Duration.millis(delay), event -> visualizeSwaps()));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    private void visualizeSort(int[] numbers) {
        int arrayLength = numbers.length;
        double maxHeight = canvas.getHeight();
        double maxValue = Arrays.stream(numbers).max().getAsInt();
        double scaleFactor = maxHeight / maxValue;
        double barWidth = canvas.getWidth() / arrayLength;

        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < arrayLength; i++) {
            double barHeight = numbers[i] * scaleFactor;
            double x = i * barWidth;
            double y = maxHeight - barHeight;
            graphicsContext.fillRect(x, y, barWidth, barHeight);
        }
    }
}
