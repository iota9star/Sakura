package star.iota.sakura.ui.team;

public class TeamBean {
    private final String id;
    private final String name;

    TeamBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
