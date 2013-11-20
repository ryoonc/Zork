import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Richard
 * Date: 11/4/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Creature extends GameObject{
    private String name;
    private String description;
    private String status;
    private ArrayList<String> vulnerability;
    private Attack attack;
    private ArrayList<Trigger> triggers;

    public Creature(String name, String description, String status, Attack attack, ArrayList<String> vulnerability, ArrayList<Trigger> triggers)
    {
        this.name = name;
        this.description = description;
        this.status = status;
        this.triggers = triggers;
        this.vulnerability = vulnerability;
        this.attack = attack;
//        System.out.println("Creature <" + name + "> Created");
    }

    public boolean isVulnerable(String weapon)
    {
        for (String s : vulnerability)
        {
            if (s.equals(weapon))
                return true;
        }
        return false;
    }

    public Attack getAttack() {
        return attack;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean hasObject (String objectToFind)
    {
        return false;
    }

    public ArrayList<Trigger> getTriggers()
    {
        return triggers;
    }
}
