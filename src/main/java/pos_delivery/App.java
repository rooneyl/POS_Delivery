package pos_delivery;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pos_delivery.model.Source;
import pos_delivery.module.Configurator;
import pos_delivery.module.DataBaseController;
import pos_delivery.module.OrderPlacer;
import pos_delivery.view.Layout;
import pos_delivery.view.OrderScene;
import pos_delivery.view.Util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App extends Application {
    private Scene mainScene;
    private Stage primaryStage;

    private OrderScene orderScene;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // MainScene, Entry Point of App
        FlowPane mainPane = new FlowPane();
        mainPane.setAlignment(Pos.CENTER);
        mainScene = new Scene(mainPane, Layout.SCREEN_WIDTH,Layout.SCREEN_HEIGHT);

        // Source Selector
        HBox sourceSelectorBox = Util.getHBox(Layout.SCREEN_WIDTH, Math.floor(Layout.SCREEN_HEIGHT * 0.9));
        Map<Source, Button> companyButtons = new LinkedHashMap<>();
        companyButtons.put(Source.DOORDASH, Util.createButton(Layout.DOORDASH_ICON, Layout.COMPANY_BUTTON_SIZE, false));
        companyButtons.put(Source.FOODORA, Util.createButton(Layout.FOODORA_ICON, Layout.COMPANY_BUTTON_SIZE, false));
        companyButtons.forEach(((source, button) -> {
            button.setOnAction(event ->
                    Util.getInputText("Customer").ifPresent(customer -> {
                        orderScene = new OrderScene(new AnchorPane(),Layout.SCREEN_WIDTH,Layout.SCREEN_HEIGHT);
//                        orderScene.setOrderPlacer(new OrderPlacer(customer, source));
                        primaryStage.setScene(orderScene);
                    }));
            sourceSelectorBox.getChildren().add(button);
        }));

        // Exit Button
        Button exitButton = Util.createButton(Layout.EXIT_ICON, Layout.SETTING_BUTTON_SIZE, false);
        exitButton.setOnAction(event -> primaryStage.close());

        // Control Buttons
        HBox controlButtonBox = Util.getHBox(Layout.SCREEN_WIDTH, Math.floor(Layout.SCREEN_HEIGHT * 0.10));
        controlButtonBox.setAlignment(Pos.TOP_RIGHT);
        controlButtonBox.getChildren().addAll(exitButton);

        mainPane.getChildren().addAll(sourceSelectorBox, controlButtonBox);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("POS_Delivery");
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);
        primaryStage.setScene(mainScene);
        primaryStage.show();
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
