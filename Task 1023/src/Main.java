import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long k = scanner.nextLong();
        int[] s = new int[n];
        for (int i = 0; i < s.length; i++) {
            s[i] = scanner.nextInt();
        }
        Arrays.sort(s);

        double left = 0;
        double right = s[s.length - 1];

        while (right - left > 1e-4) {
            double middle = (right + left) / 2;

            if (isPossible(s, k, middle)) left = middle;
            else right = middle;
        }
        System.out.printf("%.6f\n", left);
    }
    public static boolean isPossible(int[] s, long k, double middle) {
        long count = 0;
        for (int i = 0; i < s.length; i++) {
            count += s[i] / middle;
        }
        return count >= k;
    }
}