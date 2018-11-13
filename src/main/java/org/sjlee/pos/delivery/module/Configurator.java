package org.sjlee.pos.delivery.module;

import org.sjlee.pos.delivery.model.Category;
import org.sjlee.pos.delivery.model.Menu;

import java.io.*;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Configurator {

    private static final String PROPERTY_LOCATION = "./resources/config.properties";
    private static final String MENU_LOCATION = "./resources/menu";

    private static final String BILL_PRINTER = "BILL_PRINTER";
    private static final String ROLL_PRINTER = "ROLL_PRINTER";
    private static final String SASHIMI_PRINTER = "SASHIMI_PRINTER";
    private static final String KITCHEN_PRINTER = "KITCHEN_PRINTER";

    public static void load() {
        try {
            FileInputStream input = new FileInputStream(PROPERTY_LOCATION);
            Properties prop = new Properties();
            prop.load(input);

            PrinterService.ROLL_PRINTER.append(prop.getProperty(ROLL_PRINTER));
            PrinterService.SASHIMI_PRINTER.append(prop.getProperty(SASHIMI_PRINTER));
            PrinterService.KITCHEN_PRINTER.append(prop.getProperty(KITCHEN_PRINTER));
            PrinterService.BILL_PRINTER.append(prop.getProperty(BILL_PRINTER));

            if (!Boolean.parseBoolean(prop.getProperty("INITIALIZED"))) {
                parseMenu();
//                FileOutputStream output = new FileOutputStream(PROPERTY_LOCATION);
//                prop.setProperty("INITIALIZED", "TRUE");
//                prop.store(output, null);
//                output.close();
            }

            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(PrinterService.Printer printer, String printerName) throws Exception {
        FileInputStream input = new FileInputStream(PROPERTY_LOCATION);
        Properties prop = new Properties();
        prop.load(input);

        switch (printer) {
            case SASHIMI:
                prop.setProperty(SASHIMI_PRINTER, printerName);
                PrinterService.SASHIMI_PRINTER.setLength(0);
                PrinterService.SASHIMI_PRINTER.append(prop.getProperty(SASHIMI_PRINTER));
                break;
            case ROLL:
                prop.setProperty(ROLL_PRINTER, printerName);
                PrinterService.ROLL_PRINTER.setLength(0);
                PrinterService.ROLL_PRINTER.append(prop.getProperty(ROLL_PRINTER));
                break;
            case KITCHEN:
                prop.setProperty(KITCHEN_PRINTER, printerName);
                PrinterService.KITCHEN_PRINTER.setLength(0);
                PrinterService.KITCHEN_PRINTER.append(prop.getProperty(KITCHEN_PRINTER));
                break;
            case NO_PRINTER:
                prop.setProperty(BILL_PRINTER, printerName);
                PrinterService.BILL_PRINTER.setLength(0);
                PrinterService.BILL_PRINTER.append(prop.getProperty(BILL_PRINTER));
                break;

            default:
                break;
        }

        FileOutputStream output = new FileOutputStream(PROPERTY_LOCATION);
        prop.store(output, null);

        input.close();
        output.close();
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