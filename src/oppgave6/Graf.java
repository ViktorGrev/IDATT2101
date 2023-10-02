package oppgave6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class Node {
  public int id;
  public ArrayList<Node> kobledeNoder;

  public Node(int id) {
    this.id = id;
    this.kobledeNoder = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public ArrayList<Node> getKobledeNoder() {
    return kobledeNoder;
  }

  public void addNode(Node node) {
    kobledeNoder.add(node);
  }
}

class Graf {
  private int v;
  private Node[] nodes;

  public Graf(int v) {
    this.v = v;
    this.nodes = new Node[v];
    for (int i = 0; i < v; i++) {
      nodes[i] = new Node(i);
    }
  }

  void addEdge(int v, int w) {
    nodes[v].addNode(nodes[w]);
    nodes[w].addNode(nodes[v]);
  }

  void BFS(int targetNode) {
    int[] distance = new int[v];
    for (int i = 0; i < nodes.length; i++) {
      distance[i] = -1; // Initialize all distances as -1 (unreachable)
    }

    Queue<Node> queue = new LinkedList<>();
    queue.add(nodes[targetNode]);
    distance[targetNode] = 0; // Distance to target node is 0

    while (!queue.isEmpty()) {
      Node currentNode = queue.poll();

      ArrayList<Node> connectedNodes = currentNode.getKobledeNoder();
      for (Node n : connectedNodes) {
        if (distance[n.getId()] == -1) {
          distance[n.getId()] = distance[currentNode.getId()] + 1;
          queue.add(n);
        }
      }
    }

    // Print distances to all nodes from the target node
    for (int i = 0; i < v; i++) {
      System.out.println("Distance from Node " + targetNode + " to Node " + i + ": " + distance[i]);
    }
  }

  // ...
  public static void main(String args[]) {
    int numNodes = 0;
    try (BufferedReader br = new BufferedReader(new FileReader("src/oppgave6/textFile.txt"))) {
      String[] edgeInfo;
      String line;
      if ((line = br.readLine()) != null) {
        edgeInfo = line.split(" ");
        numNodes = Integer.parseInt(edgeInfo[0]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Graf graf = new Graf(numNodes); // Initialize Graf with the correct number of nodes

    try (BufferedReader br = new BufferedReader(new FileReader("src/oppgave6/textFile.txt"))) {
      String[] edgeInfo;
      String line;
      boolean firstLine = true; // Skip the first line which contains node and edge counts
      while ((line = br.readLine()) != null) {
        if (firstLine) {
          firstLine = false;
          continue;
        }
        edgeInfo = line.split(" ");
        graf.addEdge(Integer.parseInt(edgeInfo[0]), Integer.parseInt(edgeInfo[1]));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    int targetNode = 5; // Set the target node
    System.out.println("Shortest distances to Node " + targetNode + " from all other nodes:");
    graf.BFS(targetNode);
  }
}
