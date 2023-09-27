package Algorithms5;

import java.util.Random;

public class YtelseHashtabell {
  private int[] table;
  private int size;
  private int collisions;

  public YtelseHashtabell(int capacity) {
    table = new int[capacity];
    size = 0;
    collisions = 0;
  }

  // Insert method for linear probing
  public void insertLinearProbing(int key) {
    long index = modHash(key);
    int attempts = 0;

    while (table[(int) index] != 0) {
      index = (index + 1) % table.length; // Use a simple increment for linear probing
      attempts++;
      collisions++;
    }

    table[(int) index] = key;
    size++;
    //System.out.println("Key " + key + " inserted after " + attempts + " attempts.");
  }

  // Insert method for double hashing
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
    size++;
  }

  public void insertDoubleHashing2(int key) {
    long index = modHash(key);
    long stepSize = multiHash(key);
    int attempts = 0;

    while (table[(int) index] != 0) {
      index = (index + stepSize) % table.length;
      attempts++;
      collisions++;
    }

    table[(int) index] = key;
    size++;

    //System.out.println("Key " + key + " inserted after " + attempts + " attempts.");
  }

  public boolean search(int key) {
    long index = modHash(key);

    while (table[(int) index] != 0) {
      if (table[(int) index] == key) {
        return true;
      }
      index = (index + multiHash(key)) % table.length;
    }

    return false;
  }

  protected long modHash(int key) {
    return (long) key % table.length;
  }

  private long multiHash(int key) {
    return 1 + ((long) key % (table.length - 1));
  }

  private long doubleHash(int key) {
    int prime = findPrimeLessThanCapacity(); // Find a prime less than table.length
    return prime - (key % prime);
  }

  private int findPrimeLessThanCapacity() {
    int capacity = table.length;
    for (int i = capacity - 1; i >= 2; i--) {
      boolean isPrime = true;
      for (int j = 2; j * j <= i; j++) {
        if (i % j == 0) {
          isPrime = false;
          break;
        }
      }
      if (isPrime) {
        return i;
      }
    }
    return 2; // Default to 2 if no prime is found (should not happen for reasonable table sizes)
  }

  public int getCollisions() {
    return collisions;
  }

  public static void main(String[] args) {
    int[] data = generateRandomData(10000000); // Generate 10 million random numbers
    int[] fillPercentages = { 50, 80, 90, 99, 100 };

    for (int fillPercentage : fillPercentages) {
      int dataSize = (int) (data.length * (fillPercentage / 100.0));
      int[] subset = new int[dataSize];
      System.arraycopy(data, 0, subset, 0, dataSize);

      int tableSize = (int) (subset.length / 0.7); // Adjust the load factor as needed
      System.out.println("Experiment: " + fillPercentage + "% fill rate");

      System.out.println("Linear Probing:");
      YtelseHashtabell linearProbingTable = new YtelseHashtabell(tableSize);
      testHashTable(linearProbingTable, subset, true);

      System.out.println("Double Hashing:");
      YtelseHashtabell doubleHashingTable = new YtelseHashtabell(tableSize);
      testHashTable(doubleHashingTable, subset, false);

      System.out.println();
    }
  }

  public static int[] generateRandomData(int size) {
    int[] data = new int[size];
    Random random = new Random();

    for (int i = 0; i < size; i++) {
      data[i] = random.nextInt(Integer.MAX_VALUE);
    }

    return data;
  }

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
    System.out.println("Time taken: " + elapsedTime + " milliseconds");
    System.out.println(table.search(55));
  }
}
