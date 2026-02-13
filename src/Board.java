import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Board {
    private HashMap<Resource, Integer> resources = new HashMap<>();

    private Node[] nodes = new Node[54];
    private Tile[] tiles = new Tile[19];
    private List<Road> roads = new ArrayList<>();
    private int turn = 0;
    private ActionController actionController;

    private Player longestRoad = null;

    public Board() { //Constructor method
        SetupGame setup = new SetupGame();
        tiles = setup.getTiles();
        nodes = setup.getNodes();

        for (int i = 0; i < tiles.length; i++) {
            this.tiles[i].setNodeTiles(this);
        }

        resources.put(Resource.WOOD, 19);
        resources.put(Resource.BRICK, 19);
        resources.put(Resource.WHEAT, 19);
        resources.put(Resource.SHEEP, 19);
        resources.put(Resource.ORE, 19);

        actionController = new ActionController(this);
    }

    public Tile[] getTiles() {
        return tiles;
    } //Getter

    public Node[] getNodes() {
        return nodes;
    } //Getter

    public Node getNodes(int node) {
        return nodes[node];
    }

    public List<Road> getRoads() {
        return roads;
    } //Getter

    public void addRoad(Road road) {
        roads.add(road);
    } //Setter

    public void updateResources(Resource resource, int change) { //Updates resources
        int amount = resources.get(resource);
        resources.put(resource, amount + change);
    }

    public int getResources(Resource resource) {
        return resources.get(resource);
    } //Getter

    public boolean checkResources(Resource resource, int change) { //Checks if resources would be 0 or greater if changed
        if (resource == null) {
            return false;
        }
        int amount = resources.get(resource);
        return amount + change >= 0;
    }

    public void printResources() { //Prints resources
        System.out.println(resources.get(Resource.BRICK) + " " + resources.get(Resource.WOOD) + " " + resources.get(Resource.ORE) + " " + resources.get(Resource.WHEAT) + " " + resources.get(Resource.SHEEP));
    }

    public boolean placeSettlement(Player player, Node node) {
        return actionController.handleBuildSettlement(player, node, turn);
    }

    public boolean placeSettlement(Player player, int node) {
        return actionController.handleBuildSettlement(player, getNodes(node), turn);
    }

    public boolean upgradeCity(Player player, Node node) {
        return actionController.handleUpgradeCity(player, node);
    }

    public boolean placeRoad(Player player, Node start, Node end) {
        return actionController.handleBuildRoad(player, start, end, turn);
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<Boolean> checkActions(Player player, List<Node> settlementNodes, List<Node> roadNodes) {
        return new ArrayList<>(actionController.checkActions(player, settlementNodes, roadNodes));
    }

    public void setLongestRoad(Player player) { //Sets longest road owner to player
        longestRoad = player;
    }

    public Player getLongestRoad() { //Gets the longest road player
        return longestRoad;
    }

    public void calcLongestRoad(List<Player> players) {
        Player owner = null;
        int longestRoad = 0;
        int length = 0;
        for (Player player: players) {
            length = actionController.calcLongestRoad(player);
            if (length > longestRoad) {
                owner = player;
                longestRoad = length;
            }
            else if (length == longestRoad) {
                owner = null;
            }
        }
        setLongestRoad(owner);
    }
}