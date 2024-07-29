import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Particle {
    double x;
    double y;
    double w = 1;

    public Particle(double x, double y, double w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }

    public Particle() {}

    //    @Override
//    public String toString() {
//        return "Particle{" +
//                "x=" + x +
//                ", y=" + y +
//                ", w=" + w +
//                '}';
//    }
}

public class Main {
    static Random random = new Random();

    static double n, k,
            Sl, Sx, Sy, min_x = 1005, max_x = -1005, min_y = 1005,
            max_y = -1005,
            xStart, yStart;
    static int particlesCount = 500;

    static List<Pair<Pair<Double, Double>, Pair<Double, Double>>> environmentMap = new ArrayList<>();

    static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    static boolean isPointOnLine(Pair<Pair<Double, Double>, Pair<Double, Double>> l1, Pair<Double, Double> p) {
        double xMin = Math.min(l1.first.first, l1.second.first);
        double xMax = Math.max(l1.first.first, l1.second.first);
        double yMin = Math.min(l1.first.second, l1.second.second);
        double yMax = Math.max(l1.first.second, l1.second.second);

        if (p.first <= xMax && p.first >= xMin) {
            if (p.second <= yMax && p.second >= yMin) {
                return true;
            }
        }

        return false;
    }


    static int calculateDirection(Pair<Double, Double> a, Pair<Double, Double> b, Pair<Double, Double> c) {
        double val = (b.second - a.second) * (c.first - b.first)
                - (b.first - a.first) * (c.second - b.second);

        if (val == 0)
            return 0;  // !!Collinear!!
        else if (val < 0)
            return 2;  // !!Anti-clockwise direction!!
        else
            return 1;  // !!Clockwise direction!!
    }

    static boolean isIntersect(Pair<Pair<Double, Double>, Pair<Double, Double>> l1,
                               Pair<Pair<Double, Double>, Pair<Double, Double>> l2) {

        int dir1 = calculateDirection(l1.first, l1.second, l2.first);
        int dir2 = calculateDirection(l1.first, l1.second, l2.second);
        int dir3 = calculateDirection(l2.first, l2.second, l1.first);
        int dir4 = calculateDirection(l2.first, l2.second, l1.second);

        if (dir1 != dir2 && dir3 != dir4)
            return true;

        if (dir1 == 0 && isPointOnLine(l1, l2.first))
            return true;

        if (dir2 == 0 && isPointOnLine(l1, l2.second))
            return true;

        if (dir3 == 0 && isPointOnLine(l2, l1.first))
            return true;

        if (dir4 == 0 && isPointOnLine(l2, l1.second))
            return true;

        return false;
    }

    static boolean isInsidePolygon(Pair<Double, Double> pair) {
        Pair<Pair<Double, Double>, Pair<Double, Double>> extendedLine = new Pair<>(pair, new Pair<>(9999.0, pair.second));

        int count = 0;
        int i = 0;
        do {
            if (isIntersect(environmentMap.get(i), extendedLine)) {
                if (calculateDirection(environmentMap.get(i).first, pair, environmentMap.get(i).second) == 0)
                    return isPointOnLine(environmentMap.get(i), pair);
                count++;
            }
            i = (i + 1) % environmentMap.size();
        } while (i != 0);

        return (count & 1) == 1;
    }

    static double calculateLidar(Particle p, List<Double> robotMeasure) {
        double diff = 0;

        for (int i = 0; i < k; i++) {
            double newX, newY;

            newX = p.x + robotMeasure.get(i) * Math.cos((2.0 * Math.PI / k) * i);
            newY = p.y + robotMeasure.get(i) * Math.sin((2.0 * Math.PI / k) * i);

            double deviation = 10005;

            for (var l : environmentMap) {
                double x1, y1, x2, y2;

                x1 = l.first.first;
                y1 = l.first.second;
                x2 = l.second.first;
                y2 = l.second.second;

                double delta = 0, px, py;

                delta = ((newX - x1) * (x2 - x1) + (newY - y1) * (y2 - y1)) /
                        ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

                px = x1 + delta * (x2 - x1);
                py = y1 + delta * (y2 - y1);

                if (!(px >= Math.min(x1, x2) && px <= Math.max(x1, x2)) ||
                        !(py >= Math.min(y1, y2) && py <= Math.max(y1, y2))) {
                    double d1 = calculateDistance(newX, newY, x1, y1);
                    double d2 = calculateDistance(newX, newY, x2, y2);
                    if (d1 <= d2) {
                        deviation = Math.min(deviation, d1);
                    } else {
                        deviation = Math.min(deviation, d2);
                    }
                } else {
                    deviation = Math.min(deviation, calculateDistance(newX, newY, px, py));
                }
            }

            diff += deviation * deviation;
        }

//        System.out.println(diff + "DIFF!!!");
        return diff;
    }

    static void sense(List<Particle> particles, List<Double> robotMeasurements) {
        for (Particle particle : particles) {
            double deviation = calculateLidar(particle, robotMeasurements);
            deviation /= k;
//            System.out.println(diff +" new diff");
            deviation = Math.sqrt(deviation);

            particle.w = (1.0 / (Math.sqrt(2.0 * Math.PI) * (Sl))) * (Math.exp(((-0.5 * deviation * deviation) / (Sl * Sl))));
//            System.out.println(p.w + " p.w");
        }

    }

    static void normalize(List<Particle> particles) {
        double particleSum = 0;
        for (Particle particle : particles) {
            particleSum += particle.w;
        }

        for (Particle particle : particles) {
            particle.w /= particleSum;
        }
    }

    static double generateRandom() {
        return random.nextDouble();
    }


    static int weightedRandom(List<Particle> particles) {
        double randomWeight = generateRandom();
        double tempTotal = 0;

        for (int i = 0; i < particles.size(); i++) {
            tempTotal += particles.get(i).w;

            if (randomWeight < tempTotal) {
                return i;
            }
        }

        return 0;
    }

    static List<Particle> repopulate(List<Particle> oldParticles) {
        normalize(oldParticles);
        List<Particle> newParticles = new ArrayList<>();

        for (int i = 0; i < particlesCount; i++) {
            int index = weightedRandom(oldParticles);
            Particle particle = oldParticles.get(index);
            newParticles.add(new Particle(particle.x, particle.y, particle.w));
        }

        return newParticles;
    }


    static List<Particle> initialGenerate(int n, int mode) {
        List<Particle> particles = new ArrayList<>();

        while (particles.size() < n) {
            if (mode == 0) {
                double xRand = min_x + (max_x - min_x) * generateRandom();
                double yRand = min_y + (max_y - min_y) * generateRandom();

                Particle particle = new Particle();
                particle.x = xRand;
                particle.y = yRand;

                if (isInsidePolygon(new Pair<>(xRand, yRand))) {
                    particles.add(particle);
                }
            } else {
                Particle particle = new Particle();
                particle.x = xStart;
                particle.y = yStart;

                particles.add(particle);
            }
        }

        return particles;
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        n = scanner.nextDouble();

        Pair<Double, Double> p0;
        double x1, y1, x2, y2;
        x1 = scanner.nextDouble();
        y1 = scanner.nextDouble();
        p0 = new Pair<>(x1, y1);

        min_x = Math.min(min_x, x1);
        max_x = Math.max(max_x, x1);
        min_y = Math.min(min_y, y1);
        max_y = Math.max(max_y, y1);

        for (int i = 0; i < n - 1; i++) {
            x2 = scanner.nextDouble();
            y2 = scanner.nextDouble();

            min_x = Math.min(min_x, x2);
            max_x = Math.max(max_x, x2);
            min_y = Math.min(min_y, y2);
            max_y = Math.max(max_y, y2);

            environmentMap.add(new Pair<>(new Pair<>(x1, y1), new Pair<>(x2, y2)));
            x1 = x2;
            y1 = y2;
        }

        environmentMap.add(new Pair<>(p0, new Pair<>(x1, y1)));
//        System.out.println(environmentMap + " map");

        double m;
        m = scanner.nextDouble();
        k = scanner.nextDouble();
        Sl = scanner.nextDouble();
        Sx = scanner.nextDouble();
        Sy = scanner.nextDouble();

        int mode;
        mode = scanner.nextInt();
        if (mode == 1) {
            xStart = scanner.nextDouble();
            yStart = scanner.nextDouble();
        }

        List<Particle> particles = initialGenerate(particlesCount, mode);
//        System.out.println(particles + " particles");

        for (int i = 0; i < m + 1; i++) {
            List<Double> robotLidar = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                double d = scanner.nextDouble();
                robotLidar.add(d);
            }

            sense(particles, robotLidar);
            particles = repopulate(particles);
//            System.out.println(particles + " particles after repopulate");

            if (i != m) {
                double x, y;
                x = scanner.nextDouble();
                y = scanner.nextDouble();
                move(particles, x, y);
            }
        }

        double xRes = particles.stream().mapToDouble(particle -> particle.x).average().orElse(0);
        double yRes = particles.stream().mapToDouble(particle -> particle.y).average().orElse(0);


        System.out.printf("%.10f %.10f", xRes, yRes);
    }

    public static void move(List<Particle> particles, double x, double y) {
        particles.forEach(p -> {
            double randomX, randomY;

            do {
                randomX = ThreadLocalRandom.current().nextGaussian() * Sx;
            } while (Math.abs(randomX) > Sx);

            do {
                randomY = ThreadLocalRandom.current().nextGaussian() * Sy;
            } while (Math.abs(randomY) > Sy);

            p.x += x + randomX;
//            System.out.println(p.x + " p.x");
            p.y += y + randomY;
//            System.out.println(p.y + " p.y");
        });
    }

}

class Pair<K, V> {
    K first;
    V second;

    Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

//    @Override
//    public String toString() {
//        return "Pair{" +
//                "first=" + first +
//                ", second=" + second +
//                '}';
//    }
}
