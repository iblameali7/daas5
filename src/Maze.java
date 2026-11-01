import java.util.*;

/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[Math.max(0, w.cells() - 1)];
        int m = 0;
        for (int k = 0; k < w.count(); k++) {
            if (dsu.union(w.a()[k], w.b()[k])) broken[m++] = k;
        }
        return Arrays.copyOf(broken, m);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int count = w.count();
        // pack (cost, index) into one long so that sorting the long[] sorts by cost (no boxing, fast on 2 million walls)
        long[] keys = new long[count];
        for (int k = 0; k < count; k++) keys[k] = ((long) w.cost()[k] << 32) | k;
        Arrays.sort(keys);
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[Math.max(0, w.cells() - 1)];
        int m = 0;
        for (long key : keys) {
            int k = (int) (key & 0xFFFFFFFFL);
            if (dsu.union(w.a()[k], w.b()[k])) {
                broken[m++] = k;
                if (m == broken.length) break;   // the tree is complete
            }
        }
        return Arrays.copyOf(broken, m);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells(), count = w.count();
        // adjacency lists in compressed form: edges of cell u are at positions start[u] .. start[u+1]-1
        int[] start = new int[n + 1];
        for (int k = 0; k < count; k++) { start[w.a()[k] + 1]++; start[w.b()[k] + 1]++; }
        for (int i = 0; i < n; i++) start[i + 1] += start[i];
        int[] fill = Arrays.copyOf(start, n);
        int[] to = new int[2 * count], cost = new int[2 * count];
        for (int k = 0; k < count; k++) {
            int a = w.a()[k], b = w.b()[k], c = w.cost()[k];
            to[fill[a]] = b; cost[fill[a]++] = c;
            to[fill[b]] = a; cost[fill[b]++] = c;
        }
        boolean[] inTree = new boolean[n];
        PriorityQueue<Long> pq = new PriorityQueue<>();   // entry = cost * 2^32 + cell: smallest cost first
        long total = 0;
        int taken = 0;
        pq.add(0L);                                        // start from cell 0 with cost 0
        while (!pq.isEmpty() && taken < n) {
            long top = pq.poll();
            int u = (int) (top & 0xFFFFFFFFL);
            if (inTree[u]) continue;                       // stale entry: the cell was reached cheaper before
            inTree[u] = true;
            taken++;
            total += top >>> 32;
            for (int e = start[u]; e < start[u + 1]; e++) {
                if (!inTree[to[e]]) pq.add(((long) cost[e] << 32) | to[e]);
            }
        }
        return total;
    }
}
