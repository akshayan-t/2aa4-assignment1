import java.util.ArrayList;
import java.util.List;

public class Node {
    private int number;
    private Building building;
    private Player owner;
    private List<Road> roads = new ArrayList<>();
    private List<Integer> adjacentNodes = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();

    public Node(List<Integer> adjacentNodes) {
        this.adjacentNodes = new ArrayList<>(adjacentNodes);
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }
    public int getTileSize() {
        return tiles.size();
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setResources() {
        for (Tile tile: tiles) {
            resources.add(tile.getResource());
        }
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Building getBuilding() {
        return building;
    }

    public Player getOwner() {
        return owner;
    }

    public void addRoad(Road road) {
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

    public List<Road> getRoads() {
        return roads;
    }

    public List<Integer> getAdjacentNodes() {
        return adjacentNodes;
    }
}
