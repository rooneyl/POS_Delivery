package org.sjlee.pos.delivery.view.Scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.sjlee.pos.delivery.model.Category;
import org.sjlee.pos.delivery.model.Menu;
import org.sjlee.pos.delivery.module.OrderPlacer;
import org.sjlee.pos.delivery.view.Component.MenuBoard;
import org.sjlee.pos.delivery.view.Component.OrderView;
import org.sjlee.pos.delivery.view.Layout;
import org.sjlee.pos.delivery.view.Util;

public class OrderScene extends SceneBase {

    private OrderPlacer orderPlacer;
    private TextFlow orderInfo;

    private MenuBoard menuBoard;
    private OrderView orderView;

    public OrderScene(Stage primaryStage, Scene scene) {
        super(primaryStage, scene, true);
        /* Initialize MenuBoard */
        initializeMenuBoard();

        /* Create ControlBox Consisting : OrderView and Control Buttons */
        VBox controlBox = createControlBox();

        /* Coordinate Each Node and Add to Root */
        AnchorPane.setLeftAnchor(controlBox, 0.0);
        AnchorPane.setTopAnchor(controlBox, 0.0);

        root.getChildren().add(controlBox);

        /* Draw Divider */
        Line hLine = new Line(Layout.ONE_THIRD_WIDTH, 0, Layout.ONE_THIRD_WIDTH, Layout.SCREEN_HEIGHT);
        this.root.getChildren().addAll(hLine);
    }

    public void setOrderPlacer(OrderPlacer orderPlacer) {
        this.orderPlacer = orderPlacer;
        menuBoard.changeCategory(Category.APPETIZER);

        // TODO
        Text customerName = new Text(orderPlacer.getCustomer().getName() + "\n");
        Text source = new Text(orderPlacer.getCustomer().getSource() + "\n");

        orderInfo.getChildren().addAll(customerName, source);

        orderView.clear();
    }

    public void reload() {
        initializeMenuBoard();
    }


    private VBox createControlBox() {
        orderView = new OrderView(true, Layout.LISTVIEW_WIDTH, Layout.LISTVIEW_HEIGHT);

        VBox controlBox = new VBox(Layout.SPACING);
        controlBox.getChildren().addAll(orderView, createControlPanel());
        controlBox.setPrefSize(Layout.ONE_THIRD_WIDTH, Layout.SCREEN_HEIGHT);
        controlBox.setPadding(new Insets(Layout.SPACING));
        controlBox.setAlignment(Pos.CENTER);

        return controlBox;
    }

    private HBox createControlPanel() {
        double controlButtonSize = Math.floor(Layout.ONE_THIRD_WIDTH / 8);

        setReturnButtonSize(controlButtonSize);

        Button memoButton = Util.createButton(Layout.MEMO_ICON, controlButtonSize, true);
        memoButton.setOnAction(event -> Util.getInputText("").ifPresent(detail -> {
            if (detail.equals("") || orderView.getSelectedOrder() == null) return;
            orderPlacer.addDetail(orderView.getSelectedOrder(), detail, orderView.getSection());
            orderView.updateTable(orderPlacer.getOrderList());
        }));

        Button deleteButton = Util.createButton(Layout.DELETE_ICON, controlButtonSize, true);
        deleteButton.setOnAction(event -> {
            if (orderView.getSelectedOrder() == null) return;

            if (orderView.getSelectedOrder().getQuantity() == 0) {
                // case where user selected section(i.e. separator)
                orderPlacer.getOrderList().remove(orderView.getSelectedOrder().getMenu().getPosition());
            } else {
                // case where user selected menu
                orderPlacer.removeOrder(orderView.getSelectedOrder(), orderView.getSection());
                if (orderPlacer.getOrderList().size() > 1 && orderPlacer.getOrderList().get(orderView.getSection()).isEmpty()) {
                    // remove section if empty
                    orderPlacer.getOrderList().remove(orderView.getSection());
                }
            }

            // update table
            orderView.updateTable(orderPlacer.getOrderList());
        });

        Button separatorButton = Util.createButton(Layout.SEPARATE_ICON, controlButtonSize, true);
        separatorButton.setOnAction(event -> {
            if (!orderPlacer.getOrderList().get(orderPlacer.getOrderList().size() - 1).isEmpty()) {
                orderPlacer.addSeparator();
                orderView.updateTable(orderPlacer.getOrderList());
            }
        });

        Button printButton = Util.createButton(Layout.PRINT_ICON, controlButtonSize, true);
        printButton.setPrefWidth(controlButtonSize * 3 + Layout.SPACING);
        printButton.setOnAction(event -> {
            orderPlacer.placeOrder();
            returnButton.fire();
        });

        HBox controlButtons1 = new HBox(Layout.SPACING);
        controlButtons1.setAlignment(Pos.CENTER);
        controlButtons1.getChildren().addAll(memoButton, deleteButton, separatorButton);

        HBox controlButtons2 = new HBox(Layout.SPACING);
        controlButtons2.setAlignment(Pos.CENTER);
        controlButtons2.getChildren().addAll(returnButton, printButton);

        VBox controlButtonBox = new VBox(Layout.SPACING);
        controlButtonBox.setAlignment(Pos.CENTER);
        controlButtonBox.getChildren().addAll(controlButtons1, controlButtons2);


        orderInfo = new TextFlow();
        orderInfo.setStyle("-fx-border:black;");
        orderInfo.setPrefWidth(controlButtonSize * 3);
        orderInfo.setPrefHeight(controlButtonSize * 2 + Layout.SPACING);

        HBox controlBox = new HBox(Layout.SPACING);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.getChildren().addAll(orderInfo, controlButtonBox);


        return controlBox;
    }

    private void menuSelectAction(Menu menu) {
        orderPlacer.addOrder(menu, orderView.getSection());
        orderView.updateTable(orderPlacer.getOrderList());
    }

    public void initializeMenuBoard() {
        if (menuBoard != null)
            this.root.getChildren().remove(menuBoard);

        menuBoard = new MenuBoard(false, Layout.TWO_THIRD_WIDTH, Layout.SCREEN_HEIGHT);
        menuBoard.getMenuBoard().values().forEach(flowPane -> // let clicking a button to place an order
                flowPane.getChildren().forEach(node -> {
                    Button button = (Button) node;
                    button.setOnAction(event -> menuSelectAction((Menu) button.getUserData()));
                }));

        AnchorPane.setLeftAnchor(menuBoard, Layout.ONE_THIRD_WIDTH);
        AnchorPane.setTopAnchor(menuBoard, 0.0);
        this.root.getChildren().add(menuBoard);
    }
}