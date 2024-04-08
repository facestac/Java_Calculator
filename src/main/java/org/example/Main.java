package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) {

        while (true) {

        String inputExpression = getLine();

        if (inputExpression.equals("q")) System.exit(0);

        if (!checkInputString(inputExpression)) {
            System.out.println("Invalid expression");
            continue;
        }

        inputExpression = replaceUnaryOperations(inputExpression);

        List<String> tokens = getTokensFromString(inputExpression);

        List<String> tokensRPN = RPN(tokens);

        System.out.println("RESULT: " + calculate(tokensRPN));
        }

    }

    static String getLine() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter math expression to calculate");
        System.out.println("Enter 'q' to exit");

        String inputExpression = sc.nextLine();
        inputExpression = inputExpression.replaceAll(" ", "");

        return inputExpression;
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


    static boolean checkInputString(String str) {
        String regexPattern = "^(([+-]?[(]?)*(\\d*\\.?\\d+))[(]*([/*%^+-][(]*[+-]?(\\d*\\.?\\d+)+[)]*)*";

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
//        int i = 0;

//        System.out.println("SIZE: " + tokens.size());

        for (String currentToken : tokens) {
//            String currentToken = tokens.get(i);

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

//            i++;
        }

//            if (i < tokens.size()) opStack.push(tokens.get(i));
//            i++;



        while (!opStack.empty()) {
            tokensRPN.add(opStack.pop());
        }

//        System.out.println("STACK");
//        for (String el : tokensRPN) {
//            System.out.println("el: " + el);
//        }
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
//        int i = 0;

//        System.out.println("SIZE: " + tokens.size());

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