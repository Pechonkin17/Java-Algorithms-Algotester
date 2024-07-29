import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < a.length; i++) {
            a[i] = scanner.nextInt();
        }

        System.out.println(max(a, n));

    }

    public static int max(int[] a, int n) {
        int[] f = new int[n];

        int result = 1;

        for (int i = 0; i < n; i++) {
            f[i] = 1;

            for (int j = 0; j < i; j++) {
                if (a[j] < a[i]) {
                    f[i] = Math.max(f[i], 1 + f[j]);
                }
            }
            result = Math.max(result, f[i]);
        }
        return result;
    }
}