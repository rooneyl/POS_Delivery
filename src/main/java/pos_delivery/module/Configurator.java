package pos_delivery.module;

import pos_delivery.model.Category;
import pos_delivery.model.Menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Configurator {

    private static final String MENU_LOCATION = "./resources/menu";

    public static void load() {
        try {
            parseMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseMenu() throws Exception {
        File file = new File(MENU_LOCATION);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            String[] description = st.split(",");

            Menu menu = new Menu();
            String name = Stream.of(description[0].split("=")[1].toLowerCase().trim().split("\\s"))
                    .filter(word -> word.length() > 0)
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "))
                    .replace("T/s", "T/S")
                    .replace("P/s", "P/S")
                    .replace("Bc", "BC")
                    .replace("S/s", "S/S");
            menu.setName(name);
            menu.setDescription(description[1].split("=")[1].replace("\n", "\\n"));
            menu.setCategory(Category.valueOf(description[2].split("=")[1]));
            menu.setPrinter(PrinterService.Printer.valueOf(description[4].split("=")[1]));
            menu.setValid(true);
            menu.setPosition(Integer.parseInt(description[6].split("=")[1]) - 1);
            menu.setColor(description[7].split("=")[1]);

            MenuController.getInstance().createMenu(menu);
        }
    }
}
