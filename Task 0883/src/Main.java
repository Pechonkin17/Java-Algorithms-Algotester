import java.util.Scanner;

public class Main {
    static double x, y, x1, y1, x2, y2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        x = scanner.nextDouble();
        y = scanner.nextDouble();

        x1 = scanner.nextDouble();
        y1 = scanner.nextDouble();
        x2 = scanner.nextDouble();
        y2 = scanner.nextDouble();


        System.out.println(solve());
    }
    public static double dist(double x, double y, double xx, double yy) {
        return Math.sqrt(Math.pow(x - xx, 2) + Math.pow(y - yy, 2));
    }
    public static double f(double t) {
        double xt = x1 + (x2 - x1) * t;
        double yt = y1 + (y2 - y1) * t;

        return dist(x, y, xt, yt);
    }

    public static double find_min(double a, double b) {
        while (b - a > 1e-4) {
            double m1 = a + (b - a) / 3.0;
            double m2 = b - (b - a) / 3.0;

            if (f(m1) > f(m2)) {
                a = m1;
            }
            else {
                b = m2;
            }
        }
        return f(a);
    }

    public static double solve() {
        return find_min(0.0, 1.0);
    }
}