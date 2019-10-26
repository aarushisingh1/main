package rims.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import rims.resource.Reservation;
import rims.resource.ReservationList;
import rims.resource.Resource;

/**
 * Handles the taking in of input from the user and passes it to the Parser to translate it into usable commands.
 * Also handles printing of messages for the user's reading.
 */
public class Ui {
    protected Scanner inputScanner;
    protected String input;
    protected int intInput;
    protected String line = "____________________________________________________________________________________________________________________________________________";
    protected String dash = "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";
    protected String hash = "********************************************************************************************************************************************";
    protected String tab = "\t";
    protected ArrayList<String> welcomeMsg = new ArrayList<String>(Arrays.asList("Welcome to RIMS, your Resource & Inventory Management System.",
                                                                                 "How can I help you?"));
    protected ArrayList<String> commands = new ArrayList<String>(Arrays.asList("add - add a new resource to inventory",
                                                                               "delete - delete an existing resource from inventory",
                                                                               "loan - loan out an item from now till your desired future date",
                                                                               "reserve - reserve an item between two future dates",
                                                                               "return - return a loan or reservation",
                                                                               "list - see all resources and current reservations",
                                                                               "\t" + "list /item - see all loans and future reservations of a particular item",
                                                                               "\t" + "list /room - see all loans and future reservations of a particular room"));

    /**
     * Constructor of the Ui. Initializes the scanner to take in user input,
     * and prints the RIMS welcome message.
     */
    public Ui() {
        inputScanner = new Scanner(System.in);
        welcome();
    }

    /**
     * Obtains a new String input from the user.
     * @return the new input typed by the user.
     */
    public String getInput() {
        input = inputScanner.nextLine();
        return input;
    }

    /**
     * Prints a question for the user, before obtaining a new String input in response from the user.
     * @return the new input typed by the user.
     */
    public String getInput(String question) {
        formattedPrint(question);
        input = inputScanner.nextLine();
        return input;
    }

    /**
     * Obtains a new integer input from the user, without requiring conversion from String to integer format.
     * @return the new integer input typed by the user.
     */
    public int getIntegerInput() {
        intInput = inputScanner.nextInt();
        return intInput;
    }

    /**
     * Prints a question for the user, before obtaining a new integer input in response from the user.
     * @return the new integer input typed by the user.
     */
    public int getIntegerInput(String question) {
        formattedPrint(question);
        intInput = inputScanner.nextInt();
        return intInput;
    }

    /**
     * Prints a line of underscores.
     */
    public void printLine() {
        System.out.println(tab + line);
    }

    /**
     * Prints a line of dashes.
     */
    public void printDash() {
        System.out.println(tab + dash);
    }

    /**
     * Prints a desired line for the user to read.
     */
    public void print(String input) {
        System.out.println(tab + input);
    }

    /**
     * Prints an array of lines.
     */
    public void printArray(ArrayList<String> inputs) {
        for (int i = 0; i < inputs.size(); i++) {
            System.out.println("\t" + inputs.get(i));
        }
    }

    /**
     * Prints an empty line.
     */
    public void printEmptyLine() {
        System.out.println();
    }

    /**
     * Prints an array of lines in the standard RIMS format, bordered by lines.
     */
    public void formattedPrintArray(ArrayList<String> inputs) {
        printLine();
        printArray(inputs);
        printLine();
    }

    /**
     * Prints a line in the standard RIMS format, bordered by lines.
     */
    public void formattedPrint(String input) {
        printLine();
        print(input);
        printLine();
    }

    /**
     * Prints the farewell message when RIMS is closed.
     */
    public void farewell() {
        formattedPrint("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a welcome message when RIMS is started up.
     */
    public void welcome() {
        printLogo();
        formattedPrintArray(welcomeMsg);
        formattedPrintArray(commands);
    }

    /**
     * Prints the RIMS logo, as part of the RIMS welcome message.
     */
    public void printLogo() {
        String logo = "\n" +
                tab + "          _____                    _____                    _____                    _____          \n" +
                tab + "         /\\    \\                  /\\    \\                  /\\    \\                  /\\    \\         \n" +
                tab + "        /::\\    \\                /::\\    \\                /::\\____\\                /::\\    \\        \n" +
                tab + "       /::::\\    \\               \\:::\\    \\              /::::|   |               /::::\\    \\       \n" +
                tab + "      /::::::\\    \\               \\:::\\    \\            /:::::|   |              /::::::\\    \\      \n" +
                tab + "     /:::/\\:::\\    \\               \\:::\\    \\          /::::::|   |             /:::/\\:::\\    \\     \n" +
                tab + "    /:::/__\\:::\\    \\               \\:::\\    \\        /:::/|::|   |            /:::/__\\:::\\    \\    \n" +
                tab + "   /::::\\   \\:::\\    \\              /::::\\    \\      /:::/ |::|   |            \\:::\\   \\:::\\    \\   \n" +
                tab + "  /::::::\\   \\:::\\    \\    ____    /::::::\\    \\    /:::/  |::|___|______    ___\\:::\\   \\:::\\    \\  \n" +
                tab + " /:::/\\:::\\   \\:::\\____\\  /\\   \\  /:::/\\:::\\    \\  /:::/   |::::::::\\    \\  /\\   \\:::\\   \\:::\\    \\ \n" +
                tab + "/:::/  \\:::\\   \\:::|    |/::\\   \\/:::/  \\:::\\____\\/:::/    |:::::::::\\____\\/::\\   \\:::\\   \\:::\\____\\\n" +
                tab + "\\::/   |::::\\  /:::|____|\\:::\\  /:::/    \\::/    /\\::/    / ~~~~~/:::/    /\\:::\\   \\:::\\   \\::/    /\n" +
                tab + " \\/____|:::::\\/:::/    /  \\:::\\/:::/    / \\/____/  \\/____/      /:::/    /  \\:::\\   \\:::\\   \\/____/ \n" +
                tab + "       |:::::::::/    /    \\::::::/    /                       /:::/    /    \\:::\\   \\:::\\    \\     \n" +
                tab + "       |::|\\::::/    /      \\::::/____/                       /:::/    /      \\:::\\   \\:::\\____\\    \n" +
                tab + "       |::| \\::/____/        \\:::\\    \\                      /:::/    /        \\:::\\  /:::/    /    \n" +
                tab + "       |::|  ~|               \\:::\\    \\                    /:::/    /          \\:::\\/:::/    /     \n" +
                tab + "       |::|   |                \\:::\\    \\                  /:::/    /            \\::::::/    /      \n" +
                tab + "       \\::|   |                 \\:::\\____\\                /:::/    /              \\::::/    /       \n" +
                tab + "        \\:|   |                  \\::/    /                \\::/    /                \\::/    /        \n" +
                tab + "         \\|___|                   \\/____/                  \\/____/                  \\/____/         \n" +
                tab + "                                                                                                    \n";
        System.out.println(logo);
    }

}