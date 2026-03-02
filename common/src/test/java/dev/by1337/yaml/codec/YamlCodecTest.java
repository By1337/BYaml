package dev.by1337.yaml.codec;

import dev.by1337.yaml.YamlMap;
import dev.by1337.yaml.YamlReaderImpl;
import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.reflect.ReflectCodecFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class YamlCodecTest {

    private static List<String> list;
    private static Map<String, Integer> map;
    private static Map<String, List<String>> map2list;
    private static Map<String, Map<String, Integer>> map2map;
    private static Map<String, Map<String, List<String>>> map2map2list;
    private static String s;
    private static String[] sa;
    private static int[] ia;
    private static Map rawMap;


    @Test
    public void run2() throws Exception {

        if(true){
            System.out.println(16384*2/512);
      //      System.out.println(grid.cells.length);
            return;
        }
        List<AABB> all = new ArrayList<>();
        var grid = new AABBGrid(500);
        Random random = new Random();
        for (int i = 0; i < 10_000_000; i++) {
            int x = random.nextInt(5000);
            int y = random.nextInt(255-10);
            int z = random.nextInt(5000);
            var aabb = new AABB(
                    x + 10,
                    y + 10,
                    z + 10,
                    x - 10,
                    y - 10,
                    z - 10
            );
            all.add(aabb);
            grid.add(aabb);
        }
        AABB base = all.get(all.size()-1);
        for (int i = 0; i < 20; i++) {
            long nanos = System.nanoTime();
            int x = 0;
            for (AABB aabb : all) {
                if (aabb.intersect(base)) {
                    x++;
                }
            }
            System.out.println(((System.nanoTime() - nanos) / 1000D) + "us " + x);
        }
        for (int i = 0; i < 20; i++) {
            long nanos = System.nanoTime();
            int x = 0;
            if (grid.intersect(base)) {
                x++;
            }
            System.out.println(((System.nanoTime() - nanos) / 1000D) + "us " + x);
        }
    }

    private record AABBPair(AABB base, List<AABB> inners) {
    }

    public static final class Octree {

        private static final int DEFAULT_CAPACITY = 8;
        private static final int DEFAULT_MAX_DEPTH = 64;

        private final Node root;

        public Octree(AABB boundary) {
            this(boundary, DEFAULT_CAPACITY, DEFAULT_MAX_DEPTH);
        }

        public Octree(AABB boundary, int capacity, int maxDepth) {
            this.root = new Node(boundary, capacity, maxDepth, 0);
        }

        public boolean add(AABB aabb) {
            return root.insert(aabb);
        }

        public boolean remove(AABB aabb) {
            return root.remove(aabb);
        }

        public boolean intersect(AABB area) {
            return intersect(area, null);
        }
        public boolean intersect(AABB area, List<AABB> result) {
           return root.query(area, result);
        }

        // =========================================================

        private static final class Node {

            private final AABB boundary;
            private final int capacity;
            private final int maxDepth;
            private final int depth;

            private final List<AABB> objects = new ArrayList<>();
            private Node[] children; // 8

            Node(AABB boundary, int capacity, int maxDepth, int depth) {
                this.boundary = boundary;
                this.capacity = capacity;
                this.maxDepth = maxDepth;
                this.depth = depth;
            }

            boolean insert(AABB aabb) {
                if (!boundary.intersect(aabb)) {
                    return false;
                }

                if (children != null) {
                    int index = getChildIndex(aabb);
                    if (index != -1) {
                        return children[index].insert(aabb);
                    }
                }

                objects.add(aabb);

                if (objects.size() > capacity && depth < maxDepth) {
                    if (children == null) {
                        subdivide();
                    }

                    // перераспределяем
                    for (int i = 0; i < objects.size(); ) {
                        AABB obj = objects.get(i);
                        int index = getChildIndex(obj);
                        if (index != -1) {
                            objects.remove(i);
                            children[index].insert(obj);
                        } else {
                            i++;
                        }
                    }
                }

                return true;
            }

            boolean remove(AABB aabb) {
                if (!boundary.intersect(aabb)) {
                    return false;
                }

                if (objects.remove(aabb)) {
                    return true;
                }

                if (children != null) {
                    int index = getChildIndex(aabb);
                    if (index != -1) {
                        return children[index].remove(aabb);
                    }

                    // если объект был "растянут" и лежит не в одном ребёнке
                    for (Node child : children) {
                        if (child.remove(aabb)) {
                            return true;
                        }
                    }
                }

                return false;
            }

            boolean query(AABB area, List<AABB> result) {
                if (!boundary.intersect(area)) {
                    return false;
                }

                for (AABB obj : objects) {
                    if (obj.intersect(area)) {
                        if (result == null) return true;
                        result.add(obj);
                    }
                }

                if (children != null) {
                    for (Node child : children) {
                       var v = child.query(area, result);
                       if (result == null && v) return true;
                    }
                }
                if (result == null) return false;
                return !result.isEmpty();
            }

            private void subdivide() {
                children = new Node[8];

                double midX = (boundary.minX + boundary.maxX) * 0.5;
                double midY = (boundary.minY + boundary.maxY) * 0.5;
                double midZ = (boundary.minZ + boundary.maxZ) * 0.5;

                for (int i = 0; i < 8; i++) {
                    double minX = (i & 1) == 0 ? boundary.minX : midX;
                    double maxX = (i & 1) == 0 ? midX : boundary.maxX;

                    double minY = (i & 2) == 0 ? boundary.minY : midY;
                    double maxY = (i & 2) == 0 ? midY : boundary.maxY;

                    double minZ = (i & 4) == 0 ? boundary.minZ : midZ;
                    double maxZ = (i & 4) == 0 ? midZ : boundary.maxZ;

                    children[i] = new Node(
                            new AABB(minX, minY, minZ, maxX, maxY, maxZ),
                            capacity,
                            maxDepth,
                            depth + 1
                    );
                }
            }

            /**
             * Возвращает индекс ребёнка (0–7),
             * если AABB полностью помещается в него.
             * Иначе -1.
             */
            private int getChildIndex(AABB aabb) {

                double midX = (boundary.minX + boundary.maxX) * 0.5;
                double midY = (boundary.minY + boundary.maxY) * 0.5;
                double midZ = (boundary.minZ + boundary.maxZ) * 0.5;

                boolean left = aabb.maxX <= midX;
                boolean right = aabb.minX >= midX;

                boolean bottom = aabb.maxY <= midY;
                boolean top = aabb.minY >= midY;

                boolean back = aabb.maxZ <= midZ;
                boolean front = aabb.minZ >= midZ;

                if (!(left || right)) return -1;
                if (!(bottom || top)) return -1;
                if (!(back || front)) return -1;

                int index = 0;
                if (right) index |= 1;
                if (top) index |= 2;
                if (front) index |= 4;

                return index;
            }
        }
    }


    public static final class AABBGrid {

        private static final int MIN = -10_000;
        private static final int MAX = 10_000;
        private static final int WORLD_SIZE = MAX - MIN;

        private final int cellSize;
        private final int gridSize; // cells per axis

        private final List<AABB>[] cells;

        @SuppressWarnings("unchecked")
        public AABBGrid(int cellSize) {
            this.cellSize = cellSize;
            this.gridSize = WORLD_SIZE / cellSize;

            if (WORLD_SIZE % cellSize != 0) {
                throw new IllegalArgumentException("World size must be divisible by cellSize");
            }

            this.cells = new List[gridSize * gridSize];

            for (int i = 0; i < cells.length; i++) {
                cells[i] = new ArrayList<>(8);
            }
        }

        // ========================= PUBLIC API =========================

        public void add(AABB aabb) {
            int minX = toCellX(aabb.minX);
            int maxX = toCellX(aabb.maxX);
            int minZ = toCellZ(aabb.minZ);
            int maxZ = toCellZ(aabb.maxZ);

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cells[index(x, z)].add(aabb);
                }
            }
        }

        public boolean remove(AABB aabb) {
            boolean removed = false;

            int minX = toCellX(aabb.minX);
            int maxX = toCellX(aabb.maxX);
            int minZ = toCellZ(aabb.minZ);
            int maxZ = toCellZ(aabb.maxZ);

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    removed |= cells[index(x, z)].remove(aabb);
                }
            }

            return removed;
        }

        public boolean intersect(AABB aabb) {
            int minX = toCellX(aabb.minX);
            int maxX = toCellX(aabb.maxX);
            int minZ = toCellZ(aabb.minZ);
            int maxZ = toCellZ(aabb.maxZ);

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    List<AABB> cell = cells[index(x, z)];

                    for (int i = 0; i < cell.size(); i++) {
                        if (cell.get(i).intersect(aabb)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        // ========================= INTERNAL =========================

        private int toCellX(double x) {
            int cx = (int) ((x - MIN) / cellSize);
            return clamp(cx);
        }

        private int toCellZ(double z) {
            int cz = (int) ((z - MIN) / cellSize);
            return clamp(cz);
        }

        private int clamp(int v) {
            if (v < 0) return 0;
            if (v >= gridSize) return gridSize - 1;
            return v;
        }

        private int index(int x, int z) {
            return x + z * gridSize;
        }
    }

    public static class AABB {
        private double minX;
        private double minY;
        private double minZ;
        private double maxX;
        private double maxY;
        private double maxZ;

        public AABB(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.minX = x1;
            this.minY = y1;
            this.minZ = z1;
            this.maxX = x2;
            this.maxY = y2;
            this.maxZ = z2;
            resize();
        }

        public void resize() {
            double minX = this.minX;
            double minY = this.minY;
            double minZ = this.minZ;
            double maxX = this.maxX;
            double maxY = this.maxY;
            double maxZ = this.maxZ;
            this.minX = Math.min(minX, maxX);
            this.minY = Math.min(minY, maxY);
            this.minZ = Math.min(minZ, maxZ);
            this.maxX = Math.max(minX, maxX);
            this.maxY = Math.max(minY, maxY);
            this.maxZ = Math.max(minZ, maxZ);
        }

        public void resize(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.minX = Math.min(x1, x2);
            this.minY = Math.min(y1, y2);
            this.minZ = Math.min(z1, z2);
            this.maxX = Math.max(x1, x2);
            this.maxY = Math.max(y1, y2);
            this.maxZ = Math.max(z1, z2);
        }

        public boolean intersect(AABB aabb) {
            return minX <= aabb.maxX &&
                    maxX >= aabb.minX &&
                    minY <= aabb.maxY &&
                    maxY >= aabb.minY &&
                    minZ <= aabb.maxZ &&
                    maxZ >= aabb.minZ;
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f, %.2f) <-> (%.2f, %.2f, %.2f)",
                    minX,
                    minY,
                    minZ,
                    maxX,
                    maxY,
                    maxZ
            );
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            AABB aabb = (AABB) o;
            return Double.compare(minX, aabb.minX) == 0 && Double.compare(minY, aabb.minY) == 0 && Double.compare(minZ, aabb.minZ) == 0 && Double.compare(maxX, aabb.maxX) == 0 && Double.compare(maxY, aabb.maxY) == 0 && Double.compare(maxZ, aabb.maxZ) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    @Test
    public void run1() throws Exception {
        var ignored = new YamlReaderImpl();
        ReflectCodecFactory<Tester> b = new ReflectCodecFactory<>(() -> {
            var r = new Tester();
            r.reset();
            return r;
        });

        YamlCodec<Tester> codec = b.make(Tester.class);
        YamlValue v = codec.encode(new Tester());
        YamlMap map = new YamlMap();
        map.set("test", v);
        System.out.println(map.saveToString());

        System.out.println(
                new Tester().equals(
                        map.get("test").decode(codec).result()
                )
        );
    }

    @Test
    public void run() throws Exception {
        //  System.out.println(Map.class.isAssignableFrom(HashMap.class));
        for (Field field : YamlCodecTest.class.getDeclaredFields()) {
            field.setAccessible(true);

            //  System.out.println(field.getType());
            var type = field.getGenericType();
            if (type instanceof ParameterizedType pt) {
                System.out.println(pt.getRawType());
                System.out.println(Arrays.toString(pt.getActualTypeArguments()) + pt.getActualTypeArguments().length + " " + pt.getRawType());
            } else {
                System.out.println(type);
            }
        }
    }

    public static class Tester {
        private Integer[][] pricol = new Integer[][]{{1, 2, 3}, {4, 5, 6}};
        private Map<String, Integer> map = Map.of("123", 444, "321", 555);
        private Map<String, Map<String, Integer>> map2 = Map.of("666", map, "777", map);
        private Map<String, Map<String, Map<String, Integer>>>
                map3 = Map.of("666", map2, "777", map2);
        private Map<String, Map<String, Map<String, Map<String, Integer>>>>
                map4 = Map.of("666", map3, "777", map3);
        private HashMap<Integer, Map<String, Map<String, Map<String, Map<String, Integer>>>>>
                map5 = new HashMap<>(Map.of(567, map4));

        private void reset() {
            map = null;
            map2 = null;
            map3 = null;
            map4 = null;
            map5 = null;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Tester tester = (Tester) o;
            return Objects.equals(map, tester.map) && Objects.equals(map2, tester.map2) && Objects.equals(map3, tester.map3) && Objects.equals(map4, tester.map4) && Objects.equals(map5, tester.map5);
        }

        @Override
        public int hashCode() {
            return Objects.hash(map, map2, map3, map4, map5);
        }
    }
}