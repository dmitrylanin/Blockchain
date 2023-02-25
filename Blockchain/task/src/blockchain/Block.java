package blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;

public class Block {
    protected long timeStamp;
    protected int id;
    protected Miner author;
    protected String hashOfPreviousBlock;
    protected String hashOfThisBlock;
    protected Block lastBlock;
    private int magicNumber;
    private ArrayList<Message> messages = new ArrayList<>();

    public Block(long timeStamp,
                 int id,
                 int countZero,
                 Block lastBlock,
                 String hashOfPreviousBlock,
                 Miner author)
    {
        this.timeStamp = timeStamp;
        this.hashOfPreviousBlock = hashOfPreviousBlock;
        this.id = id;
        this.lastBlock = lastBlock;
        this.hashOfThisBlock = selectHashOfThisBlock(id, hashOfPreviousBlock, countZero);
        this.author = author;
    }

    public synchronized String selectHashOfThisBlock(int id, String hashOfPreviousBlock, int countZero){
        String strWithZero = generateZeroStr(countZero);
        Random random = new Random();
        int randInt = random.nextInt();
        String hashOfThisBlock = StringUtil.applySha256(hashOfPreviousBlock + timeStamp + id+randInt);
        while (true){
            if(hashOfThisBlock.substring(0, countZero).equals(strWithZero)){
                this.magicNumber = randInt;
                return hashOfThisBlock;
            }else{
                hashOfThisBlock = StringUtil.applySha256(hashOfPreviousBlock + timeStamp + id+random.nextInt());
            }
        }

    }

    private synchronized String generateZeroStr(int countZero){
        String str = "";
        for (int j=0; j<countZero; j++){
            str +="0";
        }
        return str;
    }

    public void blockToString(){
        System.out.println("Block:");
        System.out.println("Created by " + this.author.minerName);
        System.out.println(this.author.minerName + " gets 100 VC");
        System.out.println("Id: " + this.id);
        System.out.println("Timestamp: "+ this.timeStamp);
        System.out.println("Magic number: " + this.magicNumber);
        System.out.println("Hash of the previous block:");
        System.out.println(hashOfPreviousBlock);
        System.out.println("Hash of the block:");
        System.out.println(hashOfThisBlock);
        System.out.println("Block data:");
        if(id == 1){
            System.out.println("No transactions");
        }else{
            for (int i=0; i<messages.size(); i++){
                System.out.println(messages.get(i).getAuthor().minerName + " sent " + messages.get(i).getPrice() + " VC to " + messages.get(i).getMessage());
            }
        }
    }

    public int getId() {
        return id;
    }

    public Block getLastBlock() {
        return lastBlock;
    }

    public String getHashOfPreviousBlock() {
        return hashOfPreviousBlock;
    }

    public String getHashOfThisBlock() {
        return hashOfThisBlock;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Miner getAuthor() {
        return author;
    }
}

class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
