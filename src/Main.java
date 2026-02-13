import java.util.*;

public class Main {
    public void testOne() { //Testing building and wins
        System.out.println("Test One");
        int turns = Integer.parseInt(System.getenv("turns"));
        Board board = new Board();
        Player player1 = new Player(1, board);

        List<Player> players = new ArrayList<>(Arrays.asList(player1));

        player1.updateResources(Resource.WOOD, 100);
        player1.updateResources(Resource.BRICK, 100);
        player1.updateResources(Resource.WHEAT, 100);
        player1.updateResources(Resource.SHEEP, 100);
        player1.updateResources(Resource.ORE, 100);

        board.placeSettlement(player1, 17);
        board.upgradeCity(player1, 17);
        board.placeRoad(player1, 17, 18);
        board.placeRoad(player1, 17, 500); //Testing impossible

        Gameplay play = new Gameplay(turns, players, board);
        play.runGame(); //Since player1 has > 7 resources, it will do at least one action every turn
        player1.printBuildings();
    }

    public void testTwo() { //Testing Resources
        System.out.println("\n\nTest Two");
        Board board = new Board();
        Player player1 = new Player(1, board);

        List<Player> players = new ArrayList<>(Arrays.asList(player1));

        player1.updateResources(Resource.WOOD, 10);
        player1.updateResources(Resource.BRICK, 10);
        player1.updateResources(Resource.WHEAT, 10);
        player1.updateResources(Resource.SHEEP, 10);
        player1.updateResources(Resource.ORE, 10);

        player1.printResources(); //Shows current resource amounts
        board.printResources();

        board.placeSettlement(player1, 17);
        board.placeRoad(player1, 17, 18);
        board.placeSettlement(player1, 2);
        board.placeRoad(player1, 2, 3);

        player1.printResources(); //No changes since setup turn
        board.printResources();

        board.upgradeCity(player1, 17);

        player1.printResources(); //-1 wheat -2 ore
        board.printResources(); //+1 wheat +2 ore

        board.setTurn(3); //Resources start being taken
        board.placeRoad(player1, 3, 4); // -1 wood and -1 brick
        board.placeSettlement(player1, 4); // -1 for wood, brick, wheat, sheep

        player1.printResources(); //In total, -2 wood, -2 brick, +1 wheat, +1 sheep
        board.printResources(); // +2 wood, +2 brick, -1 wheat, -1 sheep

        player1.printBuildings();

        System.out.println("Tile 0 generating resources");
        board.getTiles()[0].makeResources(board); //+2 wood

        player1.printResources(); //+2 wood
        board.printResources(); //-2 wood;
    }

    public void testThree() {
        System.out.println("\n\nTest Three");
        System.out.println();
        int turns = Integer.parseInt(System.getenv("turns"));
        Board board = new Board();
        ActionController controller = new ActionController(board);
        Player player1 = new Player(1, board);
        Player player2 = new Player(2, board);

        List<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
        Gameplay play = new Gameplay(turns, players, board);

        board.placeRoad(player1, 0, 1);
        board.placeRoad(player1, 0, 20);
        board.placeRoad(player1, 20, 19);
        board.placeRoad(player1, 20, 22);
        board.placeRoad(player1, 1, 2);
        board.placeRoad(player1, 2, 3);
        board.placeRoad(player1, 0, 5);
        board.calcLongestRoad(players);
        board.printLongestRoad(); //Longest road is 5

        play.printPoints();

        board.placeSettlement(player2, 0); //If other player settlement blocks road
        board.calcLongestRoad(players);
        board.printLongestRoad(); //Longest road is now 3
        play.printPoints(); //Player 1 loses longest road points
    }

    public void testFour() { //Showing build limit
        System.out.println("\n\nTest Four");
        Board board = new Board();
        Player player1 = new Player(1, board);

        board.placeSettlement(player1, 18);
        board.placeSettlement(player1, 4);
        board.placeSettlement(player1, 9);
        board.placeSettlement(player1, 33);
        board.placeSettlement(player1, 41);
        board.placeSettlement(player1, 52);
        board.placeSettlement(player1, 0);
        player1.printBuildings(); //Max 5 settlements
    }

    public void playGame() {
        System.out.println("\n\nTest Gameplay");
        Gameplay play = new Gameplay();
        play.runGame();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.testOne();
        main.testTwo();
        main.testThree();
        main.testFour();
        main.playGame();
    }
}
