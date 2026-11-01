import java.util.*;

/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i].
 */
public class Zones {

    /**
     * Mission 4. Kosaraju with two iterative DFS passes. zoneOf[cell] = number of the zone.
     * Zones are numbered in the order of the second pass, which is a topological order of the zone graph.
     */
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] out = Graphs.slidesFrom(s);
        int[][] in = Graphs.slidesInto(s);

        // Pass 1: DFS on the slides, record cells in the order they are finished.
        boolean[] seen = new boolean[n];
        int[] order = new int[n];
        int finished = 0;
        int[] stack = new int[n];
        int[] pos = new int[n];          // pos[u] = how many slides of u were already looked at
        for (int root = 0; root < n; root++) {
            if (seen[root]) continue;
            int top = 0;
            stack[top++] = root;
            seen[root] = true;
            while (top > 0) {
                int u = stack[top - 1];
                if (pos[u] < out[u].length) {
                    int v = s.to()[out[u][pos[u]++]];
                    if (!seen[v]) { seen[v] = true; stack[top++] = v; }
                } else {
                    order[finished++] = u;   // all slides of u are done
                    top--;
                }
            }
        }

        // Pass 2: from the last finished cell to the first, DFS on the REVERSED slides.
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zones = 0;
        for (int i = n - 1; i >= 0; i--) {
            int root = order[i];
            if (zoneOf[root] != -1) continue;
            int top = 0;
            stack[top++] = root;
            zoneOf[root] = zones;
            while (top > 0) {
                int u = stack[--top];
                for (int idx : in[u]) {
                    int v = s.from()[idx];
                    if (zoneOf[v] == -1) { zoneOf[v] = zones; stack[top++] = v; }
                }
            }
            zones++;
        }
        return zoneOf;
    }

    /**
     * Mission 5. One pass over the zones in the order of their numbers (no priority queue).
     * [0] flow stages: the number of zones on the longest chain of zones;
     * [1] the cost of the cheapest route from the zone of cell 0 to the zone of the last cell.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        int m = s.count();
        // group the slides by the zone they start in (counting sort), so we can walk zone by zone
        int[] start = new int[zoneCount + 1];
        for (int i = 0; i < m; i++) start[zoneOf[s.from()[i]] + 1]++;
        for (int z = 0; z < zoneCount; z++) start[z + 1] += start[z];
        int[] fill = Arrays.copyOf(start, zoneCount);
        int[] byZone = new int[m];
        for (int i = 0; i < m; i++) byZone[fill[zoneOf[s.from()[i]]]++] = i;

        long INF = Long.MAX_VALUE / 4;
        long[] chain = new long[zoneCount];   // longest chain of zones that ends in this zone (counted in zones)
        long[] dist = new long[zoneCount];    // cheapest cost from the zone of the start
        Arrays.fill(chain, 1);
        Arrays.fill(dist, INF);
        int startZone = zoneOf[0], exitZone = zoneOf[s.n() - 1];
        dist[startZone] = 0;

        long stages = zoneCount == 0 ? 0 : 1;
        for (int z = 0; z < zoneCount; z++) {      // zone numbers are a topological order: all edges go to larger numbers
            stages = Math.max(stages, chain[z]);
            for (int e = start[z]; e < start[z + 1]; e++) {
                int i = byZone[e];
                int y = zoneOf[s.to()[i]];
                if (y == z) continue;              // slide inside a zone: free, not an edge of the zone graph
                chain[y] = Math.max(chain[y], chain[z] + 1);
                if (dist[z] < INF) dist[y] = Math.min(dist[y], dist[z] + s.time()[i]);
            }
        }
        return new long[]{stages, dist[exitZone]};
    }
}
