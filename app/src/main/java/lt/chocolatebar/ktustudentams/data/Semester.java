package lt.chocolatebar.ktustudentams.data;

import java.util.List;

public class Semester {

    private String name;
    private List<Module> modules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
