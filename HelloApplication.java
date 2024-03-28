package com.example.proj1;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;;
import javafx.animation.Timeline;

import java.util.Arrays;

public class HelloApplication extends Application {
    private int[] numbers = {30,25,24,23,22,21,20,19,18,17,15,14,13,2,11,1,14,15,12,40,39,38,37};
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private PauseTransition pause = new PauseTransition(Duration.seconds(0.2));


    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.setFillWidth(true);

        canvas = new Canvas(1500,600);
        graphicsContext = canvas.getGraphicsContext2D();
        VBox.setVgrow(canvas, Priority.ALWAYS);


        ComboBox<String> algorithmSelection = new ComboBox<>();
        algorithmSelection.getItems().addAll("Selection Sort","Bubble Sort","Merge Sort","Quick Sort");
        algorithmSelection.setOnAction(event -> {
            // Get the selected algorithm
            String selectedAlgorithm = algorithmSelection.getValue();
            // Call the sorting algorithm based on selection
            switch (selectedAlgorithm) {
                case "Bubble Sort":
                    bubbleSort(numbers);
                    break;
                default:
                    break;
            }
        });
        root.getChildren().add(canvas);
        root.getChildren().add(algorithmSelection);
        VBox.setMargin(canvas, new Insets(10));

        Scene scene = new Scene(root,1400, 650);
        stage.setTitle("Sorting Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    private void bubbleSort(int[] numbers) {
        visualizeSort(numbers);
        pause.setOnFinished(event -> {
            for (int i = 0; i < numbers.length - 1; i++) {
                if (numbers[i] > numbers[i + 1]) {
                    int temp = numbers[i];
                    numbers[i] = numbers[i + 1];
                    numbers[i + 1] = temp;
                    visualizeSort(numbers);
                    pause.play(); // Play animation after each swap
                    return; // Return to avoid further execution until the next pause
                }
            }
        });
        // Start the sorting animation
        pause.play();
    }

    private void visualizeSort(int[] numbers){
        int arrayLength = numbers.length;
        double maxHeight = canvas.getHeight();
        double maxValue = Arrays.stream(numbers).max().getAsInt() + 100;
        double scaleFactor = maxHeight / maxValue;
        double currX = 0;
        int totalBarWidth = 0;

        for(int number : numbers){
            totalBarWidth += number * scaleFactor;
        }

        double barSpacing = 5;
        double availableWidth = canvas.getWidth() - (arrayLength - 1) * barSpacing;
        double barWidth = Math.min(availableWidth / arrayLength, totalBarWidth / arrayLength);


        graphicsContext.clearRect(0,0,canvas.getWidth(), canvas.getHeight());

        for(int i = 0; i < numbers.length; i++){
            double barHeight = (numbers[i] * scaleFactor);
            //double x = i * barWidth;
            double x = currX;
            double y = maxHeight - barHeight;
            graphicsContext.fillRect(x, y, barWidth, barHeight);

            String value = String.valueOf(numbers[i]);
            double textX = x + (barWidth / 2);
            double textY = y - 5;
            graphicsContext.fillText(value, textX,textY);

            currX += barWidth + barSpacing;
        }
    }
}