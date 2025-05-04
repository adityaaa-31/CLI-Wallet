
public class Wallet {
    private double balance;

    public Wallet(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount, String username) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        this.balance += amount;
        System.out.println("Debug: Deposited " + amount + " for " + username + ". New balance: " + this.balance); // Optional debug
    }

    public boolean withdraw(double amount, String username) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (this.balance >= amount) {
            this.balance -= amount;
            System.out.println("Debug: Withdrew " + amount + " from " + username + ". New balance: " + this.balance); // Optional debug
            return true;
        } else {
            System.out.println("Debug: Withdrawal failed for " + username + ". Amount: " + amount + ", Balance: " + this.balance); // Optional debug
            return false;
        }
    }
}
