package org.sjlee.pos.delivery.view.Component;


import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.sjlee.pos.delivery.model.Category;
import org.sjlee.pos.delivery.model.Menu;
import org.sjlee.pos.delivery.module.MenuController;
import org.sjlee.pos.delivery.view.Layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuBoard extends VBox {

    private FlowPane currentTab;
    private Map<Category, FlowPane> menuBoard;
    private List<CategoryListener> listeners;

    /**
     * Creates MenuBoard with specified width and height
     *
     * @param width  width of MenuBoard
     * @param height height of Menu Board
     */
    public MenuBoard(boolean showAll, double width, double height) {
        super();
        this.menuBoard = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.setPrefSize(width, height);

        double tabHeight = Math.floor(height * 0.1);
        double tabSpacing = Math.floor(tabHeight * 0.03);
        double tabPadding = Math.floor(tabSpacing * 8);
        double tabButtonHeight = Math.floor((tabHeight - (3 * tabSpacing)) / 2);

        double tabDetailHeight = Math.floor(height * 0.88);
        double tabDetailSpacing = Math.floor(tabSpacing * 2);
        // Tab Detail has 6 * 8 menu Buttons
        double menuButtonSize_W = Math.floor((width - (tabDetailSpacing * 7)) / 6);
        double menuButtonSize_H = Math.floor((tabDetailHeight - (tabDetailSpacing * 9)) / 8);

        /* Tab */
        FlowPane tab = new FlowPane(tabSpacing, tabSpacing);
        tab.setPrefSize(width, tabHeight);
        tab.setPadding(new Insets(tabSpacing));

        for (Category category : Category.values()) {

            /* Creating Tab Button */
            Button categoryButton = new Button(category.getDescription());
            categoryButton.setPrefHeight(tabButtonHeight);
            categoryButton.setPadding(new Insets(0, tabPadding, 0, tabPadding));
            categoryButton.setStyle(Layout.TAB_STYLE);
            categoryButton.setOnAction(event -> changeCategory(category));
            tab.getChildren().add(categoryButton);

            /* Filling Tab Detail and Populating Menu Buttons */
            FlowPane menuPane = new FlowPane(tabDetailSpacing, tabDetailSpacing);
            menuPane.setPadding(new Insets(tabDetailSpacing));
            menuPane.setPrefSize(width, tabDetailHeight);
            menuPane.setUserData(category);

            List<Button> menuButtons = new ArrayList<>();
            for (int i = 0; i < 48; i++) { // create Buttons
                Button menuButton = new Button();
                menuButton.setPrefSize(menuButtonSize_W, menuButtonSize_H);
                menuButton.setVisible(showAll);
                menuButton.setTextAlignment(TextAlignment.CENTER);
                menuButtons.add(menuButton);
                menuButton.setStyle(Layout.MENU_STYLE + "#ffffff;");
            }

            List<Menu> menuList = MenuController.getInstance().getMenus(category);
            for (Menu menu : menuList) { // make active valid buttons
                if (menu.getPosition() < 0 || !menu.isValid()) continue;
                Button button = menuButtons.get(menu.getPosition());
                button.setStyle(Layout.MENU_STYLE + menu.getColor());
                button.setText(menu.getDescription());
                button.setUserData(menu);
                button.setVisible(true);
            }

            menuPane.getChildren().addAll(menuButtons);
            menuBoard.put(category, menuPane);
        }

        /* Add Nodes into VBox */
        currentTab = menuBoard.get(Category.APPETIZER);
        this.getChildren().addAll(tab, new Separator(), currentTab);
    }

    /**
     * Return map of flowPane for configuring setOnAction of each menu buttons
     *
     * @return Map of flowPane consists of each menu buttons
     */
    public Map<Category, FlowPane> getMenuBoard() {
        return menuBoard;
    }

    /**
     * Set Listener
     *
     * @param categoryListener
     */
    public void setListener(CategoryListener categoryListener) {
        listeners.add(categoryListener);
    }

    /**
     * Change Tab Detail to Corresponding Category
     *
     * @param category Category that is going to be active tab
     */
    public void changeCategory(Category category) {
        this.getChildren().remove(currentTab);
        currentTab = menuBoard.get(category);
        this.getChildren().add(currentTab);

        /* Notify Listener */
        if (!listeners.isEmpty())
            listeners.forEach(listener -> listener.listen(category));
    }
}