import javafx.util.Pair;

import java.util.ArrayList;

public class AStarGraph {
    private final int nodeCount;
    private final ArrayList<ArrayList<Pair<Integer, Integer>>> graph;

    public AStarGraph(int nodeCount) {
        this.nodeCount = nodeCount;
        graph = new ArrayList<>(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            graph.add(new ArrayList<>());
        }
    }

    void addEdge(int from, int to, int length) {
        graph.get(from).add(new Pair<>(to, length));
        graph.get(to).add(new Pair<>(from, length));
    }

    void aStar(int start, int end, int[] straightLengths) {
        int[] costsFromStart = new int[nodeCount];
        boolean[] visited = new boolean[nodeCount];
        ArrayList<Integer> way = new ArrayList<>();
        way.add(start);
        for (int i = 0; i < nodeCount; i++) {
            costsFromStart[i] = Integer.MAX_VALUE;
        }
        costsFromStart[start] = 0;
        int currentNode = start;
        while (currentNode != end) {
            visited[currentNode] = true;
            int nextNode = currentNode;
            int minLength = Integer.MAX_VALUE;
            for (Pair<Integer, Integer> nodePair : graph.get(currentNode)) {
                int node = nodePair.getKey();
                if (visited[node]) continue;
                int length = nodePair.getValue();
                if (costsFromStart[node] > costsFromStart[currentNode] + length)
                    costsFromStart[node] = costsFromStart[currentNode] + length;
                if (costsFromStart[node] + straightLengths[node] < minLength) {
                    minLength = costsFromStart[node] + straightLengths[node];
                    nextNode = node;
                }
            }
            currentNode = nextNode;
            way.add(nextNode);
            System.out.println();
            System.out.println("Next node = " + currentNode);
            System.out.println("Min cost = " + minLength);
            System.out.println("Costs from start");
            for (int node : costsFromStart) System.out.print(node + " ");
        }
        System.out.println();
        System.out.println("Full cost = " + costsFromStart[end]);
        System.out.println("Way:");
        for (int node : way) System.out.print(node + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        AStarGraph graph = new AStarGraph(6);
        graph.addEdge(0, 1, 7);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 2, 15);
        graph.addEdge(1, 4, 13);
        graph.addEdge(2, 4, 11);
        graph.addEdge(2, 5, 5);
        graph.addEdge(3, 4, 8);
        graph.addEdge(3, 5, 12);
        graph.addEdge(4, 5, 16);
        graph.aStar(0, 3, new int[]{20, 14, 8, 0, 6, 5});
    }
}
