public class PlacementRules {
    private boolean checkSettlement(Node node, Board board) { //Checks if node has owner
        if (node.getOwner() == null) {
            return true;
        }
        return false;
    }

    public boolean canBuildSettlement(Player player, Node node, Board board) { //Checks if player can build settlement
        for (Road road: player.getRoads()) { //For all of players roads
            if ((road.getStart() == node || road.getEnd() == node) && checkSettlement(node, board) && checkDistance(node, board) && player.getSettlementCount() < 5) { //If road is connected, distance matches, node empty
                if (player.getResources(Resource.BRICK) >= 1 && player.getResources(Resource.WOOD) >= 1 && player.getResources(Resource.SHEEP) >= 1 && player.getResources(Resource.WHEAT) >= 1) { //If player has resources
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canUpgradeCity(Player player, Node node) { //Checks if player can upgrade city
        if (player.getSettlements().contains(node) && player.getCityCount() < 4) { //If player owns settlement there
            if (player.getResources(Resource.ORE) >= 3 && player.getResources(Resource.WHEAT) >= 2) { //If player has resources
                return true;
            }
        }
        return false;
    }

    private boolean checkRoad(Player player, Node start, Node end, Board board) { //Checks if unclaimed road exists
        if (start.checkRoadOwner(player) && start.getAdjacentNodes().contains(end.getNumber())) { //Checks if player doesn't own road at current node
            for (Road road: start.getRoads()) {
                if (road.getEnd() == end) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkDistance(Node node, Board board) { //Checks if node is at least 2 nodes away from building
        for (int i : node.getAdjacentNodes()) { //If adjacent nodes have building
            if ((board.getNodes(i).getOwner() != null)) {
                return false;
            }
        }
        return true;
    }

    public boolean canBuildRoad(Player player, Node start, Node end, Board board) { //Checks if player can build road
        if (checkRoad(player, start, end, board) && player.getResources(Resource.BRICK) >= 1 && player.getResources(Resource.WOOD) >= 1) {
            return canPlaceRoad(player, start, end, board);
        }
        return false;
    }

    public boolean canPlaceRoad(Player player, Node start, Node end, Board board) { //For start of game, checks if player can place road without spending resources
        if (player.getRoads().size() < 15 && start.getAdjacentNodes().contains(end.getNumber())) {
            for (Road currentRoad: board.getRoads()) {
                if ((currentRoad.getStart() == start && currentRoad.getEnd() == end) || (currentRoad.getStart() == end && currentRoad.getEnd() == start)) { //If backwards road doesn't already exist
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean canPlaceSettlement(Player player, Node node, Board board) { //For start of game, checks if player can build settlement without checking resources
        if (checkSettlement(node, board) && checkDistance(node, board) && player.getSettlements().size() < 5) {
            return true;
        }
        return false;
    }
}


