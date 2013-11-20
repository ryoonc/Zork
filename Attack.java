import java.util.ArrayList;

/**
 * Chang Yoon Kim
 * User: Richard
 * Date: 11/6/12
 * Time: 4:41 AM
 */
public class Attack {
    private String print;
    private ArrayList<String> actions;
    private Condition condition;

    public Attack(String print, ArrayList<String> actions, Condition condition)
    {
        this.print = print;
        this.actions = actions;
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public String getPrint() {
        return print;
    }

    public ArrayList<String> getActions() {
        return actions;
    }


}
