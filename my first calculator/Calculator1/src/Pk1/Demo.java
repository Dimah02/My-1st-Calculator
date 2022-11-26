package Pk1;

import java.util.Scanner;

public class Demo {

    public static String postfix = "";

    public static boolean isNum(char c) {
        return (c >= 48 && c <= 57) || (c == 46);
    }

    public static boolean isChar(char c) {
        return c >= 42 && c <= 47;
    }

    // Check if the entered mathematical expression is valid
    public static boolean CheckExp(String exp) {
        if (!isNum(exp.charAt(0)) && exp.charAt(0) != '(') {
            return false;
        }
        /////////////////////////////////////////
        // make sure that brackets are balansed
        int countbrackets = 0;
        for (int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) == '(') {
                countbrackets++;
            } else if (exp.charAt(i) == ')') {
                countbrackets--;
                if (countbrackets < 0) {
                    return false;
                }
            }
            // check if char  
            // not equal 0 1 2 3 4 5 6 7 8 9 / * - + ( )
            if ((!(exp.charAt(i) >= 40 && exp.charAt(i) <= 57)) || exp.charAt(i) == 44) {
                return false;
            }
        }
        if (countbrackets != 0) {
            return false;
        }
        //////////////////////////////////////////
        for (int i = 0; i < exp.length() - 1; i++) {
            // check if somthing like 5+/9 exist
            if ((exp.charAt(i) >= 42 && exp.charAt(i) <= 47) && (exp.charAt(i + 1) >= 42 && exp.charAt(i + 1) <= 47)) {
                return false;
            }

            // check if somthing like 4..2 exist
            if (exp.charAt(i) == '.' && exp.charAt(i + 1) == '.') {
                return false;
            }

            // check if somthing like (6+) exist
            if (isChar(exp.charAt(i)) && exp.charAt(i + 1) == ')') {
                return false;
            }

            // check if somthing like 5( exist
            if (isNum(exp.charAt(i)) && exp.charAt(i + 1) == '(') {
                return false;
            }
        }
        return true;
    }

    // Converting normal expretion to postfix
    public static void process(char c, ArrayBoundedStack<Character> stack) {
        if (stack.isEmpty() || stack.top() == '(') {
            stack.push(c);
        } else if ((c == '*' || c == '/') && (stack.top() == '-' || stack.top() == '+')) {
            stack.push(c);
        } else {
            postfix += stack.top();
            stack.pop();
            process(c, stack);
        }
    }

    public static String ExpressionToPostfix(String exp) {
        ArrayBoundedStack<Character> stack = new ArrayBoundedStack<>(exp.length());
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (isNum(c)) {
                postfix += c;
            } else if (c == '(') {
                stack.push(c);
            } else if (isChar(c)) {
                stack.push(' ');
                process(c, stack);
            } else if (c == ')') {
                while (stack.top() != '(') {
                    postfix += stack.top();
                    stack.pop();
                }
                stack.pop();
            }
        }
        while (!stack.isEmpty()) {
            postfix += stack.top();
            stack.pop();
        }
        return postfix;
    }

    // Evaluate an expression in postfix notation
    // I used a way that i learned in DS class
    public static double EvaluatePostfix(String str) {
        boolean onenum = true;
        for(int i=0;i<str.length();i++){
            if(isChar(str.charAt(i)))
                onenum = false;
        }
        if(onenum)
            return Double.parseDouble(str);
        ArrayBoundedStack<String> stack = new ArrayBoundedStack<>(str.length());
        String a = "";
        double ans = 0;
        for (int i = 0; i < str.length(); i++) {
            if (isNum(str.charAt(i))) {
                a += str.charAt(i);
            } else if (str.charAt(i) == ' ' || isChar(str.charAt(i))) {
                if (!a.equals("")) {
                    stack.push(a);
                }
                a = "";
                if (isChar(str.charAt(i))) {
                    double num2 = Double.parseDouble(stack.top());
                    stack.pop();
                    double num1 = Double.parseDouble(stack.top());
                    stack.pop();
                    switch (str.charAt(i)) {
                        case '*':
                            ans = num1 * num2;
                            break;
                        case '/':
                            ans = num1 / num2;
                            break;
                        case '+':
                            ans = num1 + num2;
                            break;
                        case '-':
                            ans = num1 - num2;
                            break;
                        default:
                            break;
                    }
                    String pushanstostack = String.valueOf(ans);
                    stack.push(pushanstostack);
                }
            }
        }
        return Double.parseDouble(stack.top());
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean terminate = false;
        while (true) {
            System.out.println("Enter your mathematics expression");
            System.out.println("Please make sure that the expression does not contain any spaces.");
            System.out.println("You only can use + - * / ( )");
            System.out.println("Enter X to exit");
            String exp = in.next();
            postfix = "";
            while (!CheckExp(exp)) {
                System.out.println("Expression is not correct, try again or enter X to exit");
                if (exp.equalsIgnoreCase("X")) {
                    return;
                }
                exp = in.next();
            }
            if (exp.equalsIgnoreCase("X")) {
                    return;
                }
            String ans = ExpressionToPostfix(exp);
            double k = EvaluatePostfix(ans);
            System.out.println("answer is: " + k);
        }
    }
}
