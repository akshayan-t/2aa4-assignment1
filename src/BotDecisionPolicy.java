import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BotDecisionPolicy { //Chooses command for computer
    private Random rand = new Random();
    private BenefitScoringStrategy strategy = new BenefitScoringStrategy();

    public PlayerCommand chooseNextCommand(Gameplay game, TurnController turnController, CommandManager commandManager) { //Chooses next command
        Board board = game.getBoard();
        Player player = game.getCurrentPlayer();

        List<PlayerCommand> legalActions = board.getLegalActions(player, getSettlementNodes(player), getRoadNodes(player));

        if (legalActions.isEmpty()) {
            return null;
        }

        // R3.3: constraints must be resolved before value-based actions
        PlayerCommand constraintCommand = chooseConstraintCommand(game, turnController, legalActions);
        if (constraintCommand != null) {
            return constraintCommand;
        }

        // R3.2: evaluate benefit of each legal action
        PlayerCommand bestMove = null;
        double maxVal = -1.0;
        List<PlayerCommand> tiedMoves = new ArrayList<>();

        for (PlayerCommand cmd : legalActions) {
            double currentVal = strategy.calculateValue(cmd, game, turnController);

            if (currentVal > maxVal) {
                maxVal = currentVal;
                bestMove = cmd;
                tiedMoves.clear();
                tiedMoves.add(cmd);
            } else if (currentVal == maxVal) {
                tiedMoves.add(cmd);
            }
        }

        if (!tiedMoves.isEmpty()) {
            return tiedMoves.get(rand.nextInt(tiedMoves.size()));
        }

        return bestMove; //Returns null if no actions available
    }

    private PlayerCommand chooseConstraintCommand(Gameplay game, TurnController turnController, List<PlayerCommand> legalActions) {
        Player player = game.getCurrentPlayer();
        Board board = game.getBoard();
        List<Player> players = game.getPlayers();

        // R3.3.1: If there are more than 7 cards, the agent must spend them
        if (player.getTotalResources() > 7) {
            PlayerCommand spendingAction = chooseBestSpendingAction(legalActions);
            if (spendingAction != null) {
                return spendingAction;
            }
        }

        // R3.3.2: If there exist two road segments that are maximum two units away,
        // the agent should try to buy roads to connect the segments
        BuildRoadCommand connectingRoad = findRoadConnectingSegments(player, board, legalActions);
        if (connectingRoad != null) {
            return connectingRoad;
        }

        // R3.3.3: If other players have a longest road that is at most one road shorter
        // than the agent's, the agent should buy a connected road segment
        if (isLongestRoadThreatened(player, players, turnController)) {
            BuildRoadCommand extensionRoad = findAnyLegalRoad(legalActions);
            if (extensionRoad != null) {
                return extensionRoad;
            }
        }

        return null;
    }

    private PlayerCommand chooseBestSpendingAction(List<PlayerCommand> legalActions) {
        PlayerCommand best = null;
        int maxCost = -1;

        for (PlayerCommand cmd : legalActions) {
            if (cmd.getCost() > maxCost) {
                maxCost = cmd.getCost();
                best = cmd;
            }
        }

        return best;
    }

    private BuildRoadCommand findRoadConnectingSegments(Player player, Board board, List<PlayerCommand> legalActions) {
        for (PlayerCommand cmd : legalActions) {
            if (!(cmd instanceof BuildRoadCommand)) {
                continue;
            }

            BuildRoadCommand roadCmd = (BuildRoadCommand) cmd;
            Node start = board.getNodes(roadCmd.getStart());
            Node end = board.getNodes(roadCmd.getEnd());

            if (connectsNearbyRoadSegments(player, board, start, end)) {
                return roadCmd;
            }
        }

        return null;
    }

    private boolean connectsNearbyRoadSegments(Player player, Board board, Node start, Node end) {
        boolean startTouchesOwnedRoad = start.checkRoadOwner(player);
        boolean endTouchesOwnedRoad = end.checkRoadOwner(player);

        // Connects two existing parts of the player's road network
        if (startTouchesOwnedRoad && endTouchesOwnedRoad) {
            return true;
        }

        // Approximates "maximum two units away":
        // one endpoint touches owned road, the other is one node away from owned road
        if (startTouchesOwnedRoad && isOneStepFromOwnedRoad(player, board, end, start.getNumber())) {
            return true;
        }

        if (endTouchesOwnedRoad && isOneStepFromOwnedRoad(player, board, start, end.getNumber())) {
            return true;
        }

        return false;
    }

    private boolean isOneStepFromOwnedRoad(Player player, Board board, Node node, int excludedNode) {
        for (int adjacent : node.getAdjacentNodes()) {
            if (adjacent == excludedNode) {
                continue;
            }

            Node adjacentNode = board.getNodes(adjacent);
            if (adjacentNode.checkRoadOwner(player)) {
                return true;
            }
        }

        return false;
    }

    private boolean isLongestRoadThreatened(Player player, List<Player> players, TurnController turnController) {
        int playerLongestRoad = turnController.calcLongestRoad(player);

        for (Player other : players) {
            if (other == player) {
                continue;
            }

            int otherLongestRoad = turnController.calcLongestRoad(other);

            // "other players have a longest road that is at most one road shorter"
            // Example: player has 7, other has 6 -> build road
            if (otherLongestRoad >= playerLongestRoad - 1) {
                return true;
            }
        }

        return false;
    }

    private BuildRoadCommand findAnyLegalRoad(List<PlayerCommand> legalActions) {
        for (PlayerCommand cmd : legalActions) {
            if (cmd instanceof BuildRoadCommand) {
                return (BuildRoadCommand) cmd;
            }
        }
        return null;
    }

    private ArrayList<Node> getSettlementNodes(Player player) { //Gets settlement nodes from player
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : player.getSettlements()) {
            nodes.add(node);
        }
        return nodes;
    }

    private ArrayList<Node> getRoadNodes(Player player) { //Gets all unique road nodes from player
        ArrayList<Node> nodes = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        for (Road road : player.getRoads()) {
            if (!seen.contains(road.getStart().getNumber())) {
                nodes.add(road.getStart());
                seen.add(road.getStart().getNumber());
            }
            if (!seen.contains(road.getEnd().getNumber())) {
                nodes.add(road.getEnd());
                seen.add(road.getEnd().getNumber());
            }
        }

        return nodes;
    }
}
