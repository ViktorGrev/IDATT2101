package oppgave6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class Topologisk {
    int N;
    int K;
    Node1[] node;

    public void ny_ugraf(BufferedReader br) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stringTokenizer.nextToken());
        node = new Node1[N];
        for (int i=0;i<N;i++) {
            node[i] = new Node1();
            node[i].number=i;
        }
        K = Integer.parseInt(stringTokenizer.nextToken());
        for (int i=0;i<K;i++){
            stringTokenizer = new StringTokenizer(br.readLine());
            int fra = Integer.parseInt(stringTokenizer.nextToken());
            int til = Integer.parseInt(stringTokenizer.nextToken());
            node[fra].kant1 = new Kant(node[til],node[fra].kant1);
        }
    }

    public Node1 df_topo(Node1 n, Node1 l){
        Topo_lst nd = (Topo_lst) n.d;
        if(nd.funnet) return l;
        nd.funnet = true;
        for(Kant k = n.kant1; k!=null; k=k.neste){
            l = df_topo(k.til,l);
        }
        nd.neste = l;
        System.out.println(n.number);
        return n;
    }

    public Node1 topologisort(){
        Node1 l = null;
        for(int i=N;i-->0;){
            node[i].d = new Topo_lst();
        }
        for (int i=N;i-->0;){
            l = df_topo(node[i],l);
        }
        return l;
    }
}

class Topo_lst {
    boolean funnet;
    Node1 neste;
}

class Node1 {
    Kant kant1;
    Object d;
    int number;
}

class Kant {
    Kant neste;
    Node1 til;
    public Kant(Node1 n, Kant nst){
        til = n;
        neste = nst;
    }
}

class Main {
    public static void main(String[] args) {
        Topologisk topologisk1 = new Topologisk();
        Topologisk topologisk2 = new Topologisk();
        try {
            System.out.println("/////////////ø6g5////////////////");
            
            URL url = new URL("https://www.idi.ntnu.no/emner/idatt2101/uv-graf/ø6g5");
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            topologisk1.ny_ugraf(reader);
            topologisk1.topologisort();

            System.out.println("/////////////ø6g7////////////////");

            URL url1 = new URL("https://www.idi.ntnu.no/emner/idatt2101/uv-graf/ø6g7");
            URLConnection connection1 = url1.openConnection();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
            topologisk2.ny_ugraf(reader1);
            topologisk2.topologisort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

