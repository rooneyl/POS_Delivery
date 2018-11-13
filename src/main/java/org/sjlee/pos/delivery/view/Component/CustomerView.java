package org.sjlee.pos.delivery.view.Component;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.sjlee.pos.delivery.model.Customer;
import org.sjlee.pos.delivery.view.Layout;

import java.util.List;

public class CustomerView extends VBox {
    private TableView<Customer> customerView;

    public CustomerView(double width, double height) {
        double columnSize = Math.floor(Layout.ONE_THIRD_WIDTH / 4);

        TableColumn<Customer, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setPrefWidth(columnSize);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Customer, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setPrefWidth(columnSize);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Customer, String> sourceColumn = new TableColumn<>("Source");
        sourceColumn.setPrefWidth(columnSize);
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));

        TableColumn<Customer, String> nameColumn = new TableColumn<>("C.Name");
        nameColumn.setPrefWidth(columnSize - 1);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        customerView = new TableView<>();
        customerView.setId("customer-view");
        customerView.setPrefWidth(width);
        customerView.setPrefHeight(height);
        customerView.getColumns().addAll(dateColumn, timeColumn, sourceColumn, nameColumn);
        this.getChildren().add(customerView);
    }

    public void setCustomers(List<Customer> customers) {
        customerView.getItems().clear();
        customerView.setItems(FXCollections.observableArrayList(customers));
    }

    public TableView<Customer> getView() {
        return customerView;
    }
}

