import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

public class InfoGraph {
    private final int nodeCount;
    private final ArrayList<ArrayList<Pair<Integer, Integer>>> graph;

    InfoGraph(int nodeCount) {
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

    ArrayList<Integer> breedSearch(int start, int end, int[] straightLengths) {
        int currentNode = start;
        int wayLength = 0;
        ArrayList<Integer> wayPoints = new ArrayList<>();
        wayPoints.add(currentNode);
        while (currentNode != end) {
            int nextNode = -1;
            int nextLength = Integer.MAX_VALUE;
            int nextWayLength = 0;
            for (Pair<Integer, Integer> way : graph.get(currentNode)) {
                int node = way.getKey();
                if (straightLengths[node] < nextLength) {
                    nextLength = straightLengths[node];
                    nextNode = node;
                    nextWayLength = way.getValue();
                }
            }
            wayLength += nextWayLength;
            System.out.println("Next node: " + nextNode);
            currentNode = nextNode;
            wayPoints.add(currentNode);
        }
        System.out.println("Way length: " + wayLength);
        System.out.println("Straight length from start: " + straightLengths[start]);
        System.out.println();
        return wayPoints;
    }

    Stack<Integer> aStar(int start, int end, int[] straightLengths) {
        int[] costsFromStart = new int[nodeCount];
        int[] fullCost = new int[nodeCount];
        boolean[] visited = new boolean[nodeCount];
        int[] prevNode = new int[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            costsFromStart[i] = Integer.MAX_VALUE;
            fullCost[i] = Integer.MAX_VALUE;
        }
        costsFromStart[start] = 0;
        int currentNode = start;
        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(Comparator.comparing(Pair::getValue));
        while (currentNode != end) {
            visited[currentNode] = true;
            for (Pair<Integer, Integer> nodePair : graph.get(currentNode)) {
                int node = nodePair.getKey();
                if (visited[node]) continue;
                int length = nodePair.getValue();
                if (costsFromStart[node] > costsFromStart[currentNode] + length) {
                    costsFromStart[node] = costsFromStart[currentNode] + length;
                    prevNode[node] = currentNode;
                    queue.remove(new Pair<>(node, fullCost[node]));
                    fullCost[node] = costsFromStart[node] + straightLengths[node];
                    queue.offer(new Pair<>(node, fullCost[node]));
                }
            }
            currentNode = queue.poll().getKey();
            System.out.println("Next node: " + currentNode);
        }
        Stack<Integer> way = new Stack<>();
        currentNode = end;
        while (currentNode != start) {
            way.push(currentNode);
            currentNode = prevNode[currentNode];
        }
        way.add(start);
        System.out.println("Way cost = " + costsFromStart[end]);
        System.out.println();
        return way;
    }

    public static void main(String[] args) {
        InfoGraph graph = new InfoGraph(6);

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
//        graph.breedSearch(0, 3, new int[]{20, 14, 8, 0, 6, 5});
    }
}
