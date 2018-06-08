package pos_delivery.module;

import pos_delivery.model.Customer;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.util.*;

public class PrinterService {
    static StringBuffer ROLL_PRINTER = new StringBuffer();
    static StringBuffer SASHIMI_PRINTER = new StringBuffer();
    static StringBuffer KITCHEN_PRINTER = new StringBuffer();
    static StringBuffer BILL_PRINTER = new StringBuffer();

    public static void print(Customer customer, List orders) {
        Map<String, PrinterJobBuilder> printers = new HashMap<>();

        printers.forEach((printer, builder) -> executePrint(printer, builder.getByte()));
    }

    private static void executePrint(String printer, byte[] b) {
        try {
            PrinterName printerName = new PrinterName(printer, null);
            AttributeSet attrSet = new HashPrintServiceAttributeSet(printerName);
            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);
            job.print(doc, null);
        } catch (javax.print.PrintException pex) {
            System.out.println("Printer Error " + pex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getPrinters() {

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printServices[] = PrintServiceLookup.lookupPrintServices(flavor, pras);

        List<String> printerList = new ArrayList<>();
        for (PrintService printerService : printServices) {
            printerList.add(printerService.getName());
        }

        return printerList;
    }

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
