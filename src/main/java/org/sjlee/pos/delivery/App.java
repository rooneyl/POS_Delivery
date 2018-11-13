package org.sjlee.pos.delivery;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.sjlee.pos.delivery.model.Source;
import org.sjlee.pos.delivery.module.Configurator;
import org.sjlee.pos.delivery.module.DataBaseController;
import org.sjlee.pos.delivery.module.OrderPlacer;
import org.sjlee.pos.delivery.view.Scene.*;
import org.sjlee.pos.delivery.view.Layout;
import org.sjlee.pos.delivery.view.Scene.*;
import org.sjlee.pos.delivery.view.Util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App extends Application {
    private Scene mainScene;
    private Stage primaryStage;

    private OrderScene orderScene;
    private HistoryScene historyScene;
    private MenuLayoutScene menuLayoutScene;
    private EditScene editScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // MainScene, Entry Point of App
        FlowPane mainPane = new FlowPane();
        mainPane.setAlignment(Pos.CENTER);
        mainScene = new Scene(mainPane, Layout.SCREEN_WIDTH, Layout.SCREEN_HEIGHT);

        // Source Selection
        HBox upperBox = Util.getHBox(Layout.SCREEN_WIDTH, Math.floor(Layout.SCREEN_HEIGHT * 0.9));
        Map<Source, Button> companyButtons = new LinkedHashMap<>();
        companyButtons.put(Source.DOORDASH, Util.createButton(Layout.DOORDASH_ICON, Layout.COMPANY_BUTTON_SIZE, false));
        companyButtons.put(Source.FOODORA, Util.createButton(Layout.FOODORA_ICON, Layout.COMPANY_BUTTON_SIZE, false));
        companyButtons.forEach(((source, button) -> {
            button.setOnAction(event ->
                    Util.getInputText("Customer").ifPresent(customer -> {
                        orderScene = (OrderScene) createScene(orderScene, OrderScene.class);
                        orderScene.setOrderPlacer(new OrderPlacer(customer, source));
                        primaryStage.setScene(orderScene);
                    }));
            upperBox.getChildren().add(button);
        }));

        // HistoryScene
        Button historySceneButton = Util.createButton(Layout.HISTORY_ICON, Layout.SETTING_BUTTON_SIZE, false);
        historySceneButton.setOnAction(event -> historyScene = (HistoryScene) createScene(historyScene, HistoryScene.class));

        // Configuration Scene Selector
        Button configSceneButton = Util.createButton(Layout.CONFIG_ICON, Layout.SETTING_BUTTON_SIZE, false);
        configSceneButton.setOnAction(event -> configSelector());

        // ExitButton
        Button exitButton = Util.createButton(Layout.EXIT_ICON, Layout.SETTING_BUTTON_SIZE, false);
        exitButton.setOnAction(event -> primaryStage.close());

        // SceneSelection Control Box
        HBox lowerBox = Util.getHBox(Layout.SCREEN_WIDTH, Math.floor(Layout.SCREEN_HEIGHT * 0.10));
        lowerBox.setAlignment(Pos.TOP_RIGHT);
        lowerBox.getChildren().addAll(configSceneButton, historySceneButton, exitButton);
        mainPane.getChildren().addAll(upperBox, lowerBox);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("POS_Delivery");
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private SceneBase createScene(SceneBase sceneBase, Class<? extends SceneBase> sceneClass) {
        try {
            if (sceneBase == null) {
                sceneBase = sceneClass.getConstructor(Stage.class, Scene.class).newInstance(primaryStage, mainScene);
            } else {
                sceneBase.reload();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setScene(sceneBase);
        return sceneBase;
    }

    private void configSelector() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(Layout.SPACING);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(new Text("Configuration"));

        Button editMenuButton = new Button("Edit Menu");
        editMenuButton.setPrefSize(100, 40);
        editMenuButton.setOnAction(event -> {
            editScene = (EditScene) createScene(editScene, EditScene.class);
            dialog.close();
        });
        dialogVbox.getChildren().add(editMenuButton);

        Button menuLayoutButton = new Button("Menu Layout");
        menuLayoutButton.setPrefSize(100, 40);
        menuLayoutButton.setOnAction(event -> {
            menuLayoutScene = (MenuLayoutScene) createScene(menuLayoutScene, MenuLayoutScene.class);
            dialog.close();
        });
        dialogVbox.getChildren().add(menuLayoutButton);

        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        DataBaseController.getInstance().startDB();
        Configurator.load();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataBaseController.getInstance().closeDataBase();
    }
}
