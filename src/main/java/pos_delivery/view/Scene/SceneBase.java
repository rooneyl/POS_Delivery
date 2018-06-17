package pos_delivery.view.Scene;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pos_delivery.view.Layout;
import pos_delivery.view.Util;

public abstract class SceneBase extends Scene {

    final AnchorPane root;
    final Button returnButton;

    SceneBase(Stage primaryStage, Scene scene, boolean enableStyle) {
        super(new AnchorPane(), Layout.SCREEN_WIDTH, Layout.SCREEN_HEIGHT);
        root = (AnchorPane) getRoot();
        returnButton = Util.createButton(Layout.RETURN_ICON, Layout.SETTING_BUTTON_SIZE, true);
        returnButton.setOnAction(event -> primaryStage.setScene(scene));

        if (enableStyle)
            getStylesheets().add(getClass().getResource("../Style/style.css").toExternalForm());
    }

    final void setReturnButtonSize(double size) {
        ImageView imageView = (ImageView) returnButton.getGraphic();
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);

        returnButton.setGraphic(imageView);
        returnButton.setPrefSize(size, size);
    }

    public void reload() {

    }
}
