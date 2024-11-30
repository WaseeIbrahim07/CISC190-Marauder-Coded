package edu.sdccd.cisc190.views;

import edu.sdccd.cisc190.players.HumanPlayer;
import edu.sdccd.cisc190.machines.Slot;
import edu.sdccd.cisc190.machines.*;
import edu.sdccd.cisc190.services.PlayerSavesService;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SlotMachineView extends Application {

    private static final Label betAmount = new Label();
    private static final Label maxBet = new Label();
    private static final Label minBet = new Label();
    private static final Label returnAmount = new Label();
    private static final Label slot1 = new Label("❓");
    private static final Label slot2 = new Label("❓");
    private static final Label slot3 = new Label("❓");
    private static final Label won = new Label("Spin to see!");
    private static final Label money = new Label("Balance: $" + HumanPlayer.getInstance().getMoney());

    static Button spinButton = createStyledButton("Spin");
    static Button changeBet = createStyledButton("Change Bet");
    static Button mainMenu = createStyledButton("Return to Main Menu");

    static MainMenuView.SlotOptions machineSelect;
    static Slot slotMachine;
    static PlayerSavesService playerSavesService;

    @Override
    public void start(Stage primaryStage) {
        showWindow(primaryStage, 0, MainMenuView.SlotOptions.DIAMOND_DASH);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void showWindow(Stage primaryStage, int betAmt, MainMenuView.SlotOptions selectedMachine) {

        machineSelect = selectedMachine;
        switch (selectedMachine) {
            case DIAMOND_DASH -> slotMachine = new DiamondDash();
            case HONDA_TRUNK -> slotMachine = new HondaTrunk();
            case TREASURE_SPINS -> slotMachine = new TreasureSpins();
            case MEGA_MOOLAH -> slotMachine = new MegaMoolah();
            case RAINBOW_RICHES -> slotMachine = new RainbowRiches();
            default -> slotMachine = new DiamondDash();
        }

        primaryStage.setTitle("Casino Royale - Slot Machine");

        // Styled Labels
        betAmount.setText("You're betting: $" + betAmt);
        betAmount.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        betAmount.setTextFill(Color.LIGHTGOLDENRODYELLOW);

        maxBet.setText("Max. Bet: " + slotMachine.getMaxBet());
        maxBet.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        minBet.setText("Min. Bet: " + slotMachine.getMinBet());
        minBet.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        returnAmount.setText("Return: " + slotMachine.getReturnAmt());
        returnAmount.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        maxBet.setTextFill(Color.RED);
        minBet.setTextFill(Color.RED);
        returnAmount.setTextFill(Color.RED);


        slot1.setStyle("-fx-font-size: 60px;");
        slot2.setStyle("-fx-font-size: 60px;");
        slot3.setStyle("-fx-font-size: 60px;");
        slot1.setTextFill(Color.ORANGERED);
        slot2.setTextFill(Color.ORANGERED);
        slot3.setTextFill(Color.ORANGERED);


        won.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        won.setTextFill(Color.GOLD);

        money.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        money.setTextFill(Color.LIGHTGREEN);

        // Button Actions
        spinButton.setOnAction(e -> spin(betAmt, primaryStage));
        changeBet.setOnAction(e -> {
            primaryStage.close();
            BetView.showWindow(primaryStage, machineSelect);
        });
        mainMenu.setOnAction(e -> {
            primaryStage.close();
            MainMenuView.setupWindow(primaryStage);
        });

        // Slot information
        HBox slotInformation = new HBox(10, maxBet, minBet, returnAmount);
        slotInformation.setAlignment(Pos.CENTER);

        // Slots Display Row
        HBox slotsRow = new HBox(20, slot1, slot2, slot3);
        slotsRow.setAlignment(Pos.CENTER);

        // Main Layout
        VBox layout = new VBox(20, betAmount, won, money, slotInformation, slotsRow, spinButton, changeBet, mainMenu);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #000000, #660000);" +
                        "-fx-padding: 30px;"
        );

        // Scene Setup
        Scene scene = new Scene(layout, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void spin(int betAmt, Stage primaryStage) {
        if (!slotMachine.canBet(betAmt)) {
            showAlert("Invalid Bet", "Your bet is outside the allowed range or exceeds your balance.");
            primaryStage.close();
            BetView.showWindow(primaryStage, machineSelect);
            return;
        }

        String[] symbols = slotMachine.generateSpunSymbols();
        slot1.setText(symbols[0]);
        slot2.setText(symbols[1]);
        slot3.setText(symbols[2]);

        int newBalance = slotMachine.calculatePayout(HumanPlayer.getInstance().getMoney(), symbols, betAmt);
        HumanPlayer.getInstance().setMoney(newBalance);

        if (slotMachine.evaluateWinCondition(symbols) >= 2) {
            won.setText("Wow, you won!");
        } else {
            won.setText("You lost :(");
        }

        money.setText("Balance: $" + HumanPlayer.getInstance().getMoney());

        if (HumanPlayer.getInstance().getMoney() <= 0) {
            showAlert("Game Over", "You're out of money! Better luck next time.");
            PlayerSavesService.deleteState();
            primaryStage.close();
        }
    }
    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffcc00, #ff9900);" +
                        "-fx-text-fill: black;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10px 20px;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff9900, #ff6600);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10px 20px;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffcc00, #ff9900);" +
                        "-fx-text-fill: black;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10px 20px;"
        ));

        return button;
    }
}