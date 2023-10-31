package oppgave8;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LZSSEncodingDecoding {
  //Man method
  public static void main(String[] args) {
    try {
      String input = readFromFile("src/oppgave8/text.txt");
      int windowSize = 12;
      int lookAheadBufferSize = 4;

      System.out.println("Original Data: " + input);

      ArrayList<LZSSToken> encodedTokens = encodeLZSS(input, windowSize, lookAheadBufferSize);
      System.out.println("Encoded Data: " + encodedTokens);

      writeToFile(encodedTokens, "src/oppgave8/encodedData.lyx");

      String decodedData = decodeLZSS(encodedTokens, windowSize);
      System.out.println();
      System.out.println("Decoded Data: " + decodedData);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //LZSS token class
  static class LZSSToken {
    boolean isLiteral;
    char value;
    int offset;
    int length;

    //Token constuctor
    public LZSSToken(boolean isLiteral, char value, int offset, int length) {
      this.isLiteral = isLiteral;
      this.value = value;
      this.offset = offset;
      this.length = length;
    }

    //To String method
    @Override
    public String toString() {
      if (isLiteral) {
        return String.valueOf(value);
      } else {
        return Integer.toString(offset) + Integer.toString(length);
      }
    }
  }

  //TODO Implement Huffman coding

  //Encoding method
  static ArrayList<LZSSToken> encodeLZSS(String input, int windowSize, int lookAheadBufferSize) {
    ArrayList<LZSSToken> encodedTokens = new ArrayList<>();
    int inputLength = input.length();
    int currentPos = 0;

    while (currentPos < inputLength) {
      int maxMatchLength = 0;
      int maxMatchOffset = 0;

      for (int offset = 1; offset <= Math.min(currentPos, windowSize); offset++) {
        for (int length = 1; length <= Math.min(lookAheadBufferSize, inputLength - currentPos); length++) {
          boolean match = true;
          for (int i = 0; i < length; i++) {
            if (input.charAt(currentPos - offset + i) != input.charAt(currentPos + i)) {
              match = false;
              break;
            }
          }
          if (match && length > maxMatchLength) {
            maxMatchLength = length;
            maxMatchOffset = offset;
          }
        }
      }

      if (maxMatchLength > 0) {
        encodedTokens.add(new LZSSToken(false, ' ', maxMatchOffset, maxMatchLength));
        currentPos += maxMatchLength;
      } else {
        encodedTokens.add(new LZSSToken(true, input.charAt(currentPos), 0, 0));
        currentPos++;
      }
    }

    return encodedTokens;
  }

  //Deceoding method
  static String decodeLZSS(ArrayList<LZSSToken> encodedTokens, int windowSize) {
    StringBuilder decodedData = new StringBuilder();
    int currentPos = 0;

    for (LZSSToken token : encodedTokens) {
      if (token.isLiteral) {
        decodedData.append(token.value);
        currentPos++;
      } else {
        int startPos = currentPos - token.offset;
        for (int i = 0; i < token.length; i++) {
          char c = decodedData.charAt(startPos + i);
          decodedData.append(c);
          currentPos++;
        }
      }
    }

    return decodedData.toString();
  }

  //Read from file method
  static String readFromFile(String fileName) throws IOException {
    StringBuilder content = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String line;
    while ((line = reader.readLine()) != null) {
      content.append(line).append("\n");
    }
    reader.close();
    return content.toString();
  }

  //Write to file method.
  static void writeToFile(ArrayList<LZSSToken> encodedTokens, String fileName) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
    FileOutputStream fout=new FileOutputStream(fileName);
    for (LZSSToken token : encodedTokens) {
      //Kan skille her på å sende inn tall eller String, men tall skaper unreadable char
      if(token.isLiteral){
        String Char = String.valueOf(token.value);
        byte[] textByte = Char.getBytes(StandardCharsets.UTF_8);
        fout.write(textByte);
      } else {
        String text = token.offset + "" + token.length;
        byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        fout.write(textByte);
      }
    }
    writer.close();
  }
}

