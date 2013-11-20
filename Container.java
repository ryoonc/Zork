import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Richard
 * Date: 11/4/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Container extends GameObject{
    private String name;
    private String description;
    private String status;
    private ArrayList<String> accept;
    private Map<String, Item> item;
    private ArrayList<Trigger> triggers;

    public Container(String name, String description, String status, ArrayList<String> accept, HashMap<String, Item> item, ArrayList<Trigger> triggers)
    {
        this.name = name;
        this.description = description;
        this.status = status;
        this.triggers = triggers;
        this.accept = accept;
        this.item = item;
//        System.out.println("Container <" + name + "> Created");
    }

    public boolean acceptItem(String itemName)
    {
        if (accept.contains(itemName))
            return true;
        else
            return false;
    }

    public void addItem(String itemName, Item objItem)
    {
        item.put(itemName, objItem);
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

    public Map<String, Item> getItem() {
        return item;
    }

    public ArrayList<Trigger> getTriggers()
    {
        return triggers;
    }

    public boolean hasObject (String objectToFind)
    {
        boolean has = false;
        has = item.containsKey(objectToFind);
        return has;
    }
}
