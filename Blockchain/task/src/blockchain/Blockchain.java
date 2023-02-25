package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Blockchain {
    private boolean isBlockCreated;
    private Block lastBlock;
    private ArrayList<Message> messages = new ArrayList<>();

    private final ArrayList<Miner> miners = new ArrayList<>();

    private final static AtomicInteger counter = new AtomicInteger();

    private static final Blockchain INSTANCE = new Blockchain();
    private Blockchain(){
        miners.add(new Miner(this, "miner1"));
        miners.add(new Miner(this, "miner2"));
        miners.add(new Miner(this, "miner3"));
    };

    public static Blockchain getInstance() {
        return INSTANCE;
    }

    public Block generateNewBlock(int countZero) throws Exception{
        isBlockCreated = false;
        Thread messagingThread = new Thread(new Messaging(this));
        messagingThread.start();
        long timeStamp = new Date().getTime();

        if(this.lastBlock == null) {
            miners.stream().forEach(x -> x.mining(timeStamp, 1, countZero, "0", null));
        }else{
            miners.stream().forEach(x -> x.mining(timeStamp, this.lastBlock.getId()+1, countZero, this.lastBlock.getHashOfThisBlock(), this.lastBlock));

        }

        ArrayList<FutureTask<Block>> blocks = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();

        IntStream.range(0, miners.size())
                .forEach(x -> blocks.add(new FutureTask<>(miners.get(x))));

        IntStream.range(0, blocks.size())
                .forEach(x -> threads.add(new Thread(blocks.get(x))));

        threads.stream().forEach(x-> x.start());
        Thread.sleep(200);

        while (true){
            Map<Boolean, List<FutureTask<Block>>> blocksStatus = blocks.stream()
                    .collect(Collectors.partitioningBy(x -> x.isDone()));

            if (blocksStatus.get(true).size()==1 && validateBlockchain(blocksStatus.get(true).get(0).get())){
                isBlockCreated = true;
                blocksStatus.get(true).get(0).get().setMessages(messages);
                blocksStatus.get(true).get(0).get().getAuthor().putMoney(100);
                setLastBlock(blocksStatus.get(true).get(0).get());
                threads.stream().forEach(x-> {
                    if (x.isAlive()){
                        try {
                            x.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                        threads.stream().forEach(x-> x=null );
                break;
            };
        }
        //System.out.println("555");
        messagingThread = null;
        return lastBlock;
    }

    public static int getId() {
        return counter.incrementAndGet();
    }

    public void setLastBlock(Block lastBlock) {
        this.lastBlock = lastBlock;
    }

    public boolean validateBlockchain(Block block){
        while (true){
            if(lastBlock == null && block.getId() == 1){
                return true;
            }else if(block.getHashOfPreviousBlock().equals(lastBlock.getHashOfThisBlock())){
                block = block.getLastBlock();
                lastBlock = block.getLastBlock();
            }else{
                return false;
            }
        }
    }

    public synchronized void addMessage(Message message){
        if(!isBlockCreated()) {
            messages.add(message);
        }
    }

    public boolean isBlockCreated() {
        return isBlockCreated;
    }

    public ArrayList<Miner> getMiners() {
        return miners;
    }
}
