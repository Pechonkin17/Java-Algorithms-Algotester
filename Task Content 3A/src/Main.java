import java.util.*;

public class Main {

    static class Particle {
        double x;
        double y;
        double w = 1;
    }

    static Random random = new Random();
    static List<Pair<Pair<Double, Double>, Pair<Double, Double>>> map = new ArrayList<>();
    static double n, k, Sl, Sx, Sy, min_x = 1005, max_x = -1005, min_y = 1005, max_y = -1005, x_start, y_start;
    static int particles_count = 3;

    static class Pair<T, U> {
        T first;
        U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    static double dis(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    static boolean onLine(Pair<Pair<Double, Double>, Pair<Double, Double>> l1, Pair<Double, Double> p) {
        return p.first <= Math.max(l1.first.first, l1.second.first)
                && p.first >= Math.min(l1.first.first, l1.second.first)
                && (p.second <= Math.max(l1.first.second, l1.second.second)
                && p.second >= Math.min(l1.first.second, l1.second.second));
    }

    static int direction(Pair<Double, Double> a, Pair<Double, Double> b, Pair<Double, Double> c) {
        double val = (b.second - a.second) * (c.first - b.first)
                - (b.first - a.first) * (c.second - b.second);

        if (val == 0)
            return 0;  // Collinear
        else if (val < 0)
            return 2;  // Anti-clockwise direction
        else
            return 1;  // Clockwise direction
    }

    static boolean isIntersect(Pair<Pair<Double, Double>, Pair<Double, Double>> l1,
                               Pair<Pair<Double, Double>, Pair<Double, Double>> l2) {
        int dir1 = direction(l1.first, l1.second, l2.first);
        int dir2 = direction(l1.first, l1.second, l2.second);
        int dir3 = direction(l2.first, l2.second, l1.first);
        int dir4 = direction(l2.first, l2.second, l1.second);

        if (dir1 != dir2 && dir3 != dir4)
            return true;

        if (dir1 == 0 && onLine(l1, l2.first))
            return true;

        if (dir2 == 0 && onLine(l1, l2.second))
            return true;

        if (dir3 == 0 && onLine(l2, l1.first))
            return true;

        return dir4 == 0 && onLine(l2, l1.second);
    }

    static boolean checkIfInsidePolygon(Pair<Double, Double> p) {
        Pair<Pair<Double, Double>, Pair<Double, Double>> exline = new Pair<>(p, new Pair<>(9999.0, p.second));
        int count = 0;
        int i = 0;
        do {
            if (isIntersect(map.get(i), exline)) {
                if (direction(map.get(i).first, p, map.get(i).second) == 0)
                    return onLine(map.get(i), p);
                count++;
            }
            i = (i + 1) % map.size();
        } while (i != 0);

        return (count & 1) == 1;
    }

    static double particleLidar(Particle p, List<Double> robotMeasure) {
        double diff = 0;

        for (int i = 0; i < k; i++) {
            double new_x = p.x + robotMeasure.get(i) * Math.cos((2.0 * Math.PI / k) * i);
            double new_y = p.y + robotMeasure.get(i) * Math.sin((2.0 * Math.PI / k) * i);
            double dev = 10005;

            for (Pair<Pair<Double, Double>, Pair<Double, Double>> l : map) {
                double x1 = l.first.first;
                double y1 = l.first.second;
                double x2 = l.second.first;
                double y2 = l.second.second;

                double delta = ((new_x - x1) * (x2 - x1) + (new_y - y1) * (y2 - y1))
                        / ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

                double px = x1 + delta * (x2 - x1);
                double py = y1 + delta * (y2 - y1);

                if (!(px >= Math.min(x1, x2) && px <= Math.max(x1, x2)) ||
                        !(py >= Math.min(y1, y2) && py <= Math.max(y1, y2))) {
                    double d_1 = dis(new_x, new_y, x1, y1);
                    double d_2 = dis(new_x, new_y, x2, y2);
                    if (d_1 <= d_2) {
                        dev = Math.min(dev, d_1);
                    } else {
                        dev = Math.min(dev, d_2);
                    }
                } else {
                    dev = Math.min(dev, dis(new_x, new_y, px, py));
                }
            }

            diff += dev * dev;
        }

        return diff;
    }

    static void sense(List<Particle> particles, List<Double> robotMeasure) {
        for (Particle p : particles) {
            double diff = particleLidar(p, robotMeasure);
            diff /= k;
            diff = Math.sqrt(diff);
            p.w = ((1.0 / (Math.sqrt(2.0 * Math.PI) * (Sl))) *
                    (Math.exp(((-0.5 * diff * diff) / (Sl * Sl)))));
        }
    }

    static void normalize(List<Particle> particles) {
        double eta = particles.stream().mapToDouble(p -> p.w).sum();

        for (Particle p : particles) {
            p.w /= eta;
        }
    }

    static double uniformGenerator() {
        return random.nextDouble();
    }

    static int weightedRandom(List<Particle> particles) {
        double randomWeight = uniformGenerator();
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

        for (int i = 0; i < particles_count; i++) {
            Particle p = oldParticles.get(weightedRandom(oldParticles));
            newParticles.add(p);
        }

        return newParticles;
    }

    static void move(List<Particle> particles, double x, double y) {
        for (Particle p : particles) {
            p.x += x + random.nextGaussian() * Sx;
            p.y += y + random.nextGaussian() * Sy;
        }
    }

    static List<Particle> initialGenerate(int n, int mode) {
        List<Particle> particles = new ArrayList<>();

        while (particles.size() < n) {
            if (mode == 0) {
                double xRand = min_x + (max_x - min_x) * uniformGenerator();
                double yRand = min_y + (max_y - min_y) * uniformGenerator();

                Particle p = new Particle();
                p.x = xRand;
                p.y = yRand;

                if (checkIfInsidePolygon(new Pair<>(xRand, yRand))) {
                    particles.add(p);
                }
            } else {
                Particle p = new Particle();
                p.x = x_start;
                p.y = y_start;

                particles.add(p);
            }
        }

        return particles;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        n = scanner.nextDouble();

        double x1, y1, x2, y2;
        x1 = scanner.nextDouble();
        y1 = scanner.nextDouble();
        Pair<Double, Double> p0 = new Pair<>(x1, y1);

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

            map.add(new Pair<>(new Pair<>(x1, y1), new Pair<>(x2, y2)));
            x1 = x2;
            y1 = y2;
        }

        map.add(new Pair<>(p0, new Pair<>(x1, y1)));

        double m = scanner.nextDouble();
        k = scanner.nextDouble();
        Sl = scanner.nextDouble();
        Sx = scanner.nextDouble();
        Sy = scanner.nextDouble();

        int mode = scanner.nextInt();
        if (mode == 1) {
            x_start = scanner.nextDouble();
            y_start = scanner.nextDouble();
        }

        List<Particle> particles = initialGenerate(particles_count, mode);

        for (int i = 0; i < m + 1; i++) {
            List<Double> robotLidar = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                double d = scanner.nextDouble();
                robotLidar.add(d);
            }

            sense(particles, robotLidar);
            particles = repopulate(particles);

            if (i != m) {
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                move(particles, x, y);
            }
        }

        double xRes = 0, yRes = 0;
        for (Particle p : particles) {
            xRes += p.x;
            yRes += p.y;
        }

        xRes /= particles_count;
        yRes /= particles_count;

        System.out.println(xRes + " " + yRes);
    }
}
