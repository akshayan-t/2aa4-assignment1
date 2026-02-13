import java.util.*;

public class Player {
    private int playerNumber;

    private HashMap<Resource, Integer> resources = new HashMap<>();

    private List<Node> settlements = new ArrayList<>();
    private List<Node> cities = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();
    private List<Building> buildings = new ArrayList<>();
    private PlacementRules rules = new PlacementRules();

    public Player(int playerNumber, Board board) { //Constructor
        this.playerNumber = playerNumber;
        resources.put(Resource.WOOD, 0);
        resources.put(Resource.BRICK, 0);
        resources.put(Resource.WHEAT, 0);
        resources.put(Resource.SHEEP, 0);
        resources.put(Resource.ORE, 0);
    }

    public int getResources(Resource resource) {
        return resources.get(resource);
    } //Gets resources

    public void updateResources(Resource resource, int change) { //Updates resources
        int amount = resources.get(resource);
        resources.put(resource, amount + change);
    }

    public List<Building> getBuildings() {
        return buildings;
    } //Gets buildings

    public List<Node> getSettlements() { //Gets settlements
        return settlements;
    }

    public List<Node> getCities() { //Gets settlements
        return cities;
    }

    public int getSettlementCount() { //Gets settlements
        int counter = 0;
        for (Building building: buildings) {
            if (building instanceof Settlement) {
                counter++;
            }
        }
        return counter;
    }

    public int getCityCount() { //Gets cities
        int counter = 0;
        for (Building building: buildings) {
            if (building instanceof City) {
                counter++;
            }
        }
        return counter;
    }

    public void addBuilding(Building building) {
        this.buildings.add(building);
    }

    public void addSettlement(Node node) {
        this.settlements.add(node);
    }

    public void addCity(Node node) {
        this.cities.add(node);
    }

    public void addRoad(Road road) {
        this.roads.add(road);
    }

    public void removeSettlement(Node node) {
        if (getCities().contains(node)) {
            this.settlements.remove(node);
        }
    }

    public int calcPoints(List<Player> players, Board board) {
        int points = 0;
        for (Building building: buildings) {
            points += building.getPoints();
        }
        board.calcLongestRoad(players);
        if (board.getLongestRoad() == this) {
            points += 2;
        }
        return points;
    }

    public int getTotalResources() {
        int total = resources.get(Resource.BRICK) + resources.get(Resource.WOOD) + resources.get(Resource.SHEEP) + resources.get(Resource.WHEAT) + resources.get(Resource.ORE);
        return total;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void printBuildings() {
        System.out.println("Player " + playerNumber + " buildings");
        System.out.println("Settlements: " + getSettlements());
        System.out.println("Cities: " + getCities());
        System.out.print("Roads: ");
        for (Road road: getRoads()) {
            road.printRoad();
        }
    }

    public void printResources() {
        System.out.println("\nPlayer " + playerNumber + " resources");
        for (Resource resource: Resource.values()) {
            System.out.println(resource + ": " + resources.get(resource));
        }
    }
}
