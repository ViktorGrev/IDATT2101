package oppgave8;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidParameterException;
import java.util.*;

public class LZ77 {

    public static class LZ77Encoder {
        private BitWriter bitWriter;
        private SlidingWindow slidingWindow;

        public void encode(int nrBitsForOffset, int nrBitsForLength, InputStream inputStream, OutputStream outputStream) throws IOException {
            slidingWindow = new SlidingWindow(nrBitsForOffset, nrBitsForLength, inputStream);
            bitWriter = new BitWriter(outputStream);
            bitWriter.writeNBitValue(nrBitsForLength, 3);
            bitWriter.writeNBitValue(nrBitsForOffset, 4);
            while (slidingWindow.lookAheadSize() > 0) {
                Token token = slidingWindow.nextToken();
                bitWriter.writeNBitValue(token.getLength(), nrBitsForLength);
                bitWriter.writeNBitValue(token.getOffset(), nrBitsForOffset);
                bitWriter.writeNBitValue(token.getSymbol(), 8);
            }
            bitWriter.flush();
        }
    }

    public static class LZ77Decoder {
        private BitReader bitReader;
        private CircularArrayList<Integer> buffer;

        public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
            bitReader = new BitReader(inputStream);
            long nrBitsForLength = bitReader.readNBitValue(3);
            long nrBitsForOffset = bitReader.readNBitValue(4);
            buffer = new CircularArrayList<>((1 << nrBitsForOffset) + 1);
            try {
                while (true) {
                    long length = bitReader.readNBitValue((int) nrBitsForLength);
                    long offset = bitReader.readNBitValue((int) nrBitsForOffset);
                    long symbol = bitReader.readNBitValue(8);
                    long sequenceStartIndex = buffer.size() - 2 - offset;
                    for (int i = 0; i < length; i++) {
                        if (buffer.size() == buffer.capacity()) {
                            buffer.remove(0);
                        } else {
                            sequenceStartIndex++;
                        }
                        buffer.add(buffer.size(), buffer.get((int) sequenceStartIndex));
                        outputStream.write(buffer.get((int) sequenceStartIndex));
                    }
                    if (buffer.size() == buffer.capacity()) {
                        buffer.remove(0);
                    }
                    buffer.add(buffer.size(), (int) symbol);
                    outputStream.write((int) symbol);
                }
            } catch (UnsupportedOperationException e) { //end of stream

            }
            outputStream.flush();
        }
    }

    public static class BitReader {
        private InputStream inputStream;
        private ReadBuffer buffer = new ReadBuffer();

        public BitReader(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public int readBit() throws IOException {
            if (buffer.isEmpty()) {
                int readByte = inputStream.read();
                if (readByte == -1) {
                    throw new UnsupportedOperationException("Cannot read bit, stream ended");
                }
                buffer.refill((byte) readByte);
            }
            return buffer.nextBit();
        }

        public long readNBitValue(int n) throws IOException {
            long nBitValue = 0;
            for (int i = 0; i < n; i++) {
                nBitValue |= (readBit() << i);
            }
            return nBitValue;
        }
    }

    public static class BitWriter {
        private OutputStream outputStream;
        private WriteBuffer buffer = new WriteBuffer();

        public BitWriter(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public void writeBit(int bit) throws IOException {
            if (buffer.isFull()) {
                outputStream.write(buffer.getByteAndClear());
            }
            buffer.putBit(bit);
        }

        public void writeNBitValue(long value, int n) throws IOException {
            for (int i = 0; i < n; i++) {
                writeBit((int) ((value >> i) & 1));
            }
        }

        public void flush() throws IOException {
            outputStream.write(buffer.getByteAndClear());
            outputStream.flush();
        }
    }

    public static class CircularArrayList<E>
            extends AbstractList<E> implements RandomAccess {

        private final int n;
        private final List<E> buf;
        private int head = 0;
        private int tail = 0;

        public CircularArrayList(int capacity) {
            n = capacity + 1;
            buf = new ArrayList<E>(Collections.nCopies(n, (E) null));
        }

        public int capacity() {
            return n - 1;
        }

        private int wrapIndex(int i) {
            int m = i % n;
            if (m < 0) {
                m += n;
            }
            return m;
        }

        private void shiftBlock(int startIndex, int endIndex) {
            assert (endIndex > startIndex);
            for (int i = endIndex - 1; i >= startIndex; i--) {
                set(i + 1, get(i));
            }
        }

        @Override
        public int size() {
            return tail - head + (tail < head ? n : 0);
        }

        @Override
        public E get(int i) {
            if (i < 0 || i >= size()) {
                throw new IndexOutOfBoundsException("index:" + i + " capacity:" + capacity() + " size:" + size());
            }
            return buf.get(wrapIndex(head + i));
        }

        @Override
        public E set(int i, E e) {
            if (i < 0 || i >= size()) {
                throw new IndexOutOfBoundsException("index:" + i + " capacity:" + capacity() + " size:" + size());
            }
            return buf.set(wrapIndex(head + i), e);
        }

        @Override
        public void add(int i, E e) {
            int s = size();
            if (s == n - 1) {
                throw new IllegalStateException(
                        "CircularArrayList is filled to capacity. "
                                + "(You may want to remove from front"
                                + " before adding more to back.)");
            }
            if (i < 0 || i > s) {
                throw new IndexOutOfBoundsException("index:" + i + " capacity:" + capacity() + " size:" + size());
            }
            tail = wrapIndex(tail + 1);
            if (i < s) {
                shiftBlock(i, s);
            }
            set(i, e);
        }

        @Override
        public E remove(int i) {
            int s = size();
            if (i < 0 || i >= s) {
                throw new IndexOutOfBoundsException("index:" + i + " capacity:" + capacity() + " size:" + size());
            }
            E e = get(i);
            if (i > 0) {
                shiftBlock(0, i);
            }
            head = wrapIndex(head + 1);
            return e;
        }
    }

    public static class ReadBuffer {
        private byte bits;
        private int pos = 8;

        public int length() {
            return 8 - pos;
        }

        public void refill(byte value) {
            bits = value;
            pos = 0;
        }

        public boolean isEmpty() {
            return pos == 8;
        }

        public int nextBit() {
            return bits >> pos++ & 1;
        }
    }

    public static class WriteBuffer {
        private byte bits;
        private int pos = 0;

        public void putBit(int bit) {
            bits |= (bit << pos++);
        }

        public boolean isFull() {
            return pos == 8;
        }

        public int length() {
            return pos;
        }

        public byte getByteAndClear() {
            byte theByte = bits;
            bits = 0;
            pos = 0;
            return theByte;
        }
    }

    public static class Token {
        private long length;
        private long offset;
        private long symbol;

        public Token(long length, long offset, long symbol) {
            this.length = length;
            this.offset = offset;
            this.symbol = symbol;
        }

        public Token() {
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {

            this.length = length;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public long getSymbol() {
            return symbol;
        }

        public void setSymbol(long newSymbol) {
            this.symbol = newSymbol;
        }

        @Override
        public boolean equals(Object obj) {
            Token otherToken = (Token) obj;
            if (otherToken.getLength() != length
                    || otherToken.getOffset() != offset
                    || otherToken.getSymbol() != symbol) {
                return false;
            }
            return true;
        }
    }

    public static class SlidingWindow {
        private CircularArrayList<Integer> circularList;
        private InputStream inputStream;
        private int searchSize = 0;
        private int lookAheadSize = 0;
        private int searchCapacity;
        private int lookAheadCapacity;
        private boolean streamEnded = false;

        public SlidingWindow(int nrBitsForOffset, int nrBitsForLength, InputStream inputStream) throws IOException {
            this.inputStream = inputStream;
            searchCapacity = (1 << nrBitsForOffset);
            lookAheadCapacity = (1 << nrBitsForLength);
            circularList = new CircularArrayList<>(lookAheadCapacity + searchCapacity);
            while (lookAheadSize < lookAheadCapacity) {
                int readByte = inputStream.read();
                if (readByte == -1) {
                    streamEnded = true;
                    break;
                } else {
                    circularList.add(circularList.size(), readByte);
                    lookAheadSize++;
                }
            }
        }

        public boolean searchBufferIsFull() {
            return searchSize == searchCapacity;
        }

        public boolean lookAheadBufferIsFull() {
            return lookAheadSize == lookAheadCapacity;
        }

        public int lookAheadSize() {
            return lookAheadSize;
        }

        public boolean streamEnded() {
            return streamEnded;
        }

        public int get(int index) {
            return circularList.get(index);
        }

        public int lookAheadStartIndex() {
            return searchSize;
        }

        public void slide(long slideLength) throws IOException {
            if (lookAheadSize == 0 && streamEnded()) {
                throw new RuntimeException("Stream ended & look ahead is empty");
            }
            if (slideLength < 1) {
                throw new InvalidParameterException("Cannot slide with the length of " + slideLength + " bytes");
            }
            if (slideLength > lookAheadSize) {
                throw new IndexOutOfBoundsException("Cannot slide more than the LookAheadBuffer's size at once. " +
                        "Analyze those bytes, don't just slide over them, boy!");
            }
            if (!lookAheadBufferIsFull() && !streamEnded) {
                throw new IllegalStateException("Stream didn't end, but LAB is not full.");
            }
            for (int i = 0; i < slideLength; i++) {
                int readByte = 0;
                if (!streamEnded()) {
                    readByte = inputStream.read();
                    if (readByte == -1) {
                        streamEnded = true;
                    }
                }
                if (!streamEnded()) {
                    if (searchBufferIsFull()) {
                        circularList.remove(0);
                    } else {
                        searchSize++;
                    }
                    circularList.add(circularList.size(), readByte);
                } else { //stream ended
                    if (lookAheadSize > 0) {
                        if (searchBufferIsFull()) {
                            circularList.remove(0);
                        } else {
                            searchSize++;
                        }
                        lookAheadSize--;
                    } else {
                        throw new UnsupportedOperationException("Cannot slide when stream ended & LAB is empty");
                    }
                }
            }
        }

        public Token nextToken() throws IOException {
            if (lookAheadSize() == 0) {
                throw new RuntimeException("Cannot build next token. Look ahead is empty.");
            }
            Token token = new Token(0, 0, get(lookAheadStartIndex()));
            if (lookAheadSize > 1) {
                for (int sbIndex = searchSize - 1; sbIndex >= 0; sbIndex--) {
                    int newLength = 0;
                    while (get(sbIndex + newLength) == get(lookAheadStartIndex() + newLength)
                            && newLength < lookAheadSize - 1) {
                        newLength++;
                    }
                    if (newLength > token.getLength()) {
                        token.setLength(newLength);
                        token.setOffset(searchSize - sbIndex - 1);
                        token.setSymbol(get(lookAheadStartIndex() + newLength));
                        if (newLength == lookAheadSize - 1) {
                            break;
                        }
                    }
                }
            }
            slide(token.getLength() + 1);
            return token;
        }
    }
}

class Main {
    public static void main(String[] args) throws IOException {
        /*File encoderInputFile = new File("src\\oppgave8\\text.txt");
        File encoderOutputFile = new File("src\\oppgave8\\textEncoded.txt");
        File decoderOutputFile = new File("src\\oppgave8\\textDecoded.txt");*/

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
            LZ77.LZ77Encoder encoder = new LZ77.LZ77Encoder();
            encoderInputStream = new BufferedInputStream(connection.getInputStream());
            encoder.encode(13, 5, encoderInputStream, encoderOutputStream);
            encoderOutputStream.flush();
            encoderOutputStream.close();

            decoderOutputFile.createNewFile();
            InputStream decoderInputStream = new BufferedInputStream(new FileInputStream(encoderOutputFile));
            OutputStream decoderOutputStream = new BufferedOutputStream(new FileOutputStream(decoderOutputFile));
            LZ77.LZ77Decoder decoder = new LZ77.LZ77Decoder();
            decoder.decode(decoderInputStream, decoderOutputStream);
            decoderOutputStream.flush();
            decoderOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
