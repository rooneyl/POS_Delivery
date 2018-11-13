package org.sjlee.pos.delivery.module;

import org.sjlee.pos.delivery.model.Customer;
import org.sjlee.pos.delivery.model.Order;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * PrinterService Handles Printing Task When Order Is Placed.
 * Each Order Belong To Specific Section In The Kitchen.
 * These Orders Are Sent Using Printers To Assign What To Make.
 */
public class PrinterService {
    static StringBuffer ROLL_PRINTER = new StringBuffer();
    static StringBuffer SASHIMI_PRINTER = new StringBuffer();
    static StringBuffer KITCHEN_PRINTER = new StringBuffer();
    static StringBuffer BILL_PRINTER = new StringBuffer();

    /**
     * Initiating Print Job.
     */
    public static void print(Customer customer, List<List<Order>> orders) {

        // Map of task printer jobs
        Map<String, PrinterJobBuilder> printers = new HashMap<>();

        for (List<Order> section : orders) {
            // Order with multiple bagging are separated using section
            for (Order order : section) {
                // denote that printer job is written
                Set<String> editedPrinters = new HashSet<>();

                for (StringBuffer printerName : order.getMenu().getPrinter().getPrinters()) {
                    String printerKey = printerName.toString();
                    editedPrinters.add(printerKey);

                    if (!printers.containsKey(printerKey)) { // First task for dedicated dept
                        printers.put(printerKey, createTask(customer));
                    }

                    // print quantity, item name, and details
                    PrinterJobBuilder printerBuilder = printers.get(printerKey).writeString(order.toString());
                    order.getDetails().forEach(detail -> printerBuilder.writeString(" (" + detail + ")"));
                }

                //
                editedPrinters.forEach(editedPrinter -> printers.get(editedPrinter).addSeparator());
            }
        }

        // Create receipt
        createBill(customer, orders);

        // Execute printers
        printers.forEach((printer, builder) -> executePrint(printer, builder.getByte()));
    }

    /**
     * Create Receipt For Customers.
     */
    private static void createBill(Customer customer, List<List<Order>> order) {
        PrinterJobBuilder bill = new PrinterJobBuilder();
        bill
                .bold(false)
                .writeString(
                        "Sushi Aria",
                        "4018 Cambie Street",
                        "Vancouver, BC")
                .newLine()
                .bold(true)
                .reverseColor(true)
                .writeString(
                        customer.getSource().toString(),
                        customer.getDate().toString())
                .newLine()
                .addSeparator()
                .bold(false)
                .reverseColor(false);

        order.forEach(section -> {
            String[] sectionDetail = section.stream()
                    .map(Order::toString)
                    .toArray(String[]::new);

            bill.writeString(sectionDetail);
            bill.addSeparator();
        });

        executePrint(BILL_PRINTER.toString(), bill.getByte());
    }

    /**
     * Create Task Header.
     */
    private static PrinterJobBuilder createTask(Customer customer) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime now = customer.getTime().toLocalTime().plusMinutes(15);

        String orderInfo = " " + customer.getSource() + " #" + customer.getName();
        String pickUpInfo = " Pickup: " + dtf.format(now);

        return new PrinterJobBuilder()
                .addBuzzer()
                .reverseColor(true)
                .writeString(orderInfo, pickUpInfo)
                .reverseColor(false)
                .addSeparator()
                .bold(false);
    }

    /**
     * Execute Printer.
     * @param printer name of printer
     * @param b byte array of what to print
     */
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

    /**
     * Search For Connected Printer Names.
     * @return list of connected printer names
     */
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

    /**
     * Enum Representing Department Associated With Making Item.
     * If More Than Two Department Is Collaborating, The Two Dept Are Named Using
     * under dash.
     */
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
