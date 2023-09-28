package oppgave5;

import java.util.Random;

public class YtelseHashtabell {
  //The table where the hash and probing will be done
  private final int[] table;
  //A number counting collisions
  private long collisions;

  private int antallTall;

  //Constructor
  public YtelseHashtabell(long capacity) {
    table = new int[(int) capacity];
    collisions = 0;
    antallTall = 0;
  }

  //Method for inserting in the table with linear probing
  public void insertLinearProbing(int key) {
    int index = modHash(key);

    while (table[index] != 0) {
      index = (index + 1) % table.length;
      collisions++;
    }
    table[index] = key;
    antallTall++;
  }

  //A method for inserting with double hashing, was way too slow
  public void insertDoubleHashing(int key) {
    long index = modHash(key);
    long stepSize = doubleHash(key);
    int attempt = 1;

    while (table[(int) index] != 0) {
      index = (index + attempt * stepSize) % table.length;
      attempt++;
      collisions++;
    }

    table[(int) index] = key;
  }

  //Working and the used insert method for double hashing
  public void insertDoubleHashing2(int key) {
    long index = modHash(key);
    long stepSize = 1 + (key % (table.length - 1));

    if (table[(int) index] == 0) {
      table[(int) index] = key;
      antallTall++;
      return;
    }

    int attempts = 0;

    for (;;) {
      index = (index + stepSize) % table.length;
      attempts++;

      if (table[(int) index] == 0) {
        table[(int) index] = key;
        collisions += attempts;
        antallTall++;
        return;
      }
    }
  }

  //A search method, not tested
  public boolean search(int key) {
    long index = modHash(key);

    while (table[(int) index] != 0) {
      if (table[(int) index] == key) {
        return true;
      }
      index = index + (1 + (key % (table.length - 1)));
    }

    return false;
  }

  //Modulo hash method
  protected int modHash(int key) {
    return key % table.length;
  }

  //Bitwise hashing method, could be used
  private int bitwiseXORHash(int key) {
    int hash = key;
    hash ^= (hash >>> 20) ^ (hash >>> 12);
    return hash ^ (hash >>> 7) ^ (hash >>> 4);
  }

  //Method for double hashing in the first, not used insert method
  private long doubleHash(int key) {
    int prime = findPrimeLessThanCapacity();
    return prime - (key % prime);
  }

  //Method to find prime less than capacity
  private int findPrimeLessThanCapacity() {
    int capacity = table.length;
    boolean[] isComposite = new boolean[capacity];

    for (int i = 2; i * i < capacity; i++) {
      if (!isComposite[i]) {
        for (int j = i * i; j < capacity; j += i) {
          isComposite[j] = true;
        }
      }
    }

    for (int i = capacity - 1; i >= 2; i--) {
      if (!isComposite[i]) {
        return i;
      }
    }

    return 2;
  }

  //Method to get the collisions
  public long getCollisions() {
    return collisions;
  }

  public int getAntallTall(){ return antallTall;}

  //Main method
  public static void main(String[] args) {
    int[] data = generateRandomData(10000019);
    int[] fillPercentages = { 50, 80, 90, 99, 100 };

    for (int fillPercentage : fillPercentages) {
      int dataSize = (int) (data.length * (fillPercentage / 100.0));
      int[] subset = new int[dataSize];
      System.arraycopy(data, 0, subset, 0, dataSize);

      long tableSize = findPrimeSizeOverThreshold(dataSize, data.length-1);

      if (tableSize == -1) {
        System.out.println("No prime size found over 10,000,000 for fill percentage " + fillPercentage);
      } else {
        System.out.println("---Experiment: " + fillPercentage + "% fill rate---");
        System.out.println("Table Size: " + tableSize);

        System.out.println("Linear Probing:");
        YtelseHashtabell linearProbingTable = new YtelseHashtabell(tableSize);
        testHashTable(linearProbingTable, subset, true);

        System.out.println("Double Hashing:");
        YtelseHashtabell doubleHashingTable = new YtelseHashtabell(tableSize);
        testHashTable(doubleHashingTable, subset, false);

        System.out.println();
      }
    }
  }

  //Methods to find the first prime number that is above the threshold
  public static long findPrimeSizeOverThreshold(int minSize, int threshold) {
    for (long size = minSize; size < Long.MAX_VALUE; size++) {
      if (isPrime(size) && size > threshold) {
        return size;
      }
    }
    return -1;
  }

  public static boolean isPrime(long n) {
    if (n <= 1) {
      return false;
    }
    if (n <= 3) {
      return true;
    }
    if (n % 2 == 0 || n % 3 == 0) {
      return false;
    }
    for (long i = 5; i * i <= n; i += 6) {
      if (n % i == 0 || n % (i + 2) == 0) {
        return false;
      }
    }
    return true;
  }

  //Generating random numbers that will be hashed
  public static int[] generateRandomData(int size) {
    if (size < 1) {
      throw new IllegalArgumentException("Størrelsen må være minst 1");
    }

    int[] data = new int[size];
    Random random = new Random();

    data[0] = random.nextInt(250) + 1;

    for (int i = 1; i < size; i++) {
      data[i] = data[i - 1] + random.nextInt(250) + 1;
    }
    // Shuffle the array
    for (int i = size - 1; i > 0; i--) {
      int index = random.nextInt(i + 1);

      int temp = data[i];
      data[i] = data[index];
      data[index] = temp;
    }
    return data;
  }

  //Method that runs the test
  public static void testHashTable(YtelseHashtabell table, int[] data, boolean useLinearProbing) {
    long startTime = System.currentTimeMillis();

    for (int item : data) {
      if (useLinearProbing) {
        table.insertLinearProbing(item);
      } else {
        table.insertDoubleHashing2(item);
      }
    }



    long endTime = System.currentTimeMillis();
    long elapsedTime = endTime - startTime;

    System.out.println("Collisions: " + table.getCollisions());
    System.out.println("Tid brukt: " + elapsedTime + " milliseconds");
    System.out.println("Tall lagt til:" + table.getAntallTall());
    System.out.println("Størrelse på tabell: " + table.table.length);

    // Beregne fyllingsprosenten og skrive den ut
    double fillPercentage = (double) table.getAntallTall() / table.table.length * 100;
    System.out.println("Fyllingsprosent: " + fillPercentage + "%");
  }
}