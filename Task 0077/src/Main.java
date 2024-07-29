import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long left = 0;
        long right = (long) 1e+10;
        // long middle = (right + left) / 2;
        char n;
//        do {
//            System.out.println(middle);
//            System.out.flush();
//            n = scanner.next().charAt(0);
//
//            if (n == '<') {
//                left = middle - 1;
//            } else if (n == '>') {
//                right = middle + 1;
//            }
//            middle = (right + left) / 2;
//        } while (n != '=');

        while (left <= right) {
            long middle = left + (right - left) / 2;
            System.out.println(middle);
            System.out.flush();

            n = scanner.next().charAt(0);
            System.out.flush();

            if (n == '>') {
                right = middle - 1;
            } else if (n == '<') {
                left = middle + 1;
            } else break;
        }
        scanner.close();
    }
}