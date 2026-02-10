void main() {
    Board board = new Board();
    Player player1 = new Player(1, board);
    Player player2 = new Player(2, board);
    Player player3 = new Player(3, board);
    Player player4 = new Player(4, board);

    List<Player> players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));

    player1.placeSettlement(17);
    player1.placeSettlement(27);
    player1.placeSettlement(40);

    PlayGame play = new PlayGame(20, players, board);
    play.runGame();

//    int turn = 1;
//    play.roundOne();
//    System.out.println("Victory points: " + player1.getPoints() + ", " + player2.getPoints() + ", " + player3.getPoints() + ", " + player4.getPoints());
//
//    turn = 2;
//    play.roundTwo();
//    System.out.println("Victory points: " + player1.getPoints() + ", " + player2.getPoints() + ", " + player3.getPoints() + ", " + player4.getPoints());
//
//    turn = 3;
//    boolean win;
//    for (int i=0; i<= 100; i++) {
//        System.out.print("Round " + turn + " / ");
//        if (play.playRound(player1)) {
//            System.out.println("Player 1 wins!\n");
//            break;
//        }
//        System.out.print("Round " + turn + " / ");
//        if (play.playRound(player2)) {
//            System.out.println("Player 2 wins!\n");
//            break;
//        }
//        System.out.print("Round " + turn + " / ");
//        if (play.playRound(player3)) {
//            System.out.println("Player 3 wins!\n");
//            break;
//        }
//        System.out.print("Round " + turn + " / ");
//        if (play.playRound(player4)) {
//            System.out.println("Player 4 wins!\n");
//            break;
//        }
//        System.out.println("Victory points: " + player1.getPoints() + ", " + player2.getPoints() + ", " + player3.getPoints() + ", " + player4.getPoints());
//        turn++;
//    }
//    System.out.println("Player 1 points: " + player1.getPoints());
//    System.out.println("Player 2 points: " + player2.getPoints());
//    System.out.println("Player 3 points: " + player3.getPoints());
//    System.out.println("Player 4 points: " + player4.getPoints());
//    System.out.println("Final points: " + player1.getPoints() + ", " + player2.getPoints() + ", " + player3.getPoints() + ", " + player4.getPoints());
//
//    System.out.println(player1.getSettlementNodes());
//    System.out.println(player1.getCityNodes());
//    for (Road road: player1.getRoads()) {
//        System.out.print(road.getStart() + " ");
//    }
}
