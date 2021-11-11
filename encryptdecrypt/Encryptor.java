package encryptdecrypt;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;

public class Encryptor {
    public static final String ENC = "enc";
    public static final String DEC = "dec";
    public static final int EN_VOL = 26;
    public static final char[] lowEnAlph;
    public static final char[] upEnAlph;

    static {
        lowEnAlph = new char[26];
        upEnAlph = new char[26];

        for (int i = 0; i < 26; i++) {
            lowEnAlph[i] = (char) (i + 97);
            upEnAlph[i] = (char) (i + 65);
        }
    }

    private int key;
    private String mode;
    private Writer out;

    public Encryptor(int key, String mode, Writer out) {
        if (!mode.equals(ENC) && !mode.equals(DEC))
            return;
        if (mode.equals(DEC))
            key = -key;

        this.key = key;
        this.mode = mode;
        this.out = out;
    }

    public int getKey() {
        return key;
    }

    public String getMode() {
        return mode;
    }

    public void intKeyShiftEncryption(String data) throws IOException {
        for (int i = 0; i < data.length(); i++) {
            char cur = data.charAt(i);

            if (cur >= 'a' && cur <= 'z')
                out.write(lowEnAlph[getRealMod(cur - 97 + key, EN_VOL)]);
            else if (cur >= 'A' && cur <= 'Z')
                out.write(upEnAlph[getRealMod(cur - 65 + key, EN_VOL)]);
            else
                out.write(cur);
        }
        out.flush();
    }

    public void intKeyShiftEncryption(File data) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            int cur;

            while ((cur = br.read()) != -1) {

                if (cur >= 'a' && cur <= 'z')
                    out.write(lowEnAlph[getRealMod(cur - 97 + key, EN_VOL)]);
                else if (cur >= 'A' && cur <= 'Z')
                    out.write(upEnAlph[getRealMod(cur - 65 + key, EN_VOL)]);
                else
                    out.write(cur);
            }
            out.flush();
        }
    }

    private int getRealMod(int a, int b) {
        return ((a % b) + b) % b;
    }

    public void uniEncryption(String data) throws IOException {
        for (int i = 0; i < data.length(); i++) {
            char cur = (char) (data.charAt(i) + key);
            out.write(cur);
        }
        out.flush();
    }

    public void uniEncryption(File data) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            int cur;

            while ((cur = br.read()) != -1) {
                out.write((char) (cur + key));
            }
            out.flush();
        }
    }
}
