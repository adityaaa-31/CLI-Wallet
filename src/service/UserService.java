package service;

import model.User;
import model.Wallet;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserService {

    protected static final Map<String, User> users = new HashMap<>();

    public static void registerUser(Scanner scanner) {
        System.out.println("\n--- Register New Model.User ---");
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

}
