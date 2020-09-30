import javafx.util.Pair;

import java.util.ArrayList;

public class InfoGraph {
    private final int nodeCount;
    private final ArrayList<ArrayList<Pair<Integer, Integer>>> graph;
    private int[] straightLengths;

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

    void initLengths(int[] lengths) {
        straightLengths = lengths;
    }

    void breedSearch(int from, int to) {
        int currentNode = from;
        int wayLength = 0;
        ArrayList<Integer> wayPoints = new ArrayList<>();
        wayPoints.add(currentNode);
        while (currentNode != to) {
            int nextNode = -1;
            int currentLength = straightLengths[currentNode];
            int nextLength = currentLength;
            int nextWayLength = 0;
            for (Pair<Integer, Integer> way : graph.get(currentNode)) {
                if (currentLength - way.getValue() < nextLength) {
                    nextLength = currentLength - way.getValue();
                    nextNode = way.getKey();
                    nextWayLength = way.getValue();
                }
            }
            wayLength += nextWayLength;
            System.out.println("Next node: " + nextNode);
            System.out.println("Next length: " + nextLength);
            System.out.println("Way length: " + wayLength);
            currentNode = nextNode;
            wayPoints.add(currentNode);
        }
        System.out.println("Full length: " + wayLength);
        System.out.println("Straight length: " + straightLengths[from]);
        System.out.println("Way:");
        for (int node : wayPoints) System.out.print(node + " ");
    }

    void aStar(int from, int to) {

    }

    public static void main(String[] args) {
        InfoGraph g = new InfoGraph(6);

        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 7);
//        g.addEdge(0, 4);
        g.addEdge(1, 2, 5);
        g.addEdge(2, 4, 1);
        g.addEdge(2, 5, 10);
        g.addEdge(3, 4, 2);
        g.addEdge(4, 1, 6);
        g.addEdge(4, 5, 3);
        g.addEdge(5, 3, 18);

        g.initLengths(new int[]{12, 8, 4, 0, 4, 6});

        g.breedSearch(0, 3);
    }
}
