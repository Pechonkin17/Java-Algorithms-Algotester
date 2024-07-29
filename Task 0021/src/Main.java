import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int []a = {1, 2, 5, 10, 20, 50, 100, 200, 500};
        long n = scanner.nextLong();
        int counter = a.length - 1;
        int move = 0;
        while (n != 0) {
            if (n - a[counter] >= 0) {
                n -= a[counter];
                move++;
            } else counter--;
        }
        System.out.println(move);
    }
}