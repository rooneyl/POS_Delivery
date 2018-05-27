package pos_delivery.module;

import pos_delivery.model.Category;
import pos_delivery.model.Menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MenuController {

    private static MenuController menuController = null;
    private Map<Category, List<Menu>> menus;

    /**
     * Retrieve All Menus from DB and Store in Map According to Category.
     * The Map Only Store Valid Menus.
     */
    private MenuController() {
        menus = new LinkedHashMap<>();
        for (Category category : Category.values()) {
            menus.put(category, new ArrayList<>());
        }

        DataBaseController.getInstance()
                .retrieveMenu()
                .stream()
                .filter(Menu::isValid)
                .forEach(menu -> menus.get(menu.getCategory()).add(menu));
    }

    /**
     * Singleton MenuController.
     */
    public static MenuController getInstance() {
        if (menuController == null)
            menuController = new MenuController();
        return menuController;
    }

    /**
     * Return All the Valid Menus in a Category.
     */
    public List<Menu> getMenus(Category category) {
        return menus.get(category);
    }

    /**
     * Delete Menu in the Map and Mark the Menu as Invalid.
     * Deleted Menu also reflected in DB.
     */
    public void deleteMenu(final Menu menu) {
        menus.get(menu.getCategory()).stream()
                .filter(m -> m.equals(menu))
                .findFirst()
                .ifPresent(m -> {
                    m.setValid(false);
                    DataBaseController.getInstance().updateMenu(m);
                });
    }

    /**
     * Add Menu in the Map and Store in DB.
     */
    public void createMenu(final Menu menu) {
        menus.get(menu.getCategory()).add(menu);
        DataBaseController.getInstance().createMenu(menu);
    }

    /**
     * Update Existing Menu.
     */
    public void updateMenu(final Menu menu) {
        List<Menu> menuList = menus.get(menu.getCategory());
        menuList.stream()
                .filter(m -> m.equals(menu))
                .findFirst()
                .ifPresent(m -> {
                    menuList.remove(m);
                    menuList.add(menu);
                    DataBaseController.getInstance().updateMenu(menu);
                });
    }
}