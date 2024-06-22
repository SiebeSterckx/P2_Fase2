package domain.service;

import domain.model.Player;

import java.util.List;
import java.util.Map;

public class AppService {

//    private DBSQLConnection db;

    private PlayerService players = new DBSQLConnection();

    /*public AppService() {
        db = new DBSQLConnection();

    }*/

    public List<Player> getBetterKeepersSavesPer90() {
        return players.getBetterKeepersSavesPer90();
    }

    public List<Player> getAll() {
        return players.getAll();
    }

    public Player getCurrentKeeper() {
        return players.getCurrentKeeper();
    }

    public Player getKeeper(String id) {
        return players.getKeeper(id);
    }

    public Float getAverageSavesPer90() {
        return players.getAverageSavesPer90();
    }
    public double averageMaxSpeed(){
        return players.averageMaxSpeed();
    }

    public Float averageDmConcededGoals(){
        return players.averageDmConcededGoals();
    }

    public Float getActualDividedByExpectedGoals(String player_id){
        return players.getActualDividedByExpectedGoals(player_id);
    }

    public List<Player> getBetterKeepersActualDevidedByExpectedGoals(){
        return players.getBetterKeepersActualDevidedByExpectedGoals();
    }

    public Float getMaxSpeed(String player_id){
        return players.getMaxSpeed(player_id);
    }

    public List<Player> getFasterMaxSpeedKeepers(){
        return players.getFasterMaxSpeedKeepers();
    }

    public Object getExpConcededGoals(String id) {
        return players.getExpConcedendGoals(id);
    }
}
