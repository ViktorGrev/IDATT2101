package oppgave6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class BFS {
    int N;
    int K;
    Node2[] node;

    public void ny_ugraf(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        node = new Node2[N];
        for (int i=0;i<N;i++) {
            node[i] = new Node2();
            node[i].number=i;
        }
        K = Integer.parseInt(st.nextToken());
        for (int i=0;i<K;i++){
            st = new StringTokenizer(br.readLine());
            int fra = Integer.parseInt(st.nextToken());
            int til = Integer.parseInt(st.nextToken());
            node[fra].kant1 = new Kant2(node[til],node[fra].kant1);
        }
    }

    public void initForgj(Node2 s){
        for (int i=N;i-->0;){
            node[i].d = new Forgj();
        }
        ((Forgj) s.d).dist = 0;
    }


    public void bfs(Node2 s){
        initForgj(s);
        ArrayDeque<Node2> queue = new ArrayDeque<>();
        queue.add(s);
        while (!queue.isEmpty()){
            Node2 n = queue.getFirst();
            for(Kant2 k = n.kant1;k!=null;k=k.neste){
                Forgj f =  (Forgj) k.til.d;
                if (f.dist == f.uendelig){
                    f.dist = ((Forgj)n.d).dist+1;
                    f.forgj = n;
                    queue.add(k.til);
                }
            }
            queue.removeFirst();
        }
    }

    public void printBFSResults(){
        System.out.println("Node:  Forgj:  Dist:  ");
        for (int i=0;i<N;i++){
            int forgj;
            if(((Forgj)node[i].d).forgj == null){
                forgj = -1;
            }
            else{
                forgj = ((Forgj)node[i].d).forgj.number;
            }
            System.out.println(node[i].number+"      "+forgj+"     "+ ((Forgj)node[i].d).dist);
        }
    }
}

class Forgj {
    int dist;
    Node2 forgj;
    static int uendelig = 1000000000;
    public int finn_dist(){return dist;}
    public Node2 finn_forgj(){return forgj;}
    public Forgj() {
        dist = uendelig;
    }
}

class Node2 {
    Kant2 kant1;
    Object d;
    int number;
}

class Kant2 {
    Kant2 neste;
    Node2 til;
    public Kant2(Node2 n, Kant2 nst){
        til = n;
        neste = nst;
    }
}

class Main1 {
    public static void main(String[] args) {
        BFS bfs1 = new BFS();
        try {
            URL url = new URL("https://www.idi.ntnu.no/emner/idatt2101/uv-graf/ø6g1");
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            bfs1.ny_ugraf(reader);
            bfs1.bfs(bfs1.node[5]);
            bfs1.printBFSResults();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


