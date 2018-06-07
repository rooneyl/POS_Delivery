package pos_delivery.module;

import java.io.ByteArrayOutputStream;

public class PrinterJobBuilder {
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


    private ByteArrayOutputStream byteArray;

    public PrinterJobBuilder() {
        this.byteArray = new ByteArrayOutputStream();
        setMode(init, doubleSize, bold_on);
    }

    public byte[] getByte() {
        return setMode(cut).getByte();
    }

    public PrinterJobBuilder fondSize(int size) {
        return setMode(size == 2 ? doubleSize : regualrSize);
    }

    public PrinterJobBuilder reverseColor(boolean bool) {
        return setMode(bool ? reverseColor_on : reverseColor_Off);
    }

    public PrinterJobBuilder bold(boolean bool) {
        return setMode(bool ? bold_on : bold_off);
    }

    public PrinterJobBuilder addSeparator() {
        return writeString(separator);
    }

    public PrinterJobBuilder addBuzzer() {
        return setMode(buzzer, buzzer, buzzer);
    }

    public PrinterJobBuilder newLine() {
        return setMode(lineFeed);
    }

    public PrinterJobBuilder writeString(String... strings) {
        for (String string : strings) {
            setMode(string.getBytes(), lineFeed);
        }

        return this;
    }

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