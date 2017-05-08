package lt.chocolatebar.ktustudentams.data;

import java.util.List;

public class Module {

    private String code;
    private String name;
    private String credits;
    private String suggestedGrade;
    private String finalGrade;
    private String credited;
    private List<String> gradesRequestParams;
    private List<Week> weeks;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getSuggestedGrade() {
        return suggestedGrade;
    }

    public void setSuggestedGrade(String suggestedGrade) {
        this.suggestedGrade = suggestedGrade;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getCredited() {
        return credited;
    }

    public void setCredited(String credited) {
        this.credited = credited;
    }

    public List<String> getGradesRequestParams() {
        return gradesRequestParams;
    }

    public void setGradesRequestParams(List<String> gradesRequestParams) {
        this.gradesRequestParams = gradesRequestParams;
    }

    public List<Week> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Week> weeks) {
        this.weeks = weeks;
    }
}
