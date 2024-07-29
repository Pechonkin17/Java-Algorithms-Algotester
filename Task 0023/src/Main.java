import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        int[] candles = new int[m];

        for (int i = 0; i < m; i++) {
            candles[i] = scanner.nextInt();
        }
        Arrays.sort(candles);

        double left = 0;
        double right = candles[m - 1];

        while (right - left > 1e-7) {
            double mid = (left + right) / 2;
            if (isPossible(candles, n, mid)) {
                left = mid;
            } else {
                right = mid;
            }
        }
        System.out.printf("%.7f\n", left);
    }
    private static boolean isPossible(int[] candles, int n, double atLeast) {
        int count = 0;
        for (int i = 0; i < candles.length; i++) {
            count += candles[i] / atLeast;
        }
        return count >= n;
    }
}