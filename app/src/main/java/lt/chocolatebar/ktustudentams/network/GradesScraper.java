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
import lt.chocolatebar.ktustudentams.data.Week;
import lt.chocolatebar.ktustudentams.data.Module;

public class GradesScraper extends AsyncTask<Void, Void, Void> {

    private final static String GRADES_URL = "https://uais.cr.ktu.lt/ktuis/STUD_SS2.infivert";
    private final static int LEFT_COLUMNS_WITHOUT_GRADES = 4;
    private final static int RIGHT_COLUMNS_WITHOUT_GRADES = 4;

    private Module module;
    private Document gradesPage;
    private List<Integer> weeksIndexes;
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
            parseShortNames();
            parseFullNames();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseFirstGrade() {
        Elements thirdRow = gradesPage.select("table:nth-child(4) > tbody > tr:nth-child(3) > td");
        weeksIndexes = new ArrayList<>();
        List<Week> weeks = new ArrayList<>();
        int i;
        for (i = LEFT_COLUMNS_WITHOUT_GRADES - 1; i < thirdRow.size() - RIGHT_COLUMNS_WITHOUT_GRADES; i++) {
            Element cell = thirdRow.get(i);
            if (isCellOpenForGrade(cell)) {
                weeksIndexes.add(i);
                Week week = new Week();
                if (!isCellEmpty(cell)) {
                    week.setFirstGrade(cell.html());
                }
                weeks.add(week);
            }
        }
        module.setWeeks(weeks);
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
                    module.getWeeks().get(j).setSecondGrade(cell.html());
                }
                j++;
            }
        }
    }

    private boolean columnHasFirstGrade(int i) {
        return weeksIndexes.contains(i + LEFT_COLUMNS_WITHOUT_GRADES - 1);
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
            if (columnHasFirstGrade(i)) {
                if (isCellOpenForGrade(cell)) {
                    module.getWeeks().get(j).setThirdGradeAvailable(true);
                    if (!isCellEmpty(cell)) {
                        module.getWeeks().get(j).setThirdGrade(fifthRow.get(i).html());
                    }
                }
                j++;
            }
        }
    }

    private void parseWeeks() {
        Elements firstRow = gradesPage.select("table:nth-child(4) > tbody > tr:nth-child(1) > td");
        int j = 0;
        for (int i = LEFT_COLUMNS_WITHOUT_GRADES - 1; i < firstRow.size() - 2; i++) {
            if (weeksIndexes.contains(i)) {
                module.getWeeks().get(j++).setWeek(firstRow.get(i).html());
            }
        }
    }

    private void parseShortNames(){
        Elements shortNameRow = gradesPage.select("table:nth-child(4) > tbody > tr:nth-child(2) > td");
        int j = 0;
        for (int i = 1; i < shortNameRow.size() - 3; i++){
            Element cell = shortNameRow.get(i);
                if(!isCellEmpty(cell))
                    module.getWeeks().get(j++).setShortName(shortNameRow.get(i).html());
        }
    }

    private void parseFullNames(){
        Elements namesRows = gradesPage.select("body > table:nth-child(5) > tbody > tr");
        int j = 0;
        for (int i = 1; i < namesRows.size() - 1; i++){
            String shortName = namesRows.get(i).select(":root > td:nth-child(1)").html();
            String fullName = namesRows.get(i).select(":root > td:nth-child(2)").html();
            Week foundWeek = findWeekByShortName(shortName);
            if (foundWeek != null) {
                foundWeek.setName(fullName);
            }
        }
    }

    private Week findWeekByShortName(String shortName){
        for(Week week : module.getWeeks()){
            if(week.getShortName().equals(shortName)){
                return week;
            }
        }
        return null;
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
