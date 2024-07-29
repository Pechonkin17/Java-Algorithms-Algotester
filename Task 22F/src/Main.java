import java.util.Random;

public class Main {

    public static void main(String[] args) {
        double a = 0;
        double b = 1;

        double targetError = 1e-4; // Початкова похибка

        double probability = calculateProbability(a, b, targetError);

        System.out.println(probability);
    }

    private static double calculateProbability(double a, double b, double targetError) {
        Random random = new Random();
        int vegetarianCount = 0;
        int totalTrials = 0;
        double currentError = Double.MAX_VALUE;

        while (currentError > targetError && totalTrials < Integer.MAX_VALUE) {
            double person1 = random.nextDouble();
            double person2 = random.nextDouble();
            double person3 = random.nextDouble();

            double sum = person1 + person2 + person3;

            if (sum >= a && sum <= b) {
                vegetarianCount++;
            }

            totalTrials++;
            double currentProbability = (double) vegetarianCount / totalTrials;
            currentError = Math.abs(currentProbability - 0.25); // 0.25 is the expected probability for the given range
        }

        return (double) vegetarianCount / totalTrials;
    }
}
