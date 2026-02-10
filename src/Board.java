import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Board {
    private HashMap<Resource, Integer> resources = new HashMap<>();

    private Node[] nodes = new Node[54];
    private Tile[] tiles = new Tile[19];
    private List<Road> roads = new ArrayList<>();

    private Player longestRoad = null;
    private Player[] players;

    public Board() {
        SetupGame setup = new SetupGame();
        tiles = setup.getTiles();
        nodes = setup.getNodes();

        for (int i = 0; i < tiles.length; i++) {
            this.tiles[i].setBoard(this);
            this.tiles[i].setNodeTiles();
        }

        resources.put(Resource.WOOD, 19);
        resources.put(Resource.BRICK, 19);
        resources.put(Resource.WHEAT, 19);
        resources.put(Resource.SHEEP, 19);
        resources.put(Resource.ORE, 19);
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void addRoad(Road road) {
        roads.add(road);
    }

    public void updateResources(Resource resource, int change) {
        int amount = resources.get(resource);
        resources.put(resource, amount + change);
    }

    public int getResources(Resource resource) {
        return resources.get(resource);
    }

    public boolean checkResources(Resource resource, int change) {
        if (resource == null) {
            return false;
        }
        int amount = resources.get(resource);
        return amount + change >= 0;
    }

    public void printResources() {
        System.out.println(resources.get(Resource.BRICK) + " " + resources.get(Resource.WOOD) + " " + resources.get(Resource.ORE) + " " + resources.get(Resource.WHEAT) + " " + resources.get(Resource.SHEEP));
    }

    public void setLongestRoad(Player player) {
        longestRoad = player;
    }

    public Player getLongestRoad() {
        return longestRoad;
    }
}