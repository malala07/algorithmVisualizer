package com.example.proj1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    private int[] numbers = {30, 25, 24, 23, 22, 21, 20, 19, 18, 17, 15, 14, 13, 2, 11, 1, 14, 15, 12, 40, 39, 38, 37};
    private int[] originalNumbers = Arrays.copyOf(numbers, numbers.length);
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private List<int[]> swaps = new ArrayList<>();
    private int currentSwapIndex = 0;
    private Timeline timeline = new Timeline();
    private Label complexityLabel = new Label();
    private Label timeLabel = new Label();
    private long startTime;
    private long endTime;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.setFillWidth(true);

        canvas = new Canvas(1500, 600);
        graphicsContext = canvas.getGraphicsContext2D();
        VBox.setVgrow(canvas, Priority.ALWAYS);

        ComboBox<String> algorithmSelection = new ComboBox<>();
        algorithmSelection.getItems().addAll("Selection Sort", "Bubble Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Heap Sort");
        algorithmSelection.setOnAction(event -> {
            // Reset swaps and currentSwapIndex for new sorting
            swaps.clear();
            currentSwapIndex = 0;
            timeline.stop();
            timeLabel.setText("");

            // Get the selected algorithm
            String selectedAlgorithm = algorithmSelection.getValue();
            // Copy the array to avoid modifying the original on each run
            int[] numbersCopy = Arrays.copyOf(numbers, numbers.length);
            // Start the timer
            startTime = System.currentTimeMillis();
            // Call the sorting algorithm based on selection
            switch (selectedAlgorithm) {
                case "Bubble Sort":
                    complexityLabel.setText("Bubble Sort: Average - O(n^2), Worst - O(n^2)");
                    bubbleSort(numbersCopy);
                    break;
                case "Selection Sort":
                    complexityLabel.setText("Selection Sort: Average - O(n^2), Worst - O(n^2)");
                    selectionSort(numbersCopy);
                    break;
                case "Insertion Sort":
                    complexityLabel.setText("Insertion Sort: Average - O(n^2), Worst - O(n^2)");
                    insertionSort(numbersCopy);
                    break;
                case "Merge Sort":
                    complexityLabel.setText("Merge Sort: Average - O(n log n), Worst - O(n log n)");
                    mergeSort(numbersCopy);
                    visualizeSwaps();
                    break;
                case "Quick Sort":
                    complexityLabel.setText("Quick Sort: Average - O(n log n), Worst - O(n^2)");
                    quickSort(numbersCopy, 0, numbersCopy.length - 1);
                    visualizeSwaps();
                    break;
                case "Heap Sort":
                    complexityLabel.setText("Heap Sort: Average - O(n log n), Worst - O(n log n)");
                    heapSort(numbersCopy);
                    visualizeSwaps();
                    break;
                default:
                    break;
            }
        });

        Button generateButton = new Button("Generate New Array");
        generateButton.setOnAction(event -> {
            Random rand = new Random();
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = rand.nextInt(100);
            }
            originalNumbers = Arrays.copyOf(numbers, numbers.length);
            visualizeSort(numbers);
        });

        Button resetButton = new Button("Reset Array");
        resetButton.setOnAction(event -> {
            numbers = Arrays.copyOf(originalNumbers, originalNumbers.length);
            swaps.clear();
            currentSwapIndex = 0;
            timeline.stop();
            timeLabel.setText("");
            visualizeSort(numbers);
        });

        root.getChildren().add(canvas);
        root.getChildren().add(algorithmSelection);
        root.getChildren().add(complexityLabel);
        root.getChildren().add(timeLabel);
        root.getChildren().add(generateButton);
        root.getChildren().add(resetButton);
        VBox.setMargin(canvas, new Insets(10));

        Scene scene = new Scene(root, 1400, 750);
        stage.setTitle("Sorting Visualizer");
        stage.setScene(scene);
        stage.show();

        visualizeSort(numbers);
    }

    public static void main(String[] args) {
        launch();
    }

    private void bubbleSort(int[] numbers) {
        timeline = new Timeline();
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = 0; j < numbers.length - i - 1; j++) {
                int[] snapshot = Arrays.copyOf(numbers, numbers.length);
                if (numbers[j] > numbers[j + 1]) {
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
                swaps.add(snapshot);
            }
        }
        endTime = System.currentTimeMillis();
        timeLabel.setText("Time taken: " + (endTime - startTime) + " ms");
        visualizeSwaps();
    }

    private void selectionSort(int[] numbers) {
        timeline = new Timeline();
        int n = numbers.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (numbers[j] < numbers[minIdx]) {
                    minIdx = j;
                }
            }
            int[] snapshot = Arrays.copyOf(numbers, numbers.length);
            int temp = numbers[minIdx];
            numbers[minIdx] = numbers[i];
            numbers[i] = temp;
            swaps.add(snapshot);
        }
        endTime = System.currentTimeMillis();
        timeLabel.setText("Time taken: " + (endTime - startTime) + " ms");
        visualizeSwaps();
    }

    private void insertionSort(int[] numbers) {
        timeline = new Timeline();
        int n = numbers.length;
        for (int i = 1; i < n; ++i) {
            int key = numbers[i];
            int j = i - 1;

            while (j >= 0 && numbers[j] > key) {
                numbers[j + 1] = numbers[j];
                j = j - 1;
            }
            numbers[j + 1] = key;
            swaps.add(Arrays.copyOf(numbers, numbers.length));
        }
        endTime = System.currentTimeMillis();
        timeLabel.setText("Time taken: " + (endTime - startTime) + " ms");
        visualizeSwaps();
    }

    private void mergeSort(int[] numbers) {
        mergeSortRecursive(numbers, 0, numbers.length - 1);
        endTime = System.currentTimeMillis();
        timeLabel.setText("Time taken: " + (endTime - startTime) + " ms");
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
        if (low < high) {
            int pi = partition(numbers, low, high);
            quickSort(numbers, low, pi - 1);
            quickSort(numbers, pi + 1, high);
        }
        endTime = System.currentTimeMillis();
        timeLabel.setText("Time taken: " + (endTime - startTime) + " ms");
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
        int n = numbers.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(numbers, n, i);

        for (int i = n - 1; i >= 0; i--) {
            int temp = numbers[0];
            numbers[0] = numbers[i];
            numbers[i] = temp;
            swaps.add(Arrays.copyOf(numbers, numbers.length));
            heapify(numbers, i, 0);
        }
        endTime = System.currentTimeMillis();
        timeLabel.setText("Time taken: " + (endTime - startTime) + " ms");
    }

    private void heapify(int[] numbers, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && numbers[left] > numbers[largest])
            largest = left;

        if (right < n && numbers[right] > numbers[largest])
            largest = right;

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
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> visualizeSwaps()));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    private void visualizeSort(int[] numbers) {
        int arrayLength = numbers.length;
        double maxHeight = canvas.getHeight();
        double maxValue = Arrays.stream(numbers).max().getAsInt() + 100;
        double scaleFactor = maxHeight / maxValue;
        double currX = 0;
        int totalBarWidth = 0;

        for (int number : numbers) {
            totalBarWidth += number * scaleFactor;
        }

        double barSpacing = 5;
        double availableWidth = canvas.getWidth() - (arrayLength - 1) * barSpacing;
        double barWidth = Math.min(availableWidth / arrayLength, totalBarWidth / arrayLength);

        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < numbers.length; i++) {
            double barHeight = (numbers[i] * scaleFactor);
            double x = currX;
            double y = maxHeight - barHeight;
            graphicsContext.fillRect(x, y, barWidth, barHeight);

            String value = String.valueOf(numbers[i]);
            double textX = x + (barWidth / 2);
            double textY = y - 5;
            graphicsContext.fillText(value, textX, textY);

            currX += barWidth + barSpacing;
        }
    }
}
