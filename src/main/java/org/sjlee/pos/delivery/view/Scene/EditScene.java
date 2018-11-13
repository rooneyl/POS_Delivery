package org.sjlee.pos.delivery.view.Scene;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sjlee.pos.delivery.model.Category;
import org.sjlee.pos.delivery.model.Menu;
import org.sjlee.pos.delivery.module.MenuController;
import org.sjlee.pos.delivery.module.PrinterService;
import org.sjlee.pos.delivery.view.Layout;
import org.sjlee.pos.delivery.view.Util;

import java.util.List;
import java.util.stream.Collectors;

public class EditScene extends SceneBase {

    private TextField nameField;
    private TextField descriptionField;
    private ComboBox<Category> categoryComboBox;
    private ComboBox<PrinterService.Printer> printerComboBox;
    private ColorPicker colorPicker;

    private Menu selectedMenu;

    public EditScene(Stage primaryStage, Scene scene) {
        super(primaryStage, scene, false);
        createMenuSelector();
        menuDetail();

        setReturnButtonSize(Layout.SETTING_BUTTON_SIZE);
        AnchorPane.setBottomAnchor(returnButton, Layout.SPACING);
        AnchorPane.setRightAnchor(returnButton, Layout.SPACING);
        this.root.getChildren().addAll(returnButton);
    }

    private void createMenuSelector() {


        TreeTableColumn<Menu, String> column = new TreeTableColumn<>("Name");
        column.setPrefWidth(Math.floor(Layout.LISTVIEW_WIDTH));
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Menu, String> p) -> {
            Menu Menu = p.getValue().getValue();
            return new ReadOnlyStringWrapper(Menu.getName());
        });

        TreeTableView<Menu> treeTableView = new TreeTableView<>();
        treeTableView.setPrefSize(Layout.LISTVIEW_WIDTH, Layout.LISTVIEW_HEIGHT);
        treeTableView.getColumns().add(column);
        treeTableView.setRoot(new TreeItem<>());
        treeTableView.setShowRoot(false);


        for (Category category : Category.values()) {
            List<TreeItem<Menu>> menus = MenuController.getInstance().getMenus(category).stream()
                    .map(TreeItem::new)
                    .collect(Collectors.toList());

            Menu rootMenu = new Menu();
            rootMenu.setName(category.name());
            rootMenu.setValid(false);

            TreeItem<Menu> rootTree = new TreeItem<>(rootMenu);
            rootTree.getChildren().addAll(menus);

            treeTableView.getRoot().getChildren().add(rootTree);
        }

        treeTableView.setOnMouseClicked(event -> {
            if (treeTableView.getSelectionModel().getSelectedItem() == null) return;

            selectedMenu = treeTableView.getSelectionModel().getSelectedItem().getValue();
            setMenuDetail();

        });

        Button addNewMenu = Util.createButton("xx", Layout.SETTING_BUTTON_SIZE, true);
        addNewMenu.setOnAction(event -> {
            createNewMenu();
        });
        Button deleteMenu = Util.createButton("yy", Layout.SETTING_BUTTON_SIZE, true);
        Button saveMenu = Util.createButton("zz", Layout.SETTING_BUTTON_SIZE, true);
        saveMenu.setOnAction(event -> {
            if (selectedMenu == null) {
                Menu menu = new Menu();
                menu.setValid(true);
                menu.setName(nameField.getText());
                menu.setPosition(-1);
                menu.setCategory(categoryComboBox.getValue());
                menu.setDescription(descriptionField.getText());
                menu.setPrinter(printerComboBox.getValue());

                String c = colorPicker.getValue().toString();
                String color = "#" + c.substring(2, 8);
                System.out.println(c);
                System.out.println(color);
                menu.setColor(color);
                MenuController.getInstance().createMenu(menu);
            } else {

                selectedMenu.setCategory(categoryComboBox.getValue());
                selectedMenu.setDescription(descriptionField.getText());
                selectedMenu.setPrinter(printerComboBox.getValue());

                String c = colorPicker.getValue().toString();
                String color = "#" + c.substring(2, 8);
                System.out.println(c);
                System.out.println(color);
                selectedMenu.setColor(color);
                MenuController.getInstance().updateMenu(selectedMenu);
            }
        });

        HBox hBox = Util.getHBox(Layout.LISTVIEW_WIDTH, Layout.SETTING_BUTTON_SIZE);
        hBox.getChildren().addAll(addNewMenu, deleteMenu, saveMenu);

        VBox vBox = Util.getVBox(Layout.ONE_THIRD_WIDTH, Layout.SCREEN_HEIGHT);
        vBox.getChildren().addAll(treeTableView, hBox);

        AnchorPane.setRightAnchor(vBox, Layout.SCREEN_WIDTH / 2 + Layout.SPACING);
        AnchorPane.setTopAnchor(vBox, 0.0);

        this.root.getChildren().add(vBox);
    }

    private void menuDetail() {


        double width = Math.floor(Layout.LISTVIEW_WIDTH / 2);


        HBox nameBox = Util.getHBox(Layout.LISTVIEW_WIDTH, 0);
        Label nameLabel = createLabel("Name", width);
        nameField = new TextField();
        nameField.editableProperty().set(false);
        nameField.setPrefWidth(width);
        nameBox.getChildren().addAll(nameLabel, nameField);

        HBox descriptionBox = Util.getHBox(Layout.LISTVIEW_WIDTH, 0);
        Label descriptionLabel = createLabel("DESCRIPTION", width);
        descriptionField = new TextField();
        descriptionField.setPrefWidth(width);
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);

        HBox categoryBox = Util.getHBox(Layout.LISTVIEW_WIDTH, 0);
        Label categoryLabel = createLabel("CATEGORY", width);
        categoryComboBox = new ComboBox<>(FXCollections.observableArrayList(Category.values()));
        categoryComboBox.setPrefWidth(width);
        categoryBox.getChildren().addAll(categoryLabel, categoryComboBox);

        HBox printerBox = Util.getHBox(Layout.LISTVIEW_WIDTH, 0);
        Label printerLabel = createLabel("PRINTER", width);
        printerComboBox = new ComboBox<>(FXCollections.observableArrayList(PrinterService.Printer.values()));
        printerComboBox.setPrefWidth(width);
        printerBox.getChildren().addAll(printerLabel, printerComboBox);

        HBox colorBox = Util.getHBox(Layout.LISTVIEW_WIDTH, 0);
        Label colorLabel = createLabel("COLOR", width);
        colorPicker = new ColorPicker();
        colorPicker.setPrefWidth(width);
        colorBox.getChildren().addAll(colorLabel, colorPicker);

        VBox detailBox = Util.getVBox(Layout.LISTVIEW_WIDTH, Layout.LISTVIEW_HEIGHT);
        detailBox.setAlignment(Pos.CENTER);
        detailBox.getChildren().addAll(nameBox, descriptionBox, categoryBox, printerBox, colorBox);

        AnchorPane.setLeftAnchor(detailBox, Layout.SCREEN_WIDTH / 2 + Layout.SPACING);
        AnchorPane.setTopAnchor(detailBox, 0.0);
        this.root.getChildren().add(detailBox);
    }

    private void setMenuDetail() {
        if (selectedMenu != null && selectedMenu.isValid()) {
            nameField.setText(selectedMenu.getName());
            descriptionField.setText(selectedMenu.getDescription());
            categoryComboBox.getSelectionModel().select(selectedMenu.getCategory());
            printerComboBox.getSelectionModel().select(selectedMenu.getPrinter());
            colorPicker.setValue(Color.valueOf(selectedMenu.getColor()));
        } else {
            nameField.setText(null);
            descriptionField.setText(null);
            categoryComboBox.getSelectionModel().clearSelection();
            printerComboBox.getSelectionModel().clearSelection();
            colorPicker.setValue(Color.WHITE);
        }
    }

    private void createNewMenu() {
        setMenuDetail();
        nameField.editableProperty().set(true);
        selectedMenu = null;

        nameField.setText("New Menu");
        descriptionField.setText("New Menu");
        categoryComboBox.getSelectionModel().select(Category.APPETIZER);
        printerComboBox.getSelectionModel().select(PrinterService.Printer.NO_PRINTER);
    }

    private Label createLabel(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setAlignment(Pos.CENTER);

        return label;
    }
}
