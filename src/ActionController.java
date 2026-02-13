import java.util.*;

public class ActionController {
    private Board board;
    private PlacementRules rules = new PlacementRules();

    public ActionController(Board board) {
        this.board = board;
    }

    public boolean handleBuildSettlement(Player player, Node node, int turn) {
        if (turn <= 2) {
            if (!rules.canPlaceSettlement(player, node, board)) {
                return false;
            }
        }
        else if (!rules.canBuildSettlement(player, node, board)) {
            return false;
        }
        Settlement settlement = new Settlement(player, node);
        player.addSettlement(node);
        player.addBuilding(settlement);
        node.setBuilding(settlement);
        node.setOwner(player);

        if (turn > 2) {
            player.updateResources(Resource.BRICK, player.getResources(Resource.BRICK) - 1);
            player.updateResources(Resource.WOOD, player.getResources(Resource.WOOD) - 1);
            player.updateResources(Resource.SHEEP, player.getResources(Resource.SHEEP) - 1);
            player.updateResources(Resource.WHEAT, player.getResources(Resource.WHEAT) - 1);

            board.updateResources(Resource.BRICK, 1);
            board.updateResources(Resource.WOOD, 1);
            board.updateResources(Resource.SHEEP, 1);
            board.updateResources(Resource.WHEAT, 1);
        }
        return true;
    }

    public boolean handleUpgradeCity(Player player, Node node) {
        if (rules.canUpgradeCity(player, node)) {
            player.getBuildings().remove(node.getBuilding());
            City city = new City(player, node);
            node.setBuilding(city);
            player.addBuilding(city);
            player.addCity(node);
            player.removeSettlement(node);

            player.updateResources(Resource.ORE, player.getResources(Resource.ORE) - 3);
            player.updateResources(Resource.WHEAT, player.getResources(Resource.WHEAT) - 1);
            board.updateResources(Resource.ORE, 3);
            board.updateResources(Resource.WHEAT, 2);
            return true;
        }
        return false;
    }

    public boolean handleBuildRoad(Player player, Node start, Node end, int turn) {
        if (turn <= 2) {
            if (!rules.canPlaceRoad(player, start, end, board)) {
                return false;
            }
        }
        else if (!rules.canBuildRoad(player, start, end, board)) {
            return false;
        }
        Road road = new Road(start, end, player);
        start.addRoad(road);
        end.addRoad(road);
        player.addRoad(road);
        board.addRoad(road);

        if (turn > 2) {
            player.updateResources(Resource.BRICK, player.getResources(Resource.BRICK) - 1);
            player.updateResources(Resource.WOOD, player.getResources(Resource.WOOD) - 1);
            board.updateResources(Resource.BRICK, 1);
            board.updateResources(Resource.WOOD, 1);
        }
        return true;
    }

    public List<Boolean> checkActions(Player player, List<Node> settlementNodes, List<Node> roadNodes) {
        Random rand = new Random();
        boolean settlement = false;
        boolean city = false;
        boolean road = false;
        while (settlementNodes.size() > 0) {
            int random = rand.nextInt(settlementNodes.size());
            Node node = settlementNodes.get(random);
            settlementNodes.remove(random);
            if (rules.canUpgradeCity(player, node)) {
                city = true;
                break;
            }
        }
        while (roadNodes.size() > 0) {
            int random = rand.nextInt(roadNodes.size());
            Node node = roadNodes.get(random);
            roadNodes.remove(random);
            if (rules.canBuildSettlement(player, node, board) && settlement == false) {
                settlement = true;
            }
            if (road == false) {
                for (int end : node.getAdjacentNodes()) {
                    if (rules.canBuildRoad(player, node, board.getNodes(end), board)) {
                        road = true;
                    }
                }
            }
            if (settlement == true && road == true) {
                break;
            }
        }
        return Arrays.asList(settlement, city, road);
    }

    private int getLongestRoad(Player player, int max, Road road, Set<Road> checkedRoads, Node startNode) {
        max += 1;
        int newMax = max;
        checkedRoads.add(road);
        int length = 0;
        Node nextNode = null;
        if (startNode == road.getStart()) {
            nextNode = road.getEnd();
        }
        else if (startNode == road.getEnd()) {
            nextNode = road.getStart();
        }
        Player settlement = nextNode.getOwner();
        if (settlement == null || settlement == player) {
            for (Road nextRoad : nextNode.getRoads()) {
                if (nextRoad != road && !checkedRoads.contains(nextRoad) && nextRoad.getOwner() == player) {
                    length = getLongestRoad(player, max, nextRoad, checkedRoads, nextNode);
                    if (length > newMax) {
                        newMax = length;
                    }
                }
            }
        }
        return newMax;
    }

    public int calcLongestRoad(Player player) {
        int max = 0;
        for  (Road road: player.getRoads()) {
            Set<Road> checkedRoads = new HashSet<>();
            int length = getLongestRoad(player, 0, road, checkedRoads, road.getStart());
            if (length > max) {
                max = length;
            }
//            System.out.println("Current Road: " + road.getStart() + ", " + road.getEnd());
//            System.out.println("Longest Road: " + max);
        }
//        System.out.println("Longest Road: " + max);
        return max;
    }

}
