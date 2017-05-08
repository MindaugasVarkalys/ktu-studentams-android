package lt.chocolatebar.ktustudentams.data;

public class Week {

    private String week = "";
    private String shortName = "";
    private String name = "";
    private String percentage = "";
    private String firstGrade = "";
    private String secondGrade = "";
    private String thirdGrade = "";
    private boolean isThirdGradeAvailable;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getFirstGrade() {
        return firstGrade;
    }

    public void setFirstGrade(String firstGrade) {
        this.firstGrade = firstGrade;
    }

    public String getSecondGrade() {
        return secondGrade;
    }

    public void setSecondGrade(String secondGrade) {
        this.secondGrade = secondGrade;
    }

    public String getThirdGrade() {
        return thirdGrade;
    }

    public void setThirdGrade(String thirdGrade) {
        this.thirdGrade = thirdGrade;
    }

    public boolean isThirdGradeAvailable() {
        return isThirdGradeAvailable;
    }

    public void setThirdGradeAvailable(boolean thirdGradeAvailable) {
        isThirdGradeAvailable = thirdGradeAvailable;
    }
}
