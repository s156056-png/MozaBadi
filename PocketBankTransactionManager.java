/*
 * PocketBank Transaction Manager
 * This is my solution for the COMP3203 project in Data Structures and Algorithms.
 * I used a simple array to store the transactions like we did in class.
 * All the sorting and searching algorithms are implemented exactly as taught:
 * Selection Sort for amount, Insertion Sort for ID, Binary Search for ID,
 * and Linear Search for category filter.
 * No fancy stuff, just basic Java with Scanner and Random.
 * I added a flag to track if the list is sorted by ID for the binary search reminder.
 */

import java.util.Scanner;
import java.util.Random;

public class PocketBankTransactionManager {

    // Simple Transaction class as described in the project
    static class Transaction {
        int id;
        String merchant;
        double amount;
        String category;

        Transaction(int id, String merchant, double amount, String category) {
            this.id = id;
            this.merchant = merchant;
            this.amount = amount;
            this.category = category;
        }

        public String toString() {
            return "ID: " + id + " | Merchant: " + merchant + 
                   " | Amount: $" + String.format("%.2f", amount) + 
                   " | Category: " + category;
        }
    }

    private static final int MAX_TRANSACTIONS = 100;
    private static String[] merchants = {"Starbucks", "Amazon", "Steam Games", "Whole Foods", "Shell Gas", "Netflix"};

    // Helper to map merchant to category like the pro tip in the project
    private static String getCategoryForMerchant(String merchant) {
        switch (merchant) {
            case "Starbucks":
                return "Food";
            case "Amazon":
                return "Shopping";
            case "Steam Games":
                return "Entertainment";
            case "Whole Foods":
                return "Groceries";
            case "Shell Gas":
                return "Transport";
            case "Netflix":
                return "Subscriptions";
            default:
                return "Other";
        }
    }

    // Generate random transactions exactly as described
    private static void generateTransactions(Transaction[] transactions, int size, Random rand) {
        int currentID = 100000;
        for (int i = 0; i < size; i++) {
            int id = currentID;
            currentID += rand.nextInt(50) + 1;  // random small increment for unique mostly-increasing IDs
            int mIdx = rand.nextInt(merchants.length);
            String merchant = merchants[mIdx];
            String category = getCategoryForMerchant(merchant);
            // Amount between 1.00 and 500.00 with two decimal places
            int cents = rand.nextInt(50000 - 100 + 1) + 100;
            double amount = cents / 100.0;
            transactions[i] = new Transaction(id, merchant, amount, category);
        }
    }

    // Selection Sort by Amount (Smallest to Largest) - exactly from class
    private static void sortByAmountSelection(Transaction[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j].amount < arr[minIndex].amount) {
                    minIndex = j;
                }
            }
            // swap
            Transaction temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    // Insertion Sort by ID (Numerical Order) - exactly from class
    private static void sortByIDInsertion(Transaction[] arr, int n) {
        for (int i = 1; i < n; i++) {
            Transaction key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j].id > key.id) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Binary Search by ID - assumes list is sorted by ID
    private static int binarySearchByID(Transaction[] arr, int n, int target) {
        int left = 0;
        int right = n - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid].id == target) {
                return mid;
            } else if (arr[mid].id < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    // Linear Search filter by Category - collects into temp array as required
    private static void filterByCategory(Transaction[] arr, int n, String category) {
        Transaction[] matches = new Transaction[n];
        int matchCount = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i].category.equals(category)) {
                matches[matchCount++] = arr[i];
            }
        }
        if (matchCount == 0) {
            System.out.println("No transactions found in category: " + category);
        } else {
            System.out.println("Found " + matchCount + " transactions in " + category + ":");
            for (int i = 0; i < matchCount; i++) {
                System.out.println(matches[i]);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        Transaction[] transactions = new Transaction[MAX_TRANSACTIONS];
        int numTransactions = 0;
        boolean isSortedByID = false;

        System.out.println("Welcome to PocketBank Transaction Manager!");

        while (true) {
            System.out.println("\n--- PocketBank Admin Menu ---");
            System.out.println("1. View All Transactions (Unsorted)");
            System.out.println("2. Sort by Amount: Smallest to Largest (Selection Sort)");
            System.out.println("3. Sort by ID: Numerical Order (Insertion Sort)");
            System.out.println("4. Search for Transaction by ID (Binary Search)");
            System.out.println("5. Filter: Find All Transactions in a Category (Linear Search)");
            System.out.println("6. Randomly generate data for new Transactions (ask user to specify the data size)");
            System.out.println("7. Exit Portal");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // clear the line

            switch (choice) {
                case 1:
                    if (numTransactions == 0) {
                        System.out.println("No transactions yet. Please generate data using option 6 first.");
                    } else {
                        System.out.println("All Transactions (current order):");
                        for (int i = 0; i < numTransactions; i++) {
                            System.out.println(transactions[i]);
                        }
                    }
                    break;

                case 2:
                    if (numTransactions == 0) {
                        System.out.println("No transactions yet. Please generate data using option 6 first.");
                    } else {
                        sortByAmountSelection(transactions, numTransactions);
                        isSortedByID = false;
                        System.out.println("Transactions sorted by Amount (Smallest to Largest) using Selection Sort.");
                    }
                    break;

                case 3:
                    if (numTransactions == 0) {
                        System.out.println("No transactions yet. Please generate data using option 6 first.");
                    } else {
                        sortByIDInsertion(transactions, numTransactions);
                        isSortedByID = true;
                        System.out.println("Transactions sorted by ID (Numerical Order) using Insertion Sort.");
                    }
                    break;

                case 4:
                    if (numTransactions == 0) {
                        System.out.println("No transactions yet. Please generate data using option 6 first.");
                    } else if (!isSortedByID) {
                        System.out.println("List must be sorted by ID first!");
                        System.out.println("Please run Option 3 first.");
                    } else {
                        System.out.print("Enter Transaction ID to search: ");
                        int searchID = scanner.nextInt();
                        int index = binarySearchByID(transactions, numTransactions, searchID);
                        if (index != -1) {
                            System.out.println("Transaction found:");
                            System.out.println(transactions[index]);
                        } else {
                            System.out.println("Transaction with ID " + searchID + " not found.");
                        }
                    }
                    break;

                case 5:
                    if (numTransactions == 0) {
                        System.out.println("No transactions yet. Please generate data using option 6 first.");
                    } else {
                        System.out.print("Enter category to filter (e.g. Food): ");
                        String cat = scanner.nextLine().trim();
                        filterByCategory(transactions, numTransactions, cat);
                    }
                    break;

                case 6:
                    System.out.print("Enter the number of transactions to generate: ");
                    int size = scanner.nextInt();
                    scanner.nextLine();
                    if (size <= 0 || size > MAX_TRANSACTIONS) {
                        System.out.println("Invalid size. Must be between 1 and " + MAX_TRANSACTIONS);
                    } else {
                        generateTransactions(transactions, size, rand);
                        numTransactions = size;
                        isSortedByID = false;
                        System.out.println("Generated " + size + " new transactions.");
                    }
                    break;

                case 7:
                    System.out.println("Exiting PocketBank. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
