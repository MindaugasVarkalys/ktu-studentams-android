package lt.chocolatebar.ktustudentams.data;

import java.util.List;

public class Module {

    private String code;
    private String name;
    private String credits;
    private String suggestedGrade;
    private String finalGrade;
    private String isCredited;
    private List<Grade> grades;

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

    public String getIsCredited() {
        return isCredited;
    }

    public void setIsCredited(String isCredited) {
        this.isCredited = isCredited;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
