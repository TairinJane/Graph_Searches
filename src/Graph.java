import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

class Graph {
    private final int vertices;
    private final ArrayList<LinkedList<Integer>> graph;

    Graph(int vertices) {
        this.vertices = vertices;
        graph = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            graph.add(new LinkedList<>());
        }
    }

    void addEdge(int from, int to) {
        graph.get(from).add(to);
        graph.get(to).add(from);
    }

    void findWay(int from, int to, int[] prev) {
        Stack<Integer> way = new Stack<>();
        way.push(to);
        int cur = prev[to];
        while (cur != from) {
            way.push(cur);
            cur = prev[cur];
        }
        way.push(from);
        System.out.println("Way: ");
        while (!way.empty()) {
            System.out.print(way.pop() + " ");
        }
        System.out.println();
    }

    void dfs(int start, int end) {
        boolean[] visited = new boolean[vertices];
        int[] prev = new int[vertices];
        prev[start] = start;
        System.out.println("DFS from " + start + ": ");
        dfs(start, visited, prev);
        findWay(start, end, prev);
    }

    void dfs(int start, boolean[] visited, int[] prev) {
        visited[start] = true;
        for (int vertex : graph.get(start)) {
            if (!visited[vertex]) {
                prev[vertex] = start;
                dfs(vertex, visited, prev);
            }
        }
    }

    boolean limitedDfs(int start, int end, int limit) {
        boolean[] visited = new boolean[vertices];
        int[] prev = new int[vertices];
        prev[start] = start;
        int depth = 0;
        System.out.println("DFS from " + start + " with limit = " + limit + ": ");
        limitedDfs(start, visited, prev, depth, limit);
        if (visited[end]) {
            findWay(start, end, prev);
        } else System.out.println("No way with limit = " + limit);
        return visited[end];
    }

    void limitedDfs(int start, boolean[] visited, int[] prev, int depth, int limit) {
        if (depth > limit) return;
        visited[start] = true;
        for (int vertex : graph.get(start)) {
            if (!visited[vertex]) {
                prev[vertex] = start;
                limitedDfs(vertex, visited, prev, depth + 1, limit);
            }
        }
    }

    void iterativeDfs(int start, int end) {
        int limit = 1;
        System.out.println("Iterative DFS from " + start + ":");
        while (!limitedDfs(start, end, limit)) limit++;
    }

    void bfs(int start, int end) {
        LinkedList<Integer> queue = new LinkedList<>();
        boolean[] used = new boolean[vertices];
        int[] prev = new int[vertices];
        prev[start] = start;
        queue.add(start);
        System.out.println("BFS from " + start + ":");
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
        findWay(start, end, prev);
    }

    void findBidirectionalWay(int start, int end, int[] prev, int[] prevEnd, int intersect) {
        LinkedList<Integer> way = new LinkedList<>();
        way.add(intersect);
        int cur = prev[intersect];
        while (cur != start) {
            way.add(cur);
            cur = prev[cur];
        }
        way.add(start);
        cur = prevEnd[intersect];
        while (cur != end) {
            way.push(cur);
            cur = prevEnd[cur];
        }
        way.push(end);
        System.out.println("Way: ");
        while (!way.isEmpty()) {
            System.out.print(way.removeLast() + " ");
        }
        System.out.println();
    }

    void bidirectionalBfs(int start, int end) {
        int intersect = -1;
        LinkedList<Integer> queue = new LinkedList<>();
        boolean[] used = new boolean[vertices];
        int[] prev = new int[vertices];
        prev[start] = -1;
        queue.add(start);
        LinkedList<Integer> queueEnd = new LinkedList<>();
        boolean[] usedEnd = new boolean[vertices];
        int[] prevEnd = new int[vertices];
        prevEnd[end] = -1;
        queueEnd.add(end);
        System.out.println("BFS from " + start + " and " + end + ":");
        while (!queue.isEmpty() && !queueEnd.isEmpty() && intersect == -1) {
            int first = queue.removeFirst();
            for (int node : graph.get(first)) {
                if (!used[node]) {
                    used[node] = true;
                    prev[node] = first;
                    queue.add(node);
                }
            }
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
        System.out.println("Intersect: " + intersect);
        findBidirectionalWay(start, end, prev, prevEnd, intersect);
    }

    public static void main(String[] args) {
        Graph g = new Graph(6);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
//        g.addEdge(0, 4);
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
        g.bidirectionalBfs(0, 3);
    }
}