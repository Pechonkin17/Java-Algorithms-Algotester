import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

class Letter {
    int width;
    int height;
    int classNumber;
    int distance;

    public Letter(int width, int height, int classNumber) {
        this.width = width;
        this.height = height;
        this.classNumber = classNumber;
    }

//    @Override
//    public String toString() {
//        return "Letter{" +
//                "width=" + width +
//                ", height=" + height +
//                ", classNumber=" + classNumber +
//                ", distance=" + distance +
//                '}';
//    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int kk = k;
        int m = scanner.nextInt();

        List<Letter> classes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int width = scanner.nextInt();
            int height = scanner.nextInt();
            classes.add(new Letter(width, height, i + 1));
        }

        for (int i = 0; i < m; i++) {
            int width = scanner.nextInt();
            int height = scanner.nextInt();

            for (Letter letter : classes) {
                letter.distance = Math.max(
                        Math.abs(letter.width - width), Math.abs(letter.height - height)
                );

            }

            classes.sort((a, b) -> Integer.compare(a.distance, b.distance));

//            for (Letter l : classes) {
//                System.out.println(l + " ");
//            }

            int assignedClass = 0;
            int[] classCounts = new int[n];

            if (k > classes.size()) {
                k = classes.size();
            }


            for (int j = k; j < classes.size(); j++) {
                if (classes.get(j).distance == classes.get(k - 1).distance) {
                    k++;
                }
            }

            for (int j = 0; j < k; j++) {
                classCounts[classes.get(j).classNumber - 1]++;
            }

            int maxCountValue = classCounts[0];

            for (int count : classCounts) {
                if (count > maxCountValue) {
                    maxCountValue = count;
                }
            }

            List<Integer> maxCountIndices = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (classCounts[j] == maxCountValue) {
                    maxCountIndices.add(j + 1);
                }
            }

            assignedClass = maxCountIndices.get(0);
            classes.add(new Letter(width, height, assignedClass));

            System.out.print(assignedClass + " ");
            k = kk;
        }
    }
}

