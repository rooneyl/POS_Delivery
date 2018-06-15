package pos_delivery;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import pos_delivery.module.Configurator;
import pos_delivery.module.DataBaseController;

/**
 * Hello world!
 */
public class App extends Application {
    private Scene mainScene;
    private Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("POS_Delivery");
        primaryStage.setResizable(false);

        FlowPane mainPane = new FlowPane();
        mainPane.setAlignment(Pos.CENTER);
        mainScene = new Scene(mainPane, 100,100);

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
