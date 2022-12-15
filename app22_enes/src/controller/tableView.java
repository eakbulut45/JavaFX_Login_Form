package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class tableView implements Initializable {


    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfPassword;
    @FXML
    private TableView<Register> tvRegister;
    @FXML
    private TableColumn<Register, Integer> colId;
    @FXML
    private TableColumn<Register, String> colName;
    @FXML
    private TableColumn<Register, String> colEmail;
    @FXML
    private TableColumn<Register, Integer> colPassword;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnInsert){
            insertRecord();
        }else if (event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteButton();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showRegister();
    }

    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kontakt", "root","");
            return conn;
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    public ObservableList<Register> getKontaktList(){
        ObservableList<Register> kontaktList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM register";
        Statement st;
        ResultSet rs;

        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Register register;
            while(rs.next()){
                register = new Register(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
                kontaktList.add(register);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return kontaktList;
    }

    public void showRegister(){
        ObservableList<Register> list = getKontaktList();

        colId.setCellValueFactory(new PropertyValueFactory<Register, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Register, String>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Register, String>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Register, Integer>("password"));

        tvRegister.setItems(list);
    }
    private void insertRecord(){
        String query = "INSERT INTO register VALUES ('" + tfId.getText() + "','" + tfName.getText() + "','" + tfEmail.getText() + "','"
                + tfPassword.getText() + "')";
        executeQuery(query);
        showRegister();
    }
    private void updateRecord(){
        String query = "UPDATE  register SET name  = '" + tfName.getText() + "', email = '" + tfEmail.getText() + "', password = '" +
                tfPassword.getText() + "' WHERE id = '" + tfId.getText() + "'";
        executeQuery(query);
        showRegister();
    }
    private void deleteButton(){
        String query = "DELETE FROM register WHERE id =" + tfId.getText() + "";
        executeQuery(query);
        showRegister();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public class Register {
        private int id;
        private String name;
        private String email;
        private String password;

        public Register(int id, String name, String email, String password) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
