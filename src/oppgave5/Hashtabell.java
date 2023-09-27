package oppgave5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class Hashtabell {
    private final int size;
    Node[] table;
    int collisions;
    private int itemCount;

    public Hashtabell(int size){
        this.size = size;
        this.table = new Node[size];
        this.itemCount = 0;
    }

    public int divhash(int k){
        return k % size;
    }

    public void add(int h, Node newNode){
        if(table[h] != null){
            System.out.println("Collision. New person name: " + newNode.value + ", Old person name: " + table[h].value);
            newNode.next = table[h];
            table[h].next = null;
            collisions++;
            itemCount--;
        }
        table[h] = newNode;
        itemCount++;
    }

    public boolean find(String name){
        int hashed = stringHash(name);

        if(table[hashed] == null) return false;
        Node thisNode = table[hashed];

        for(int i = 0;i<table.length-hashed;i++){

            if(table[hashed+i] == null) continue;
            while(table[hashed+i].next != null){
                if (thisNode.value.equals(name)) return true;
                thisNode = thisNode.next;
            }

        }
        return false;
    }

    public int stringHash(String s) {
        int sum=0;
        char[] arr = s.toCharArray();
        for(int i = 1; i < arr.length+1; i++){
            int ascii = arr[i-1];
            sum += (ascii*(Math.pow(7,i)))%1000;
        }
        return divhash(sum);
    }

    public int getCollisions() {
        return collisions;
    }

    public int getSize() {
        return size;
    }

    public int getItemCount(){
        return itemCount;
    }
}

class Node {
    String value;
    int key;
    Node next;

    public Node(String value, int key, Node n){
        this.value = value;
        this.key = key;
        next = n;
    }
}

class Main {
    public static void main(String[] args) {
        int size = 101;
        Hashtabell ht = new Hashtabell(size);
        try {
            URL url = new URL("https://www.idi.ntnu.no/emner/idatt2101/hash/navn.txt");
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            int quantityPersons = 0;
            while((line = reader.readLine()) != null){
                Node newNode = new Node(line,0,null);
                ht.add(ht.stringHash(line), newNode);
                quantityPersons++;
            }
            System.out.println("Antall kollisjoner: " + ht.getCollisions());
            System.out.println("Lastfaktor: " + ((double)ht.getItemCount() / size));
            System.out.println("Antall kollisjoner per person: " + (ht.getCollisions()/(double)quantityPersons));
            //Testing the find method to with our own names
            System.out.println(ht.find("Viktor Gunnar Grevskott"));
            System.out.println(ht.find("Julia Vik Remøy"));
            System.out.println(ht.find("Victor Ekholt Gunrell Kaste"));
            System.out.println(ht.find("Melissa Visnjic"));
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
