import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Richard
 * Date: 11/4/12
 * Time: 3:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class Trigger {
    private String triggercommand;
    private String triggertype;
    private String triggerprint;
    private ArrayList<Condition> triggercond;
    private String triggeraction;
    private boolean used;

    public Trigger(String triggercommand, String triggertype, String triggerprint, ArrayList<Condition> triggercond, String triggeraction)
    {
        this.triggercommand = triggercommand;
        this.triggertype = triggertype;
        this.triggerprint = triggerprint;
        this.triggercond = triggercond;
        this.triggeraction = triggeraction;
        used = false;
//        System.out.println("Trigger <" + triggercommand + "> Created");
    }

    public String getTriggercommand() {
        return triggercommand;
    }

    public String getTriggeraction() {
        return triggeraction;
    }

    public boolean hasBeenUsed() {
        return used;
    }

    public void setUsed()
    {
        used = true;
    }

    public String getTriggertype() {
        return triggertype;
    }

    public String getTriggerprint() {
        return triggerprint;
    }

    public ArrayList<Condition> getTriggercond() {
        return triggercond;
    }
}
