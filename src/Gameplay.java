import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Gameplay { //Class for running gameflow
    private Random rand = new Random();
    private Board board; //Board
    private List<Player> players = new ArrayList<>(); //List for players
    private int turn = 0; //Current turn
    private int maxTurns; //Max turns

    public Gameplay () { //Constructor
        this.board = new Board();
        this.maxTurns = Integer.parseInt(System.getenv("turns"));
        Player player1 = new Player(1, board);
        Player player2 = new Player(2, board);
        Player player3 = new Player(3, board);
        Player player4 = new Player(4, board);
        this.players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));
    }

    public Gameplay (int maxTurns, List<Player> players, Board board) { //Constructor for testing with customizable options
        this.board = board;
        this.maxTurns = maxTurns;
        this.players = players;
    }

    public void runGame() { //Gameflow
        if (maxTurns < 0 || maxTurns > 8192) { //If maxturns is valid
            System.out.println("Error: Turns must be between 0 and 8192");
            return;
        }
        turn = 1;
        board.setTurn(turn);
        roundOne(); //Round 1
        printPoints(); //Prints points
        if (maxTurns > 1) {
            turn++;
            board.setTurn(turn);
            roundTwo(); //Round 2
            printPoints();
        }
        if (maxTurns > 2) {
            while (turn < maxTurns) { //Plays game until turn reaches max turns
                turn++; //Increments turn each time
                board.setTurn(turn); //Updates board's turn
                for (Player player : players) { //For each player
                    System.out.print("Round " + turn + " / ");
                    if (playRound(player)) { //Plays round, if player wins
                        System.out.println();
                        System.out.println("Player " + player.getPlayerNumber() + " wins!\n");
                        printResults(); //Print win message and results
                        return;
                    }
                }
                printPoints(); //Prints points after each round
            }
        }
        printResults(); //Prints results if nobody wins
    }

    private int rollDice() { //Roll dice method
        int die1 = rand.nextInt(6 - 1 + 1) + 1; //Creates two dice from 1-6
        int die2 = rand.nextInt(6 - 1 + 1) + 1;
        int number = die1 + die2; //Returns rolled number
        return number;
    }

    private Node chooseNode() { //Chooses random node
        int counter = 0;
        while (true) { //While loop to choose node
            counter += 1;
            int node = rand.nextInt(54); //Number from 0 to 53
            int tileCount = board.getNodes(node).getTileSize();
            if (tileCount == 3) { //Prioritizes nodes connected to 3 tiles
                return board.getNodes(node);
            }
            else if (counter >= 5 && tileCount == 2) { //I
                return board.getNodes(node);
            }
            else if (counter >= 10 && tileCount == 1) {
                return board.getNodes(node);
            }

        }
    }

    private Node chooseAdjacentNode(Node node) {
        ArrayList<Integer> nodeList = (ArrayList)node.getAdjacentNodes();
        int adjNode = nodeList.get(rand.nextInt(nodeList.size()));
        return board.getNodes(adjNode);
    }

    private ArrayList<Node> getSettlementNodes(Player player) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node: player.getSettlements()) {
            nodes.add(node);
        }
        return nodes;
    }

    private ArrayList<Node> getRoadNodes(Player player) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Road road: player.getRoads()) {
            nodes.add(road.getEnd());
        }
        return nodes;
    }

    private void roundOne() {
        for (Player player: players) {
            while (true) {
                Node node = chooseNode();
                if (board.placeSettlement(player, node) == true) {
                    while (true) {
                        Node end = chooseAdjacentNode(node);
                        if (board.placeRoad(player, node, end) == true) {
                            System.out.println("Round 1 / " + player.getPlayerNumber() + ": built settlement at " + node + ", built road at (" + node + ", " + end + ")");
                            break;
                        }
                    }
                    isGameOver(player);
                    break;
                }
            }
        }
    }

    private void roundTwo() {
        int counter = 0;
        for (int i=players.size()-1;i>=0;i--) {
            while (true) {
                Node node = chooseNode();
                if (board.placeSettlement(players.get(i), node) == true) {
                    while (true) {
                        Node end = chooseAdjacentNode(node);
                        if (board.placeRoad(players.get(i), node, end) == true) {
                            System.out.println("Round 2 / " + players.get(i).getPlayerNumber() + ": built settlement at " + node + ", built road at (" + node + ", " + end + ")");
                            break;
                        }
                    }
                    isGameOver(players.get(i));
                    break;
                }
            }
        }
    }

    private boolean playRound(Player player) {
        System.out.print(player.getPlayerNumber() + ": ");
        makeResources();
        boolean built = false;
        int action = -100;
        ArrayList<Node> settlementNodes = getSettlementNodes(player);
        ArrayList<Node> roadNodes = getRoadNodes(player);

        if (player.getTotalResources() > 7) {
            List<Boolean> actions = board.checkActions(player, getSettlementNodes(player), getRoadNodes(player)); //Create new instances of settlement and road nodes to not alter originals
            while (actions.size() > 0 && player.getTotalResources() > 7) {
                if (isGameOver(player)) {
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
                    Node node = settlementNodes.get(random);
                    settlementNodes.remove(random);
                    if (board.upgradeCity(player, node)) {
                        System.out.print(", upgraded city at node " + node);
                        break;
                    }
                }
            }
            else if (action == 0 || action == 2) {
                while (roadNodes.size() > 0 && player.getTotalResources() > 7) {
                    if (isGameOver(player)) {
                        return true;
                    }
                    int random = rand.nextInt(roadNodes.size());
                    Node node = roadNodes.get(random);
                    roadNodes.remove(random);
                    if (action == 0) { //Build settlement
                        if (board.placeSettlement(player, node)) {
                            System.out.print(", built settlement at node " + node);
                            break;
                        }
                    }
                    else if (action == 2) { //Build road
                        for (int end : node.getAdjacentNodes()) {
                            if (board.placeRoad(player, node, board.getNodes(end))) {
                                System.out.print(", built road at (" + node + ", " + end + ")");
                                break;
                            }
                        }
                    }
                }
            }

        }
        board.calcLongestRoad(players);
        System.out.println();
        return isGameOver(player);
    }

    private void makeResources() {
        int number = rollDice();
        System.out.print("Rolled " + number);
        int total;
        Player owner1 = null;
        Player owner2 = null;
        switch (number) {
            case 6:
                owner1 = board.getTiles()[8].getOwner();
                owner2 = board.getTiles()[10].getOwner();
                total = board.getTiles()[8].getResourcesProduced() + board.getTiles()[10].getResourcesProduced();
                if (board.checkResources(Resource.ORE, -total) || (owner1 == owner2) || owner1 == null || owner2 == null) {
                    board.getTiles()[8].makeResources(board); //ore
                    board.getTiles()[10].makeResources(board); //ore
                }

                total = 0;
                break;
            case 7:
//                tiles[16].makeResources(nodes); //desert
                break;
            case 8:
                owner1 = board.getTiles()[2].getOwner();
                owner2 = board.getTiles()[14].getOwner();
                total = board.getTiles()[2].getResourcesProduced() + board.getTiles()[14].getResourcesProduced();
                if (board.checkResources(Resource.BRICK, -total) || (owner1 == owner2) || owner1 == null || owner2 == null) {
                    board.getTiles()[2].makeResources(board); //brick
                    board.getTiles()[14].makeResources(board); //brick
                }
                total = 0;
                break;
            default:
                for (Tile tile : board.getTiles()) {
                    if (number == tile.getNumber()) {
                        tile.makeResources(board);
                    }
                }
        }
    }

    public void printPoints() {
        System.out.print("Victory points: ");
        for (Player player : players) {
            System.out.print(player.calcPoints(players, board) + " ");
        }
        System.out.println();
    }

    public void printResults() {
        System.out.print("Final points: ");
        for (Player p : players) {
            System.out.print(p.calcPoints(players, board) + " ");
        }
    }

    public boolean isGameOver(Player player) {
        if (player.calcPoints(players, board) >= 10) {
            return true;
        }
        return false;
    }

}
