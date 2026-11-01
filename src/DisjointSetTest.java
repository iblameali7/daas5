/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);
        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");
        d.union(2, 3); d.union(1, 3);
        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5, "a single cell is its own root");

        // my own checks
        DisjointSet e = new DisjointSet(1);
        check(e.count() == 1 && e.find(0) == 0, "a set of one cell");
        check(!e.union(0, 0), "union of a cell with itself returns false");
        check(e.count() == 1, "count does not change after union(0, 0)");

        int n = 1_000_000;
        DisjointSet chain = new DisjointSet(n);
        boolean allTrue = true;
        for (int i = 0; i + 1 < n; i++) allTrue &= chain.union(i, i + 1);
        check(allTrue, "1 000 000 unions in a chain all return true");
        check(chain.count() == 1, "one set after the chain");
        check(chain.connected(0, n - 1), "both ends of the chain are connected (no stack overflow)");
        check(!chain.union(5, n - 5), "union inside one set returns false");

        DisjointSet g = new DisjointSet(10);
        for (int i = 0; i < 10; i += 2) g.union(i, (i + 2) % 10);
        check(g.count() == 6, "evens joined into one set: 1 + 5 odd cells = 6 sets");
        check(g.connected(0, 8) && !g.connected(0, 1), "evens together, odd apart");
        int root = g.find(8);
        check(g.find(0) == root && g.find(4) == root, "find gives the same root for the whole set");

        System.out.println(failed == 0 ? "All checks passed." : failed + " checks failed.");
    }
}
