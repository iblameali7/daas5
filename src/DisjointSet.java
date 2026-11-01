/**
 * Mission 0. Disjoint set union (DSU).
 * Union by size + path compression. find is iterative, so a long chain cannot overflow the stack.
 */
public class DisjointSet {

    private final int[] parent;
    private final int[] size;   // size[root] = number of cells in that tree
    private int sets;

    public DisjointSet(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
        sets = n;
    }

    /** The root of the set that contains x. */
    public int find(int x) {
        int root = x;
        while (parent[root] != root) root = parent[root];      // 1) walk up to the root
        while (parent[x] != root) {                            // 2) path compression: hang everything on the root
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    /** Joins the sets of a and b. Returns true if they were different sets, false if they were already one set. */
    public boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }   // ra is the larger tree
        parent[rb] = ra;                                            // smaller tree goes under the larger
        size[ra] += size[rb];
        sets--;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    /** Number of sets now. */
    public int count() {
        return sets;
    }
}
