package org.sjlee.pos.delivery.view.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.sjlee.pos.delivery.model.Category;
import org.sjlee.pos.delivery.model.Menu;
import org.sjlee.pos.delivery.module.MenuController;
import org.sjlee.pos.delivery.view.Component.CategoryListener;
import org.sjlee.pos.delivery.view.Component.MenuBoard;
import org.sjlee.pos.delivery.view.Util;
import org.sjlee.pos.delivery.view.Layout;

import java.util.List;

public class MenuLayoutScene extends SceneBase implements CategoryListener {


    private MenuBoard menuBoard;
    private Category currentCategory;
    private TableView<Menu> menuView;
    private Button selectedButton;

    public MenuLayoutScene(Stage primaryStage, Scene scene) {
        super(primaryStage, scene, true);
        /* Create TreeTableView of Menu */
        VBox controlBox = createTreeView();

        /* Initialize MenuBoard */
        menuBoard = new MenuBoard(true, Layout.TWO_THIRD_WIDTH, Layout.SCREEN_HEIGHT);
        menuBoard.setListener(this);
        menuBoard.getMenuBoard().values().forEach(flowPane -> // let clicking a button to focus menu on the list
                flowPane.getChildren().forEach(node -> {
                    Button button = (Button) node;
                    button.setStyle(Layout.MENU_STYLE + "#ffffff");
                    button.setOnAction(event -> clickButtonEvent(button));
                }));

        /* Coordinate Each Node and Add to Root */
        AnchorPane.setLeftAnchor(menuBoard, Layout.ONE_THIRD_WIDTH);
        AnchorPane.setTopAnchor(menuBoard, 0.0);
        this.root.getChildren().addAll(controlBox, menuBoard);

        /* Draw Divider */
        Line hLine = new Line(Layout.ONE_THIRD_WIDTH, 0, Layout.ONE_THIRD_WIDTH, Layout.SCREEN_HEIGHT);
        this.root.getChildren().addAll(hLine);
    }

    private VBox createTreeView() {

        TableColumn<Menu, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setPrefWidth(Layout.LISTVIEW_WIDTH);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        menuView = new TableView<>();
        menuView.setPrefSize(Layout.LISTVIEW_WIDTH, Layout.LISTVIEW_HEIGHT);
        menuView.getColumns().add(nameColumn);

        VBox controlBox = new VBox(Layout.SPACING);
        controlBox.getChildren().addAll(menuView, createControlPanel());
        controlBox.setPrefSize(Layout.ONE_THIRD_WIDTH, Layout.SCREEN_HEIGHT);
        controlBox.setPadding(new Insets(Layout.SPACING));
        controlBox.setAlignment(Pos.CENTER);

        listen(Category.APPETIZER);
        return controlBox;
    }

    private HBox createControlPanel() {
        setReturnButtonSize(Layout.SETTING_BUTTON_SIZE);
        Button unlink = Util.createButton("Unlink", Layout.SETTING_BUTTON_SIZE, true);
        unlink.setOnAction(event -> unlink(selectedButton));

        Button link = Util.createButton("Link", Layout.SETTING_BUTTON_SIZE, true);
        link.setOnAction(event -> {
            Menu menu = menuView.getSelectionModel().getSelectedItem();
            if (menu == null || selectedButton == null) return;
            unlink(selectedButton);

            ObservableList<Node> menus = menuBoard.getMenuBoard().get(currentCategory).getChildren();
            menus.stream()
                    .filter(node1 -> node1.getUserData() != null)
                    .filter(node1 -> node1.getUserData() == menu)
                    .findFirst()
                    .ifPresent(node -> unlink((Button) node));


            Integer index = menus.indexOf(selectedButton);
            menu.setPosition(index);
            MenuController.getInstance().updateMenu(menu);
            selectedButton.setUserData(menu);
            selectedButton.setText(menu.getDescription());
        });

        HBox controlBox = new HBox(Layout.SPACING);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.getChildren().addAll(returnButton, unlink, link);
        return controlBox;
    }

    private void unlink(Button button) {
        if (button != null && button.getUserData() != null) {
            Menu menu = (Menu) button.getUserData();
            menu.setPosition(-1);
            MenuController.getInstance().updateMenu(menu);
            button.setUserData(null);
            button.setText("");
        }
    }

    private void clickButtonEvent(Button button) {
        if (selectedButton != null)
            selectedButton.setStyle(Layout.MENU_STYLE + "#ffffff");

        selectedButton = button;
        button.setStyle("-fx-border-color: #00bbff;");
    }

    public void listen(Category category) {
        List<Menu> menus = MenuController.getInstance().getMenus(category);
        currentCategory = category;
        menuView.setItems(FXCollections.observableArrayList(menus));
    }
}
