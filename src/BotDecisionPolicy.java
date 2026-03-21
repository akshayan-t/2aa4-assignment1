/*
R3.3 Constraint Implementation Summary:

Constraint 1:
- If player has >7 resources, choose highest-cost legal action to spend resources.
- Verified through gameplay runs (triggered multiple times).

Constraint 2:
- Detects when two road segments are within two units and prioritizes connecting road.
- Verified using a controlled test (Constraint2Test).

Constraint 3:
- If another player’s longest road is at most one less than current player,
  bot prioritizes building a connected road.
- Verified using a controlled test (Constraint3Test).

Constraints are evaluated in priority order before R3.2 scoring.
*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BotDecisionPolicy {
    private Random rand = new Random();

    public PlayerCommand chooseNextCommand(Gameplay game, TurnController turnController, MoveScoringStrategy strategy) {
        Board board = game.getBoard();
        Player player = game.getCurrentPlayer();

        List<PlayerCommand> legalActions = board.getLegalActions(player, getSettlementNodes(player), getRoadNodes(player));

        if (legalActions.isEmpty()) {
            return null;
        }

        // R3.3 constraints (priority)
        PlayerCommand constraintCommand = chooseConstraintCommand(game, turnController, legalActions);
        if (constraintCommand != null) {
            return constraintCommand;
        }

        // R3.2 scoring fallback
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

        return bestMove;
    }

    private PlayerCommand chooseConstraintCommand(Gameplay game, TurnController turnController, List<PlayerCommand> legalActions) {
        Player player = game.getCurrentPlayer();
        Board board = game.getBoard();
        List<Player> players = game.getPlayers();

        // Constraint 1: spend resources if > 7
        if (player.getTotalResources() > 7) {
            PlayerCommand spendingAction = chooseBestSpendingAction(legalActions);
            if (spendingAction != null) {
                return spendingAction;
            }
        }

        // Constraint 2: connect nearby road segments
        BuildRoadCommand connectingRoad = findRoadConnectingSegments(player, board, legalActions);
        if (connectingRoad != null) {
            return connectingRoad;
        }

        // Constraint 3: extend road if longest road is threatened
        if (isLongestRoadThreatened(player, players, turnController)) {
            BuildRoadCommand extensionRoad = findAnyLegalRoad(legalActions, game, turnController);
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

        if (startTouchesOwnedRoad && endTouchesOwnedRoad) {
            return true;
        }

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

            if (otherLongestRoad >= playerLongestRoad - 1) {
                return true;
            }
        }

        return false;
    }

    private BuildRoadCommand findAnyLegalRoad(List<PlayerCommand> legalActions, Gameplay game, TurnController turnController) {
        BuildRoadCommand roadCmd = null;
        for (PlayerCommand cmd : legalActions) {
            if (cmd instanceof BuildRoadCommand) {
                roadCmd = (BuildRoadCommand) cmd;
                if (roadCmd.extendsLongestRoad(game, turnController)) {
                    return roadCmd;
                }
            }
        }
        return roadCmd;
    }

    private ArrayList<Node> getSettlementNodes(Player player) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : player.getSettlements()) {
            nodes.add(node);
        }
        return nodes;
    }

    private ArrayList<Node> getRoadNodes(Player player) {
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
