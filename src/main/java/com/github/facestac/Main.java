package com.github.facestac;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

/**
 * Основной класс
 */
public class Main {

    /**
     * Точка входа в программу.
     *
     * @param args входные параметры.
     */
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

    /**
     * Убираем пробелы из строки и заменяем унарные знаки.
     * Вызываем функции для трансформации строки в список строк (как токенов).
     * Вызываем функции преобоазования списка в обратную польскую нотацию и итоговую калькуляцию.
     *
     * @param inputExpression входная строка (арифметическое выражение).
     * @return итоговое вычисленное значение выражения.
     */
    public static double getResultExpression(String inputExpression) {
        inputExpression = inputExpression.replaceAll(" ", "");
        inputExpression = replaceUnaryOperations(inputExpression);

        List<String> tokens = getTokensFromString(inputExpression);
        List<String> tokensRPN = shuntingYard(tokens);

        return calculate(tokensRPN);
    }

    /**
     * Считываем вводимую строку.
     * Если ввели символ 'q' или нажали 'Enter' - выходим из функции
     * и прокидываем наверх соответствующий символ.
     *
     * @param prewResult предыдущий вычисленный результат для конкатенации с текущей строкой.
     * @return возвращаем введенную строку.
     */
    private static String getLine(Double prewResult) {
        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine();

        if (input.equals("q")) return input;
        else if (input.isEmpty()) return "n";

        return (prewResult == 0 ? " " : prewResult.toString()) + input;
    }

    /**
     * Заменяем все вхождения унарных минуса и плюса в исходной строке.
     * Плюс просто удаляем. Минус заменяем на знак '~'.
     *
     * @param str строка для замены.
     * @return возвращаем измененную строку.
     */
    private static String replaceUnaryOperations(String str) {
        String changedStr = str;

        // Убираем унарный плюс
        if (changedStr.charAt(0) == '+') changedStr = changedStr.replaceFirst("\\+", "");
        changedStr = changedStr.replaceAll("\\(\\+", "(");

        // Заменяем унарный минус
        if (changedStr.charAt(0) == '-') changedStr = changedStr.replaceFirst("-", "~");
        changedStr = changedStr.replaceAll("\\(-", "(~");

        return changedStr;
    }


    /**
     * Проверяем входную строку на совпадение и порядок закрывающих и открывающих скобок.
     *
     * @param str строка для проверки.
     * @return возвращаем результат проверки.
     */
    public static boolean checkBracketsMatching(String str) {
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


    /**
     * Задаем шаблон входной строки и вызываем функцию для проверки соответствия.
     *
     * @param str проверяемая строка.
     * @return результат функции поиска совпадений.
     */
    public static boolean checkInputString(String str) {
        String regexPattern = "^(([+-]?[(]?)*(\\d*\\.?\\d+))[()]*([/*%^+-][(]*[+-]?(\\d*\\.?\\d+)[)]*)*";

        return isMatch(str, regexPattern);
    }

    /**
     * Проверяем, соответствует ли строка заданному регулярному выражению.
     *
     * @param str строка для проверки.
     * @param pattern регулярное выражение.
     * @return возвращаем истину или ложь.
     */
    private static boolean isMatch(String str, String pattern) {
        return str.matches(pattern);
    }

    /**
     * Устанавливаем шаблон для поиска и применяем его к строке поиска.
     * Циклично ищем совпадения в строке и добавляем части исходной строки в список (как токены).
     *
     * @param str строка для поиска
     * @return список токенов
     */
    private static List<String> getTokensFromString(String str) {
        List<String> tokens = new ArrayList<>();
        String regex = "[-+/*%^()~]|([0-9]*\\.?[0-9]+)";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(str);

        // циклично ищем совпадения и переходим к ним
        while (matcher.find()) {
            // возвращает подстроку, которая совпала с шаблоном
            tokens.add(matcher.group());
        }

        return tokens;
    }

    /**
     * Функция преобразования списка токенов в обратную польскую нотацию.
     * Алгоритм Дейкстры (Алгоритм сортировочной станции) основан на стеке.
     * В процессе преобразования используется стек арифметических операций,
     * хранящий ещё не добавленные в преобразованный список операции.
     * Последовательно в цикле считываются токены и выстраиваются в новый список,
     * согласно алгоритму с соблюдением приоритетности арифметических операций.
     *
     * @param tokens входной список токенов.
     * @return список токенов в виде обратной польской нотации.
     */
    private static List<String> shuntingYard(List<String> tokens) {
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


    /**
     * Проверяем строку на соответствие вещественному числу.
     * @param str входная строка.
     * @return результат проверки.
     */
    private static boolean isNumber(String str) {
        String regexPattern = "([0-9]*\\.?[0-9]+)";

        return isMatch(str, regexPattern);
    }


    /**
     * Возвращаем приоритет арифметической операции
     * @param str арифметичсекая операция в виде строки
     * @return целое число - приоритет
     */
    private static int getPrecedence(String str) {
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

    /**
     * Функция вычисления выражения из обратной польской нотации.
     * Числа закидываем в стек и вытаскиваем обратно, если встречаем знак операции.
     *
     * @param tokensRPN список токенов в виде RPN.
     * @return возвращаем итоговый результат всего выражения.
     */
    private static double calculate(List<String> tokensRPN) {
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


    /**
     * Список применяемых арифметических операций.
     *
     * @param value1 первое число.
     * @param value2 второе число.
     * @param op знак арифметической операции.
     * @return результат выражения.
     */
    private static double applyArithmeticOperation(double value1, double value2, String op) {

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