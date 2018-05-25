package pos_delivery.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrinterService {
    static StringBuffer ROLL_PRINTER = new StringBuffer();
    static StringBuffer SASHIMI_PRINTER = new StringBuffer();
    static StringBuffer KITCHEN_PRINTER = new StringBuffer();
    static StringBuffer BILL_PRINTER = new StringBuffer();

    public enum Printer {
        ALL(new ArrayList<>(Arrays.asList(SASHIMI_PRINTER, KITCHEN_PRINTER, ROLL_PRINTER))),
        SASHIMI_ROLL(new ArrayList<>(Arrays.asList(SASHIMI_PRINTER, ROLL_PRINTER))),
        ROLL_KITCHEN(new ArrayList<>(Arrays.asList(KITCHEN_PRINTER, ROLL_PRINTER))),
        SASHIMI_KITCHEN(new ArrayList<>(Arrays.asList(SASHIMI_PRINTER, KITCHEN_PRINTER))),
        SASHIMI(new ArrayList<>(Arrays.asList(SASHIMI_PRINTER))),
        ROLL(new ArrayList<>(Arrays.asList(ROLL_PRINTER))),
        KITCHEN(new ArrayList<>(Arrays.asList(KITCHEN_PRINTER))),
        NO_PRINTER(new ArrayList<>(Arrays.asList(BILL_PRINTER)));

        private List<StringBuffer> printers;

        Printer(List<StringBuffer> printers) {
            this.printers = printers;
        }

        public List<StringBuffer> getPrinters() {
            return printers;
        }
    }
}
