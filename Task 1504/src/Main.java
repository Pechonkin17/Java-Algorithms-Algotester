import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        long []a = new long[n * 2];
        for (int i = 0; i < a.length; i++) {
            a[i] = scanner.nextInt();
        }
        Arrays.sort(a);
        for (int i = 1; i < a.length; i++) {
            if (i % 2 == 0) {
                continue;
            }
            System.out.print(a[i - 1] + " " + a[i]);
            System.out.println();
        }
    }
}