package domain.model;

import java.util.Date;

public class Player {

    private String id;
    private String firstName;
    private String lastName;
    private String matchName;
    private String nationality;
    private String position;
    private String type;
    private String nr;
    private String active;
    private String contestantId;
    private String contestantName;
    private String contestantShortName;
    private String contestantClubName;
    private String contestantCode;
    private Date contestantStartDate;
    private String contestantEndDate;
    private Date contestantLastUpdated;

    private Float goalsper90;

    private Float maxSpeed;

    private Float actualDevidedByExpectedGoals;

    public Player(String id, String firstName, String lastName, String matchName, String nationality, String position, String type, String nr, String active, String contestantId, String contestantName, String contestantShortName, String contestantClubName, String contestantCode, Date contestantStartDate, String contestantEndDate, Date contestantLastUpdated) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.matchName = matchName;
        this.nationality = nationality;
        this.position = position;
        this.type = type;
        this.nr = nr;
        this.active = active;
        this.contestantId = contestantId;
        this.contestantName = contestantName;
        this.contestantShortName = contestantShortName;
        this.contestantClubName = contestantClubName;
        this.contestantCode = contestantCode;
        this.contestantStartDate = contestantStartDate;
        this.contestantEndDate = contestantEndDate;
        this.contestantLastUpdated = contestantLastUpdated;
    }

    public void setMaxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public void setActualDevidedByExpectedGoals(Float actualDevidedByExpectedGoals) {
        this.actualDevidedByExpectedGoals = actualDevidedByExpectedGoals;
    }

    public Float getActualDevidedByExpectedGoals() {
        return actualDevidedByExpectedGoals;
    }

    public void setGoalsper90(Float goalsper90) {
        this.goalsper90 = goalsper90;
    }

    public Float getGoalsper90() {
        return goalsper90;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getContestantId() {
        return contestantId;
    }

    public void setContestantId(String contestantId) {
        this.contestantId = contestantId;
    }

    public String getContestantName() {
        return contestantName;
    }

    public void setContestantName(String contestantName) {
        this.contestantName = contestantName;
    }

    public String getContestantShortName() {
        return contestantShortName;
    }

    public void setContestantShortName(String contestantShortName) {
        this.contestantShortName = contestantShortName;
    }

    public String getContestantClubName() {
        return contestantClubName;
    }

    public void setContestantClubName(String contestantClubName) {
        this.contestantClubName = contestantClubName;
    }

    public String getContestantCode() {
        return contestantCode;
    }

    public void setContestantCode(String contestantCode) {
        this.contestantCode = contestantCode;
    }

    public Date getContestantStartDate() {
        return contestantStartDate;
    }

    public void setContestantStartDate(Date contestantStartDate) {
        this.contestantStartDate = contestantStartDate;
    }

    public String getContestantEndDate() {
        return contestantEndDate;
    }

    public void setContestantEndDate(String contestantEndDate) {
        this.contestantEndDate = contestantEndDate;
    }

    public Date getContestantLastUpdated() {
        return contestantLastUpdated;
    }

    public void setContestantLastUpdated(Date contestantLastUpdated) {
        this.contestantLastUpdated = contestantLastUpdated;
    }
}
