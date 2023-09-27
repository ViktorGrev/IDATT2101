package oppgave4;

public class JosephusProblem {

    static Node findLastPersonStanding(int interval, int soldiers) {
        Enkellenke enkellenke = new Enkellenke();
        for(int i=1; i<soldiers+1;i++){
            enkellenke.settlnnNode(i);
        }

        Node pointer1 = enkellenke.hode;
        Node pointer2 = enkellenke.hode;

        while(pointer1.neste != pointer1)
        {
            int count = 1;
            while(count != interval)
            {
                pointer2 = pointer1;
                pointer1 = pointer1.neste;
                count++;
            }

            pointer2.neste = pointer1.neste;
            pointer1 = pointer2.neste;
        }
        return pointer1;
    }

    static void runTime(int interval, int soldiers) {
        int rounds = 0;
        long startTime = System.currentTimeMillis();
        long endTime;

        do {
            findLastPersonStanding(interval, soldiers);
            endTime = System.currentTimeMillis();
            rounds++;
        } while (endTime - startTime < 1000);

        double time = (double)(endTime-startTime) / rounds;

        System.out.println("Interval constant: " + interval);
        System.out.println("Number of people in the circle: " + soldiers);
        System.out.println ("Last soldier standing " + "(Josephus Position) is " +
            findLastPersonStanding(interval,soldiers).element);
        System.out.println("Runtime pr round: " + time + " ms");
        System.out.println();
    }
}

class Node {
    public int element;
    public Node neste;
    public Node(int e){
        element = e;
    }
}

class Enkellenke {
    public Node hode = null;
    public Node hale = null;

    public void settlnnNode(int verdi) {
        Node newNode = new Node(verdi);
        if (hode != null) {
            hale.neste = newNode;
        } else hode = newNode;
        hale = newNode;
        hale.neste = hode;
    }
}

class Main {
    public static void main(String[] args) {
        JosephusProblem.runTime(4,10);
        JosephusProblem.runTime(3,40);
        JosephusProblem.runTime(5,100000);
    }
}