package oppgave2;

import java.util.Date;
import java.util.Objects;

public class Recursion {

    public static void main(String[] args) throws IllegalAccessException {
        printMethodsResult(100, 2);
        printMethodsResult(1000, 10);
        printMethodsResult(5043, 43);
        printMethodsResult(10000, 10);
        printMethodsResult(10001, 10);
    }

    private static double method1(int n, double x){
        if (n == 1) return x;
        return x + method1(n - 1, x);
    }

    private static double method2(int n, double x){
        if((n & 1) == 1) return x + method2((n - 1)/2, x + x);
        else {
            if (n == 0) return 0;
            return method2(n / 2, x + x);
        }
    }

    private static void printMethodsResult(int n, double x) throws IllegalAccessException {
        requirePositive(n, "n has to be a positive integer");
        Information infoMethod1 = method1CheckTime(n, x);
        Information infoMethod2 = method2CheckTime(n, x);
        System.out.println("Method 1 (" + n + " * " + x + " = " + infoMethod1.result + " Time: " + infoMethod1.time);
        System.out.println("Method 2 (" + n + " * " + x + " = " + infoMethod2.result + " Time: " + infoMethod2.time);
    }

    private static Information method1CheckTime(int n, double x) {
        double result;
        Date start = new Date();
        int rounds = 0;
        double time;
        Date end;
        do {
            result = method1(n, x);
            end = new Date();
            rounds++;
        } while (end.getTime()-start.getTime() < 1000);
        time = (double) (end.getTime()-start.getTime()) / rounds;
        return new Information(result, time);
    }

    private static Information method2CheckTime(int n, double x) {
        double result;
        Date start = new Date();
        int rounds = 0;
        double time;
        Date end;
        do {
            result = method2(n , x);
            end = new Date();
            rounds++;
        } while (end.getTime()-start.getTime() < 1000);
        time = (double) (end.getTime()-start.getTime()) / rounds;
        return new Information(result, time);
    }

    private static <T extends Number> void requirePositive(T number, String message) throws IllegalAccessException {
        if (Objects.requireNonNull(number, message).doubleValue() < 0)
            throw new IllegalAccessException(message);
    }

    private record Information(double result, double time){}
}

