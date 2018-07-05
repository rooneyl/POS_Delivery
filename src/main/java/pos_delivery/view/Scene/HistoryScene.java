package pos_delivery.view.Scene;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pos_delivery.model.Customer;
import pos_delivery.model.Order;
import pos_delivery.model.Source;
import pos_delivery.module.DataBaseController;
import pos_delivery.view.Component.CustomerView;
import pos_delivery.view.Component.OrderView;
import pos_delivery.view.Layout;
import pos_delivery.view.Util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HistoryScene extends SceneBase {
    private ToggleGroup sourceSelector;
    private DatePicker fromDate;
    private DatePicker toDate;

    private CustomerView customerView;
    private OrderView orderView;

    public HistoryScene(Stage primaryStage, Scene scene) {
        super(primaryStage, scene, true);
        createOrderViewPanel();
        createCustomerViewPanel();

        setReturnButtonSize(Layout.SETTING_BUTTON_SIZE);
        AnchorPane.setBottomAnchor(returnButton, Layout.SPACING);
        AnchorPane.setRightAnchor(returnButton, Layout.SPACING);

        this.root.getChildren().addAll(returnButton);
    }

    private void createOrderViewPanel() {
        orderView = new OrderView(false, Layout.LISTVIEW_WIDTH, Layout.LISTVIEW_HEIGHT);

        Button deleteButton = Util.createButton(Layout.DELETE_ICON, Layout.LISTVIEW_WIDTH * 0.15, true);
        deleteButton.setOnAction(event -> {
            Customer customer = customerView.getView().getSelectionModel().getSelectedItem();
            if (customer != null && Util.getConfirmationDialog()) {
                DataBaseController.getInstance().deleteOrder(customer);
                updateCustomerList();
                orderView.clear();
            }
        });

        VBox orderViewPanel = new VBox();
        orderViewPanel.setAlignment(Pos.TOP_RIGHT);
        orderViewPanel.setSpacing(Layout.SPACING);
        orderViewPanel.getChildren().addAll(deleteButton, orderView);
        AnchorPane.setLeftAnchor(orderViewPanel, Layout.SCREEN_WIDTH / 2 + Layout.SPACING);
        AnchorPane.setBottomAnchor(orderViewPanel, Layout.SPACING);
        this.root.getChildren().addAll(orderViewPanel);
    }

    private void createCustomerViewPanel() {
        /* DatePicker */
        fromDate = new DatePicker(LocalDate.now());
        toDate = new DatePicker(LocalDate.now());

        fromDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isAfter(toDate.getValue()))
                toDate.setValue(fromDate.getValue());
            updateCustomerList();
        });

        toDate.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isBefore(fromDate.getValue()))
                fromDate.setValue(toDate.getValue());
            updateCustomerList();
        }));

        HBox datePickerBox = new HBox(Layout.SPACING);
        datePickerBox.setPrefHeight(Layout.SPACING * 2);
        datePickerBox.getChildren().addAll(fromDate, new Text("~"), toDate);
        datePickerBox.setAlignment(Pos.CENTER);

        /* Source Toggle */
        sourceSelector = new ToggleGroup();
        sourceSelector.selectedToggleProperty().addListener((p, ov, nv) -> updateCustomerList());

        ToggleButton all = new ToggleButton("ALL");
        all.setPrefWidth(((Layout.ONE_THIRD_WIDTH - (Layout.SPACING * 4)) / Source.values().length + 1) / 2);
        all.setToggleGroup(sourceSelector);

        Map<Source, ToggleButton> sourceToggles = Stream
                .of(Source.values())
                .collect(Collectors.toMap(source -> source, source -> new ToggleButton(source.toString())));

        sourceToggles.forEach((source, toggleButton) -> {
            toggleButton.setPrefWidth((Layout.ONE_THIRD_WIDTH - (Layout.SPACING * 4)) / Source.values().length + 1);
            toggleButton.setToggleGroup(sourceSelector);
        });

        HBox sourceSelectBox = new HBox(Layout.SPACING);
        sourceSelectBox.setPrefHeight(Layout.SPACING * 2);
        sourceSelectBox.getChildren().add(all);
        sourceSelectBox.getChildren().addAll(sourceToggles.values());
        sourceSelectBox.setAlignment(Pos.CENTER);

        /* Customer TableView */
        customerView = new CustomerView(Layout.LISTVIEW_WIDTH, Layout.LISTVIEW_HEIGHT);
        customerView.getView().setOnMouseClicked(event -> {
            Customer customer = customerView.getView().getSelectionModel().getSelectedItem();
            if (customer != null) {
                List<Order> orders = DataBaseController.getInstance().retrieveOrderDetail(customer);
                orderView.updateTable(new ArrayList<>(Collections.singletonList(orders)));
            }
        });

        VBox customerViewPanel = new VBox(Layout.SPACING);
        customerViewPanel.setPrefWidth(Layout.ONE_THIRD_WIDTH);
        customerViewPanel.getChildren().addAll(sourceSelectBox, datePickerBox, customerView);
        customerViewPanel.setAlignment(Pos.CENTER);

        AnchorPane.setRightAnchor(customerViewPanel, Layout.SCREEN_WIDTH / 2 + Layout.SPACING);
        AnchorPane.setBottomAnchor(customerViewPanel, Layout.SPACING);
        this.root.getChildren().addAll(customerViewPanel);
        all.setSelected(true);
    }

    public void updateCustomerList() {
        /* Retrieve Customers */
        Date from = Date.valueOf(fromDate.getValue());
        Date to = Date.valueOf(toDate.getValue());
        List<Customer> customers = DataBaseController.getInstance().retrieveOrder(from, to);

        /* Filter Customers According to Toggle Selection */
        String selection = ((ToggleButton) sourceSelector.getSelectedToggle()).getText();
        if (!selection.equals("ALL"))
            customers = customers.stream()
                    .filter(customer -> customer.getSource().equals(Source.valueOf(selection)))
                    .collect(Collectors.toList());

        /* Display Result */
        customerView.setCustomers(customers);
    }
}

