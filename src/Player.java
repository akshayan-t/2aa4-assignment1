import java.util.*;

public class Player {
    private int playerNumber;

    private HashMap<Resource, Integer> resources = new HashMap<>();

    private int points = 0;
    private int longestRoad = 0;

    private List<Integer> settlements = new ArrayList<>();
    private List<Integer> cities = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();

    private Tile[] tiles;
    private Node[] nodes;
    private Board board;

    private List<Building> buildings = new ArrayList<>();

    public Player(int playerNumber, Board board) {
        this.playerNumber = playerNumber;
        this.board = board;
        this.tiles = board.getTiles();
        this.nodes = board.getNodes();

        resources.put(Resource.WOOD, 0);
        resources.put(Resource.BRICK, 0);
        resources.put(Resource.WHEAT, 0);
        resources.put(Resource.SHEEP, 0);
        resources.put(Resource.ORE, 0);
    }
    public int getResources(Resource resource) {
        return resources.get(resource);
    }

    public void updateResources(Resource resource, int change) {
        int amount = resources.get(resource);
        resources.put(resource, amount + change);
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public int getSettlements() {
        int counter = 0;
        for (Building building: buildings) {
            if (building instanceof Settlement) {
                counter++;
            }
        }
        return counter;
    }

    public int getCities() {
        int counter = 0;
        for (Building building: buildings) {
            if (building instanceof City) {
                counter++;
            }
        }
        return counter;
    }

    public boolean checkSettlement(int node) {
        if (nodes[node].getOwner() == null) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean canBuildSettlement(int node) {
        for (Road road: roads) {
            if (road.getStart() == node && checkSettlement(node) && checkDistance(node) && getSettlements() < 5) {
                if (resources.get(Resource.BRICK) >= 1 && resources.get(Resource.WOOD) >= 1 && resources.get(Resource.SHEEP) >= 1 && resources.get(Resource.WHEAT) >= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean buildSettlement(int node) {
    if (canBuildSettlement(node)) {
        Settlement settlement = new Settlement(this);
        settlements.add(node);
        buildings.add(settlement);
        nodes[node].setBuilding(settlement);
        nodes[node].setOwner(this);

        resources.put(Resource.BRICK, resources.get(Resource.BRICK) - 1);
        resources.put(Resource.WOOD, resources.get(Resource.WOOD) - 1);
        resources.put(Resource.SHEEP, resources.get(Resource.SHEEP) - 1);
        resources.put(Resource.WHEAT, resources.get(Resource.WHEAT) - 1);

        board.updateResources(Resource.BRICK, 1);
        board.updateResources(Resource.WOOD, 1);
        board.updateResources(Resource.SHEEP, 1);
        board.updateResources(Resource.WHEAT, 1);
        return true;
    }
    return false;
    }

    public boolean canBuildCity(int node) {
        if (settlements.contains(node) && getCities() < 4) {
            if (resources.get(Resource.ORE) >= 3 && resources.get(Resource.WHEAT) >= 2) {
                return true;
            }
        }
        return false;
    }

    public boolean buildCity(int node) {
        if (canBuildCity(node)) {
            buildings.remove(nodes[node].getBuilding());
            City city = new City(this);
            nodes[node].setBuilding(city);
            buildings.add(city);
            settlements.remove(Integer.valueOf(node));
            cities.add(node);

            resources.put(Resource.ORE, resources.get(Resource.ORE) - 3);
            resources.put(Resource.WHEAT, resources.get(Resource.WHEAT) - 1);
            board.updateResources(Resource.ORE, 3);
            board.updateResources(Resource.WHEAT, 2);
            return true;
        }
        return false;
    }

    public boolean checkRoad(int node, int end) {
        if ((nodes[node].checkRoadOwner(this))) { //Checks if player doesn't own road at current node
            for (Road road: nodes[node].getRoads()) {
                if (road.getEnd() == end) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkDistance(int node) {
        for (int i : nodes[node].getAdjacentNodes()) {
            if ((nodes[i].getOwner() != null)) {
                return false;
            }
        }
        return true;
    }

    public boolean canBuildRoad(int start, int end) {
        if (checkRoad(start, end) && resources.get(Resource.BRICK) >= 1 && resources.get(Resource.WOOD) >= 1) {
            return canPlaceRoad(start, end);
        }
        return false;
    }

    public boolean canPlaceRoad(int start, int end) {
        if (roads.size() < 15) {
            for (Road currentRoad: board.getRoads()) {
                if ((currentRoad.getStart() == start && currentRoad.getEnd() == end) || (currentRoad.getStart() == end && currentRoad.getEnd() == start)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean buildRoad(int start, int end) {
        if (canBuildRoad(start, end)) {
            Road road = new Road(start, end, this);
            nodes[start].addRoad(road);
            nodes[end].addRoad(road);
            roads.add(road);
            board.addRoad(road);

            resources.put(Resource.BRICK, resources.get(Resource.BRICK) - 1);
            resources.put(Resource.WOOD, resources.get(Resource.WOOD) - 1);
            board.updateResources(Resource.BRICK, 1);
            board.updateResources(Resource.WOOD, 1);
            return true;
        }
        return false;
    }

    public boolean placeSettlement(int node) { //For start of game
        if (checkSettlement(node) && checkDistance(node) && getSettlements() < 5) {
            Settlement settlement = new Settlement(this);
            buildings.add(settlement);
            nodes[node].setBuilding(settlement);

            settlements.add(node);
//            nodes[node].buildSettlement(this);
            nodes[node].setOwner(this);
            return true;
        }
        else {
//            System.out.println("Cannot build settlement at node " + node);
            return false;
        }
    }

    public boolean placeRoad(int start, int end) { //For start of game
        if ((settlements.contains(start) || checkRoad(start, end)) && canPlaceRoad(start, end) && roads.size() < 15) {
            Road road = new Road(start, end, this);
            nodes[start].addRoad(road);
            nodes[end].addRoad(road);
            roads.add(road);
            board.addRoad(road);
            return true;
        }
        return false;
    }


    public boolean checkWin() {
        points = 0;
        for (Building building: buildings) {
            points += building.getPoints();
        }
        if (board.getLongestRoad() == this) {
            points += 2;
        }
        if (points >= 10) {
            return true;
        }
        else {
            return false;
        }
    }
    public int getPoints() {
        return points;
    }

    public int getTotalResources() {
        int total = resources.get(Resource.BRICK) + resources.get(Resource.WOOD) + resources.get(Resource.SHEEP) + resources.get(Resource.WHEAT) + resources.get(Resource.ORE);
        return total;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public List<Integer> getSettlementNodes() {
        return settlements;
    }

    public List<Integer> getCityNodes() {
        return cities;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public ArrayList <Boolean> checkActions(ArrayList<Integer> settlementNodes, ArrayList<Integer> roadNodes) {
        Random rand = new Random();
        boolean settlement = false;
        boolean city = false;
        boolean road = false;
        while (settlementNodes.size() > 0) {
            int random = rand.nextInt(settlementNodes.size());
            int node = settlementNodes.get(random);
            settlementNodes.remove(random);
            if (canBuildCity(node)) {
                city = true;
                break;
            }
        }
        while (roadNodes.size() > 0) {
            int random = rand.nextInt(roadNodes.size());
            int node = roadNodes.get(random);
            roadNodes.remove(random);
            if (canBuildSettlement(node) && settlement == false) {
                settlement = true;
            }
            if (road == false) {
                for (int end : nodes[node].getAdjacentNodes()) {
                    if (canBuildRoad(node, end)) {
                        road = true;
                    }
                }
            }
            if (settlement == true && road == true) {
                break;
            }
        }
        return new ArrayList<>(Arrays.asList(settlement, city, road));
    }

    public int getLongestRoad(int max, Road road, Set<Road> checkedRoads, int startNode) {
        max += 1;
        int newMax = max;
        checkedRoads.add(road);
        int length = 0;
        int nextNode = 0;
        if (startNode == road.getStart()) {
            nextNode = road.getEnd();
        }
        else if (startNode == road.getEnd()) {
            nextNode = road.getStart();
        }
        Player settlement = nodes[nextNode].getOwner();
        if (settlement == null || settlement == this) {
            for (Road nextRoad : nodes[nextNode].getRoads()) {
                if (nextRoad != road && !checkedRoads.contains(nextRoad) && nextRoad.getOwner() == this) {
                    length = getLongestRoad(max, nextRoad, checkedRoads, nextNode);
                    if (length > newMax) {
                        newMax = length;
                    }
                }
            }
        }
        return newMax;
    }

    public int calcLongestRoad() {
        int max = 0;
        for  (Road road: roads) {
            Set<Road> checkedRoads = new HashSet<>();
            int length = getLongestRoad(0, road, checkedRoads, road.getStart());
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
