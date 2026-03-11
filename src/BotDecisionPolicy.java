import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotDecisionPolicy {
    private Random rand = new Random();

    public PlayerCommand chooseNextCommand(Gameplay game, TurnController turnController) {
        Board board = game.getBoard();
        Player player = game.getCurrentPlayer();
        List<Player> players = game.getPlayers();
        int action = -100; //Sets action out of bounds

        List<PlayerCommand> actions = board.checkActions(player, getSettlementNodes(player, board), getRoadNodes(player, board)); //Passes new instances of settlement and road nodes to not alter originals
        while (actions.size() > 0 && player.getTotalResources() > 7) { //While action can be done
            action = rand.nextInt(actions.size()); //Chooses random action
            return actions.get(action);
        }
        return null;
    }

    private ArrayList<Node> getSettlementNodes(Player player, Board board) { //Gets settlement nodes from player
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node: player.getSettlements()) {
            nodes.add(node);
        }
        return nodes;
    }

    private ArrayList<Node> getRoadNodes(Player player, Board board) { //Gets road nodes from player
        ArrayList<Node> nodes = new ArrayList<>();
        for (Road road: player.getRoads()) {
            nodes.add(road.getEnd());
        }
        return nodes;
    }
}
