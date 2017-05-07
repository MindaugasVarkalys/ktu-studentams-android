package lt.chocolatebar.ktustudentams.network;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lt.chocolatebar.ktustudentams.data.Cookies;
import lt.chocolatebar.ktustudentams.data.Module;
import lt.chocolatebar.ktustudentams.data.Semester;

public class SemestersScraper extends AsyncTask<Void, Void, List<Semester>> {

    private final static String UAIS_URL = "https://uais.cr.ktu.lt/ktuis/";
    private final static String STUDY_PLAN_PAGE_URL = "https://uais.cr.ktu.lt/ktuis/vs.ind_planas";

    private OnSemestersScrapedListener onSemestersScrapedListener;

    public void setOnSemestersScrapedListener(OnSemestersScrapedListener onSemestersScrapedListener) {
        this.onSemestersScrapedListener = onSemestersScrapedListener;
    }

    public void scrap() {
        execute();
    }

    @Override
    protected List<Semester> doInBackground(Void... params) {
        try {
            List<String> studyYearsUrls = scrapStudyYearsUrls();
            List<Semester> semesters = new ArrayList<>();
            for (String studyYearUrl : studyYearsUrls) {
                semesters.addAll(scrapSemestersAndModulesFromOneYear(studyYearUrl));
            }
            return semesters;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> scrapStudyYearsUrls() throws IOException {
        Document studyPlanPage = Jsoup.connect(STUDY_PLAN_PAGE_URL)
                .cookies(Cookies.get())
                .get();
        Elements years = studyPlanPage.select("body > div.container-fluid > div > div.span9 > ul > li");
        List<String> urls = new ArrayList<>();
        for (Element year : years) {
            urls.add(UAIS_URL + year.select("a").attr("href"));
        }
        return urls;
    }

    private List<Semester> scrapSemestersAndModulesFromOneYear(String studyYearUrl) throws IOException {
        Document oneStudyYearPage = Jsoup.connect(studyYearUrl)
                .cookies(Cookies.get())
                .get();
        Elements semestersTables = oneStudyYearPage.select("body > div.container-fluid > table");
        List<Semester> semesters = new ArrayList<>(semestersTables.size());
        for (Element semesterTable: semestersTables) {
            Semester semester = new Semester();
            semester.setName(semesterTable.select("caption > em > b").html());
            semester.setModules(scrapModulesFromSemesterTable(semesterTable));
            semesters.add(semester);
        }
        return semesters;
    }

    private List<Module> scrapModulesFromSemesterTable(Element semesterTable) {
        Elements modulesRows = semesterTable.select("tbody > tr");
        List<Module> modules = new ArrayList<>();
        for (Element moduleRow : modulesRows) {
            if (!moduleRow.attr("class").equals("info")) {
                Module module = new Module();
                module.setCode(moduleRow.select("td:nth-child(1) > a").html());
                module.setName(moduleRow.select("td:nth-child(2)").html());
                module.setCredits(moduleRow.select("td:nth-child(4)").html());
                modules.add(module);
            }
        }
        return modules;
    }

    @Override
    protected void onPostExecute(List<Semester> semesters) {
        super.onPostExecute(semesters);
        onSemestersScrapedListener.onSemestersScraped(semesters);
    }

    public interface OnSemestersScrapedListener {
        void onSemestersScraped(@Nullable List<Semester> semesters);
    }
}
