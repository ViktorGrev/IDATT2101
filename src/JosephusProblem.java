public class JosephusProblem {

    static int findLastPersonStanding(int interval, int soldiers) {
        Enkellenke enkellenke = new Enkellenke();
        for(int i=1; i<soldiers+1;i++){
            enkellenke.settlnnNode(i);
        }

        Node pointer1 = enkellenke.hode;
        Node pointer2 = enkellenke.hode;

        //as long as the node after the current, is not the same as the current
        while(pointer1.neste != pointer1)
        {
            int count = 1;
            while(count != interval)
            {
                pointer2 = pointer1;
                pointer1 = pointer1.neste;
                count++;
            }

            //removing node
            pointer2.neste = pointer1.neste;
            pointer1 = pointer2.neste;
        }
        return pointer1.element;
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
        System.out.println ("Last person left standing " + "(Josephus Position) is " +
            findLastPersonStanding(interval,soldiers));
        System.out.println("Runtime pr round: " + time + " ms");
        System.out.println();
    }

    public static void main(String[] args) {
        runTime(4,10);
        //findPosition(3,40);
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