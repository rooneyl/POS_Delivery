package pos_delivery.view;

public class Layout {
    // General
    public static final double SCREEN_WIDTH = 1024.0;
    public static final double SCREEN_HEIGHT = 768.0;
    public static final double SPACING = 10.0;

    public static final double ONE_THIRD_WIDTH = Math.floor(SCREEN_WIDTH / 3);
    public static final double TWO_THIRD_WIDTH = Math.floor(SCREEN_WIDTH - ONE_THIRD_WIDTH);


    public static final String DOORDASH_ICON = "./resources/icons/doordash.png";
    public static final String FOODORA_ICON = "./resources/icons/foodora.png";


    // Primary Scene
    public static final double COMPANY_BUTTON_SIZE = Layout.SCREEN_WIDTH * 0.15;
    public static final double SETTING_BUTTON_SIZE = Layout.SCREEN_WIDTH * 0.05;

    // ICONS
    public static final String RETURN_ICON = "./resources/icons/return.png";
    public static final String MEMO_ICON = "./resources/icons/memo.png";
    public static final String DELETE_ICON = "./resources/icons/delete.png";
    public static final String PRINT_ICON = "./resources/icons/print.png";
    public static final String SEPARATE_ICON = "./resources/icons/separator.png";
    public static final String EXIT_ICON = "./resources/icons/exit.png";

    // JavaFx Style
    public static final String TAB_STYLE = "-fx-font: 16 arial;";
    public static final String MENU_STYLE = "-fx-font-weight: bold;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #000000;" +
            "-fx-border-width: 1px;" +
            "-fx-background-color: ";
}
