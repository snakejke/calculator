package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input = reader.readLine();
            String result = calc(input);
            System.out.println(result);
        } catch (IOException | IllegalArgumentException | UnsupportedOperationException | ArithmeticException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String calc(String input) {
        return Calculator.calculate(input);
    }

    static class Calculator {
        static String calculate(String input) {
            String[] parts = input.split(" ");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, *, /)");
            }

            String operand1 = parts[0];
            String operator = parts[1];
            String operand2 = parts[2];

            if (operator.equals("/") && operand2.equals("0")) {
                throw new ArithmeticException("Деление на ноль");
            }

            boolean isOperand1Roman = isValidRoman(operand1);
            boolean isOperand2Roman = isValidRoman(operand2);
            boolean isOperand1Number = isNumber(operand1);
            boolean isOperand2Number = isNumber(operand2);

            if ((!isOperand1Number && !isOperand1Roman) || (!isOperand2Number && !isOperand2Roman)) {
                throw new IllegalArgumentException("Строка не является математической операцией");
            }

            int a;
            int b;

            if (isOperand1Roman && isOperand2Roman) {
                if (!isValidRoman(operand1) || !isValidRoman(operand2)) {
                    throw new IllegalArgumentException("Операнды должны быть в диапазоне от I до X");
                }
                a = romanToArabic(operand1);
                b = romanToArabic(operand2);
            } else if (isOperand1Number && isOperand2Number) {
                a = Integer.parseInt(operand1);
                b = Integer.parseInt(operand2);
            } else {
                throw new IllegalArgumentException("Используются одновременно разные системы счисления");
            }

            if (!isValidOperator(operator)) {
                throw new IllegalArgumentException("Неподдерживаемая операция: " + operator);
            }

            if (operator.equals("-") && (isOperand1Roman || isOperand2Roman) && a < b) {
                throw new IllegalArgumentException("В римской системе нет отрицательных чисел");
            }

            int result;
            switch (operator) {
                case "+":
                    result = a + b;
                    break;
                case "-":
                    result = a - b;
                    break;
                case "*":
                    result = a * b;
                    break;
                case "/":
                    result = a / b;
                    break;
                default:
                    throw new IllegalArgumentException("Неподдерживаемая операция: " + operator);
            }

            return (isOperand1Roman || isOperand2Roman) ? arabicToRoman(result) : String.valueOf(result);
        }

        static boolean isValidRoman(String input) {
            String validRomanPattern = "^(I|II|III|IV|V|VI|VII|VIII|IX|X)$";
            return Pattern.matches(validRomanPattern, input);
        }

        static int romanToArabic(String input) {
            Map<Character, Integer> romanMap = new HashMap<>();
            romanMap.put('I', 1);
            romanMap.put('V', 5);
            romanMap.put('X', 10);
            romanMap.put('L', 50);
            romanMap.put('C', 100);

            int result = 0;
            int prevValue = 0;

            for (int i = input.length() - 1; i >= 0; i--) {
                int curValue = romanMap.get(input.charAt(i));
                if (curValue < prevValue) {
                    result -= curValue;
                } else {
                    result += curValue;
                }
                prevValue = curValue;
            }

            return result;
        }

        static String arabicToRoman(int num) {
            if (num < 1) {
                throw new IllegalArgumentException("Римскими числа могут быть только положительные");
            }

            String[] romanSymbols = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
            int[] values = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
            StringBuilder result = new StringBuilder();

            for (int i = values.length - 1; i >= 0; i--) {
                while (num >= values[i]) {
                    result.append(romanSymbols[i]);
                    num -= values[i];
                }
            }
            if (result.length() == 0) {
                throw new IllegalArgumentException("Результат операции не может быть представлен римскими цифрами");
            }

            return result.toString();
        }

        static boolean isValidOperator(String operator) {
            return operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/");
        }

        static boolean isNumber(String input) {
            try {
                int num = Integer.parseInt(input);
                if (num < 1 || num > 10) {
                    throw new IllegalArgumentException("Операнды должны быть в диапазоне от 1 до 10");
                }
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
