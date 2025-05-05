package service;

import model.Transaction;
import model.TransactionType;
import model.User;
import model.Wallet;

import java.util.*;

import static service.UserService.users;

public class WalletService {
    private static final List<Transaction> transactions = new ArrayList<>();

    public static void topUp(Scanner scanner) {
        System.out.println("\n--- Top Up Model.Wallet ---");
        System.out.print("Enter username to top up: ");
        String username = scanner.nextLine().trim().toLowerCase();

        User user = users.get(username);

        if (user == null) {
            System.out.println("Error: Model.User '" + username + "' not found.");
            return;
        }

        double amount = 0;

        System.out.print("Enter amount to top up for '" + username + "': ");
        String amountInput = scanner.nextLine();
        amount = Double.parseDouble(amountInput);

        if (amount <= 0) {
            System.out.println("Error: Top-up amount must be positive.");
        }


        Wallet userWallet = user.getWallet();
        userWallet.setBalance(amount);
        Transaction tx = new Transaction(TransactionType.TOPUP, amount, user.getUsername(), user.getUsername());
        transactions.add(tx);

        System.out.println("Successfully topped up " + amount + " for '" + user.getUsername() + "'.");
        System.out.println("New balance for '" + user.getUsername() + "': " + amount);

    }

    public static void checkBalance(Scanner scanner) {
        System.out.println("\n--- Check Balance ---");
        System.out.print("Enter username to check balance: ");
        String username = scanner.nextLine().trim().toLowerCase();

        User user = users.get(username);

        if (user != null) {
            Wallet userWallet = user.getWallet();
            System.out.println("Balance for user '" + user.getUsername() + "': " + userWallet.getBalance());
        } else {
            System.out.println("Error: Model.User '" + username + "' not found.");
        }
    }

    public static void transferMoney(Scanner scanner) {
        System.out.println("\n--- Transfer Money ---");
        System.out.print("Enter your username (sender): ");
        String senderUsername = scanner.nextLine().trim().toLowerCase();
        User senderUser = users.get(senderUsername);
        if (senderUser == null) {
            System.out.println("Error: Sender '" + senderUsername + "' not found.");
            return;
        }
        Wallet senderUserWallet = senderUser.getWallet();

        System.out.print("Enter recipient's username: ");
        String receiverUsername = scanner.nextLine().trim().toLowerCase();
        User receiverUser = users.get(receiverUsername);
        if (receiverUser == null) {
            System.out.println("Error: Receiver '" + receiverUsername + "' not found.");
            return;
        }
        Wallet receiverUserWallet = receiverUser.getWallet();

        if (senderUsername.equals(receiverUsername)) {
            System.out.println("Error: Cannot transfer money to yourself.");
            return;
        }

        double amount;
        System.out.print("Enter amount to transfer from '" + senderUsername + "' to '" + receiverUsername + "': ");

        String amountStr = scanner.nextLine();
        amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            System.out.println("Error: Transfer amount must be positive.");
        }

        if (senderUserWallet.getBalance() < amount) {
            System.out.println("Error: Insufficient funds for '" + senderUsername + "'. Current balance: " + senderUserWallet.getBalance());
            return;
        }

        boolean withdrawalSuccess = senderUserWallet.withdraw(amount, senderUsername);

        if(withdrawalSuccess) {
            receiverUserWallet.deposit(amount, receiverUsername);

            Transaction tx = new Transaction(TransactionType.TRANSFER, amount, senderUsername, receiverUsername);
            transactions.add(tx);
        }

        System.out.println("\nSuccessfully transferred " + amount + " from '" + senderUsername + "' to '" + receiverUsername + "'.");
        System.out.println("'" + senderUsername + "' new balance: " + senderUserWallet.getBalance());
        System.out.println("'" + receiverUsername + "' new balance: " + receiverUserWallet.getBalance());
    }

    public static void viewGlobalHistory() {
        System.out.println("\n--- Global Model.Transaction History ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            return;
        }

        System.out.println("ID" + "\t" + "Timestamp" + "\t" + "Type" + "\t" + "Amount");
        for (Transaction tx : transactions) {
            System.out.println(
                    tx.getId() + "\t" + tx.getTimestamp() + "\t" +  tx.getType() + "\t" +
                            tx.getAmount());
        }
        System.out.println("-".repeat(100));
    }

    public static void viewUserHistory(Scanner scanner) {
        System.out.println("\n--- Model.User Model.Transaction History ---");
        System.out.print("Enter username to view history: ");
        String username = scanner.nextLine().trim().toLowerCase();

        if (!users.containsKey(username)) {
            System.out.println("Error: Model.User '" + username + "' not found.");
            return;
        }

        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (tx.getType() == TransactionType.TOPUP && tx.getSender().equals(username)) {
                userTransactions.add(tx);
            } else if (tx.getType() == TransactionType.TRANSFER) {
                if (tx.getSender().equals(username) || tx.getReceiver().equals(username)) {
                    userTransactions.add(tx);
                }
            }
        }

        if (userTransactions.isEmpty()) {
            System.out.println("No transactions found involving user '" + username + "'.");
            return;
        }

        System.out.println("\n--- Model.Transaction History for '" + username + "' ---");
        System.out.println("ID" + "\t" + "Timestamp" + "\t" + "Type" + "\t" + "Amount");

        for(Transaction tx: userTransactions) {
            System.out.println(
                    tx.getId() + "\t" + tx.getTimestamp() + "\t" +  tx.getType() + "\t" +
                            tx.getAmount());
        }

    }
}
