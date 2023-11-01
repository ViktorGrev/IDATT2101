package oppgave8.LZ77;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        File encoderInputFile = new File("C:\\IntelliJ\\testProsjekter\\IDATT2101_Gruppe\\src\\oppgave8\\text.txt");
        File encoderOutputFile = new File("C:\\IntelliJ\\testProsjekter\\IDATT2101_Gruppe\\src\\oppgave8\\textEncoded.txt");
        File decoderOutputFile = new File("C:\\IntelliJ\\testProsjekter\\IDATT2101_Gruppe\\src\\oppgave8\\textDecoded.txt");
        try {
            encoderOutputFile.createNewFile();
            InputStream encoderInputStream = new BufferedInputStream(new FileInputStream(encoderInputFile));
            final OutputStream encoderOutputStream =
                    new BufferedOutputStream(new FileOutputStream(encoderOutputFile));
            LZ77Encoder encoder = new LZ77Encoder();
            encoderInputStream = new BufferedInputStream(new FileInputStream(encoderInputFile));
            encoder.encode(13, 5, encoderInputStream, encoderOutputStream);
            encoderOutputStream.flush();
            encoderOutputStream.close();

            decoderOutputFile.createNewFile();
            InputStream decoderInputStream = new BufferedInputStream(new FileInputStream(encoderOutputFile));
            OutputStream decoderOutputStream = new BufferedOutputStream(new FileOutputStream(decoderOutputFile));
            LZ77Decoder decoder = new LZ77Decoder();
            decoder.decode(decoderInputStream, decoderOutputStream);
            decoderOutputStream.flush();
            decoderOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
