package blockchain;

import java.util.concurrent.ThreadLocalRandom;

public class Message {
    private Miner author;
    private String message;
    private int price;

    Message(Miner author){
        this.author = author;
        int x = ThreadLocalRandom.current().nextInt(0, 12);
        this.message = messages[x];
        this.price = prices[x];
    }

    private static String [] messages = new String[] {
            "Let It Be!", "Personal Jesus", "I Was Made for Lovin’ You",
            "Knockin’ on Heaven’s Door", "Still Loving You", "Hotel California",
            "Nothing Else Matters", "We Will Rock You", "Smoke on the Water",
            "Smells Like Teen Spirit", "Another One Bites The Dust", "Rock Is Dead"
    };

    private static int [] prices = new int[] {
            10, 20, 30, 40, 50, 25, 30, 1, 2, 4, 15, 12
    };

    public String getMessage() {
        return message;
    }

    public int getPrice() {
        return price;
    }

    public Miner getAuthor() {
        return author;
    }

    public String messageToString(){
        return this.author +": " + this.message;
    }
}
