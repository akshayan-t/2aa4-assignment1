import java.util.ArrayList;
import java.util.List;

public class Tile {
    private int number;
    private Resource resource;
    private List<Node> nodes = new ArrayList<>();
    private List<Integer> nodeLocations = new ArrayList<>();

    public Tile(List<Integer> nodeLocations) {
        this.nodeLocations = new ArrayList<>(nodeLocations);
    } //Constructor

    public void setNodeTiles(Board board) { //Sets node tiles
        for (int position: nodeLocations) {
            nodes.add(board.getNodes(position));
            board.getNodes(position).addTile(this);
        }
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    } //Sets resources

    public Resource getResource() {
        return resource;
    } //Gets resources

    public void print() { //Prints tiles + resrouce
        System.out.println("Tile " + number + " Resource: " + resource);
        for (int node: nodeLocations) {
            System.out.print(node + " ");
        }
        System.out.println();
    }

    public void setNumber(int number) {
        this.number = number;
    } //Sets tile number

    public int getNumber() {
        return number;
    } //Gets tile number

    public List<Node> getNodes() {
        return nodes;
    } //Gets connected nodes

    public void makeResources(Board board) { //Makes resources for each connected node
        int total = 0;
        Player owner = getOwner();

        for (Node node : nodes) {
            if (node.getBuilding() != null) {
                total += node.getBuilding().getResourceMult();
            }
        }

        if (board.checkResources(resource, -total)) {
            for (Node node : nodes) {
                if (node.getBuilding() != null) {
                    int change = node.getBuilding().getResourceMult();
                    Player player = node.getBuilding().getOwner();
                    player.updateResources(resource, change);
                    board.updateResources(resource, -change);
                }
            }
        }
        else if (owner != null) {
            int resources = board.getResources(resource);
            owner.updateResources(resource, resources);
            board.updateResources(resource, -resources);
        }
    }

    public int getResourcesProduced() { //Gets how many resources would be produced by the tile
        int total = 0;
        for (Node node : nodes) {
            if (node.getBuilding() != null) {
                total += node.getBuilding().getResourceMult();
            }
        }
        return total;
    }

    public Player getOwner () { //Checks if only one player owns buildings at a tile
        Player owner = null;
        Player currentPlayer = null;
        for (Node node: nodes) {
            currentPlayer = node.getOwner();
            if (owner == null)  {
                owner = currentPlayer;
            }
            else if (owner != currentPlayer && currentPlayer != null) {
                return null;
            }
        }
        return owner;
    }
}
