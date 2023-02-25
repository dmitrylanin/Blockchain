package blockchain;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;

public class Messaging implements Runnable{
    Blockchain blockchain;

    Messaging(Blockchain blockchain){
        this.blockchain = blockchain;
    }

    public void run(){
        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

        while (!blockchain.isBlockCreated()){
            executor.submit(() -> {
                Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
                Message message = generateMessage(blockchain.getMiners().get(ThreadLocalRandom.current().nextInt(0, blockchain.getMiners().size())));
                if(message != null){
                    blockchain.addMessage(message);
                }
                return true;
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdownNow();
        executor = null;
    }

    private synchronized Message generateMessage(Miner miner){
        Message message = new Message(miner);
        if(miner.withdrawMoney(message.getPrice())){
            return message;
        }
        return null;
    }
}
