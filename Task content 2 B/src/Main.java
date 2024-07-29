import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        int K = scanner.nextInt();
        scanner.nextLine();

        List<String> normalEmails = new ArrayList<>();
        List<String> spamEmails = new ArrayList();
        List<String> newEmails = new ArrayList();

        for (int i = 0; i < N; i++) {
            normalEmails.add(scanner.nextLine());
        }

        for (int i = 0; i < M; i++) {
            spamEmails.add(scanner.nextLine());
        }

        for (int i = 0; i < K; i++) {
            newEmails.add(scanner.nextLine());
        }

        Map<String, Double> normalWordsProbability = spamProbability(normalEmails);
        Map<String, Double> spamWordProbability = spamProbability(spamEmails);

        int emails = N + M;
        double normal = (double) N / emails;
        double spam = (double) M / emails;

        for (String email : newEmails) {
            String[] words = email.split(" ");
            double normalEmail = normal;
            double spamEmail = spam;

            for (String word : words) {
                normalEmail *= normalWordsProbability.getOrDefault(word, 0.0);
                spamEmail *= spamWordProbability.getOrDefault(word, 0.0);
            }

            double chanceSpam = spamEmail / (normalEmail + spamEmail);
            System.out.println(chanceSpam);
        }
    }

    public static Map<String, Double> spamProbability(List<String> emails) {
        Map<String, Integer> count = new HashMap<>();
        int allWords = 0;

        for (String email : emails) {
            String[] words = email.split(" ");
            allWords += words.length;
            for (String word : words) {
                count.put(word, count.getOrDefault(word, 0) + 1);
            }
        }

        Map<String, Double> spamProbability = new HashMap<>();
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            spamProbability.put(entry.getKey(), (double) entry.getValue() / allWords);
        }

        return spamProbability;
    }
}