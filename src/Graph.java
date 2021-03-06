import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

class Graph {
    private final int nodeCount;
    private final ArrayList<LinkedList<Integer>> graph;

    Graph(int nodeCount) {
        this.nodeCount = nodeCount;
        graph = new ArrayList<>(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            graph.add(new LinkedList<>());
        }
    }

    void addEdge(int from, int to) {
        graph.get(from).add(to);
        graph.get(to).add(from);
        graph.get(from).sort(Integer::compareTo);
        graph.get(to).sort(Integer::compareTo);
    }

    public void printGraphMatrix() {
        int[][] matrix = new int[nodeCount][nodeCount];
        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < graph.get(i).size(); j++) {
                matrix[i][graph.get(i).get(j)] = 1;
                matrix[graph.get(i).get(j)][i] = 1;
            }
        }
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                System.out.print(matrix[i][j] + ", ");
            }
            System.out.println();
        }
    }

    private Stack<Integer> findWay(int from, int to, int[] prev) {
        Stack<Integer> way = new Stack<>();
        way.push(to);
        int cur = prev[to];
        while (cur != from) {
            way.push(cur);
            cur = prev[cur];
        }
        way.push(from);
        return way;
    }

    Stack<Integer> dfs(int start, int end) {
        boolean[] visited = new boolean[nodeCount];
        int[] prev = new int[nodeCount];
        prev[start] = start;
        dfs(start, end, visited, prev);
        return findWay(start, end, prev);
    }

    private void dfs(int start, int end, boolean[] visited, int[] prev) {
        visited[start] = true;
        if (start == end) {
            return;
        }
        for (int vertex : graph.get(start)) {
            if (!visited[vertex]) {
                prev[vertex] = start;
                dfs(vertex, end, visited, prev);
            }
        }
    }

    Stack<Integer> limitedDfs(int start, int end, int limit) {
        boolean[] visited = new boolean[nodeCount];
        int[] prev = new int[nodeCount];
        prev[start] = start;
        int depth = 0;
        limitedDfs(start, visited, prev, depth, limit);
        if (visited[end]) return findWay(start, end, prev);
        return new Stack<>();
    }

    private void limitedDfs(int start, boolean[] visited, int[] prev, int depth, int limit) {
        if (depth > limit) return;
        visited[start] = true;
        for (int vertex : graph.get(start)) {
            if (!visited[vertex]) {
                prev[vertex] = start;
                limitedDfs(vertex, visited, prev, depth + 1, limit);
            }
        }
    }

    Stack<Integer> iterativeDfs(int start, int end) {
        int limit = 1;
        while (limitedDfs(start, end, limit).empty()) {
            limit++;
        }
        return limitedDfs(start, end, limit);
    }

    Stack<Integer> bfs(int start, int end) {
        LinkedList<Integer> queue = new LinkedList<>();
        boolean[] used = new boolean[nodeCount];
        int[] prev = new int[nodeCount];
        prev[start] = start;
        queue.add(start);
        while (!queue.isEmpty()) {
            int first = queue.removeFirst();
            for (int node : graph.get(first)) {
                if (!used[node]) {
                    used[node] = true;
                    prev[node] = first;
                    queue.add(node);
                }
            }
        }
        return findWay(start, end, prev);
    }

    private LinkedList<Integer> findBidirectionalWay(int start, int end, int[] prev, int[] prevEnd, int intersect) {
        LinkedList<Integer> way = new LinkedList<>();
        way.add(intersect);
        int cur = prev[intersect];
        while (cur != start) {
            way.push(cur);
            cur = prev[cur];
        }
        way.push(start);
        cur = prevEnd[intersect];
        while (cur != end) {
            way.add(cur);
            cur = prevEnd[cur];
        }
        way.add(end);
        return way;
    }

    LinkedList<Integer> bidirectionalBfs(int start, int end) {
        int intersect = -1;
        LinkedList<Integer> queue = new LinkedList<>();
        boolean[] used = new boolean[nodeCount];
        int[] prev = new int[nodeCount];
        prev[start] = -1;
        queue.add(start);
        LinkedList<Integer> queueEnd = new LinkedList<>();
        boolean[] usedEnd = new boolean[nodeCount];
        int[] prevEnd = new int[nodeCount];
        prevEnd[end] = -1;
        queueEnd.add(end);
        while (!queue.isEmpty() && !queueEnd.isEmpty() && intersect == -1) {
            int first = queue.removeFirst();
            for (int node : graph.get(first)) {
                if (!used[node]) {
                    used[node] = true;
                    prev[node] = first;
                    queue.add(node);
                }
            }
            for (int i = 0; i < used.length; i++) {
                if (used[i] && usedEnd[i]) {
                    intersect = i;
                    break;
                }
            }
            if (intersect != -1) break;
            int firstEnd = queueEnd.removeFirst();
            for (int node : graph.get(firstEnd)) {
                if (!usedEnd[node]) {
                    usedEnd[node] = true;
                    prevEnd[node] = firstEnd;
                    queueEnd.add(node);
                }
            }
            for (int i = 0; i < used.length; i++) {
                if (used[i] && usedEnd[i]) {
                    intersect = i;
                    break;
                }
            }
        }
        return findBidirectionalWay(start, end, prev, prevEnd, intersect);
    }

    public static void main(String[] args) {
        /*Graph g = new Graph(6);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 4);
        g.addEdge(2, 5);
        g.addEdge(3, 4);
        g.addEdge(4, 1);
        g.addEdge(4, 5);
        g.addEdge(5, 3);

        g.dfs(0, 3);
        System.out.println();
        g.limitedDfs(0, 3, 3);
        System.out.println();
        g.iterativeDfs(0, 3);
        System.out.println();
        g.bfs(0, 3);
        System.out.println();
        g.bidirectionalBfs(0, 3);*/
    }
}
