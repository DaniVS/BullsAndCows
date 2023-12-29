package bullscows;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int MAX_CHARACTERS = 36;
    private static final char DEFAULT_SYMBOL = 'Z';
    private static final char[] SYMBOLS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int codeLength = getValidCodeLength(scanner);
        if (codeLength == 0){
            System.out.println("Error: 0 is not a valid length for the code.");
            return;
        }

        int possibleSymbols = getPossibleSymbols(scanner);

        if (possibleSymbols < codeLength){
            System.out.println("Error: it's not possible to generate a code with a length of " + codeLength +
                    " with " + possibleSymbols + "unique symbols.");
            return;
        }

        if (possibleSymbols > 36){
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        }

        char[] secretCode = initSecretCode(codeLength, possibleSymbols);

        System.out.println("Okay, let's start the game!");

        boolean guessed = false;

        int turn = 1;
        Score grade;

        while (!guessed) {
            System.out.println("Turn " + turn + ":");

            String guess = scanner.next();

            grade = calculateGrade(guess, secretCode, codeLength);

            System.out.println(printGrade(grade, codeLength));
            System.out.println();

            if (grade.getBulls() == codeLength) {
                guessed = true;
            }

            turn++;
        }

        System.out.println("Congratulations! You guessed the secret code.");
    }

    private static int getPossibleSymbols(Scanner scanner) {
        System.out.println("Input the number of possible symbols in the code:");
        return scanner.nextInt();
    }

    private static Score calculateGrade(String guess, char[] secretCode, int codeLength) {
        Score grade = new Score(0, 0);

        for (int i = 0; i < codeLength; i++) {
            for (int s = 0; s < codeLength; s++) {
                if (guess.charAt(i) == secretCode[s]) {
                    if (i == s) {
                        grade.increaseBulls();
                    } else {
                        grade.increaseCows();
                    }
                }
            }
        }

        return grade;
    }

    private static char[] initSecretCode(int codeLength, int possibleSymbols) {
        char[] secretCode = getDefaultSecretCode(codeLength);
        char[] useSymbols = getSymbolsToUse(possibleSymbols);

        int idx = 0;
        char currentSymbol;
        do {
            Random random = new Random();

            currentSymbol = useSymbols[random.nextInt(possibleSymbols)];
            if (!alreadyUsed(secretCode, currentSymbol)) {
                secretCode[idx] = currentSymbol;
                idx++;
            }
        } while (!isCompleted(secretCode));

        printCodeIsReadyMessage(secretCode, possibleSymbols);
        return secretCode;
    }

    private static char[] getSymbolsToUse(int possibleSymbols) {
        char[] useThese = new char[possibleSymbols];

        for (int i=0; i < possibleSymbols; i++){
            useThese[i] = SYMBOLS[i];
        }

        return useThese;
    }

    private static int getValidCodeLength(Scanner scanner) {
        System.out.println("Input the length of the secret code:");

        String input = scanner.nextLine();
        int codeLength = 0;

        if (!input.matches("[0-9]+")) {
            System.out.println("\nError: You entered a non number " + input);
            codeLength = 0;
        } else {
            codeLength = Integer.parseInt(input);
        }

        while (codeLength > MAX_CHARACTERS) {
            System.out.println(
                    "Error: can't generate a secret number with a length of " +
                            codeLength + " because there aren't enough unique digits.");
            break;
            //codeLength = scanner.nextInt();
        }

        return codeLength;
    }

    private static char[] getDefaultSecretCode(int codeLength) {
        char[] secretCode = new char[codeLength];

        Arrays.fill(secretCode, DEFAULT_SYMBOL);

        return secretCode;
    }

    private static boolean alreadyUsed(char[] secretCode, char c) {
        for (int i = 0; i < secretCode.length; i++) {
            if (secretCode[i] == c) {
                return true;
            }
        }

        return false;
    }

    private static boolean isCompleted(char[] secretCode) {
        for (char i : secretCode) {
            if (i == DEFAULT_SYMBOL) {
                return false;
            }
        }

        return true;
    }

    private static String printCode(char[] theCode) {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < theCode.length; i++) {
            code.append(theCode[i]);
        }

        return code.toString();
    }

    private static String printGrade(Score grade, int codeLength) {
        StringBuilder msg = new StringBuilder();
        String bull = grade.getBulls() == 1 ? " bull" : " bulls";
        String cow = grade.getCows() == 1 ? " cow" : " cows";

        msg.append("Grade: ")
                .append(grade.getBulls())
                .append(bull);

        if (grade.getBulls() < codeLength) {
            msg.append(" and ")
                    .append(grade.getCows())
                    .append(cow);
        }

        return msg.toString();
    }

    private static void printCodeIsReadyMessage(char[] secretCode, int possibleSymbols) {
        System.out.println("The secret is preapared: " + printAsterisks(secretCode) + " " + printSymbolsRange(possibleSymbols) + ".");
    }

    private static StringBuilder printAsterisks(char[] secretCode) {
        StringBuilder asterisks = new StringBuilder();

        for (int i = 0; i< secretCode.length; i++){
            asterisks.append('*');
        }

        return asterisks;
    }

    private static String printSymbolsRange(int possibleSymbols) {
        String range = "";

        if (possibleSymbols <= 10){
            range = "(" + SYMBOLS[0] + "-" + SYMBOLS[possibleSymbols-1] + ")";
        }

        if (possibleSymbols > 10){
            range = "(" + SYMBOLS[0] + "-" + SYMBOLS[9] + ", " + SYMBOLS[10] + "-" + SYMBOLS[possibleSymbols-1] + ")";
        }

        return range;
    }
}
