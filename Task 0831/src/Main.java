import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[] r = new int[m];
        for (int i = 0; i < r.length; i++) {
            r[i] = scanner.nextInt();
        }
        Arrays.sort(r);

        double left = 0;
        double right = r[r.length - 1];
        while (right - left > 1e-9) {
            double middle = (left + right) / 2;

            if (isPossible(r, n, middle)) left = middle;
            else right = middle;
        }
        System.out.println(4.0 * Math.PI * Math.pow(left, 3) * n / 3);
    }

    public static boolean isPossible(int[] r, int n, double middle) {
        int count = 0;
        for (int i = 0; i < r.length; i++) {
            count += r[i] / middle;
        }
        return count >= n;
    }
}