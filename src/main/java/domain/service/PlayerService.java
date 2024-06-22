package domain.service;

import domain.model.Player;

import java.util.List;

public interface PlayerService {
    float getSavesPer90(String player_id);

    List<Player> getBetterKeepersSavesPer90();

    Player getCurrentKeeper();

    Player getKeeper(String player_id);

    Float getMaxSpeedPlayer(String playerHash);

    List<Player> getAll();

    Float getAverageSavesPer90();

    Float getAvgExpConsedentGoals(String position);

    Double avgSaveRate();
    
    Float getAvgGoalsPerGoalkeeper();

    Float averageDmConcededGoals();

    double averageMaxSpeed();

    Float getActualDividedByExpectedGoals(String player_id);

    List<Player> getBetterKeepersActualDevidedByExpectedGoals();

    Float getMaxSpeed(String player_id);

    List<Player> getFasterMaxSpeedKeepers();

    Float getExpConcedendGoals(String id);
}
