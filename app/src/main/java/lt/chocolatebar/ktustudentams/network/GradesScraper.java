package lt.chocolatebar.ktustudentams.network;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import lt.chocolatebar.ktustudentams.data.Cookies;
import lt.chocolatebar.ktustudentams.data.Grade;
import lt.chocolatebar.ktustudentams.data.Module;

public class GradesScraper extends AsyncTask<Void, Void, Void> {

    private final static String GRADES_URL = "https://uais.cr.ktu.lt/ktuis/STUD_SS2.infivert";
    private final static int FIRST_ROW_LEFT_COLUMNS_WITHOUT_GRADES = 4;
    private final static int FIRST_ROW_RIGHT_COLUMNS_WITHOUT_GRADES = 4;

    private Module module;
    private Document gradesPage;
    private List<Integer> gradesIndexes;
    private OnGradesScrapedListener onGradesScrapedListener;

    public void setOnGradesScrapedListener(OnGradesScrapedListener onGradesScrapedListener) {
        this.onGradesScrapedListener = onGradesScrapedListener;
    }

    public void scrap(Module module) {
        this.module = module;
        execute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            gradesPage = Jsoup.connect(GRADES_URL)
                    .cookies(Cookies.get())
                    .data("p1", module.getGradesRequestParams().get(0))
                    .data("p2", module.getGradesRequestParams().get(1))
                    .post();
            parseFirstGrade();
            parseSecondGrade();
            parseThirdGrade();
            parseWeeks();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseFirstGrade() {
        Elements thirdRow = gradesPage.select("table:nth-child(4) > tbody > tr:nth-child(3) > td");
        gradesIndexes = new ArrayList<>();
        List<Grade> grades = new ArrayList<>();
        int i;
        for (i = FIRST_ROW_LEFT_COLUMNS_WITHOUT_GRADES - 1; i < thirdRow.size() - FIRST_ROW_RIGHT_COLUMNS_WITHOUT_GRADES; i++) {
            Element cell = thirdRow.get(i);
            if (isCellOpenForGrade(cell)) {
                gradesIndexes.add(i);
                Grade grade = new Grade();
                if (!isCellEmpty(cell)) {
                    grade.setFirstGrade(cell.html());
                }
                grades.add(grade);
            }
        }
        module.setGrades(grades);
        module.setSuggestedGrade(thirdRow.get(i++).html());
        module.setCredited(thirdRow.get(i++).html());
        module.setFinalGrade(thirdRow.get(i).html());
    }

    private void parseSecondGrade() {
        Elements forthRow = gradesPage.select("table:nth-child(4) > tbody > tr:nth-child(4) > td");
        int j = 0;
        for (int i = 1; i < forthRow.size() - 1; i++) {
            Element cell = forthRow.get(i);
            if (columnHasFirstGrade(i)) {
                if (!isCellEmpty(cell)) {
                    module.getGrades().get(j).setSecondGrade(cell.html());
                }
                j++;
            }
        }
    }

    private boolean columnHasFirstGrade(int i) {
        return gradesIndexes.contains(i + FIRST_ROW_LEFT_COLUMNS_WITHOUT_GRADES - 1);
    }

    private boolean isCellOpenForGrade(Element cell) {
        return cell.attr("class").equals("grd");
    }

    private boolean isCellEmpty(Element cell) {
        return cell.html().equals("&nbsp;");
    }

    private void parseThirdGrade() {
        Elements fifthRow = gradesPage.select("table:nth-child(4) > tbody > tr:nth-child(5) > td");
        int j = 0;
        for (int i = 1; i < fifthRow.size() - 1; i++) {
            Element cell = fifthRow.get(i);
            if (columnHasFirstGrade(i) && isCellOpenForGrade(cell)) {
                module.getGrades().get(j).setThirdGradeAvailable(true);
                if (!isCellEmpty(cell)) {
                    module.getGrades().get(j).setThirdGrade(fifthRow.get(i).html());
                }
                j++;
            }
        }
    }

    private void parseWeeks() {
        Elements firstRow = gradesPage.select("table:nth-child(4) > tbody > tr:nth-child(1) > td");
        int j = 0;
        for (int i = FIRST_ROW_LEFT_COLUMNS_WITHOUT_GRADES - 1; i < firstRow.size() - FIRST_ROW_RIGHT_COLUMNS_WITHOUT_GRADES; i++) {
            if (gradesIndexes.contains(i)) {
                module.getGrades().get(j++).setWeek(firstRow.get(i).html());
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onGradesScrapedListener.onGradesScraped(module);
    }

    public interface OnGradesScrapedListener {
        void onGradesScraped(@Nullable Module module);
    }
}
