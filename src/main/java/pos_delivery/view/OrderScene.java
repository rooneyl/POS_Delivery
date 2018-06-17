package pos_delivery.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class OrderScene extends Scene {

    final AnchorPane root;
    final Button returnButton;

    public OrderScene(Parent root, double width, double height) {
        super(root, width, height);

        this.root = (AnchorPane) root;
        returnButton = Util.createButton(Layout.RETURN_ICON, Layout.SETTING_BUTTON_SIZE, true);

    }

    public void setReturnButtonSize(double size) {
        ImageView imageView = (ImageView) returnButton.getGraphic();
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);

        returnButton.setGraphic(imageView);
        returnButton.setPrefSize(size, size);
    }
}
