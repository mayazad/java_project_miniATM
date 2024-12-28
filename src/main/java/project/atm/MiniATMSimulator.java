package project.atm;

import javax.swing.*;

public class MiniATMSimulator {
    public static void main(String[] args) {
        AccountManager accountManager = new AccountManager();

        String loggedInAccount = null;
        int loginAttempts = 3;
        JFrame frame = new JFrame("Mini ATM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        while (loginAttempts > 0) {
            String enteredAccountNumber = JOptionPane.showInputDialog(frame, "Enter your account number:");
            if (enteredAccountNumber == null) {
                JOptionPane.showMessageDialog(frame, "Cancelled login. Exiting...");
                System.exit(0);
            }
            if (!accountManager.isAccountNumberValid(enteredAccountNumber)) {
                loginAttempts--;
                JOptionPane.showMessageDialog(frame, "Invalid account number. Attempts remaining: " + loginAttempts);
                continue;
            }
            while (loginAttempts > 0) {
                String enteredPin = JOptionPane.showInputDialog(frame, "Enter your PIN:");
                if (enteredPin == null) {
                    JOptionPane.showMessageDialog(frame, "Cancelled login. Exiting...");
                    System.exit(0);
                }
                if (accountManager.validateCredentials(enteredAccountNumber, enteredPin)) {
                    loggedInAccount = enteredAccountNumber;
                    break;
                } else {
                    loginAttempts--;
                    if (loginAttempts > 0) {
                        JOptionPane.showMessageDialog(frame, "Invalid PIN. Attempts remaining: " + loginAttempts);
                    }
                }
            }

            if (loggedInAccount != null) {
                break;
            }
        }

        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(frame, "Too many failed attempts. Exiting...");
            System.exit(0);
        }

        JOptionPane.showMessageDialog(frame, "Login successful!");

        while (true) {
            String[] options = {"Check Balance", "Deposit Money", "Withdraw Money", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                    frame,
                    "Choose an option:",
                    "ATM Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                double balance = accountManager.getBalance(loggedInAccount);
                JOptionPane.showMessageDialog(frame, "Current Balance: $" + balance);
            } else if (choice == 1) {
                String depositInput = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
                if (depositInput != null) {
                    try {
                        double depositAmount = Double.parseDouble(depositInput);
                        if (depositAmount <= 0) {
                            JOptionPane.showMessageDialog(frame, "Invalid deposit amount. Must be positive.");
                        } else {
                            double newBalance = accountManager.getBalance(loggedInAccount) + depositAmount;
                            accountManager.updateBalance(loggedInAccount, newBalance);
                            JOptionPane.showMessageDialog(frame, "Deposit successful!");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a numeric value.");
                    }
                }
            } else if (choice == 2) {
                String withdrawalInput = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
                if (withdrawalInput != null) {
                    try {
                        double withdrawalAmount = Double.parseDouble(withdrawalInput);
                        double currentBalance = accountManager.getBalance(loggedInAccount);
                        if (withdrawalAmount > currentBalance) {
                            JOptionPane.showMessageDialog(frame, "Insufficient funds.");
                        } else if (withdrawalAmount <= 0) {
                            JOptionPane.showMessageDialog(frame, "Invalid withdrawal amount. Must be positive.");
                        } else {
                            accountManager.updateBalance(loggedInAccount, currentBalance - withdrawalAmount);
                            JOptionPane.showMessageDialog(frame, "Withdrawal successful!");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a numeric value.");
                    }
                }
            } else if (choice == 3 || choice == JOptionPane.CLOSED_OPTION) { // Exit
                JOptionPane.showMessageDialog(frame, "Thank you for using the ATM. Goodbye!");
                System.exit(0);
            }
        }
    }
}
