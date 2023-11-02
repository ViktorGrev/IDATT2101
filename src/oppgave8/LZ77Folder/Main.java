package oppgave8.LZ77Folder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Main {
    public static void main(String[] args) throws IOException {
        /*File encoderInputFile = new File("src\\oppgave8\\text.txt");
        File encoderOutputFile = new File("src\\oppgave8\\textEncoded.txt");
        File decoderOutputFile = new File("src\\oppgave8\\textDecoded.txt");*/


        //Read this Url instead
        URL url = new URL("https://www.idi.ntnu.no/emner/idatt2101/kompr/opg8-kompr.lyx");
        URLConnection connection = url.openConnection();
        //File encoderInputFile = new File("src\\oppgave8\\document.lyx");
        File encoderOutputFile = new File("src\\oppgave8\\documentEncoded.lyx");
        File decoderOutputFile = new File("src\\oppgave8\\documentDecoded.lyx");
        try {
            encoderOutputFile.createNewFile();
            InputStream encoderInputStream = new BufferedInputStream(connection.getInputStream()); //Read file -> new BufferedInputStream(new FileInputStream(encoderInputFile));
            final OutputStream encoderOutputStream =
                    new BufferedOutputStream(new FileOutputStream(encoderOutputFile));
            LZ77Encoder encoder = new LZ77Encoder();
            encoderInputStream = new BufferedInputStream(connection.getInputStream());
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
