package oppgave1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockPicker {

    public static void main(String[] args) {
        printStockAnalysis(20000);
        printStockAnalysis(40000);
        printStockAnalysis(60000);
        printStockAnalysis(80000);
        printStockAnalysis(100000);
    }

    private static ArrayList<Integer> populateRandom(int len){
        ArrayList<Integer> randomArrayList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            int random = (int)(Math.random()*20+1) - 10; //Random number between -10 and 10
            randomArrayList.add(random);
        }
        return randomArrayList;
    }

    private static Information stockPicker(List<Integer> historicalStockChanges){
        int buyDay = 0;
        int sellDay = 0;
        int profit = 0;
        int change;

        Date start = new Date();
        int rounds = 0;
        double time;
        Date end;
        do {
            for (int i = 0; i < historicalStockChanges.size() - 1; i++) {
                change = 0;
                for (int j = i + 1; j < historicalStockChanges.size(); j++) {
                    change += historicalStockChanges.get(j);
                    if (change > profit) {
                        profit = change;
                        buyDay = i + 1;
                        sellDay = j + 1;
                    }
                }
            }
            end = new Date();
            rounds++;
        } while (end.getTime()-start.getTime() < 1000);
        time = (double) (end.getTime()-start.getTime()) / rounds;
        return new Information(buyDay, sellDay, profit, time);
    }

    private static void printStockAnalysis(int days){
        ArrayList<Integer> listWithStockChanges = populateRandom(days);
        Information information = stockPicker(listWithStockChanges);
        System.out.println("Predictions with " + days + " days:");
        System.out.println("Buy: " + information.buyDay + " Sell: " + information.sellDay + " Profit: " + information.profit);
        System.out.println("Time: " + information.time + "ms \n");
    }

    private record Information(int buyDay, int sellDay, int profit, double time){}
}
