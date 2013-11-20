import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Richard
 * Date: 11/4/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Item extends GameObject{
    private String name;
    private String description;
    private String writing;
    private String status;
    private TurnOn turnon;
    private ArrayList<Trigger> triggers;

    public Item(String name, String description, String writing, String status, TurnOn turnOns, ArrayList<Trigger> triggers)
    {
        this.name = name;
        this.description = description;
        this.writing = writing;
        this.status = status;
        this.triggers = triggers;
        this.turnon = turnOns;
//        System.out.println("Item <" + name + "> Created");
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

    public String getWriting() {
        return writing;
    }

    public void setStatus(String status) {
        this.status = status;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean hasObject (String objectToFind)
    {
        return false;
    }

    public TurnOn getTurnon() {
        return turnon;
    }

    public ArrayList<Trigger> getTriggers()
    {
        return triggers;
    }
}
