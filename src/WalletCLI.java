import java.util.*;

import static service.UserService.registerUser;
import static service.WalletService.*;

public class WalletCLI {


    private static void displayMenu() {
        System.out.println("\n===== Model.Wallet CLI =====");
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
