import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        String[] words = new String[n];
        for (int i = 0; i < n; i++) {
            words[i] = scanner.next();
        }

        int minSteps = findMinSteps(words);
        System.out.println(minSteps);
    }

    public static int findMinSteps(String[] words) {
        if (words.length < 2) {
            return 0;
        }

        String target = words[words.length - 1];
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.offer(words[0]);
        visited.add(words[0]);
        int steps = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                if (word.equals(target)) {
                    return steps;
                }

                for (String nextWord : words) {
                    if (!visited.contains(nextWord) && connection(word, nextWord)) {
                        queue.offer(nextWord);
                        visited.add(nextWord);
                    }
                }
            }

            steps++;
        }
        return -1;
    }

    public static boolean connection(String word1, String word2) {
        return word1.charAt(word1.length() - 2) == word2.charAt(1);
    }
}
