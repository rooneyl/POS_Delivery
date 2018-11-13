package org.sjlee.pos.delivery.view.Component;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.sjlee.pos.delivery.model.Menu;
import org.sjlee.pos.delivery.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderView extends VBox {

    private TableView<Order> tableView;
    private TreeTableView<Order> treeView;

    private int section = 0;
    private Order selectedOrder;

    private boolean enableSelection;

    public OrderView(boolean enableSelection, double width, double height) {
        this.enableSelection = enableSelection;

        /* Table View */
        tableView = new TableView<>();
        tableView.setPrefHeight(height);
        tableView.setPrefWidth(width);

        TableColumn<Order, String> qtyTable = new TableColumn<>("qty");
        qtyTable.setPrefWidth(Math.floor(width * 0.1));
        qtyTable.getStyleClass().add("order-view-table");
        qtyTable.setCellValueFactory((TableColumn.CellDataFeatures<Order, String> param) -> {
            String qtyStr = "";
            if (param.getValue().getQuantity() != 0)
                qtyStr = Integer.toString(param.getValue().getQuantity());
            return new ReadOnlyStringWrapper(qtyStr);
        });

        TableColumn<Order, String> itemTable = new TableColumn<>("Item");
        itemTable.setPrefWidth(Math.floor(width * 0.9 - 1));
        itemTable.setCellValueFactory((TableColumn.CellDataFeatures<Order, String> p) -> {
            Order order = p.getValue();
            return new ReadOnlyStringWrapper(order.getMenu().getName());
        });

        tableView.getColumns().add(qtyTable);
        tableView.getColumns().add(itemTable);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            section = 0;
            selectedOrder = newValue;
        });

        /* Tree Table View */
        treeView = new TreeTableView<>();
        treeView.setPrefHeight(height);
        treeView.setPrefWidth(width);

        TreeTableColumn<Order, String> qtyTree = new TreeTableColumn<>("qty");
        qtyTree.setPrefWidth(Math.floor(width * 0.1));
        qtyTree.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            String qtyStr = "";
            if (param.getValue().getValue().getQuantity() != 0)
                qtyStr = Integer.toString(param.getValue().getValue().getQuantity());
            return new ReadOnlyStringWrapper(qtyStr);
        });

        TreeTableColumn<Order, String> itemTree = new TreeTableColumn<>("Item");
        itemTree.setPrefWidth(Math.floor(width * 0.9) - 1);
        itemTree.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> p) -> {
            Order order = p.getValue().getValue();
            return new ReadOnlyStringWrapper(order.getMenu().getName());
        });

        treeView.getColumns().add(qtyTree);
        treeView.getColumns().add(itemTree);
        treeView.setRoot(new TreeItem<>());
        treeView.setShowRoot(false);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedOrder = null;
                return;
            }

            selectedOrder = newValue.getValue();
            Order order = selectedOrder.getQuantity() == 0 ? selectedOrder : newValue.getParent().getValue();
            section = order.getMenu().getPosition();
        });

        if (!enableSelection) {
            tableView.setSelectionModel(null);
            treeView.setSelectionModel(null);
        }
        this.getChildren().add(tableView);
    }

    public int getSection() {
        return section;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void clear() {
        tableView.getItems().clear();
        treeView.getRoot().getChildren().clear();
    }

    public void updateTable(List<List<Order>> orderList) {
        this.getChildren().clear();

        if (orderList.size() == 1)
            updateTableView(orderList.get(0));
        else
            updateTreeView(orderList);

    }

    private void updateTableView(List<Order> orderList) {
        this.getChildren().add(tableView);

        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(orderList));

        if (enableSelection) {
            tableView.getSelectionModel().selectLast();
        }
    }

    private void updateTreeView(List<List<Order>> orderList) {
        this.getChildren().add(treeView);
        treeView.getRoot().getChildren().clear();

        for (int i = 0; i < orderList.size(); i++) {
            List<Order> orders = orderList.get(i);
            TreeItem<Order> root = new TreeItem<>(getSeparator(i));
            root.setExpanded(true);
            orders.forEach(order -> root.getChildren().add(new TreeItem<>(order)));
            treeView.getRoot().getChildren().add(root);
        }

        if (enableSelection) {
            TreeTableView.TreeTableViewSelectionModel<Order> selectionModel = treeView.getSelectionModel();
            selectionModel.clearSelection();
            selectionModel.selectLast();
        }
    }

    private Order getSeparator(int i) {
        Menu menu = new Menu();
        menu.setPosition(i);
        menu.setName("<< " + Integer.toString(i + 1) + " >>");

        Order separator = new Order();
        separator.setDetails(new ArrayList<>());
        separator.setQuantity(0);
        separator.setMenu(menu);

        return separator;
    }
}