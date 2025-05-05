import java.util.*;

public class WalletCLI {
    private static final Map<String, User> users = new HashMap<>();
    private static final List<Transaction> transactions = new ArrayList<>();

    private static void registerUser(Scanner scanner) {
        System.out.println("\n--- Register New User ---");
        System.out.print("Enter username: ");

        String usernameInput = scanner.nextLine().toLowerCase();

        if (usernameInput.isEmpty()) {
            System.out.println("Error: Username cannot be empty.");
            return;
        }

        if (users.containsKey(usernameInput)) {
            System.out.println("Error: Username '" + usernameInput + "' already exists.");
        } else {
            User newUser = new User(usernameInput, new Wallet(0));
            users.put(newUser.getUsername(), newUser);
            System.out.println("User '" + newUser.getUsername());
        }
    }

    private static void topUp(Scanner scanner) {
        System.out.println("\n--- Top Up Wallet ---");
        System.out.print("Enter username to top up: ");
        String username = scanner.nextLine().trim().toLowerCase();

        User user = users.get(username);

        if (user == null) {
            System.out.println("Error: User '" + username + "' not found.");
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

    private static void checkBalance(Scanner scanner) {
        System.out.println("\n--- Check Balance ---");
        System.out.print("Enter username to check balance: ");
        String username = scanner.nextLine().trim().toLowerCase();

        User user = users.get(username);

        if (user != null) {
            Wallet userWallet = user.getWallet();
            System.out.println("Balance for user '" + user.getUsername() + "': " + userWallet.getBalance());
        } else {
            System.out.println("Error: User '" + username + "' not found.");
        }
    }

    private static void transferMoney(Scanner scanner) {
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

    private static void viewGlobalHistory() {
        System.out.println("\n--- Global Transaction History ---");
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

    private static void viewUserHistory(Scanner scanner) {
        System.out.println("\n--- User Transaction History ---");
        System.out.print("Enter username to view history: ");
        String username = scanner.nextLine().trim().toLowerCase();

        if (!users.containsKey(username)) {
            System.out.println("Error: User '" + username + "' not found.");
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

        System.out.println("\n--- Transaction History for '" + username + "' ---");
        System.out.println("ID" + "\t" + "Timestamp" + "\t" + "Type" + "\t" + "Amount");
           System.out.println(
                    tx.getId() + "\t" + tx.getTimestamp() + "\t" +  tx.getType() + "\t" +
                            tx.getAmount());
    }

    private static void displayMenu() {
        System.out.println("\n===== Wallet CLI =====");
        System.out.println("1. Register a user");
        System.out.println("2. Top up money into a wallet");
        System.out.println("3. Check balance");
        System.out.println("4. Transfer Money to another wallet ");
        System.out.println("5. View global transaction history");
        System.out.println("6. View user transaction history");
        System.out.println("7. Exit");
        System.out.println("=====================================");
        System.out.print("Please choose an option: ");
    }

    void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                displayMenu();
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1": registerUser(scanner); break;
                    case "2": topUp(scanner); break;
                    case "3": checkBalance(scanner); break;
                    case "4": transferMoney(scanner); break;
                    case "5": viewGlobalHistory(); break;
                    case "6": viewUserHistory(scanner); break;
                    case "7": System.out.println("Exiting!"); return;
                    default: System.out.println("Invalid choice. Please enter a number between 1 and 7."); break;
                }
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        } catch (IllegalStateException e) {
            System.err.println("Scanner was closed unexpectedly. Exiting.");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
