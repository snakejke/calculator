package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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
                if (parts.length == 1) {
                    throw new IllegalArgumentException("Строка не является математической операцией");
                } else {
                    throw new IllegalArgumentException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, *, /)");
                }
            }

            String operand1 = parts[0];
            String operator = parts[1];
            String operand2 = parts[2];

            if (operator.equals("/") && operand2.equals("0")) {
                throw new ArithmeticException("Деление на ноль");
            }

            boolean isOperand1Roman = isRomanNumeral(operand1);
            boolean isOperand2Roman = isRomanNumeral(operand2);
            boolean isOperand1Number = isNumber(operand1);
            boolean isOperand2Number = isNumber(operand2);

            int a;
            int b;

            if (isOperand1Roman && isOperand2Roman) {
                a = romanToArabic(operand1);
                b = romanToArabic(operand2);
            } else if (isOperand1Number && isOperand2Number) {
                a = Integer.parseInt(operand1);
                b = Integer.parseInt(operand2);
            } else {
                throw new IllegalArgumentException("Операнды должны быть либо оба арабскими числами, либо оба римскими");
            }

            if (!isValidOperator(operator)) {
                throw new IllegalArgumentException("Неподдерживаемая операция: " + operator);
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

        static boolean isRomanNumeral(String input) {
            String validRomanNumerals = "IVXLCD";
            for (char c : input.toCharArray()) {
                if (validRomanNumerals.indexOf(c) == -1) {
                    return false;
                }
            }
            return true;
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
            if (num <= 0) {
                throw new UnsupportedOperationException("В римской системе нет отрицательных чисел");
            }

            String[] romanSymbols = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
            int[] arabicValues = {100, 90, 50, 40, 10, 9, 5, 4, 1};
            StringBuilder result = new StringBuilder();
            int i = 0;

            while (num > 0) {
                if (num >= arabicValues[i]) {
                    result.append(romanSymbols[i]);
                    num -= arabicValues[i];
                } else {
                    i++;
                }
            }

            return result.toString();
        }

        static boolean isValidOperator(String operator) {
            return operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/");
        }

        static boolean isNumber(String input) {
            try {
                int num = Integer.parseInt(input);
                return num >= 1 && num <= 10;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
