package encryptdecrypt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;

public class Main {
    private static String mode;
    private static boolean modeIsSet;
    private static int key;
    private static String data;
    private static String alg;
    private static boolean algIsSet;
    private static BufferedWriter out;
    private static boolean outIsSet;
    private static File in;

    static {
        mode = "enc";
        modeIsSet = false;
        key = 0;
        data = "";
        alg = "shift";
        algIsSet = false;
        out = new BufferedWriter(new OutputStreamWriter(System.out));
        outIsSet = false;
    }

    public static void main(String[] args) {
        try {
            processArguments(args);
            Encryptor encryptor = new Encryptor(key, mode, out);

            switch (alg) {
                case "shift":
                    if ("".equals(data) && in != null)
                        encryptor.intKeyShiftEncryption(in);
                    else
                        encryptor.intKeyShiftEncryption(data);
                    break;
                case "unicode":
                    if ("".equals(data) && in != null)
                        encryptor.uniEncryption(in);
                    else
                        encryptor.uniEncryption(data);
                    break;
            }

        } catch (InvalidArgumentsException | IOException iae) {
            System.out.println("Error: ");
            iae.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private static void processArguments(String[] args) {
        for (int i = 0; i < args.length; i += 2) {

            switch (args[i]) {
                case "-mode":
                    processMode(i, args);
                    break;
                case "-key":
                    processKey(i, args);
                    break;
                case "-data":
                    processData(i, args);
                    break;
                case "-alg":
                    processAlg(i, args);
                    break;
                case "-out":
                    processOut(i, args);
                    break;
                case "-in":
                    processIn(i, args);
                    break;
                default:
                    throw new InvalidArgumentsException("non-existent argument: " + args[i]);
            }
        }
    }

    private static void processAlg(int i, String[] args) {
        if (i + 1 == args.length || args[i + 1].charAt(0) == '-')
            throw new InvalidArgumentsException("-alg is null");
        if (algIsSet)
            throw new InvalidArgumentsException("-alg duplicates");

        switch (args[i + 1]) {
            case "shift":
                alg = "shift";
                break;
            case "unicode":
                alg = "unicode";
                break;
            default:
                throw new InvalidArgumentsException("non-existent -alg: " + args[i + 1]);
        }
        algIsSet = true;
    }

    private static void processIn(int i, String[] args) {
        if (in != null)
            throw new InvalidArgumentsException("-in duplicates");
        if (i + 1 == args.length)
            return;

        try {
            in = new File(args[i + 1]);
            if (!in.exists())
                throw new FileNotFoundException();

        } catch (FileNotFoundException ffe) {
            throw new InvalidArgumentsException(ffe.toString());
        }
    }

    private static void processOut(int i, String[] args) {
        if (outIsSet)
            throw new InvalidArgumentsException("-out duplicates");
        if (i + 1 == args.length)
            return;

        try {

            File f = new File(args[i + 1]);
            f.createNewFile();

            out.close();
            out = new BufferedWriter(new FileWriter(f, false));

        } catch (IOException ioe) {
            throw new InvalidArgumentsException(ioe.toString());
        }
        outIsSet = true;
    }

    private static void processData(int i, String[] args) {
        if (i + 1 == args.length)
            throw new InvalidArgumentsException("-data is null");
        if (!"".equals(data))
            throw new InvalidArgumentsException("-data duplicates");

        data = args[i + 1];
    }

    private static void processKey(int i, String[] args) {
        if (i + 1 == args.length || args[i + 1].charAt(0) == '-')
            throw new InvalidArgumentsException("-key is null");
        if (key != 0)
            throw new InvalidArgumentsException("-key duplicates");

        try {
            key = Integer.parseInt(args[i + 1]);
        } catch (InputMismatchException ime) {
            throw new InvalidArgumentsException(ime.toString());
        }
    }

    private static void processMode(int i, String[] args) {
        if (i + 1 == args.length || args[i + 1].charAt(0) == '-')
            throw new InvalidArgumentsException("-mode is null");
        if (modeIsSet)
            throw new InvalidArgumentsException("-mode duplicates");

        switch (args[i + 1]) {
            case "enc":
                mode = "enc";
                break;
            case "dec":
                mode = "dec";
                break;
            default:
                throw new InvalidArgumentsException("non-existent -mode: " + args[i + 1]);
        }
        modeIsSet = true;
    }
}
