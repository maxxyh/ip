package duke.ui;

import java.util.Scanner;

public class Ui {

    private static final String LOGO = "   ___      _      _ __   _              \n" +
            "  /   \\    | |    | '_ \\ | |_     __ _   \n" +
            "  | - |    | |    | .__/ | ' \\   / _` |  \n" +
            "  |_|_|   _|_|_   |_|__  |_||_|  \\__,_|  \n" +
            "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
            "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' ";
    private static final String GREETING = "Hello, Alpha here... welcome to my help centre... again :/\n"
            + "Would you like to explain what you want?\n";

    private static final String GOODBYE = "Bye. Try not to come again please... let me live.\n";
    private static final String LINE_PREFIX = "    ";

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final String DIVIDER = "____________________________________________________________\n";

    private Scanner sc;

    public Ui() {
        sc = new Scanner(System.in);
    }

    public void showWelcomeMessage() {
        System.out.println("Hello from\n" + LOGO);
        outputBlockToUser(GREETING);
    }

    public void showGoodbyeMessage() {
        outputBlockToUser(GOODBYE);
    }

    /** Shows message to user */
    public void outputBlockToUser(String... message) {
        System.out.print(LINE_PREFIX + DIVIDER.replace("\n", LINE_SEPARATOR));
        for (String m : message) {
            System.out.print(LINE_PREFIX + m.replace("\n", LINE_SEPARATOR + LINE_PREFIX));
        }
        System.out.print(DIVIDER.replace("\n", LINE_SEPARATOR));
    }

    public String getUserInput() {
        System.out.print("input command: ");
        return sc.nextLine();
    }

}