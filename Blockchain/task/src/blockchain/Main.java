package blockchain;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int countZero = 0;
        Blockchain blockchain = Blockchain.getInstance();
        long current, last = 0;

        try {
            for (int i = 0; i<15; i++){
                long start = System.nanoTime();
                blockchain.generateNewBlock(countZero).blockToString();
                current =  TimeUnit.SECONDS.convert(System.nanoTime()-start, TimeUnit.NANOSECONDS);
                System.out.println("Block was generating for " + current + " seconds");

                if(current-last<1 && countZero<3){
                    countZero++;
                    System.out.println("N was increased to " + countZero + "\n");
                    last = current;
                }else if(current-last>3 || countZero>2){
                    countZero = countZero-1;
                    System.out.println("N was decreased by " + 1 + "\n");
                    last = current;
                }else{
                    System.out.println("N stays the same\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}