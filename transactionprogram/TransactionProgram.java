package transactionprogram;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.xml.bind.DatatypeConverter.parseFloat;

public class TransactionProgram {
    private final LogFileController LogFileController;
    private Scanner scanner;
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            TransactionProgram manager = new TransactionProgram("log.html");
            manager.Run();
        } catch (IOException ex) {
            Logger.getLogger(TransactionProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param logFile : The path of the log
     * @throws IOException
     */
    public TransactionProgram(String logFile) throws IOException {
            LogFileController = new LogFileController(logFile);
    }

	//
    private void Run() throws IOException {
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter in a command (Deposit, Withdraw, Balance, Exit) :");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("balance")) {
                Balance();
            } else if (command.equalsIgnoreCase("deposit")) {
                Deposit();
            } else if (command.equalsIgnoreCase("withdraw")) {
                Withdraw();
            } else if (command.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("The command is invalid, please try again.");
            }
            System.out.println();
        }
    }
	
//    Validate the input string and parse to float, if invalid print out error and return -1
//        if the input is not float print out "Please only input number"
//        if the input is not positive print out "Please only input positive amount"
//        if the input have more tnan 2 decimal digit print out "Please only input decimal digit up to 2"
    private float ValidateInputAmount(String input) {
        try {
            float value = parseFloat(input);
            if (value < 0) {
                System.out.println("Please only input positive amount.");
                return -1;
            }
            int decimalPlace = input.indexOf(".");
            if ((decimalPlace != -1) && (input.length() - (decimalPlace + 1) > 2)) {
                System.out.println("Please only input decimal digit up to 2.");
                return -1;
            }
            return value;
        } catch (NumberFormatException ex) {
            System.out.println("Please only input number.");
            return -1;
        }
    }
	
    void Deposit() throws IOException {
        System.out.println("Please enter an amount to deposit:");
        float value = ValidateInputAmount(scanner.nextLine());
        if (value > 0) {
            LogFileController.Append(value);
            LogFileController.Save();
            System.out.format("Successfully deposit $%.2f%n", value);
        } else {
            Deposit();
        }
    }
    void Withdraw() throws IOException {
        System.out.println("Please enter an amount to withdraw:");
        float value = ValidateInputAmount(scanner.nextLine());
        if (value > 0) {
            LogFileController.Append(-value);
            LogFileController.Save();
            System.out.format("Successfully withdraw $%.2f%n", value);
        } else {
            Withdraw();
        }
    }
    void Balance() {
        System.out.format("The current balance is: $%.2f%n", LogFileController.GetBalance());
    }
}
