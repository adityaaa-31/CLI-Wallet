package model;

public class User {
    private final String username;
    private Wallet wallet;

    public User(String username, Wallet wallet) {
        this.username = username;
        this.wallet = wallet;
    }

    public String getUsername() {
        return username;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
