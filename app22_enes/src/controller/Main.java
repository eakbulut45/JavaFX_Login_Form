package controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
    String id_Data;
    String email_Data;
    String password_Data;
    String userName_Data;

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane anchorPane = createPane();
        Scene scene = new Scene(anchorPane, 595, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX App22_enes");
        primaryStage.getIcons().add(new javafx.scene.image.Image("/image/javaicon.png"));
        primaryStage.show();
    }

    private AnchorPane createPane() throws SQLException {

        Circle circle = new Circle();
        circle.setCenterX(50.0f);
        circle.setCenterY(500.0f);
        circle.setRadius(20.0f);
        circle.setFill(Color.RED);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3);

        TabPane tabPane = new TabPane();
        tabPane.setDisable(true);

        Tab tab1 = new Tab("Login");
        GridPane gridPaneLog = createGridPane();
        login(gridPaneLog);
        tab1.setContent(gridPaneLog);
        tab1.setClosable(false);

        Tab tab2 = new Tab("Registration");
        GridPane gridPaneReg = createGridPane();
        registration(gridPaneReg);
        tab2.setContent(gridPaneReg);
        tab2.setClosable(false);

        tabPane.getTabs().addAll(tab1, tab2);

        Text text = new Text("DB Fehlt!");
        text.setFill(Color.web("#ff0000"));
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        AnchorPane pane = new AnchorPane();

        AnchorPane.setTopAnchor(circle, 5.0);
        AnchorPane.setLeftAnchor(circle, 15.0);

        AnchorPane.setTopAnchor(text, 20.0);
        AnchorPane.setLeftAnchor(text, 80.0);

        AnchorPane.setTopAnchor(tabPane, 50.0);
        AnchorPane.setRightAnchor(tabPane, 15.0);
        AnchorPane.setBottomAnchor(tabPane, 15.0);
        AnchorPane.setLeftAnchor(tabPane, 15.0);
        pane.getChildren().addAll(circle, text, tabPane);
        pane.setStyle("-fx-background-color: BEIGE");

        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    ConnectionDB();
                    text.setText("DB Connect!");
                    text.setFill(Color.web("#00ff00"));
                    text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                    circle.setFill(Color.LIGHTGREEN);
                    tabPane.setDisable(false);

                } catch (SQLException throwables) {
                    throwables.fillInStackTrace();
                    showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), "Verbindung Fehler!", "Keine Verbindung!");
                }
            }
        });


        return pane;
    }

    private GridPane createGridPane() {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(50, 100, 100, 100));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        return gridPane;
    }

    private void login(GridPane gridPane) {
        Label headerLabel = new Label("Login Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20));

        Text emailText = new javafx.scene.text.Text("Email: ");
        gridPane.add(emailText, 0, 1);
        emailText.setUnderline(true);

        TextField emailTextField = new TextField();
        Tooltip tt = new Tooltip("Bitte Email eingeben!!!");
        emailTextField.setPromptText("Email");
        emailTextField.setTooltip(tt);
        gridPane.add(emailTextField, 1, 1);

        javafx.scene.text.Text passText = new javafx.scene.text.Text("Password: ");
        gridPane.add(passText, 0, 2);
        passText.setUnderline(true);

        PasswordField passTextField = new PasswordField();
        Tooltip tt1 = new Tooltip("Bitte Password eingeben!!!");
        passTextField.setPromptText("Password");
        passTextField.setTooltip(tt1);
        gridPane.add(passTextField, 1, 2);

        Button loginButton = new Button("Login");
        loginButton.setPrefHeight(40);
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(100);
        gridPane.add(loginButton, 0, 3, 2, 1);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setMargin(loginButton, new Insets(20));

        Label lbl1 = new Label("Forgot Password?");
        gridPane.add(lbl1, 1, 4);

        loginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String userEmailLog = emailTextField.getText();
                String userPassLog = passTextField.getText();
                try {
                    String url = "jdbc:mysql://localhost:3306/kontakt";
                    String user = "root";
                    String passwort = "";
                    Connection conn = DriverManager.getConnection(url, user, passwort);

                    PreparedStatement st;
                    ResultSet rs;
                    String sql = ("SELECT * FROM register" + " WHERE email = " + "'" + userEmailLog + "'");
                    st = conn.prepareStatement(sql);
                    rs = st.executeQuery(sql);
                    while (rs.next()) {
                        id_Data = rs.getString(1);
                        email_Data = rs.getString(3);
                        password_Data = rs.getString(4);
                    }

                    if (userEmailLog.equals(email_Data) && userPassLog.equals(password_Data)
                            && emailTextField.getText().isEmpty() != true && passTextField.getText().isEmpty() != true) {
                        loginMenuBar();
                    } else if (userEmailLog.equals(email_Data)) {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Password ist falsch!");
                    } else if (userPassLog.equals(password_Data)) {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Email ist falsch!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Email und Password sind falsch!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void loginMenuBar() {
        Stage stage = new Stage();

        Menu profile = new Menu("Profile");
        profile.setGraphic(new ImageView("/image/profile.png"));
        MenuItem itemEinstellung = new MenuItem("Einstellung");
        MenuItem itemAbmelden = new MenuItem("Abmelden");
        MenuItem itemBeenden = new MenuItem("Beenden");
        profile.getItems().addAll(itemEinstellung, itemAbmelden, itemBeenden);

        Menu rechner = new Menu("Rechner");
        rechner.setGraphic(new ImageView("/image/rechner.png"));
        MenuItem itemKonCalc = new MenuItem("Konversion Calc");
        rechner.getItems().addAll(itemKonCalc);

        Menu spiele = new Menu("Spiele");
        spiele.setGraphic(new ImageView("/image/spiel.png"));
        MenuItem itemSpiel1 = new MenuItem("Stein Papier Schere");
        MenuItem itemSpiel2 = new MenuItem("Würfel");
        spiele.getItems().addAll(itemSpiel1,itemSpiel2);

        Menu media = new Menu("Media");
        media.setGraphic(new ImageView("/image/media.png"));
        MenuItem itemWetterApp = new MenuItem("Wetter App");
        MenuItem itemImage = new MenuItem("Bilder");
        media.getItems().addAll(itemWetterApp, itemImage);

        Menu hilfe = new Menu("Hilfe");
        hilfe.setGraphic(new ImageView("/image/hilfe.png"));
        MenuItem itemÜberApp = new MenuItem("über App22");
        hilfe.getItems().addAll(itemÜberApp);

        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(profile, rechner, spiele, media, hilfe);

        GridPane gridPane = createGridPane();

        VBox vBox = new VBox(menuBar, gridPane);

        Scene scene = new Scene(vBox, 760, 500);
        stage.setScene(scene);
        stage.setTitle("MENU");
        stage.getIcons().add(new Image("/image/javaicon.png"));
        stage.show();

        profile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridPane.getChildren().clear();
            }
        });

        spiele.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridPane.getChildren().clear();
            }
        });

        hilfe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridPane.getChildren().clear();
            }
        });

        itemKonCalc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridPane.getChildren().clear();
                konCalc(gridPane);
            }
        });

        itemWetterApp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gridPane.getChildren().clear();
                    wetterApp(gridPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        itemImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gridPane.getChildren().clear();
                    bilder(gridPane);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        itemSpiel1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gridPane.getChildren().clear();
                    spiel1(gridPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        itemEinstellung.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gridPane.getChildren().clear();
                    tableViews(gridPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        itemSpiel2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gridPane.getChildren().clear();
                    spiel2(gridPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void spiel2(GridPane gridPane) throws Exception {
        URL Url = getClass().getClassLoader().getResource("view/spiel2.fxml");
        AnchorPane root = FXMLLoader.<AnchorPane>load(Url);
        gridPane.getChildren().add(root);
        GridPane.setHalignment(root, HPos.CENTER);
    }

    private void tableViews(GridPane gridPane) throws Exception {
        URL Url = getClass().getClassLoader().getResource("view/tableView.fxml");
        AnchorPane root = FXMLLoader.<AnchorPane>load(Url);
        gridPane.getChildren().add(root);
        GridPane.setHalignment(root, HPos.CENTER);
    }

    private void spiel1(GridPane gridPane) throws Exception {
        URL Url = getClass().getClassLoader().getResource("view/spiel1.fxml");
        AnchorPane root = FXMLLoader.<AnchorPane>load(Url);
        gridPane.getChildren().add(root);
        GridPane.setHalignment(root, HPos.CENTER);
    }

    private void bilder(GridPane gridPane) throws FileNotFoundException {

        TilePane tile = new TilePane();
        tile.setPadding(new Insets(15, 15, 15, 15));
        tile.setHgap(15);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color: DAE6F3;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tile);
        gridPane.getChildren().add(scrollPane);

        String path = "C:\\Users\\schulung\\OneDrive - BBQ Berlin\\Desktop\\images";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (final File file : listOfFiles) {
            ImageView imageView;
            imageView = createImageView(file);
            tile.getChildren().addAll(imageView);
        }
    }

    private ImageView createImageView(final File imageFile) {
        ImageView imageView = null;
        try {
            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true, true);
            imageView = new ImageView(image);
            imageView.setFitWidth(150);

            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){

                        if(mouseEvent.getClickCount() == 2){
                            try {
                                Stage newStage = new Stage();
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                Image image = new Image(new FileInputStream(imageFile));
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane, 760, 500);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return imageView;
    }

    private void wetterApp(GridPane gridPane) throws Exception {
        Label headerLabel = new Label("TEMPERATUR");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20));

        URL fxmlUrl = getClass().getClassLoader().getResource("view/WeatherView.fxml");
        AnchorPane root = FXMLLoader.<AnchorPane>load(fxmlUrl);
        gridPane.add(root, 0, 1, 2, 1);
        GridPane.setHalignment(root, HPos.CENTER);
    }

    private void konCalc(GridPane gridPane) {
        Label headerLabel = new Label("Conversion Calculator");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20));

        TextField linkTextField = new TextField();
        gridPane.add(linkTextField, 1, 1);
        TextField rechtTextField = new TextField();
        gridPane.add(rechtTextField, 2, 1);

        String m = "m";
        String dm = "dm";
        String cm = "cm";
        String mm = "mm";

        ComboBox<String> linkMenu = new ComboBox<>();
        linkMenu.getItems().addAll(m, dm, cm, mm);
        gridPane.add(linkMenu, 1, 2);

        ComboBox<String> rechtMenu = new ComboBox<>();
        rechtMenu.getItems().addAll(m, dm, cm, mm);
        linkMenu.getSelectionModel().select(0);
        rechtMenu.getSelectionModel().select(2);
        gridPane.add(rechtMenu, 2, 2);

        linkTextField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                linkTextField.selectAll();
            }
        });

        rechtTextField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rechtTextField.selectAll();
            }
        });

        linkTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    Double value = Double.valueOf(linkTextField.getText());
                    if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value * 10));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value * 100));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 1000));

                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value * 10));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 100));

                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 100));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 10));

                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 1000));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value / 100));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value));
                    }
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
            }
        });

        rechtTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    Double value = Double.valueOf(rechtTextField.getText());
                    if (rechtMenu.getValue() == "m" && linkMenu.getValue() == "m") {
                        linkTextField.setText(String.valueOf(value));
                    } else if (rechtMenu.getValue() == "m" && linkMenu.getValue() == "dm") {
                        linkTextField.setText(String.valueOf(value * 10));
                    } else if (rechtMenu.getValue() == "m" && linkMenu.getValue() == "cm") {
                        linkTextField.setText(String.valueOf(value * 100));
                    } else if (rechtMenu.getValue() == "m" && linkMenu.getValue() == "mm") {
                        linkTextField.setText(String.valueOf(value * 1000));

                    } else if (rechtMenu.getValue() == "dm" && linkMenu.getValue() == "m") {
                        linkTextField.setText(String.valueOf(value / 10));
                    } else if (rechtMenu.getValue() == "dm" && linkMenu.getValue() == "dm") {
                        linkTextField.setText(String.valueOf(value));
                    } else if (rechtMenu.getValue() == "dm" && linkMenu.getValue() == "cm") {
                        linkTextField.setText(String.valueOf(value * 10));
                    } else if (rechtMenu.getValue() == "dm" && linkMenu.getValue() == "mm") {
                        linkTextField.setText(String.valueOf(value * 100));

                    } else if (rechtMenu.getValue() == "cm" && linkMenu.getValue() == "m") {
                        linkTextField.setText(String.valueOf(value / 100));
                    } else if (rechtMenu.getValue() == "cm" && linkMenu.getValue() == "dm") {
                        linkTextField.setText(String.valueOf(value / 10));
                    } else if (rechtMenu.getValue() == "cm" && linkMenu.getValue() == "cm") {
                        linkTextField.setText(String.valueOf(value));
                    } else if (rechtMenu.getValue() == "cm" && linkMenu.getValue() == "mm") {
                        linkTextField.setText(String.valueOf(value * 10));

                    } else if (rechtMenu.getValue() == "mm" && linkMenu.getValue() == "m") {
                        linkTextField.setText(String.valueOf(value / 1000));
                    } else if (rechtMenu.getValue() == "mm" && linkMenu.getValue() == "dm") {
                        linkTextField.setText(String.valueOf(value / 100));
                    } else if (rechtMenu.getValue() == "mm" && linkMenu.getValue() == "cm") {
                        linkTextField.setText(String.valueOf(value / 10));
                    } else if (rechtMenu.getValue() == "mm" && linkMenu.getValue() == "mm") {
                        linkTextField.setText(String.valueOf(value));
                    }
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
            }
        });

        rechtMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Double value = Double.valueOf(linkTextField.getText());
                    if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value * 10));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value * 100));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 1000));

                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value * 10));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 100));

                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 100));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 10));

                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 1000));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value / 100));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value));
                    }
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
            }
        });

        linkMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Double value = Double.valueOf(linkTextField.getText());
                    if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value * 10));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value * 100));
                    } else if (linkMenu.getValue() == "m" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 1000));

                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value * 10));
                    } else if (linkMenu.getValue() == "dm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 100));

                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 100));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value));
                    } else if (linkMenu.getValue() == "cm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value * 10));

                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "m") {
                        rechtTextField.setText(String.valueOf(value / 1000));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "dm") {
                        rechtTextField.setText(String.valueOf(value / 100));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "cm") {
                        rechtTextField.setText(String.valueOf(value / 10));
                    } else if (linkMenu.getValue() == "mm" && rechtMenu.getValue() == "mm") {
                        rechtTextField.setText(String.valueOf(value));
                    }
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
            }
        });
    }

    private void registration(GridPane gridPane) throws SQLException {

        Label headerLabel = new Label("Registration Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20));

        Label nameLabel = new Label("Username : ");
        gridPane.add(nameLabel, 0, 1);
        nameLabel.setUnderline(true);

        TextField nameField = new TextField();
        nameField.setPrefHeight(30);
        nameField.setPromptText("Username");
        gridPane.add(nameField, 1, 1);

        Label emailLabel = new Label("Email : ");
        gridPane.add(emailLabel, 0, 2);
        emailLabel.setUnderline(true);

        TextField emailField = new TextField();
        emailField.setPrefHeight(30);
        emailField.setPromptText("Email");
        gridPane.add(emailField, 1, 2);

        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 3);
        passwordLabel.setUnderline(true);

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(30);
        passwordField.setPromptText("Password");
        gridPane.add(passwordField, 1, 3);

        Label bestätigungLabel = new Label("Bestätigung : ");
        gridPane.add(bestätigungLabel, 0, 4);
        bestätigungLabel.setUnderline(true);

        PasswordField bestätigungsField = new PasswordField();
        bestätigungsField.setPrefHeight(30);
        bestätigungsField.setPromptText("Bestätigung");
        gridPane.add(bestätigungsField, 1, 4);

        Button registerButton = new Button("Register");
        registerButton.setPrefHeight(40);
        registerButton.setDefaultButton(true);
        registerButton.setPrefWidth(100);
        gridPane.add(registerButton, 0, 5, 2, 1);
        GridPane.setHalignment(registerButton, HPos.CENTER);
        GridPane.setMargin(registerButton, new Insets(20));

        emailField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String userEmailLog = emailField.getText();
                try {
                    String url = "jdbc:mysql://localhost:3306/kontakt";
                    String user = "root";
                    String passwort = "";
                    Connection conn = DriverManager.getConnection(url, user, passwort);

                    PreparedStatement st;
                    ResultSet rs;
                    String sql = ("SELECT * FROM register" + " WHERE email = " + "'" + userEmailLog + "'");
                    st = conn.prepareStatement(sql);
                    rs = st.executeQuery(sql);
                    while (rs.next()) {
                        id_Data = rs.getString(1);
                        userName_Data = rs.getString(2);
                        email_Data = rs.getString(3);
                    }
                    if (!userEmailLog.equals(email_Data)) {
                        emailField.setStyle("-fx-text-fill: green;");
                    }
                    if (userEmailLog.equals(email_Data)) {
                        emailField.setStyle("-fx-text-fill: red;");
                    }
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        });

        registerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String userEmailLog = emailField.getText();
                try {
                    String url = "jdbc:mysql://localhost:3306/kontakt";
                    String user = "root";
                    String passwort = "";
                    Connection conn = DriverManager.getConnection(url, user, passwort);

                    PreparedStatement st;
                    ResultSet rs;
                    String sql = ("SELECT * FROM register" + " WHERE email = " + "'" + userEmailLog + "'");
                    st = conn.prepareStatement(sql);
                    rs = st.executeQuery(sql);
                    while (rs.next()) {
                        id_Data = rs.getString(1);
                        userName_Data = rs.getString(2);
                        email_Data = rs.getString(3);
                    }
                    if (userEmailLog.equals(email_Data)) {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Diese Email ist schon benutzt!");
                        return;
                    }
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
                if (nameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Bitte gib dein Name");
                } else if (emailField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Bitte gib dein Email ");
                } else if (passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Bitte gib dein Password");
                } else if (!emailCheck(emailField.getText())) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Bitte gib eine richtige Email Adresse!");
                } else if (!passwordField.getText().equals(bestätigungsField.getText())) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Bestätigung ist Falsch!");
                } else if (!passCheck(passwordField.getText())) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Password muss \n * mindestens 8 Zeichen\n * min. 1 Klein- ung Großbuchstaben\n * Zahlen \n * Zonderzeichen enthalten!");
                } else {
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String pass = passwordField.getText();
                    try {
                        Connection(name, email, pass);
                    } catch (Exception e) {
                        e.fillInStackTrace();
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Verbindung Fehler!", "Verbindung ist weg!");
                        System.exit(0);
                    }
                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Registration Successful!", "Welcome " + nameField.getText());
                    nameField.clear();
                    emailField.clear();
                    passwordField.clear();
                    bestätigungsField.clear();
                }
            }
        });
    }

    private boolean passCheck(String pass) {
        Pattern p = Pattern.compile("(^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$)");
        Matcher m = p.matcher(pass);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean emailCheck(String email) {
        Pattern p = Pattern.compile(("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"));
        Matcher m = p.matcher(email);
        if (m.matches()) {
            return true;
        } else {
            return false;

        }
    }

    private void Connection(String name, String email, String pass) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/kontakt";
        String user = "root";
        String passwort = "";
        Connection conn = DriverManager.getConnection(url, user, passwort);
        Statement statement = conn.createStatement();
        String sql = "INSERT INTO register(name,email,password)" + " VALUES " + "('" + name + "', '" + email + "', '" + pass + "')";
        statement.execute(sql);
    }

    private void ConnectionDB() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/kontakt";
        String user = "root";
        String passwort = "";
        Connection conn = DriverManager.getConnection(url, user, passwort);
        Statement statement = conn.createStatement();
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
