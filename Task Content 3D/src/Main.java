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

public class Main {
    private static Random random = new Random();

    private static double n, k,
            Sl, Sx, Sy,
            minX = 1000, maxX = -1000,
            minY = 1000, maxY = -1000,
            xStart, yStart;
    private static int particlesCount = 500;

    private static List<Pair<Pair<Double, Double>, Pair<Double, Double>>> environmentMap = new ArrayList<>();

    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private static boolean isPointOnLine(Pair<Pair<Double, Double>, Pair<Double, Double>> one, Pair<Double, Double> pair) {
        double xMin = Math.min(one.first.first, one.second.first);
        double xMax = Math.max(one.first.first, one.second.first);
        double yMin = Math.min(one.first.second, one.second.second);
        double yMax = Math.max(one.first.second, one.second.second);

        if (pair.first <= xMax && pair.first >= xMin) {
            if (pair.second <= yMax && pair.second >= yMin) {
                return true;
            }
        }

        return false;
    }


    private static int calculateDirection(Pair<Double, Double> one, Pair<Double, Double> two, Pair<Double, Double> three) {
        double value = (two.first - one.first) * (three.second - one.second) - (three.first - one.first) * (two.second - one.second);

        if (value == 0)
            return 0;  // !!Collinear!!
        else if (value < 0)
            return 2;  // !!Anti-clockwise direction!!
        else
            return 1;  // !!Clockwise direction!!
    }


    private static boolean isIntersect(Pair<Pair<Double, Double>, Pair<Double, Double>> pair1,
                               Pair<Pair<Double, Double>, Pair<Double, Double>> pair2) {

        int dir1 = calculateDirection(pair1.first, pair1.second, pair2.first);
        int dir2 = calculateDirection(pair1.first, pair1.second, pair2.second);
        int dir3 = calculateDirection(pair2.first, pair2.second, pair1.first);
        int dir4 = calculateDirection(pair2.first, pair2.second, pair1.second);

        if (dir1 != dir2 && dir3 != dir4)
            return true;

        if (dir1 == 0 && isPointOnLine(pair1, pair2.first))
            return true;

        if (dir2 == 0 && isPointOnLine(pair1, pair2.second))
            return true;

        if (dir3 == 0 && isPointOnLine(pair2, pair1.first))
            return true;

        if (dir4 == 0 && isPointOnLine(pair2, pair1.second))
            return true;

        return false;
    }

    private static boolean isInsidePolygon(Pair<Double, Double> pair) {
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

    private static double calculateLidar(Particle particle, List<Double> robotMeasure) {
        double difference = 0;

        for (int i = 0; i < k; i++) {
            double newX, newY;

            newX = particle.x + robotMeasure.get(i) * Math.cos((2.0 * Math.PI / k) * i);
            newY = particle.y + robotMeasure.get(i) * Math.sin((2.0 * Math.PI / k) * i);

            double deviation = 10005;

            for (var list : environmentMap) {
                double x1, y1, x2, y2;

                x1 = list.first.first;
                y1 = list.first.second;
                x2 = list.second.first;
                y2 = list.second.second;

                double delta, px, py;

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

            difference += deviation * deviation;
        }

//        System.out.println(difference + "DIFFERENCE!!!");
        return difference;
    }

    private static void sense(List<Particle> particles, List<Double> robotMeasurements) {
        for (Particle particle : particles) {
            double deviation = calculateLidar(particle, robotMeasurements);
            deviation /= k;
//            System.out.println(difference +" new difference");
            deviation = Math.sqrt(deviation);

            particle.w = (1.0 / (Math.sqrt(2.0 * Math.PI) * (Sl))) * (Math.exp(((deviation * deviation) / (-2 * Sl * Sl))));
//            System.out.println(p.w + " p.w");
        }

    }

    private static void normalize(List<Particle> particles) {
        double particleSum = 0;
        for (Particle particle : particles) {
            particleSum += particle.w;
        }

        for (Particle particle : particles) {
            particle.w /= particleSum;
        }
    }

    private static int weightedRandom(List<Particle> particles) {
        double randomWeight = random.nextDouble();
        double tempTotal = 0;

        for (int i = 0; i < particles.size(); i++) {
            tempTotal += particles.get(i).w;

            if (randomWeight < tempTotal) {
                return i;
            }
        }

        return 0;
    }

    private static List<Particle> repopulate(List<Particle> oldParticles) {
        normalize(oldParticles);
        List<Particle> newParticles = new ArrayList<>();

        for (int i = 0; i < particlesCount; i++) {
            int index = weightedRandom(oldParticles);
            Particle particle = oldParticles.get(index);
            newParticles.add(new Particle(particle.x, particle.y, particle.w));
        }

        return newParticles;
    }

    private static List<Particle> initialGenerate(int n, int mode) {
        List<Particle> particles = new ArrayList<>();

        while (particles.size() < n) {
            if (mode == 0) {
                double xRand = minX + (maxX - minX) * random.nextDouble();
                double yRand = minY + (maxY - minY) * random.nextDouble();

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

    private static void move(List<Particle> particles, double x, double y) {
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        n = scanner.nextDouble();

        Pair<Double, Double> p0;
        double x1, y1, x2, y2;
        x1 = scanner.nextDouble();
        y1 = scanner.nextDouble();
        p0 = new Pair<>(x1, y1);

        minX = Math.min(minX, x1);
        maxX = Math.max(maxX, x1);
        minY = Math.min(minY, y1);
        maxY = Math.max(maxY, y1);

        for (int i = 0; i < n - 1; i++) {
            x2 = scanner.nextDouble();
            y2 = scanner.nextDouble();

            minX = Math.min(minX, x2);
            maxX = Math.max(maxX, x2);
            minY = Math.min(minY, y2);
            maxY = Math.max(maxY, y2);

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
}

