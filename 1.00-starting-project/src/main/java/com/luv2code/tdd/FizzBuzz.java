package com.luv2code.tdd;

public class FizzBuzz {

    private FizzBuzz() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    // If number is divisible by 3, print Fizz
    // If number is divisible by 5, print Buzz
    // If number is divisible by 3 and 5, print FizzBuzz
    // If number is not divisible by 3 and 5, then print the number

    public static String compute(int number) {
        StringBuilder result = new StringBuilder();

        if (number % 3 == 0) {
            result.append("Fizz");
        }

        if (number % 5 == 0) {
            result.append("Buzz");
        }

        if (result.isEmpty()) {
            result.append(number);
        }

        return result.toString();
    }

}
