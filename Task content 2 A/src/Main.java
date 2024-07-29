import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int[] x = new int[N];
        int[] y = new int[N];

        for (int i = 0; i < N; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }

        double centerX = (minValue(x) + maxValue(x)) / 2.0;
        double centerY = (minValue(y) + maxValue(y)) / 2.0;

        double step = 0.001; // Розмір кроку

        double minDistance = calculateDistance(x, y, centerX, centerY);

        while (step > 1e-7) {
            double oldDistance = minDistance;

            double dx = 0.0;
            double dy = 0.0;

            for (int i = 0; i < N; i++) {
                double dist = Math.sqrt((centerX - x[i]) * (centerX - x[i]) + (centerY - y[i]) * (centerY - y[i]));
                dx += (centerX - x[i]) / dist;
                dy += (centerY - y[i]) / dist;
            }

            centerX -= step * dx;
            centerY -= step * dy;
            minDistance = calculateDistance(x, y, centerX, centerY);

            if (minDistance >= oldDistance) {
                step /= 2;
            }
        }

        System.out.printf("%.7f\n", minDistance);
    }

    private static double calculateDistance(int[] x, int[] y, double dx, double dy) {
        double distance = 0;
        for (int i = 0; i < x.length; i++) {
            distance += Math.sqrt((dx - x[i]) * (dx - x[i]) + (dy - y[i]) * (dy - y[i]));
        }
        return distance;
    }

    private static double maxValue(int[] arr) {
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    private static double minValue(int[] arr) {
        double min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }
}

