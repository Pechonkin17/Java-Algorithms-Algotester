import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        double[] x = new double[n];
        double[] y = new double[n];
        double[] z = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextDouble();
            y[i] = scanner.nextDouble();
            z[i] = scanner.nextDouble();
        }

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0, sumXZ = 0, sumYZ = 0, sumZ = 0;

        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
            sumY2 += y[i] * y[i];
            sumXZ += x[i] * z[i];
            sumYZ += y[i] * z[i];
            sumZ += z[i];
        }

        double[][] coefficients = {{sumX2, sumXY, sumX}, {sumXY, sumY2, sumY}, {sumX, sumY, n}};
        double[] constants = {sumXZ, sumYZ, sumZ};
        double[] result = gaus(coefficients, constants);

        double a = result[0];
        double b = result[1];
        double c = result[2];

        System.out.println(a + " " + b + " " + c);
    }

    public static double[] gaus(double[][] coefficients, double[] constants) {
        int n = coefficients.length;
        double[] solution = new double[n];

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(coefficients[k][i]) > Math.abs(coefficients[maxRow][i])) {
                    maxRow = k;
                }
            }

            double[] temp = coefficients[i];
            coefficients[i] = coefficients[maxRow];
            coefficients[maxRow] = temp;

            double tempConstant = constants[i];
            constants[i] = constants[maxRow];
            constants[maxRow] = tempConstant;

            double pivot = coefficients[i][i];
            for (int j = i; j < n; j++) {
                coefficients[i][j] /= pivot;
            }
            constants[i] /= pivot;

            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = coefficients[k][i];
                    for (int j = i; j < n; j++) {
                        coefficients[k][j] -= factor * coefficients[i][j];
                    }
                    constants[k] -= factor * constants[i];
                }
            }
        }

        for (int i = 0; i < n; i++) {
            solution[i] = constants[i];
        }

        return solution;
    }
}
