package oppgave6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        Topologisk topologisk = new Topologisk();
        try {
            BufferedReader br1 = new BufferedReader(new FileReader("C:\\IntelliJ\\testProsjekter\\IDATT2101_Gruppe\\out\\production\\IDATT2101_Gruppe\\viktor\\txtFiles\\nodes.txt"));
            topologisk.ny_ugraf(br1);
            topologisk.topologisort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

