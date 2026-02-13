import java.util.Arrays;

public class SetupGame { //Setup game class
    private Node[] nodes = new Node[54];
    private Tile[] tiles = new Tile[19];

    public SetupGame() { //Constructor setup
        createTiles();
        createNodes();
        setResources();
        setNumber();
    }

    private void createTiles() { //Creates tiles with their connected nodes
        tiles[7] = new Tile(Arrays.asList(7,24,25,26,27,8));

        tiles[8] = new Tile(Arrays.asList(9,8,27,28,29,10));

        tiles[9] = new Tile(Arrays.asList(11,10,29,30,31,32));

        tiles[10] = new Tile(Arrays.asList(13,12,11,32,33,34));

        tiles[2] = new Tile(Arrays.asList(3,2,9,10,11,12));

        tiles[1] = new Tile(Arrays.asList(1,6,7,8,9,2));

        tiles[18] = new Tile(Arrays.asList(23,52,53,24,7,6));

        tiles[17] = new Tile(Arrays.asList(49,50,51,52,23,22));

        tiles[6] = new Tile(Arrays.asList(20,22,23,6,1,0));

        tiles[0] = new Tile(Arrays.asList(5,0,1,2,3,4));

        tiles[3] = new Tile(Arrays.asList(15,4,3,12,13,14));

        tiles[11] = new Tile(Arrays.asList(37,14,13,34,35,36));

        tiles[12] = new Tile(Arrays.asList(39,17,15,14,37,38));

        tiles[4] = new Tile(Arrays.asList(18,16,5,4,15,17));

        tiles[5] = new Tile(Arrays.asList(21,19,20,0,5,16));

        tiles[16] = new Tile(Arrays.asList(46,48,49,22,20,19));

        tiles[15] = new Tile(Arrays.asList(45,47,46,19,21,43));

        tiles[14] = new Tile(Arrays.asList(44,43,21,16,18,40));

        tiles[13] = new Tile(Arrays.asList(42,40,18,17,39,41));
    }

    private void createNodes() { //Creates nodes with adjacent nodes
        nodes[0] = new Node(Arrays.asList(5,1,20));

        nodes[1] = new Node(Arrays.asList(0,2,6));

        nodes[2] = new Node(Arrays.asList(1,3,9));

        nodes[3] = new Node(Arrays.asList(2,4,12));

        nodes[4] = new Node(Arrays.asList(3,5,15));

        nodes[5] = new Node(Arrays.asList(0,4,16));

        nodes[6] = new Node(Arrays.asList(1,7,23));

        nodes[7] = new Node(Arrays.asList(6,8,24));

        nodes[8] = new Node(Arrays.asList(7,9,27));

        nodes[9] = new Node(Arrays.asList(2,8,10));

        nodes[10] = new Node(Arrays.asList(9,11,29));

        nodes[11] = new Node(Arrays.asList(10,12,32));

        nodes[12] = new Node(Arrays.asList(3,11,13));

        nodes[13] = new Node(Arrays.asList(12,14,34));

        nodes[14] = new Node(Arrays.asList(13,15,37));

        nodes[15] = new Node(Arrays.asList(4,14,17));

        nodes[16] = new Node(Arrays.asList(5,18,21));

        nodes[17] = new Node(Arrays.asList(15,18,39));

        nodes[18] = new Node(Arrays.asList(16,17,40));

        nodes[19] = new Node(Arrays.asList(20,21,46));

        nodes[20] = new Node(Arrays.asList(0,19,22));

        nodes[21] = new Node(Arrays.asList(16,19,43));

        nodes[22] = new Node(Arrays.asList(20,23,49));

        nodes[23] = new Node(Arrays.asList(6,22,52));

        nodes[24] = new Node(Arrays.asList(7,25,53));

        nodes[25] = new Node(Arrays.asList(24,26));

        nodes[26] = new Node(Arrays.asList(25,27));

        nodes[27] = new Node(Arrays.asList(8, 26, 28));

        nodes[28] = new Node(Arrays.asList(27, 29));

        nodes[29] = new Node(Arrays.asList(10, 28, 30));

        nodes[30] = new Node(Arrays.asList(29, 31));

        nodes[31] = new Node(Arrays.asList(30, 32));

        nodes[32] = new Node(Arrays.asList(11, 31, 33));

        nodes[33] = new Node(Arrays.asList(32, 34));

        nodes[34] = new Node(Arrays.asList(13, 33, 35));

        nodes[35] = new Node(Arrays.asList(34, 36));

        nodes[36] = new Node(Arrays.asList(35, 37));

        nodes[37] = new Node(Arrays.asList(14, 36, 38));

        nodes[38] = new Node(Arrays.asList(37, 39));

        nodes[39] = new Node(Arrays.asList(17, 38, 41));

        nodes[40] = new Node(Arrays.asList(18, 42, 44));

        nodes[41] = new Node(Arrays.asList(39,42));

        nodes[42] = new Node(Arrays.asList(40,41));

        nodes[43] = new Node(Arrays.asList(21, 44, 45));

        nodes[44] = new Node(Arrays.asList(40,43));

        nodes[45] = new Node(Arrays.asList(43,47));

        nodes[46] = new Node(Arrays.asList(19, 47, 48));

        nodes[47] = new Node(Arrays.asList(45,46));

        nodes[48] = new Node(Arrays.asList(46,49));

        nodes[49] = new Node(Arrays.asList(22, 48, 50));

        nodes[50] = new Node(Arrays.asList(49,51));

        nodes[51] = new Node(Arrays.asList(50,52));

        nodes[52] = new Node(Arrays.asList(23, 51, 53));

        nodes[53] = new Node(Arrays.asList(24,52));
    }

    private void setResources() { //Sets each tiles resource type
        int brick[] = {2,13,14};
        int lumber[] = {0,9,12,17};
        int ore[] = {3,8,10};
        int grain[] = {1,7,11,15};
        int wool[] = {4,5,6,18};
        //int desert = 16;

        for (int i: brick) {
            tiles[i].setResource(Resource.BRICK);
        }
        for (int i: lumber) {
            tiles[i].setResource(Resource.WOOD);
        }
        for (int i: ore) {
            tiles[i].setResource(Resource.ORE);
        }
        for (int i: grain) {
            tiles[i].setResource(Resource.WHEAT);
        }
        for (int i: wool) {
            tiles[i].setResource(Resource.SHEEP);
        }
        tiles[16].setResource(null);
    }

    public Tile[] getTiles() {
        return tiles;
    } //Gets tiles

    public Node[] getNodes() {
        return nodes;
    } //Gets nodes

    private void setNumber() { //Sets each tile's dice number
        tiles[0].setNumber(10);
        tiles[1].setNumber(11);
        tiles[2].setNumber(8);
        tiles[3].setNumber(3);
        tiles[4].setNumber(11);
        tiles[5].setNumber(5);
        tiles[6].setNumber(12);
        tiles[7].setNumber(3);
        tiles[8].setNumber(6);
        tiles[9].setNumber(4);
        tiles[10].setNumber(6);
        tiles[11].setNumber(9);
        tiles[12].setNumber(5);
        tiles[13].setNumber(9);
        tiles[14].setNumber(8);
        tiles[15].setNumber(4);
        tiles[16].setNumber(7);
        tiles[17].setNumber(2);
        tiles[18].setNumber(10);
        for (int i = 0; i < nodes.length; i++) { //Sets each node's number
            nodes[i].setNumber(i);
        }
    }


}
