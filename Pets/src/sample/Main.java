package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static Pet pet;
    private static Pet selectedPet;
    TabPane tabPane;
    ComboBox comboBoxStatus;
    HBox hBox;
    HBox hBoxbtnNew;
    HBox hBoxId;
    HBox hBoxName;
    HBox hBoxStatus;
    HBox hBoxUpdateId;
    HBox hBoxUpdateName;
    HBox hBoxUpdateStatus;
    HBox hBoxUpdateDeleteBtn;
    VBox vBox;
    Label lblFilter;
    Label lblStatus;
    Label lblId;
    Label lblSearchId;
    Label lblName;
    Label lblUpdateName;
    Label lblUpdateStatus;
    Label lblUpdateId;
    TextField txtSearchId;
    TextField txtPetId;
    TextField txtPetName;
    TextField txtUpdatePetId;
    TextField txtUpdatePetName;
    ComboBox comboBoxNewStatus;
    ComboBox comboBoxUpdate;
    TableView tablePetsData;
    Button btnSearch;
    Button btnAddNew;
    Button btnUpdate;
    Button btnDelete;
    GridPane gridPane;
    GridPane gridPaneAddNew;
    GridPane gridPaneUpdate;
    PieChart pieChart;
    static URL apiUrl;

    static {
        try {
            apiUrl = new URL("https://petstore.swagger.io/v2/pet/findByStatus?status=available,sold,pending");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    static List<Pet> allPetData;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        initialDesign(primaryStage);
        SetText();
        PrepareComboBoxStatus();
        PrepareComboBoxNewStatus();
        PrepareComboBoxUpdate();
        PrepareTableColumn();
        FillTableView();
    }

    private void initialDesign(Stage primaryStage) throws IOException {
        tabPane = new TabPane();
        comboBoxStatus = new ComboBox();
        lblFilter = new Label();
        lblStatus = new Label();
        lblName = new Label();
        lblId = new Label();
        lblUpdateStatus = new Label();
        lblUpdateName = new Label();
        lblUpdateId = new Label();
        lblSearchId = new Label();
        txtSearchId = new TextField();
        txtPetId = new TextField();
        txtPetName = new TextField();
        txtUpdatePetId = new TextField();
        txtUpdatePetName = new TextField();
        comboBoxNewStatus = new ComboBox();
        comboBoxUpdate = new ComboBox();
        tablePetsData = new TableView();
        btnSearch = new Button();
        btnAddNew = new Button();
        btnUpdate = new Button();
        btnDelete = new Button();
        hBox = new HBox();
        hBoxbtnNew = new HBox();
        hBoxName = new HBox();
        hBoxStatus = new HBox();
        hBoxId = new HBox();
        hBoxUpdateDeleteBtn = new HBox();
        hBoxUpdateName = new HBox();
        hBoxUpdateStatus = new HBox();
        hBoxUpdateId = new HBox();
        vBox = new VBox();
        gridPane = new GridPane();
        gridPaneAddNew = new GridPane();
        gridPaneUpdate = new GridPane();
        pieChart = new PieChart();

        btnAddNew.setMaxWidth(100);
        btnAddNew.setMinWidth(100);


        txtPetId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPetId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        txtUpdatePetId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtUpdatePetId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        txtSearchId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtUpdatePetId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int statusCode = 0;

                try {

                    if (!txtUpdatePetId.getText().isEmpty() && txtUpdatePetId.getText() != null && txtUpdatePetId.getText() != "" && !txtUpdatePetName.getText().isEmpty() && txtUpdatePetName.getText() != null && txtUpdatePetName.getText() != "") {
                        BigInteger id = new BigInteger(txtUpdatePetId.getText());
                        String name = txtUpdatePetName.getText();
                        String status = comboBoxUpdate.getValue().toString();

                        Pet pet1 = new Pet(id, name, status);
                        statusCode = PetController.DeletePet(pet1.getId());

                        if (statusCode == 200) {
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Pet Deleted successfully :) ", ButtonType.OK);
                            a.show();
                            txtUpdatePetId.setText("");
                            txtUpdatePetName.setText("");
                            comboBoxUpdate.setValue("available");
                            GetAllData("GET");
                            FillTableView();
                        } else {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Failed to Delete try again  ! ", ButtonType.OK);
                            a.show();
                        }
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR, " all the fields are Required ", ButtonType.OK);
                        a.show();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int statusCode = 0;

                try {

                    if (!txtUpdatePetId.getText().isEmpty() && txtUpdatePetId.getText() != null && txtUpdatePetId.getText() != "" && !txtUpdatePetName.getText().isEmpty() && txtUpdatePetName.getText() != null && txtUpdatePetName.getText() != "") {
                        BigInteger id = new BigInteger(txtUpdatePetId.getText());
                        String name = txtUpdatePetName.getText();
                        String status = comboBoxUpdate.getValue().toString();

                        Pet pet1 = new Pet(id, name, status);
                        statusCode = PetController.AddOrUpdatePet(pet1, "PUT");

                        if (statusCode == 200) {
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Pet Updated successfully :) ", ButtonType.OK);
                            a.show();
                            GetAllData("GET");
                            FillTableView();
                        }
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR, " all the fields are Required ", ButtonType.OK);
                        a.show();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int statusCode = 0;
                try {
                    apiUrl = new URL("https://petstore.swagger.io/v2/pet/" + Integer.parseInt(txtSearchId.getText()));
                    System.out.println(apiUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                statusCode = GetOnePet("GET");
                if (statusCode == 200) {
                    try {
                        FillTableView();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR, " the Pet doesn't exist ", ButtonType.OK);
                    a.show();
                }

            }
        });


        btnAddNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int statusCode = 0;

                try {

                    if (!txtPetId.getText().isEmpty() && txtPetId.getText() != null && txtPetId.getText() != "" && !txtPetName.getText().isEmpty() && txtPetName.getText() != null && txtPetName.getText() != "") {
                        BigInteger id = new BigInteger(txtPetId.getText());
                        String name = txtPetName.getText();
                        String status = comboBoxNewStatus.getValue().toString();

                        Pet pet1 = new Pet(id, name, status);
                        statusCode = PetController.AddOrUpdatePet(pet1, "POST");

                        if (statusCode == 200) {
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Pet added successfully :) ", ButtonType.OK);
                            a.show();
                            txtPetId.setText("");
                            txtPetName.setText("");
                            comboBoxNewStatus.setValue("available");
                            comboBoxStatus.setValue("All");
                            GetAllData("GET");
                            FillTableView();
                        }
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR, " all the fields are Required ", ButtonType.OK);
                        a.show();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });


        comboBoxStatus.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                try {
                    System.out.println("in box");
                    switch (newValue) {
                        case "All":
                            apiUrl = new URL("https://petstore.swagger.io/v2/pet/findByStatus?status=pending,sold,available");
                            break;
                        default:
                            apiUrl = new URL("https://petstore.swagger.io/v2/pet/findByStatus?status=" + newValue);

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                txtSearchId.setText("");
                GetAllData("GET");
                try {
                    FillTableView();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (newValue.equals(0)) {
                    try {
                        apiUrl = new URL("https://petstore.swagger.io/v2/pet/findByStatus?status=available,sold,pending");
                        GetAllData("GET");
                        comboBoxStatus.setValue("All");
                        FillTableView();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else if (newValue.equals(2)) {
                    if (tablePetsData.getSelectionModel().getSelectedItem() != null) {
                        Object selectedItem = tablePetsData.getSelectionModel().getSelectedItem();
                        selectedPet = (Pet) selectedItem;
                        txtUpdatePetId.setText(selectedPet.getId().toString());
                        txtUpdatePetName.setText(selectedPet.getName());
                        comboBoxUpdate.setValue(selectedPet.getStatus());
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Please Select the Pet you want from Pets Tab ", ButtonType.OK);
                        a.show();
                    }
                } else if (newValue.equals(3)) {
                    try {
                        PetController.getDashboardData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        primaryStage.setTitle("Pets");
        Scene scene = new Scene(PrepareTabs(), 550, 300, Color.GREY);

        primaryStage.setScene(scene);
        primaryStage.setX(50);
        primaryStage.setY(50);
        primaryStage.show();

    }

    private void SetText() {
        lblFilter.setText("Select Status : ");
        lblSearchId.setText("Enter Pet's ID : ");
        btnSearch.setText(" Search ");
        txtPetId.setPromptText("Enter Number : ");
        txtSearchId.setPromptText("Enter Number : ");
        lblId.setText("ID  : ");
        lblName.setText("Name  : ");
        lblStatus.setText("Status  : ");
        lblUpdateId.setText("ID  : ");
        lblUpdateName.setText("Name  : ");
        lblUpdateStatus.setText("Status  : ");
        btnAddNew.setText("Add");
        btnUpdate.setText("Update");
        btnDelete.setText("Delete");
    }

    private void PrepareComboBoxStatus() {

        comboBoxStatus.getItems().add("All");
        comboBoxStatus.getItems().add("available");
        comboBoxStatus.getItems().add("pending");
        comboBoxStatus.getItems().add("sold");
        comboBoxStatus.setValue("All");
        comboBoxStatus.setEditable(false);

    }

    private void PrepareComboBoxNewStatus() {

        comboBoxNewStatus.getItems().add("available");
        comboBoxNewStatus.getItems().add("pending");
        comboBoxNewStatus.getItems().add("sold");
        comboBoxNewStatus.setValue("available");
        comboBoxNewStatus.setEditable(false);

    }

    private void PrepareComboBoxUpdate() {

        comboBoxUpdate.getItems().add("available");
        comboBoxUpdate.getItems().add("pending");
        comboBoxUpdate.getItems().add("sold");
        comboBoxUpdate.setValue("available");
        comboBoxUpdate.setEditable(false);

    }

    private TabPane PrepareTabs() throws IOException {

        Tab petsTab = new Tab("Pets");
        Tab addNewTab = new Tab("Add New");
        Tab updateTab = new Tab("Update/Delete");
        Tab dashboardTab = new Tab("Dashboard");

        petsTab.setContent(PreparePetsTab());
        addNewTab.setContent(PrepareAddNewTab());
        updateTab.setContent(PrepareUpdateDeleteTab());
        dashboardTab.setContent(PrepareDashboardTab());
        tabPane.getTabs().addAll(petsTab, addNewTab, updateTab, dashboardTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        return tabPane;

    }

    private void PrepareTableColumn() {

        TableColumn idCol = new TableColumn("ID");
        TableColumn nameCol = new TableColumn("Name");
        TableColumn statusCol = new TableColumn("Status");

        idCol.setCellValueFactory(new PropertyValueFactory<Pet, BigInteger>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Pet, String>("name"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Pet, String>("status"));

        tablePetsData.setEditable(false);
        tablePetsData.getColumns().addAll(idCol, nameCol, statusCol);
        tablePetsData.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


    }

    private void FillTableView() throws MalformedURLException {

        tablePetsData.getItems().clear();
        if (allPetData == null) {
            tablePetsData.getItems().add(pet);
        } else {
            for (int i = 0; i < allPetData.size(); i++) {
                tablePetsData.getItems().add(allPetData.get(i));
            }
        }


    }

    private GridPane PreparePetsTab() {
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(0, 0, 15, 0));
        hBox.getChildren().addAll(lblFilter, comboBoxStatus, lblSearchId, txtSearchId, btnSearch);
        gridPane.setPadding(new Insets(30, 15, 10, 15));
        gridPane.add(hBox, 0, 0);
        gridPane.add(tablePetsData, 0, 1);

        return gridPane;

    }

    private VBox PrepareDashboardTab() throws IOException {
        ArrayList<Status> statusList = PetController.getDashboardData();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < statusList.size(); i++) {
            pieChartData.add(new PieChart.Data(statusList.get(i).getStatus(), statusList.get(i).getCount()));
        }
        pieChart = new PieChart(pieChartData);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(false);

        vBox.getChildren().add(pieChart);
        return vBox;
    }

    private GridPane PrepareAddNewTab() {
        hBoxName.setSpacing(10);
        hBoxName.setPadding(new Insets(10, 0, 10, 0));
        hBoxId.setSpacing(30);
        hBoxId.setPadding(new Insets(10, 0, 10, 0));
        hBoxStatus.setSpacing(10);
        hBoxStatus.setPadding(new Insets(10, 0, 10, 0));
        hBoxbtnNew.setPadding(new Insets(10, 0, 0, 20));

        hBoxId.getChildren().addAll(lblId, txtPetId);
        hBoxName.getChildren().addAll(lblName, txtPetName);
        hBoxStatus.getChildren().addAll(lblStatus, comboBoxNewStatus);
        hBoxbtnNew.getChildren().addAll(btnAddNew);

        gridPaneAddNew.add(hBoxId, 2, 3);
        gridPaneAddNew.add(hBoxName, 2, 4);
        gridPaneAddNew.add(hBoxStatus, 2, 5);

        gridPaneAddNew.add(hBoxbtnNew, 2, 6);
        gridPaneAddNew.setPadding(new Insets(30, 15, 10, 15));

        return gridPaneAddNew;

    }

    private GridPane PrepareUpdateDeleteTab() {
        hBoxUpdateName.setSpacing(10);
        hBoxUpdateName.setPadding(new Insets(10, 0, 10, 0));

        hBoxUpdateId.setSpacing(30);
        hBoxUpdateId.setPadding(new Insets(10, 0, 10, 0));

        hBoxUpdateStatus.setSpacing(10);
        hBoxUpdateStatus.setPadding(new Insets(10, 0, 10, 0));

        hBoxUpdateDeleteBtn.setSpacing(20);
        hBoxUpdateDeleteBtn.setPadding(new Insets(10, 0, 10, 0));

        hBoxUpdateId.getChildren().addAll(lblUpdateId, txtUpdatePetId);
        hBoxUpdateName.getChildren().addAll(lblUpdateName, txtUpdatePetName);
        hBoxUpdateStatus.getChildren().addAll(lblUpdateStatus, comboBoxUpdate);
        hBoxUpdateDeleteBtn.getChildren().addAll(btnUpdate, btnDelete);

        gridPaneUpdate.add(hBoxUpdateId, 2, 3);
        gridPaneUpdate.add(hBoxUpdateName, 2, 4);
        gridPaneUpdate.add(hBoxUpdateStatus, 2, 5);
        gridPaneUpdate.add(hBoxUpdateDeleteBtn, 2, 6, 3, 1);
        gridPaneUpdate.setPadding(new Insets(30, 15, 10, 15));

        return gridPaneUpdate;

    }

    private static int GetOnePet(String method) {
        int status = 0;
        try {

            HttpURLConnection connection = PetController.getConnection(apiUrl, method);
            status = connection.getResponseCode();
            if (status == 200) {
                String data = PetController.ReadAllJsonData(apiUrl);
                pet = PetController.ConvertOneToPet(data);
                allPetData = null;

            } else {
                System.out.println("Something wrong...! ");
            }

        } catch (Exception e) {
            System.out.println("Error Get One : " + e);
        }
        return status;
    }

    private static void GetAllData(String method) {
        try {

            HttpURLConnection connection = PetController.getConnection(apiUrl, method);
            int status = connection.getResponseCode();
            if (status == 200) {
                String data = PetController.ReadAllJsonData(apiUrl);
                allPetData = PetController.ConvertAllToPets(data);

            } else {
                System.out.println("Something wrong...! ");
            }

        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }

    public static void main(String[] args) throws IOException {
        GetAllData("GET");
        PetController.getDashboardData();
        launch(args);
    }
}
