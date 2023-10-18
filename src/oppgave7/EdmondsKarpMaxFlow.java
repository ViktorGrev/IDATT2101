package oppgave7;

import java.io.*;
import java.net.*;
import java.util.*;

public class EdmondsKarpMaxFlow {
  private static final int INF = Integer.MAX_VALUE;

  public static int edmondsKarpMaxFlow(int[][] capacity, int source, int sink) {
    int n = capacity.length;
    int[][] flow = new int[n][n];
    int maxFlow = 0;

    while (true) {
      int[] parent = new int[n];
      Arrays.fill(parent, -1);
      int[] pathFlow = new int[n];
      pathFlow[source] = INF;

      Queue<Integer> queue = new LinkedList<>();
      queue.add(source);
      parent[source] = source;

      while (!queue.isEmpty()) {
        int u = queue.poll();

        for (int v = 0; v < n; v++) {
          if (parent[v] == -1 && capacity[u][v] - flow[u][v] > 0) {
            parent[v] = u;
            pathFlow[v] = Math.min(pathFlow[u], capacity[u][v] - flow[u][v]);
            if (v == sink) {
              while (v != source) {
                u = parent[v];
                flow[u][v] += pathFlow[sink];
                flow[v][u] -= pathFlow[sink];
                v = u;
              }
              maxFlow += pathFlow[sink];

              //Printer ut pathen som blir fulgt for en hvis økning og økningen
              System.out.print(pathFlow[sink] + " : ");
              v = sink;
              List<Integer> path = new ArrayList<>();
              while (v != source) {
                u = parent[v];
                path.add(v);
                v = u;
              }
              path.add(source);
              Collections.reverse(path);
              for (int node : path) {
                System.out.print(node + " ");
              }
              System.out.println();

              break;
            }
            queue.add(v);
          }
        }
      }

      if (parent[sink] == -1) {
        break;
      }
    }

    return maxFlow;
  }

  public static void main(String[] args) {
    try {
      //Reading from file via URL
      //Bare endre det siste tallet for å endre fil (0 - 5)
      URL url = new URL("https://www.idi.ntnu.no/emner/idatt2101/v-graf/flytgraf3");
      URLConnection connection = url.openConnection();
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      // Les antall noder og kanter
      String[] nLine = reader.readLine().split(" ");
      int n = Integer.parseInt(nLine[0].trim()); // Antall noder
      int m = Integer.parseInt(nLine[1].trim()); // Antall kanter

      // Opprett kapasitetsmatrisen og initialiser den
      int[][] capacity = new int[n][n];
      for (int i = 0; i < m; i++) {
        String[] edgeInfo = reader.readLine().trim().split("\\s+");
        int fromNode = Integer.parseInt(edgeInfo[0]);
        int toNode = Integer.parseInt(edgeInfo[1]);
        int cap = Integer.parseInt(edgeInfo[2]);
        capacity[fromNode][toNode] = cap;
      }

      reader.close();

      // La brukeren velge startnoden og destinasjonsnoden
      Scanner scanner = new Scanner(System.in);
      System.out.print("Velg startnode (fra 0 til " + (n - 1) + "): ");
      int source = scanner.nextInt();
      System.out.print("Velg destinasjonsnode (fra 0 til " + (n - 1) + "): ");
      int sink = scanner.nextInt();

      // Beregn maksimal flyt ved hjelp av Edmonds-Karp-algoritmen
      int maxFlow = edmondsKarpMaxFlow(capacity, source, sink);

      // Skriv ut resultatet
      System.out.println("Maksimum flyt fra " + source + " til " + sink + " med Edmonds-Karp.");
      System.out.println("Maksimal flyt er " + maxFlow);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
