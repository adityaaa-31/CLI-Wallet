import java.time.LocalDateTime;
import java.util.UUID;

enum TransactionType {
    TOPUP,
    TRANSFER
}

public class Transaction {
    private final String id;
    private final TransactionType type;
    private final LocalDateTime timestamp;
    private final double amount;
    private final String sender;
    private final String receiver;

    public Transaction(TransactionType type, double amount, String sender, String receiver) {
        this.type = type;
        this.receiver = receiver;
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}
