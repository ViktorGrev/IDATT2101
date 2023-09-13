public class JosephusProblem {

    static int findPosition(int intervallConstant, int numberOfPeople) {
        Enkellenke enkellenke = new Enkellenke();
        for(int i=1; i<numberOfPeople+1;i++){
            enkellenke.settlnnNode(i);
        }

        Node ptr1 = enkellenke.hode;
        Node ptr2 = enkellenke.hode;

        //as long as the node after the current, is not the same as the current
        while(ptr1.neste != ptr1)
        {
            int count = 1;
            while(count != intervallConstant)
            {
                ptr2 = ptr1;
                ptr1 = ptr1.neste;
                count++;
            }

            //removing node
            ptr2.neste = ptr1.neste;
            ptr1 = ptr2.neste;
        }
        return ptr1.element;
    }

    static void runTime(int intervalConstant, int numberOfPeople) {
        int rounds = 0;
        long startTime = System.currentTimeMillis();
        long endTime;

        do {
            findPosition(intervalConstant, numberOfPeople);
            endTime = System.currentTimeMillis();
            rounds++;
        } while (endTime - startTime < 1000);

        double time = (double)(endTime-startTime) / rounds;

        System.out.println("Interval constant: " + intervalConstant);
        System.out.println("Number of people in the circle: " + numberOfPeople);
        System.out.println ("Last person left standing " + "(Josephus Position) is " +
            findPosition(intervalConstant,numberOfPeople));
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