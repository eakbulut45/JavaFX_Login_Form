package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class spiel2
{

    private Image image;

    @FXML
    private ImageView diceImagePlayer;
    @FXML
    private ImageView diceImageComputer;
    @FXML
    private Button rollButton;
    @FXML
    private Label result;
    @FXML
    private Label playerScore;
    @FXML
    private Label computerScore;

    Random random = new Random();
    File file1;
    File file2;

    FileInputStream[] DiceImg = new FileInputStream[6];
    Image[] Dice = new Image[6];

    public void diceIn() throws FileNotFoundException {

        DiceImg[0] = new FileInputStream("src/image/dice1.png");
        DiceImg[1] = new FileInputStream("src/image/dice2.png");
        DiceImg[2] = new FileInputStream("src/image/dice3.png");
        DiceImg[3] = new FileInputStream("src/image/dice4.png");
        DiceImg[4] = new FileInputStream("src/image/dice5.png");
        DiceImg[5] = new FileInputStream("src/image/dice6.png");

        for(int i = 0; i < Dice.length; i++){
            Dice[i] = new Image(DiceImg[i]);
        }
    }

    public int randomNumber(){
        return (int) (Math.random() * 6);
    }

    public void rollingButt() throws InterruptedException {
        int player = randomNumber();
        int computer = randomNumber();
        rollButton.setDisable(true);
        Thread thread = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 15; i++) {
                        file1 = new File("src/dice1/dice" + (random.nextInt(6) + 1) + ".png");
                        diceImagePlayer.setImage(new Image(file1.toURI().toString()));
                    }
                    for (int i = 0; i < 15; i++) {
                        file2 = new File("src/dice1/dice" + (random.nextInt(6) + 1) + ".png");
                        diceImageComputer.setImage(new Image(file2.toURI().toString()));
                        Thread.sleep(20);
                    }
                    diceImagePlayer.setImage(Dice[player]);
                    diceImageComputer.setImage(Dice[computer]);
                    rollButton.setDisable(false);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        //Thread.sleep(500);
        win(player, computer);
    }
    private void win(int player, int computer) throws InterruptedException {
        if(player > computer){
            result.setText("Win");
            playerScore.setText(String.valueOf(Integer.parseInt(playerScore.getText()) + 1));
        }
        else if(player == computer){
            result.setText("Draw");
        }
        else{
            result.setText("Lose");
            computerScore.setText(String.valueOf(Integer.parseInt(computerScore.getText()) + 1));
        }
    }
    @FXML
    private void roll(ActionEvent event) throws FileNotFoundException, InterruptedException {
        diceIn();
        rollingButt();
    }
}