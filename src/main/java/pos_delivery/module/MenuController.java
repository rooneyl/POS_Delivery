package pos_delivery.module;

import pos_delivery.model.Category;
import pos_delivery.model.Menu;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuController {

    private static MenuController menuController = null;
    private Map<Category, List<Menu>> menus;

    /**
     * Retrieve All Menus from DB and Store in Map According to Category.
     * The Map Only Store Valid Menus.
     */
    private MenuController() {
        menus = DataBaseController.getInstance()
                .retrieveMenu()
                .stream()
                .filter(Menu::isValid)
                .collect(Collectors.groupingBy(Menu::getCategory));
    }

    /**
     * Singleton MenuController.
     */
    public static MenuController getInstance() {
        if (menuController == null)
            menuController = new MenuController();
        return menuController;
    }
}