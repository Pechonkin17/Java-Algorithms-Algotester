import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long []a = new long[n];
        long res = 0;
        for (int i = 0; i < a.length; i++) {
            a[i] = scanner.nextInt();
            res += a[i] - 1;
        }
        System.out.println(res);
    }
}