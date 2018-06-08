package pos_delivery.module;

import java.io.ByteArrayOutputStream;

/**
 * Printer Job Builder Is A Byte Array Builder
 * Satisfying ESC/POS Guide Line To Operate Thermal Printer.
 * The Builder Supports Chain Method For Easier Building Commands.
 */
public class PrinterJobBuilder {

    /**
     * Byte Code Commands
     */
    private final static String separator = "------------------------";
    private final static byte[] buzzer = new byte[]{0x1B, 0x70, 0x00, (byte) 100, (byte) 250};
    private final static byte[] init = new byte[]{27, 64, 27, 100, 2};
    private final static byte[] cut = new byte[]{29, 'V', 66, 1};
    private final static byte[] lineFeed = new byte[]{10};
    private final static byte[] bold_off = new byte[]{27, 69, 0};
    private final static byte[] bold_on = new byte[]{27, 69, 1};
    private final static byte[] reverseColor_on = new byte[]{29, 66, 1};
    private final static byte[] reverseColor_Off = new byte[]{29, 66, 0};
    private final static byte[] regualrSize = new byte[]{29, 33, (byte) 0b00000000};
    private final static byte[] doubleSize = new byte[]{29, 33, (byte) 0b00010001};

    /**
     * Main Byte Array Stream
     **/
    private ByteArrayOutputStream byteArray;

    /**
     * Constructor:
     * Printer Job Builder Is Initialized With
     * Double Sized Bold Font.
     */
    public PrinterJobBuilder() {
        this.byteArray = new ByteArrayOutputStream();
        setMode(init, doubleSize, bold_on);
    }

    /**
     * Returns Generated Byte Array of ESC/POS Commands
     */
    public byte[] getByte() {
        return setMode(cut).getByte();
    }

    /**
     * Change Font Size. Only Two Size Are Supported: Double and Regular.
     *
     * @param size 2 for double size otherwise regular.
     */
    public PrinterJobBuilder fondSize(int size) {
        return setMode(size == 2 ? doubleSize : regualrSize);
    }

    /**
     * Invert Background Color and Font Color Making Black Background with
     * White Font Color.
     *
     * @param bool true for inverted color false for regular
     */
    public PrinterJobBuilder reverseColor(boolean bool) {
        return setMode(bool ? reverseColor_on : reverseColor_Off);
    }

    /**
     * Bold Font.
     */
    public PrinterJobBuilder bold(boolean bool) {
        return setMode(bool ? bold_on : bold_off);
    }

    /**
     * Add Horizontal Black Line.
     */
    public PrinterJobBuilder addSeparator() {
        return writeString(separator);
    }

    /**
     * Sound Buzzer Connected To A Printer Using Kick-Drawer Port.
     * Alarm Will Sound Three Times.
     */
    public PrinterJobBuilder addBuzzer() {
        return setMode(buzzer, buzzer, buzzer);
    }

    /**
     * Line Break
     */
    public PrinterJobBuilder newLine() {
        return setMode(lineFeed);
    }

    /**
     * Write Strings. Strings Are Separated by Line Break.
     */
    public PrinterJobBuilder writeString(String... strings) {
        for (String string : strings) {
            setMode(string.getBytes(), lineFeed);
        }

        return this;
    }

    /**
     * Internal ESC/POS Command Writer.
     *
     * @param bytes command needs to be on.
     */
    private PrinterJobBuilder setMode(byte[]... bytes) {
        try {
            for (byte[] aByte : bytes) {
                byteArray.write(aByte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }
}