public class PlacementRules {
    private boolean checkSettlement(Node node, Board board) { //Checks if
        if (node.getOwner() == null) {
            return true;
        }
        return false;
    }

    public boolean canBuildSettlement(Player player, Node node, Board board) {
        for (Road road: player.getRoads()) {
            if (road.getStart() == node && checkSettlement(node, board) && checkDistance(node, board) && player.getSettlementCount() < 5) {
                if (player.getResources(Resource.BRICK) >= 1 && player.getResources(Resource.WOOD) >= 1 && player.getResources(Resource.SHEEP) >= 1 && player.getResources(Resource.WHEAT) >= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canUpgradeCity(Player player, Node node) {
        if (player.getSettlements().contains(node) && player.getCityCount() < 4) {
            if (player.getResources(Resource.ORE) >= 3 && player.getResources(Resource.WHEAT) >= 2) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRoad(Player player, Node node, Node end, Board board) {
        if ((node.checkRoadOwner(player))) { //Checks if player doesn't own road at current node
            for (Road road: node.getRoads()) {
                if (road.getEnd() == end) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkDistance(Node node, Board board) {
        for (int i : node.getAdjacentNodes()) {
            if ((board.getNodes(i).getOwner() != null)) {
                return false;
            }
        }
        return true;
    }

    public boolean canBuildRoad(Player player, Node start, Node end, Board board) {
        if (checkRoad(player, start, end, board) && player.getResources(Resource.BRICK) >= 1 && player.getResources(Resource.WOOD) >= 1) {
            return canPlaceRoad(player, start, end, board);
        }
        return false;
    }

    public boolean canPlaceRoad(Player player, Node start, Node end, Board board) {
        if (player.getRoads().size() < 15) {
            for (Road currentRoad: board.getRoads()) {
                if ((currentRoad.getStart() == start && currentRoad.getEnd() == end) || (currentRoad.getStart() == end && currentRoad.getEnd() == start)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean canPlaceSettlement(Player player, Node node, Board board) { //For start of game
        if (checkSettlement(node, board) && checkDistance(node, board) && player.getSettlements().size() < 5) {
            return true;
        }
        return false;
    }
}


