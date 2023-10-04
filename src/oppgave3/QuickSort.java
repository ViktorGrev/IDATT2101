package oppgave3;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class QuickSort {

    public static void bytt(int[] tab, int i, int j) {
        int k = tab[j];
        tab[j] = tab[i];
        tab[i] = k;
    }

    public static int median3sort(int[] tab, int v, int h) {
        int m = (v + h) / 2;
        if (tab[v] > tab[m]) bytt(tab, v, m);
        if (tab[m] > tab[h]) {
            bytt(tab, m, h);
            if (tab[v] > tab[m]) bytt(tab, v, m);
        }
        return m;
    }

    public static int splitt(int[] tab, int v, int h) {
        int iv, ih;
        int m = median3sort(tab, v, h);
        int dv = tab[m];
        bytt(tab, m, h - 1);
        for (iv = v, ih = h - 1; ; ) {
            while (tab[++iv] < dv) ;
            while (tab[--ih] > dv) ;
            if (iv >= ih) break;
            bytt(tab, iv, ih);
        }
        bytt(tab, iv, h - 1);
        return iv;
    }

    public static void quickSort(int[] tab, int v, int h) {
        if (h - v > 2) {
            int partpos = splitt(tab, v, h);
            quickSort(tab, v, partpos - 1);
            quickSort(tab, partpos + 1, h);
        } else median3sort(tab, v, h);
    }

    public static void helpedQuickSort(int[] tab, int v, int h) {
        if (h - v > 400) {
            int partpos = splitt(tab, v, h);
            helpedQuickSort(tab, v, partpos - 1);
            helpedQuickSort(tab, partpos + 1, h);
        } else shellsort(tab, v, h);
    }

    public static void shellsort(int[] t, int v, int h) {
        int s = (h - v) / 2;
        while (s > 0) {
            for (int i = s + v; i < h + 1; i++) {
                int j = i, flytt = t[i];
                while (j >= s + v && flytt < t[j - s]) {
                    t[j] = t[j - s];
                    j -= s;
                }
                t[j] = flytt;
            }
            s = (s == 2) ? 1 : (int) (s / 2.2);
        }
    }

    public static boolean testSortedCorrectly(int[] tab) {
        for (int i = 0; i < tab.length - 1; i++) {
            if (tab[i + 1] < tab[i]) {
                return false;
            }
        }
        return true;
    }

    public static Long sum(int[] tab) {
        long sum = 0L;
        for (int i = 0; i < tab.length; i++) {
            sum += tab[i];
        }
        return sum;
    }

    private static int[] createRandomIntegerArray() {
        Random rand = new Random();
        int[] numbers = new int[1000000];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = rand.nextInt(10000);
        }
        return numbers;
    }

    private static int[] createDuplicateIntegerArray() {
        Random rand = new Random();
        int[] numbers = new int[1000000];
        for (int i = 0; i < numbers.length; i += 2) {
            numbers[i] = rand.nextInt(10000);
            numbers[i + 1] = 42;
        }
        return numbers;
    }


    public static void main(String[] args) {
        System.out.println("---- Resultater Sort-metode ----");

        int[] arrRandom1 = createRandomIntegerArray();
        int[] arrRandom2 = Arrays.copyOf(arrRandom1, arrRandom1.length);
        int[] arrDup = createDuplicateIntegerArray();
        int[] arrDup2 = Arrays.copyOf(arrDup, arrDup.length);
        Long[] sumsBefore = {sum(arrRandom1), sum(arrDup)};

        //First
        Date start1 = new Date();
        double tid1;
        Date slutt1;
        quickSort(arrRandom1, 0, arrRandom1.length - 1);
        slutt1 = new Date();
        tid1 = slutt1.getTime() - start1.getTime();
        System.out.println("Sortert liste");
        Date start4 = new Date();
        double tid4;
        Date slutt4;
        helpedQuickSort(arrRandom2, 0, arrRandom1.length - 1);
        slutt4 = new Date();
        tid4 = slutt4.getTime() - start4.getTime();
        System.out.println("Tid brukt i ms med den random listen: " + tid1);
        System.out.println("Tid brukt i ms med den random listen med hjelpe metode: " + tid4);

        //Second
        Date start2 = new Date();
        double tid2;
        Date slutt2;
        quickSort(arrDup, 0, arrDup.length - 1);
        slutt2 = new Date();
        tid2 = slutt2.getTime() - start2.getTime();
        Date start5 = new Date();
        double tid5;
        Date slutt5;
        helpedQuickSort(arrDup2, 0, arrDup2.length - 1);
        slutt5 = new Date();
        tid5 = slutt5.getTime() - start5.getTime();
        System.out.println("Tid brukt i ms med listen med mange duplikater: " + tid2);
        System.out.println("Tid brukt i ms med listen med mange duplikater med hjelpemetode: " + tid5);

        //Third
        Date start3 = new Date();
        int[] arrSortedRandom = Arrays.copyOf(arrRandom1, arrRandom1.length);
        double tid3;
        Date slutt3;
        quickSort(arrSortedRandom, 0, arrSortedRandom.length - 1);
        slutt3 = new Date();
        tid3 = slutt3.getTime() - start3.getTime();
        Date start6 = new Date();
        double tid6;
        Date slutt6;
        helpedQuickSort(arrSortedRandom, 0, arrSortedRandom.length - 1);
        slutt6 = new Date();
        tid6 = slutt6.getTime() - start6.getTime();
        System.out.println("Tid brukt i ms med den sorterte randome listen: " + tid3);
        System.out.println("Tid brukt i ms med den sorterte randome listen med hjelpe metode: " + tid6);

        Long[] sumsAfter = {sum(arrRandom1), sum(arrDup)};

        System.out.println("First sorted correctly: " + testSortedCorrectly(arrRandom1) + "\n" +
                "First sorted correctly with help method: " + testSortedCorrectly(arrRandom2) + "\n" +
                "Second sorted correctly: " + testSortedCorrectly(arrDup) + "\n" +
                "Second sorted correctly with help method: " + testSortedCorrectly(arrDup) + "\n" +
                "Third sorted correctly: " + testSortedCorrectly(arrSortedRandom) + "\n" +
                "Third sorted correctly with help method: " + testSortedCorrectly(arrSortedRandom) + "\n" +
                "Sum of arrays before sort " + Arrays.toString(sumsBefore) + "\n" +
                "Sum of arrays after sort " + Arrays.toString(sumsAfter));
    }
}

