public class Main {
    public static void main(String[] args) {
        double result = solveEquation();
        System.out.println("The solution is x = " + result);
    }
    // Функція для обчислення значення лівої частини рівняння
    private static double f(double x) {
        return Math.pow(x, 5) + Math.tan(x) - 1;
    }

    // Функція для виконання бінарного пошуку
    private static double solveEquation() {
        double left = 0.0;  // Ліва границя
        double right = 1.0; // Права границя
        double eps = 1e-6; // Точність

        while (right - left > eps) {
            double mid = (left + right) / 2;
            double fMid = f(mid);

            if (fMid > 0) {
                right = mid;
            } else {
                left = mid;
            }
        }

        // Повертаємо середнє значення як наближене рішення
        return (left + right) / 2;
    }
}
