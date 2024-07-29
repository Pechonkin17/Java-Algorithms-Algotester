import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int x = scanner.nextInt();

        int m, result = 0;
        int[] p = new int[13];

        while (n-- > 0) {
            m = scanner.nextInt();
            p[m]++;
            if (m >= 10) result++;
        }

        for (int i = 9; i > 0; i--) {
            while (p[i]-- > 0) {
                if (x >= 10 - i) {
                    x -= 10 - i;
                    result++;
                }
            }
        }
        System.out.println(result);
    }
}