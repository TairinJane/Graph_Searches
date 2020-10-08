import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Cities {
    private final ArrayList<String> cities = new ArrayList<>();
    private final HashMap<String, Integer> citiesToIndex = new HashMap<>();
    private final Graph graph;
    private final InfoGraph infoGraph;
    private final int CITIES_COUNT = 27;
    private final int[] wayLengthsToZhitomir = new int[CITIES_COUNT];

    Cities() {
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\Anna.Kozhemyako\\Desktop\\mine\\labs_code\\ii_lab\\src\\cities.txt"));
            while (scanner.hasNextLine()) {
                String city = scanner.next();
                int wayLength = scanner.nextInt();
                cities.add(city);
                wayLengthsToZhitomir[cities.size() - 1] = wayLength;
                citiesToIndex.put(city, cities.size() - 1);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        graph = new Graph(cities.size());
        infoGraph = new InfoGraph(cities.size(), cities);
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\Anna.Kozhemyako\\Desktop\\mine\\labs_code\\ii_lab\\src\\graph.txt"));
            while (scanner.hasNextLine()) {
                String city1 = scanner.next();
                String city2 = scanner.next();
                int wayLength = scanner.nextInt();
                int index1 = citiesToIndex.get(city1);
                int index2 = citiesToIndex.get(city2);
                graph.addEdge(index1, index2);
                infoGraph.addEdge(index1, index2, wayLength);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void findWayWithoutLength(String from, String to) {
        int indexFrom = citiesToIndex.get(from);
        int indexTo = citiesToIndex.get(to);
        Stack<Integer> dfsWay = graph.dfs(indexFrom, indexTo);
        System.out.println("DFS");
        printWay(dfsWay);
        Stack<Integer> limitedDfsWay = graph.limitedDfs(indexFrom, indexTo, 3);
        System.out.println("\nDFS with limit = 3");
        printWay(limitedDfsWay);
        Stack<Integer> iterativeDfsWay = graph.iterativeDfs(indexFrom, indexTo);
        System.out.println("\nIterative DFS");
        printWay(iterativeDfsWay);
        Stack<Integer> bfsWay = graph.bfs(indexFrom, indexTo);
        System.out.println("\nBFS");
        printWay(bfsWay);
        LinkedList<Integer> biBfs = graph.bidirectionalBfs(indexFrom, indexTo);
        System.out.println("\nBidirectional BFS");
        printWay(biBfs);
    }

    public void findWayToZhitomir(String from) {
        int indexFrom = citiesToIndex.get(from);
        int indexZhitomir = citiesToIndex.get("Житомир");
        System.out.println("\nBreed search");
        ArrayList<Integer> breed = infoGraph.breedSearch(indexFrom, indexZhitomir, wayLengthsToZhitomir);
        printWay(breed);
        System.out.println("\nA Star");
        Stack<Integer> aStar = infoGraph.aStar(indexFrom, indexZhitomir, wayLengthsToZhitomir);
        printWay(aStar);
    }

    public void printWay(Stack<Integer> way) {
        if (way.empty()) {
            System.out.println("No way");
            return;
        }
        while (!way.empty()) {
            int city = way.pop();
            System.out.print(cities.get(city) + " (" + city + ") -> ");
        }
        System.out.println();
    }

    public void printWay(List<Integer> way) {
        if (way.isEmpty()) {
            System.out.println("No way\n");
            return;
        }
        for (int node : way)
            System.out.print(cities.get(node) + "(" + node + ") -> ");
        System.out.println();
    }

    public static void main(String[] args) {
        Cities cities = new Cities();
        System.out.println();
        cities.findWayWithoutLength("С.Петербург", "Житомир");
        cities.findWayToZhitomir("С.Петербург");
    }
}
