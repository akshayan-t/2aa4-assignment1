import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlayGame {
    private Random rand = new Random();
    private Node[] nodes = new Node[54];
    private Tile[] tiles = new Tile[19];
    private Board board;
    private List<Player> players = new ArrayList<>();
    private int turn = 0;
    private int maxTurns;

    public PlayGame (int maxTurns, List<Player> players, Board board) {
        this.board = board;
        this.nodes = board.getNodes();
        this.tiles = board.getTiles();
        this.maxTurns = maxTurns;
        this.players = players;
    }

    public void runGame() {
        turn = 1;
        roundOne();
        printPoints();
        if (maxTurns > 1) {
            turn++;
            roundTwo();
            printPoints();
        }
        if (maxTurns > 2) {
            while (turn < maxTurns) {
                turn++;
                for (Player player : players) {
                    System.out.print("Round " + turn + " / ");
                    if (playRound(player)) {
                        System.out.println("Player " + player.getPlayerNumber() + " wins!\n");
                        printResults();
                        return;
                    }
                }
                printPoints();
            }
        }
        printResults();
    }

    public void printPoints() {
        System.out.print("Victory points: ");
        for (Player player : players) {
            System.out.print(player.getPoints() + ", ");
        }
        System.out.println();
    }

    public void printResults() {
        System.out.print("Final points: ");
        for (Player p : players) {
            System.out.print(p.getPoints() + ", ");
        }
        System.out.println("\n");
        System.out.println(players.get(0).getSettlementNodes());
        System.out.println(players.get(0).getCityNodes());
        for (Road road: players.get(0).getRoads()) {
            System.out.print(road.getStart() + " ");
        }
    }

    public int rollDice() {
        int die1 = rand.nextInt(6 - 1 + 1) + 1;
        int die2 = rand.nextInt(6 - 1 + 1) + 1;
        int number = die1 + die2;
        return number;
    }

    public int chooseNode() {
        int counter = 0;
        while (true) {
            counter += 1;
            int node = rand.nextInt(54); //Number from 0 to 53
            int tileCount = nodes[node].getTileSize();
            if (tileCount == 3) {
                return node;
            }
            else if (counter >= 5 && tileCount == 2) {
                return node;
            }
            else if (counter >= 10 && tileCount == 1) {
                return node;
            }

        }
    }

    public int chooseAdjacentNode(int node) {
        ArrayList<Integer> nodeList = (ArrayList)nodes[node].getAdjacentNodes();
        int adjNode = nodeList.get(rand.nextInt(nodeList.size()));
        return adjNode;
    }

    public ArrayList<Integer> getSettlementNodes(Player player) {
        ArrayList<Integer> nodes = new ArrayList<>();
        for (int i: player.getSettlementNodes()) {
            nodes.add(i);
        }
        return nodes;
    }

    public ArrayList<Integer> getRoadNodes(Player player) {
        ArrayList<Integer> nodes = new ArrayList<>();
        for (Road road: player.getRoads()) {
            nodes.add(road.getEnd());
        }
        return nodes;
    }

    public void roundOne() {
        for (Player player: players) {
            while (true) {
                int node = chooseNode();
                if (player.placeSettlement(node) == true) {
                    while (true) {
                        int end =  chooseAdjacentNode(node);
                        if (player.placeRoad(node, end) == true) {
                            System.out.println("Round 1 / " + player.getPlayerNumber() + ": built settlement at " + node + ", built road at (" + node + ", " + end + ")");
                            break;
                        }
                    }
                    player.checkWin();
                    break;
                }
            }
        }
    }

    public void roundTwo() {
        int counter = 0;
        for (int i=players.size()-1;i>=0;i--) {
            while (true) {
                int node = chooseNode();
                if (players.get(i).placeSettlement(node) == true) {
                    while (true) {
                        int end = chooseAdjacentNode(node);
                        if (players.get(i).placeRoad(node, end) == true) {
                            System.out.println("Round 2 / " + players.get(i).getPlayerNumber() + ": built settlement at " + node + ", built road at (" + node + ", " + end + ")");
                            break;
                        }
                    }
                    players.get(i).checkWin();
                    break;
                }
            }
        }
    }

    public boolean playRound(Player player) {
        System.out.print(player.getPlayerNumber() + ": ");
        makeResources();
        boolean built = false;
        int action = -100;
        ArrayList<Integer> settlementNodes = getSettlementNodes(player);
        ArrayList<Integer> roadNodes = getRoadNodes(player);

        if (player.getTotalResources() > 7) {
            ArrayList<Boolean> actions = player.checkActions(getSettlementNodes(player), getRoadNodes(player));
            while (actions.size() > 0) {
                if (player.checkWin()) {
                    return true;
                }
                action = rand.nextInt(actions.size());
                if (actions.get(action) == true) {
                    break;
                }
                else {
                    actions.remove(action);
                }
                action = -100;
            }
            if (action == 1) { //Build city
                while (settlementNodes.size() > 0) {
                    int random = rand.nextInt(settlementNodes.size());
                    int node = settlementNodes.get(random);
                    settlementNodes.remove(random);
                    if (player.buildCity(node)) {
                        System.out.print(", upgraded city at node " + node);
                        break;
                    }
                }
            }
            else if (action == 0 || action == 2) {
                while (roadNodes.size() > 0) {
                    if (player.checkWin()) {
                        return true;
                    }
                    int random = rand.nextInt(roadNodes.size());
                    int node = roadNodes.get(random);
                    roadNodes.remove(random);
                    if (action == 0) { //Build settlement
                        if (player.buildSettlement(node)) {
                            System.out.print(", built settlement at node " + node);
                            break;
                        }
                    }
                    else if (action == 2) { //Build road
                        for (int end : nodes[node].getAdjacentNodes()) {
                            if (player.buildRoad(node, end)) {
                                System.out.print(", built road at (" + node + ", " + end + ")");
                                break;
                            }
                        }
                    }
                }
            }
        }
//        System.out.println();
//        board.printResources();
        checkLongestRoad();
        System.out.println();
        return player.checkWin();
    }

    public void makeResources() {
        int number = rollDice();
        System.out.print("Rolled " + number);
        int total;
        Player owner1 = null;
        Player owner2 = null;
        switch (number) {
            case 6:
                owner1 = tiles[8].getOwner();
                owner2 = tiles[10].getOwner();
                total = tiles[8].getResourcesProduced(nodes) + tiles[10].getResourcesProduced(nodes);
                if (board.checkResources(Resource.ORE, -total) || (owner1 == owner2) || owner1 == null || owner2 == null) {
                    tiles[8].makeResources(nodes); //ore
                    tiles[10].makeResources(nodes); //ore
                }

                total = 0;
                break;
            case 7:
//                tiles[16].makeResources(nodes); //desert
                break;
            case 8:
                owner1 = tiles[2].getOwner();
                owner2 = tiles[14].getOwner();
                total = tiles[2].getResourcesProduced(nodes) + tiles[14].getResourcesProduced(nodes);
                if (board.checkResources(Resource.BRICK, -total) || (owner1 == owner2) || owner1 == null || owner2 == null) {
                    tiles[2].makeResources(nodes); //brick
                    tiles[14].makeResources(nodes); //brick
                }
                total = 0;
                break;
            default:
                for (Tile tile : tiles) {
                    if (number == tile.getNumber()) {
                        tile.makeResources(nodes);
                    }
                }
        }
    }

    public void checkLongestRoad() {
        Player owner = null;
        int longestRoad = 0;
        int length = 0;
        for (Player player: players) {
            length = player.calcLongestRoad();
            if (length > longestRoad) {
                owner = player;
                longestRoad = length;
            }
            else if (length == longestRoad) {
                owner = null;
            }
        }
        board.setLongestRoad(owner);
    }
}
