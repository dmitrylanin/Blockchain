package blockchain;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Miner implements Callable<Block> {
    protected Blockchain INSTANCE;
    protected String minerName;
    private AtomicInteger balance;
    private BlockParams blockParams;

    Miner(Blockchain INSTANCE,  String minerName){
        this.INSTANCE = INSTANCE;
        this.balance = new AtomicInteger(0);
        this.minerName = minerName;
    }


    public void mining(long timeStamp, int id, int countZero, String hashOfPreviousBlock, Block lastBlock){
        this.blockParams = new BlockParams(timeStamp, id, countZero, hashOfPreviousBlock, lastBlock);
    }

    @Override
    public Block call() throws Exception {
        Thread.sleep(ThreadLocalRandom.current().nextInt(200, 220));
        Block block = new Block(blockParams.timeStamp,
                blockParams.id,
                blockParams.countZero,
                blockParams.lastBlock,
                blockParams.hashOfPreviousBlock,
                this);

        blockParams = null;
        return block;
    }

    public boolean withdrawMoney(int money){
        int newBalance = this.balance.get()-money;
        if(newBalance>=0){
            this.balance.set(this.balance.get()-money);
            return true;
        }
        return false;
    }

    public void putMoney(int money){
        this.balance.set(this.balance.get()+money);
    }

    static class BlockParams{
        protected long timeStamp;
        protected int id;
        protected int countZero;
        protected String hashOfPreviousBlock;
        protected Block lastBlock;

        public BlockParams(long timeStamp, int id, int countZero, String hashOfPreviousBlock, Block lastBlock) {
            this.timeStamp = timeStamp;
            this.id = id;
            this.countZero = countZero;
            this.hashOfPreviousBlock = hashOfPreviousBlock;
            this.lastBlock = lastBlock;
        }
    }
}