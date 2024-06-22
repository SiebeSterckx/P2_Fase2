package domain.service;

import domain.model.Player;
import util.DBConnectionManager;
import util.DbConnectionService;
import util.Secret;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DBSQLConnection extends DBconnector implements PlayerService {

    private final String schemaDm;
    private final String schema;

    public DBSQLConnection(){
        this.connection = DbConnectionService.getDbConnection();
        this.schemaDm = DbConnectionService.getSchemaDm();
        this.schema = DbConnectionService.getSchema();
    }

    @Override
    public float getSavesPer90(String player_id){
        float resultF = -1;
        System.out.println("player_id: " + player_id);
        try{
            String query = String.format("SELECT %1$s.match_events.\"playerName\" AS player_name, " +
                    "CAST(%1$s.playerstatsranking.\"total saves\"::numeric / (%1$s.playerstatsranking.\"total mins played\"::numeric / 90) AS float) AS saves_per_90 " +
                    "FROM %1$s.match_events " +
                    "INNER JOIN %1$s.playerstatsranking ON %1$s.playerstatsranking.\"playerid\" = %1$s.match_events.\"playerId\" " +
                    "WHERE %1$s.match_events.\"typeId\" = 10 " +
                    "AND %1$s.playerstatsranking.\"total saves\" IS NOT NULL " +
                    "AND %1$s.playerstatsranking.\"playerid\" = ? " +
                    "GROUP BY %1$s.match_events.\"playerId\", " +
                    "         %1$s.match_events.\"playerName\", " +
                    "         %1$s.playerstatsranking.\"total mins played\", " +
                    "         %1$s.playerstatsranking.\"total saves\"", schema);

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player_id);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                resultF = result.getFloat("saves_per_90");
                System.out.println("resultF: " + resultF);
                System.out.println(result.next());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultF;
    }

    @Override
    public List<Player> getBetterKeepersSavesPer90() {

        List<Player> players = getAll();

        Map<String, Float> betterKeepers = new HashMap<String, Float>();

        List<Player> betterPlayers = new ArrayList<Player>();

        try {
            String query = String.format("WITH first_query AS (\n" +
                    "  SELECT %1$s.match_events.\"playerName\" AS player_name, \n" +
                    "         cast(%1$s.playerstatsranking.\"total saves\"::numeric / (%1$s.playerstatsranking.\"total mins played\"::numeric / 90) as numeric(5,3)) as saves_per_90,\n" +
                    "\t\t\t%1$s.match_events.\"playerId\" as player_hash\n" +
                    "  FROM %1$s.match_events \n" +
                    "  INNER JOIN %1$s.playerstatsranking ON %1$s.playerstatsranking.\"playerid\" = %1$s.match_events.\"playerId\"\n" +
                    "  WHERE %1$s.match_events.\"typeId\" = 10 \n" +
                    "  AND %1$s.playerstatsranking.\"total saves\" IS NOT NULL\n" +
                    "  GROUP BY %1$s.match_events.\"playerId\",\n" +
                    "           %1$s.match_events.\"playerName\",\n" +
                    "           %1$s.playerstatsranking.\"total mins played\",\n" +
                    "           %1$s.playerstatsranking.\"total saves\"\n" +
                    "), second_query AS (\n" +
                    "  SELECT %1$s.match_events.\"playerName\" AS player_name, \n" +
                    "         cast(%1$s.playerstatsranking.\"total saves\"::numeric / (%1$s.playerstatsranking.\"total mins played\"::numeric / 90) as numeric(5,3)) as saves_per_90\n" +
                    "  FROM %1$s.match_events \n" +
                    "  INNER JOIN %1$s.playerstatsranking ON %1$s.playerstatsranking.\"playerid\" = %1$s.match_events.\"playerId\"\n" +
                    "  WHERE %1$s.match_events.\"typeId\" = 10 \n" +
                    "  AND %1$s.playerstatsranking.\"total saves\" IS NOT NULL\n" +
                    "  AND %1$s.match_events.\"playerName\" = 'V. Cojocaru'\n" +
                    "  GROUP BY %1$s.match_events.\"playerId\",\n" +
                    "           %1$s.match_events.\"playerName\",\n" +
                    "           %1$s.playerstatsranking.\"total mins played\",\n" +
                    "           %1$s.playerstatsranking.\"total saves\"\n" +
                    ")\n" +
                    "SELECT first_query.player_name, first_query.saves_per_90, first_query.player_hash\n" +
                    "FROM first_query\n" +
                    "WHERE first_query.saves_per_90 > (\n" +
                    "  SELECT second_query.saves_per_90\n" +
                    "  FROM second_query\n" +
                    ")\n" +
                    "ORDER BY first_query.saves_per_90 DESC;", schema);


            PreparedStatement statementInsert = connection.prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();

            while (resultSet.next()) {
                betterKeepers.put(resultSet.getString("player_hash"), resultSet.getFloat("saves_per_90"));
            }
        }catch (IllegalArgumentException | SQLException throwable){
            throwable.printStackTrace();
            System.out.println("Connection no success");
        }

        for (Player player : players) {
            for (String key : betterKeepers.keySet()){
                if (player.getId().equals(key)){
                    betterPlayers.add(player);
                    player.setGoalsper90(betterKeepers.get(key));
                    player.setActualDevidedByExpectedGoals(getActualDividedByExpectedGoals(key));
                }
            }
        }

        return sortPlayersByGoalsPer90(betterPlayers);
    }

    public static List<Player> sortPlayersByGoalsPer90(List<Player> players) {
        players.sort(Comparator.comparing(Player::getGoalsper90, Comparator.reverseOrder()));
        return players;
    }

    @Override
    public Player getCurrentKeeper(){
        Player player = null;
        try{
            String query = String.format("select * from %1$s.players where \"lastName\" = 'Cojocaru'", schemaDm);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();

            while (resultSet.next()){
                String id = resultSet.getString("person_id");
                String firstName = resultSet.getString("person_firstName");
                String lastName = resultSet.getString("lastName");
                String matchName = resultSet.getString("person_matchName");
                String nationality = resultSet.getString("person_nationality");
                String position = resultSet.getString("person_position");
                String type = resultSet.getString("person_type");
                String nr = resultSet.getString("person_shirtNumber");
                String active = resultSet.getString("person_active");
                String contestantId = resultSet.getString("contestant_id");
                String contestantName = resultSet.getString("contestant_name");
                String contestantShortName = resultSet.getString("contestant_shortName");
                String contestantClubName = resultSet.getString("contestant_clubName");
                String contestantCode = resultSet.getString("contestant_code");
                Date contestantStartDate = resultSet.getDate("tournament_startDate");
                String contestantEndDate = resultSet.getString("tournament_endDate");
                Date contestantLastUpdated = resultSet.getDate("lastUpdated");

                player = new Player(id, firstName, lastName, matchName, nationality, position, type, nr, active, contestantId, contestantName, contestantShortName, contestantClubName, contestantCode, contestantStartDate, contestantEndDate, contestantLastUpdated);
                player.setMaxSpeed(getMaxSpeedPlayer(player.getId()));
                player.setGoalsper90(getSavesPer90(player.getId()));
                player.setActualDevidedByExpectedGoals(getActualDividedByExpectedGoals(player.getId()));
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }
        //System.out.println("get current keeper works\n" + player.getId());
        return player;
    }

    @Override
    public Float averageDmConcededGoals(){
        Float averageDmConcededGoals = null;
        try{
            String query = String.format("select cast(avg(sub.oega) as float) from\n" +
                    "(\n" +
                    "select sum(cast(\"goalsconceded\" as int)) as oega from %1$s.match_stats\n" +
                    "where \"player_position\" = 'Goalkeeper' and \"goalsconceded\" != ''\n" +
                    "group by \"player_matchname\", \"player_id\"\n" +
                    "  \n" +
                    ") sub;", schemaDm);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();

            while (resultSet.next()){
                averageDmConcededGoals = resultSet.getFloat("avg");
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }
        return averageDmConcededGoals;
    }

    @Override
    public double averageMaxSpeed(){
        Float averageMaxSpeed = null;
        try{
            String query = String.format("select cast(avg(sub.oega) as float) from\n" +
                    "(\n" +
                    "select sum(cast(\"goalsconceded\" as int)) as oega from %1$s.match_stats\n" +
                    "where \"player_position\" = 'Goalkeeper' and \"goalsconceded\" != ''\n" +
                    "group by \"player_matchname\", \"player_id\"\n" +
                    "  \n" +
                    ") sub;", schemaDm);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();

            while (resultSet.next()){
                averageMaxSpeed = resultSet.getFloat("avg");
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }
        return averageMaxSpeed;
    }

    @Override
    public Float getActualDividedByExpectedGoals(String player_id) {
        Double actualDividedByExpectedGoals = null;
        try {
            String query = String.format("SELECT act.player_id, act.goalsconceded / exp.expectedgoalsconceded AS \"actual divided by expected goals\"\n" +
                    "FROM (\n" +
                    "  SELECT \"player_id\", SUM(CAST(\"expectedgoalsconceded\" AS FLOAT)) AS expectedgoalsconceded\n" +
                    "  FROM %1$s.matches_expected_goals\n" +
                    "  WHERE \"player_position\" = 'Goalkeeper'\n" +
                    "  GROUP BY \"player_matchname\", \"player_id\"\n" +
                    "  ORDER BY 2 DESC\n" +
                    ") AS exp\n" +
                    "INNER JOIN (\n" +
                    "  SELECT me.player_id, me.player_matchname, SUM(CAST(me.goalsconceded AS FLOAT)) AS goalsconceded\n" +
                    "  FROM %1$s.match_stats me\n" +
                    "  WHERE me.\"match_id\" IN (\n" +
                    "    SELECT meg.\"match_id\"\n" +
                    "    FROM %1$s.matches_expected_goals meg\n" +
                    "  )\n" +
                    "  AND me.goalsconceded != ''\n" +
                    "  AND me.player_position = 'Goalkeeper'\n" +
                    "  AND me.player_id = ?\n" +
                    "  GROUP BY \"player_id\", \"player_matchname\"\n" +
                    "  ORDER BY 3 DESC\n" +
                    ") AS act\n" +
                    "ON exp.\"player_id\" = act.\"player_id\"\n" +
                    "ORDER BY 2 ASC;", schemaDm);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            statementInsert.setString(1, player_id);
            ResultSet resultSet = statementInsert.executeQuery();

            while (resultSet.next()){
                actualDividedByExpectedGoals = resultSet.getDouble("actual divided by expected goals");
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }
        return actualDividedByExpectedGoals.floatValue();
    }

    @Override
    public List<Player> getBetterKeepersActualDevidedByExpectedGoals() {
        List<Player> players = getAll();
        List<Player> _betterKeepers = new ArrayList<>();

        Map<String, Double> betterKeepers = new HashMap<String, Double>();

        try{
            String query = String.format("SELECT act.player_id, act.goalsconceded / exp.expectedgoalsconceded AS \"actual divided by expected goals\"\n" +
                    "FROM (\n" +
                    "SELECT \"player_id\", SUM(CAST(\"expectedgoalsconceded\" AS float)) AS expectedgoalsconceded\n" +
                    "FROM %1$s.matches_expected_goals\n" +
                    "WHERE \"player_position\" = 'Goalkeeper'\n" +
                    "GROUP BY \"player_matchname\", \"player_id\"\n" +
                    "ORDER BY 2 DESC\n" +
                    ") AS exp\n" +
                    "INNER JOIN (\n" +
                    "SELECT me.player_id, me.player_matchname, SUM(CAST(me.goalsconceded AS float)) AS goalsconceded\n" +
                    "FROM %1$s.match_stats me\n" +
                    "WHERE me.\"match_id\" IN (\n" +
                    "SELECT meg.\"match_id\"\n" +
                    "FROM %1$s.matches_expected_goals meg\n" +
                    ")\n" +
                    "AND me.goalsconceded != ''\n" +
                    "AND me.player_position = 'Goalkeeper'\n" +
                    "GROUP BY \"player_id\", \"player_matchname\"\n" +
                    "ORDER BY 3 DESC\n" +
                    ") AS act ON exp.\"player_id\" = act.\"player_id\"\n" +
                    "WHERE act.player_id = '49vbniwasp2kyt27jw0cv6m6t' OR (act.goalsconceded / exp.expectedgoalsconceded) < (\n" +
                    "SELECT SUM(CAST(me2.goalsconceded AS float)) / SUM(CAST(meg2.expectedgoalsconceded AS float))\n" +
                    "FROM %1$s.match_stats me2\n" +
                    "INNER JOIN %1$s.matches_expected_goals meg2 ON me2.\"match_id\" = meg2.\"match_id\" AND me2.\"player_id\" = meg2.\"player_id\"\n" +
                    "WHERE me2.goalsconceded != ''\n" +
                    "AND me2.player_position = 'Goalkeeper'\n" +
                    "AND me2.player_id = '49vbniwasp2kyt27jw0cv6m6t'\n" +
                    ")\n" +
                    "ORDER BY 2 ASC;", schemaDm);

            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString("player_id");
                Double actualDividedByExpectedGoals = resultSet.getDouble("actual divided by expected goals");
                betterKeepers.put(id, actualDividedByExpectedGoals);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }

        for (Player player : players) {
            if (betterKeepers.containsKey(player.getId())) {
                _betterKeepers.add(player);
            }
        }

        return _betterKeepers;
    }

    public static List<Player> sortBetterKeepersActualDevidedByExpectedGoals(List<Player> players) {
        players.sort(Comparator.comparing(Player::getActualDevidedByExpectedGoals, Comparator.reverseOrder()));
        return players;
    }

    @Override
    public Float getMaxSpeed(String player_id) {
        try {
            String query = String.format("select cast(max(\"topSpeed\") as float) from %1$s.player_stats\n" +
                    "where \"playerId\" = ?", schema);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            statementInsert.setString(1, player_id);
            ResultSet result = statementInsert.executeQuery();
            result.next();

            return result.getFloat("max");
        } catch (SQLException exc){
            exc.printStackTrace();
            System.out.println("Connection no success");
        }
        return null;
    }

    @Override
    public List<Player> getFasterMaxSpeedKeepers() {
        List<Player> players = getAll();

        Map<String, Float> betterKeepers = new HashMap<String, Float>();

        List<Player> _betterKeepers = new ArrayList<Player>();
        try {
            String query = String.format("select playerid, max(topspeed) from %1$s.match_fitness_players\n" +
                    "where position = 'Goalkeeper' and topspeed > (select max(topspeed) from %1$s.match_fitness_players where matchname = 'V. Cojocaru')\n" +
                    "group by playerid\n" +
                    "order by 2 desc", schemaDm);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();
            while (resultSet.next()){

                String id = resultSet.getString("playerid");
                Float maxSpeed = resultSet.getFloat("max");
                betterKeepers.put(id, maxSpeed);
            }


        } catch (SQLException exc){
            exc.printStackTrace();
            System.out.println("Connection no success");
        }
        for (Player player : players) {
            if (betterKeepers.containsKey(player.getId())) {
                _betterKeepers.add(player);
            }
        }

        return _betterKeepers;
    }

    @Override
    public Float getExpConcedendGoals(String player_id) {
        try {
            String query = String.format("select \"playerId\", sum(cast(player_stats_exp.\"expectedGoalsConceded\" as float))\n" +
                    "from %1$s.player_stats_exp\n" +
                    "where \"playerId\" in (select \"playerId\" from %1$s.line_up_players where \"position\" = 'Goalkeeper') and \"playerId\" = ?\n" +
                    "group by \"playerId\"", schema);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            statementInsert.setString(1, player_id);
            ResultSet result = statementInsert.executeQuery();
            result.next();
            return result.getFloat("sum");
        } catch (SQLException exc){
            exc.printStackTrace();
            System.out.println("Connection no success");
        }
        return null;
    }

    @Override
    public Player getKeeper(String player_id){
        Player player = null;
        try{
            String query = String.format("select * from %1$s.players where person_id = ?", schemaDm);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            statementInsert.setString(1, player_id);
            ResultSet resultSet = statementInsert.executeQuery();

            while (resultSet.next()){
                String id = resultSet.getString("person_id");
                String firstName = resultSet.getString("person_firstName");
                String lastName = resultSet.getString("lastName");
                String matchName = resultSet.getString("person_matchName");
                String nationality = resultSet.getString("person_nationality");
                String position = resultSet.getString("person_position");
                String type = resultSet.getString("person_type");
                String nr = resultSet.getString("person_shirtNumber");
                String active = resultSet.getString("person_active");
                String contestantId = resultSet.getString("contestant_id");
                String contestantName = resultSet.getString("contestant_name");
                String contestantShortName = resultSet.getString("contestant_shortName");
                String contestantClubName = resultSet.getString("contestant_clubName");
                String contestantCode = resultSet.getString("contestant_code");
                Date contestantStartDate = resultSet.getDate("tournament_startDate");
                String contestantEndDate = resultSet.getString("tournament_endDate");
                Date contestantLastUpdated = resultSet.getDate("lastUpdated");

                player = new Player(id, firstName, lastName, matchName, nationality, position, type, nr, active, contestantId, contestantName, contestantShortName, contestantClubName, contestantCode, contestantStartDate, contestantEndDate, contestantLastUpdated);
                player.setMaxSpeed(getMaxSpeedPlayer(player.getId()));
                player.setGoalsper90(getSavesPer90(player.getId()));
                player.setActualDevidedByExpectedGoals(getActualDividedByExpectedGoals(player_id));
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }
        //System.out.println("get current keeper works\n" + player.getId());
        return player;
    }

    @Override
    public Float getMaxSpeedPlayer(String playerHash){
        Float maxSpeed = null;
        try{
            String query = String.format("select max(\"topSpeed\") from %1$s.player_stats\n" +
                    "where \"playerId\" = ?", schema);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            statementInsert.setString(1, playerHash);
            ResultSet resultSet = statementInsert.executeQuery();

            while (resultSet.next()){
                maxSpeed = resultSet.getFloat("max");
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }
        return maxSpeed;
    }

    @Override
    public List<Player> getAll() {


        LinkedList<Player> players = new LinkedList<>();

        try {
            String query = String.format("SELECT * from %1$s.players;", schemaDm);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();
            while (resultSet.next()) {


                //String email = resultSet.getString("person_id");
                String id = resultSet.getString("person_id");
                String firstName = resultSet.getString("person_firstName");
                String lastName = resultSet.getString("lastName");
                String matchName = resultSet.getString("person_matchName");
                String nationality = resultSet.getString("person_nationality");
                String position = resultSet.getString("person_position");
                String type = resultSet.getString("person_type");
                String nr = resultSet.getString("person_shirtNumber");
                String active = resultSet.getString("person_active");
                String contestantId = resultSet.getString("contestant_id");
                String contestantName = resultSet.getString("contestant_name");
                String contestantShortName = resultSet.getString("contestant_shortName");
                String contestantClubName = resultSet.getString("contestant_clubName");
                String contestantCode = resultSet.getString("contestant_code");
                Date contestantStartDate = resultSet.getDate("tournament_startDate");
                String contestantEndDate = resultSet.getString("tournament_endDate");
                Date contestantLastUpdated = resultSet.getDate("lastUpdated");

                try {


                    Player player = new Player(id, firstName, lastName, matchName, nationality, position, type, nr, active, contestantId, contestantName, contestantShortName, contestantClubName, contestantCode, contestantStartDate, contestantEndDate, contestantLastUpdated);
                    player.setMaxSpeed(getMaxSpeedPlayer(player.getId()));
                    players.add(player);
                }catch(IllegalArgumentException e) {
                    e.printStackTrace();
                }



                //System.out.println(user.toString());
            }
        }catch (SQLException throwable) {
            throwable.printStackTrace();
            System.out.println("Connection no success");
        }

        return players;

    }

    @Override
    public Float getAverageSavesPer90() {
        try {
            String query = String.format("select cast(avg(sub.\"saves_per_90\") as float) from (\n" +
                    "\n" +
                    "select %1$s.match_events.\"playerName\", cast(%1$s.playerstatsranking.\"total saves\"::numeric / (%1$s.playerstatsranking.\"total mins played\"::numeric / 90) as numeric(5,3)) as saves_per_90\n" +
                    "from %1$s.match_events inner join %1$s.playerstatsranking on %1$s.playerstatsranking.\"playerid\" = %1$s.match_events.\"playerId\"\n" +
                    "where \n" +
                    "  %1$s.match_events.\"typeId\" = 10 and %1$s.playerstatsranking.\"total saves\" is not null\n" +
                    "group by \n" +
                    "  %1$s.match_events.\"playerId\",\n" +
                    "  %1$s.match_events.\"playerName\",\n" +
                    "  %1$s.playerstatsranking.\"total mins played\",\n" +
                    "  %1$s.playerstatsranking.\"total saves\"\n" +
                    "order by 2 desc\n" +
                    ") sub;", schema);

            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            result.next();

            Float average = result.getFloat("avg");
            return average;

        } catch (SQLException exc) {
            exc.printStackTrace();
            System.out.println("Connection no success");
        }
        return null;
    }

    @Override
    public Float getAvgExpConsedentGoals(String position) {
        Float avg = null;
        try{
            String query = String.format("SELECT AVG(sub.oega) " +
                    "FROM " +
                    "(SELECT SUM(CAST(\"expectedgoalsconceded\" AS float)) AS oega " +
                    "FROM %1$s.matches_expected_goals " +
                    "WHERE \"player_position\" = 'Goalkeeper' " +
                    "GROUP BY \"player_matchname\", \"player_id\") sub;", schema);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();
            while (resultSet.next()) {
                avg = resultSet.getFloat("avg");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }
        return avg;
    }

    @Override
    public Double avgSaveRate() {
        Double avg = null;
        try{
            String query = String.format("SELECT AVG(sub.\"save rate\")\n" +
                    "FROM\n" +
                    "(\n" +
                    "  SELECT DISTINCT gpk.\"playerId\", CAST(spk.\"total saves\" AS float) / (CAST(spk.\"total saves\" AS float) + CAST(gpk.\"total goals conceded\" AS float)) AS \"save rate\"\n" +
                    "  FROM\n" +
                    "  (\n" +
                    "    SELECT %1$s.playerstatsranking.\"playerid\" AS \"playerId\", %1$s.playerstatsranking.\"total goals conceded\"::integer\n" +
                    "    FROM %1$s.playerstatsranking\n" +
                    "    INNER JOIN %1$s.line_up_players ON %1$s.playerstatsranking.\"playerid\" = %1$s.line_up_players.\"playerId\"\n" +
                    "    WHERE %1$s.line_up_players.\"position\" = 'Goalkeeper'\n" +
                    "    AND %1$s.playerstatsranking.\"total goals conceded\" IS NOT NULL\n" +
                    "    GROUP BY \"playerid\", \"total goals conceded\"\n" +
                    "    ORDER BY %1$s.playerstatsranking.\"playerid\"\n" +
                    "  ) AS gpk\n" +
                    "  INNER JOIN\n" +
                    "  (\n" +
                    "    SELECT %1$s.line_up_players.\"playerId\", %1$s.playerstatsranking.\"total saves\"\n" +
                    "    FROM %1$s.line_up_players\n" +
                    "    INNER JOIN %1$s.playerstatsranking ON %1$s.line_up_players.\"playerId\" = %1$s.playerstatsranking.\"playerid\"\n" +
                    "    WHERE %1$s.line_up_players.\"position\" = 'Goalkeeper' AND %1$s.playerstatsranking.\"total saves\" IS NOT NULL\n" +
                    "  ) AS spk\n" +
                    "  ON gpk.\"playerId\" = spk.\"playerId\"\n" +
                    ") sub;", schema);
            PreparedStatement statementInsert = getConnection().prepareStatement(query);
            ResultSet resultSet = statementInsert.executeQuery();
            while (resultSet.next()) {
                avg = resultSet.getDouble("avg");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection no success");
        }
        return avg;
    }

    @Override
    public Float getAvgGoalsPerGoalkeeper() {
        try {
            String query = String.format("select avg(sub.\"goalsconceded\") from\n" +
                    "(\n" +
                    "select me.player_matchname, sum(cast(me.goalsconceded as float)) as goalsconceded\n" +
                    "from %1$s.match_stats me\n" +
                    "\n" +
                    "where me.\"match_id\" in (\n" +
                    "select meg.\"match_id\"\n" +
                    "from %1$s.matches_expected_goals meg)\n" +
                    "and me.goalsconceded != ''\n" +
                    "and me.player_position = 'Goalkeeper'\n" +
                    "\n" +
                    "group by \"player_id\", \"player_matchname\"    \n" +
                    ") sub;", schemaDm);
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            result.next();

            Float average = result.getFloat("avg");
            return average;

        } catch (SQLException exc) {
            exc.printStackTrace();
            System.out.println("Connection no success");
        }
        return null;    }


    public static Properties Connect() {

        // set properties for db connection
        Properties properties = new Properties();

        // set user and password
        try {
            Class.forName("util.Secret"); // check if Secret does exist
            Secret.setPass(properties);
        } catch (ClassNotFoundException e) {
            System.out.println("Class util.Secret with credentials not found");
        }

        properties.setProperty("ssl", "require");
        properties.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
        properties.setProperty("sslmode", "prefer");

        return properties;
    }

    public static void ExecuteStringSQLStatement(Connection connection, String query, String[] arguments) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < arguments.length; i++) {
            preparedStatement.setString(i+1, arguments[i]);
        }
        preparedStatement.execute();
    }

}
