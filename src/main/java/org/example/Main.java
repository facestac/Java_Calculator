package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        System.out.println("Enter math expression to calculate");
        System.out.println("Enter 'q' to exit");
        System.out.println("Press 'Enter' to reset expression");

        double result = 0;

        while (true) {

            String inputExpression = getLine(result).replaceAll(" ", "");

            if (inputExpression.equals("q")) System.exit(0);
            else if (inputExpression.equals("n")) {
                result = 0;
                continue;
            }

            if (!checkInputString(inputExpression) || !checkBracketsMatching(inputExpression)) {
                System.out.println("Invalid expression");
                continue;
            }

            result = getResultExpression(inputExpression);

            System.out.print(result);
        }

    }

    static double getResultExpression(String inputExpression) {
        inputExpression = inputExpression.replaceAll(" ", "");
        inputExpression = replaceUnaryOperations(inputExpression);

        List<String> tokens = getTokensFromString(inputExpression);
        List<String> tokensRPN = RPN(tokens);

        return calculate(tokensRPN);
    }

    static String getLine(Double prewResult) {
        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine();

        if (input.equals("q")) return input;
        else if (input.isEmpty()) return "n";

        return (prewResult == 0 ? " " : prewResult.toString()) + input;
    }

    static String replaceUnaryOperations(String str) {
        String changedStr = str;

        // Replace unary plus
        if (changedStr.charAt(0) == '+') changedStr = changedStr.replaceFirst("\\+", "");
        changedStr = changedStr.replaceAll("\\(\\+", "(");

        // replace unary minus
        if (changedStr.charAt(0) == '-') changedStr = changedStr.replaceFirst("-", "~");
        changedStr = changedStr.replaceAll("\\(-", "(~");

        return changedStr;
    }

    static boolean checkBracketsMatching(String str) {
        Stack<Character> chStack = new Stack<>();

        for (char ch : str.toCharArray()) {
            if (ch == ')') {
                if (chStack.empty()) return false;
                else chStack.pop();
            }

            if (ch == '(')
                chStack.push(ch);
        }

        return chStack.empty();
    }


    static boolean checkInputString(String str) {
        String regexPattern = "^(([+-]?[(]?)*(\\d*\\.?\\d+))[()]*([/*%^+-][(]*[+-]?(\\d*\\.?\\d+)[)]*)*";

        return isMatch(str, regexPattern);
    }

    static boolean isMatch(String str, String pattern) {
        return str.matches(pattern);
    }

    static List<String> getTokensFromString(String str) {
        List<String> tokens = new ArrayList<>();
        String regex = "[-+/*%^()~]|([0-9]*\\.?[0-9]+)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        return tokens;
    }

    static List<String> RPN(List<String> tokens) {
        List<String> tokensRPN = new ArrayList<>();

        Stack<String> opStack = new Stack<>();

        for (String currentToken : tokens) {
            if (isNumber(currentToken)) {
                tokensRPN.add(currentToken);
            } else if (currentToken.equals("(")) {
                opStack.push(currentToken);
            } else {
                boolean isCloseBracket = currentToken.equals(")");
                String topStackElement = opStack.isEmpty() ? "" : opStack.peek();
                int currentPrecedence = getPrecedence(currentToken);

                while (!tokensRPN.isEmpty() && !opStack.empty() &&
                        ((isCloseBracket && !topStackElement.equals("(")) ||
                                (!isCloseBracket &&
                                        (currentPrecedence > 2 ? currentPrecedence < getPrecedence(topStackElement) :
                                                currentPrecedence <= getPrecedence(topStackElement))))) {
                    tokensRPN.add(opStack.pop());
                    topStackElement = opStack.isEmpty() ? "" : opStack.peek();
                }

                if (isCloseBracket && topStackElement.equals("("))
                    opStack.pop();

                if (!isCloseBracket) opStack.push(currentToken);
            }
        }

        while (!opStack.empty()) {
            tokensRPN.add(opStack.pop());
        }

        return tokensRPN;
    }


    static boolean isNumber(String str) {
        String regexPattern = "([0-9]*\\.?[0-9]+)";

        return isMatch(str, regexPattern);
    }


    static int getPrecedence(String str) {
        int precedence = 0;

        if (str.equals("(")) {
            precedence = -2;
        } else if (str.equals(")")) {
            precedence = -1;
        } else if ("+-".contains(str)) {
            precedence = 1;
        } else if ("*/%".contains(str)) {
            precedence = 2;
        } else if (str.equals("^")) {
            precedence = 3;
        } else if (str.equals("~")) {
            precedence = 4;
        }

        return precedence;
    }


    static double calculate(List<String> tokensRPN) {
        Stack<Double> dblStack = new Stack<>();

        for (String stringValue : tokensRPN) {
            double doubleValue = 0;

            if (isNumber(stringValue)) {
                doubleValue = Double.parseDouble(stringValue);
            } else if ("+-*/%^".contains(stringValue)) {
                double value2 = dblStack.pop();
                double value1 = dblStack.pop();
                doubleValue = applyArithmeticOperation(value1, value2, stringValue);
            } else if (stringValue.equals("~")) {
                doubleValue = -dblStack.pop();
            }

            dblStack.push(doubleValue);
        }

        return dblStack.pop();
    }

    static double applyArithmeticOperation(double value1, double value2, String op) {

        return switch (op) {
            case "+" -> value1 + value2;
            case "-" -> value1 - value2;
            case "*" -> value1 * value2;
            case "/" -> value1 / value2;
            case "%" -> value1 % value2;
            case "^" -> Math.pow(value1, value2);
            default -> 0;
        };
    }
}