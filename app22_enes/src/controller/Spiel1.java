package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Spiel1 {

    private static final String PAPER = "paper";
    private static final String STONE = "stone";
    private static final String SCISSORS = "scissors";
    private Image image;

    @FXML
    private Label playerScore;
    @FXML
    private Label computerScore;
    @FXML
    private ImageView player;
    @FXML
    private Label result;
    @FXML
    private ImageView computer;
    @FXML
    private void playerTurn(ActionEvent event) {
        String playerChoice = null;
        switch (((Button) event.getSource()).getId()) {
            case "paperBtn":
                image = new Image("/image/ic_paper.png");
                playerChoice = PAPER;
                break;
            case "stoneBtn":
                image = new Image("/image/ic_stone.png");
                playerChoice = STONE;
                break;
            case "scissorsBtn":
                image = new Image("/image/ic_scissors.png");
                playerChoice = SCISSORS;
                break;
        }
        player.setImage(image);

        winner(playerChoice, computerTurn());
    }

    private String computerTurn() {
        String computerChoice = null;
        int index = (int) (Math.random() * 3);
        switch (index) {
            case 0:
                image = new Image("/image/ic_stone.png");
                computerChoice = STONE;
                break;
            case 1:
                image = new Image("/image/ic_paper.png");
                computerChoice = PAPER;
                break;
            case 2:
                image = new Image("/image/ic_scissors.png");
                computerChoice = SCISSORS;
                break;
        }
        computer.setImage(image);
        return computerChoice;
    }

    public void playerWin() {
        result.setText("Win");
        playerScore.setText(String.valueOf(Integer.parseInt(playerScore.getText()) + 1));
    }

    public void computerWin() {
        result.setText("Lose");
        computerScore.setText(String.valueOf(Integer.parseInt(computerScore.getText()) + 1));
    }

    public void draw() {
        result.setText("Draw");
    }

    private void winner(String playerChoice, String computerChoice) {
        if (playerChoice.equals(computerChoice)) {
            draw();
            return;
        }
        if (playerChoice.equals(STONE)) {
            if (computerChoice.equals(PAPER)) {
                computerWin();
            } else if (computerChoice.equals(SCISSORS)) {
                playerWin();
            }
        } else if (playerChoice.equals(PAPER)) {
            if (computerChoice.equals(STONE)) {
                playerWin();
            } else if (computerChoice.equals(SCISSORS)) {
                computerWin();
            }
        } else {
            if (computerChoice.equals(STONE)) {
                computerWin();
            } else if (computerChoice.equals(PAPER)) {
                playerWin();
            }
        }
    }
}