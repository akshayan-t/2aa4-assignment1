import java.util.ArrayList;
import java.util.List;

public class Node {
    private int number;
    private Building building;
    private Player owner;
    private List<Road> roads = new ArrayList<>();
    private List<Integer> adjacentNodes = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();

    public Node(List<Integer> adjacentNodes) {
        this.adjacentNodes = new ArrayList<>(adjacentNodes);
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    } //Adds tile to connected tile list
    public int getTileSize() {
        return tiles.size();
    } //Gets number of tiles node is connected to
    public void setNumber(int number) {
        this.number = number;
    } //Sets node number

    public int getNumber() {
        return number;
    } //Gets number

    public void setBuilding(Building building) { //Sets building
        this.building = building;
    }

    public void setOwner(Player owner) { //Sets owner
        this.owner = owner;
    }

    public Building getBuilding() { //Gets building
        return building;
    }

    public Player getOwner() { //Gets owner
        return owner;
    }

    public void addRoad(Road road) { //Adds road to list
        this.roads.add(road);
    }

    public boolean checkRoadOwner(Player player) { //Checks if player owns a road connected to this node
        for (Road road : this.roads) {
            if (road.getOwner() == player) {
                return true;
            }
        }
        return false;
    }

    public List<Road> getRoads() { //Gets list of roads
        return roads;
    }

    public List<Integer> getAdjacentNodes() { //Gets adjacent nodes
        return adjacentNodes;
    }

    public String toString() {
        return String.valueOf(number);
    }
}
