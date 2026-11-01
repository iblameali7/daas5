/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * The DFS is iterative (own array as a stack), so a 1000 x 1000 maze cannot overflow the call stack.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int rows = w.rows(), cols = w.cols(), n = w.cells();
        int[][] open = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int[] list : open) if (list.length == 1) deadEnds++;

        int exit = n - 1;
        boolean[] seen = new boolean[n];
        int[] next = new int[n];       // next[c] = which direction (0 up, 1 right, 2 down, 3 left) cell c tries next
        int[] stack = new int[n];      // the current path from the start
        int top = 0, visited = 0;

        stack[top++] = 0;
        seen[0] = true;
        visited = 1;
        while (top > 0 && stack[top - 1] != exit) {
            int cell = stack[top - 1];
            int r = cell / cols, c = cell % cols;
            boolean moved = false;
            while (next[cell] < 4 && !moved) {
                int dir = next[cell]++;
                int nb = -1;
                if (dir == 0 && r > 0) nb = cell - cols;
                else if (dir == 1 && c < cols - 1) nb = cell + 1;
                else if (dir == 2 && r < rows - 1) nb = cell + cols;
                else if (dir == 3 && c > 0) nb = cell - 1;
                if (nb < 0 || seen[nb] || !contains(open[cell], nb)) continue;
                seen[nb] = true;       // marked at the moment of discovery
                visited++;
                stack[top++] = nb;
                moved = true;
            }
            if (!moved) top--;         // all four directions tried: dead end, go back
        }
        int[] path = java.util.Arrays.copyOf(stack, top);
        return new Result(path, visited, deadEnds);
    }

    private static boolean contains(int[] list, int x) {
        for (int v : list) if (v == x) return true;
        return false;
    }
}
