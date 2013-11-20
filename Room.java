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
public class Room extends GameObject{
    private String name;
    private String description;
    private String type;
    private String status;

    private ArrayList<Trigger> triggers;
    private Map<String, Container> containers;
    private Map<String, Item> items;
    private HashMap<String, Creature> creatures;
    private Map<String,String> borders; // ex: north, Main-room

    public Room(String name, String description, String type, String status, ArrayList<Trigger> triggers, HashMap<String, Container> containers, HashMap<String, Item> items, HashMap<String, Creature> creatures, HashMap<String,String> borders)
    {
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.triggers = triggers;
        this.containers = containers;
        this.items = items;
        this.creatures = creatures;
        this.borders = borders;
//        System.out.println("Room <" + name + "> Created");
    }

    public String canGoNorth()
    {
        String north = borders.get("north");
        if (north != null)
            return north;
        else
            return null;
    }

    public String canGoSouth()
    {
        String south = borders.get("south");
        if (south != null)
            return south;
        else
            return null;
    }

    public String canGoEast()
    {
        String east = borders.get("east");
        if (east != null)
            return east;
        else
            return null;
    }

    public String canGoWest()
    {
        String west = borders.get("west");
        if (west != null)
            return west;
        else
            return null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> items) {
        this.items = items;
    }

    public void removeItem(String itemName)
    {
        items.remove(itemName);
    }

    public void addItem(String itemName, Item addedItem)
    {
        items.put(itemName, addedItem);
    }

    public Map<String, Container> getContainers() {
        return containers;
    }

    public boolean hasContainer(String containerName)
    {
        if (containers.containsKey(containerName))
            return true;
        else
            return false;
    }

    public Container getSpecificContainer(String containerName)
    {
        return containers.get(containerName);
    }

    public void addContainer(String nameContainer, Container containerToAdd)
    {
        containers.put(nameContainer, containerToAdd);
    }

    public void addCreature(String nameCreature, Creature creatureToAdd)
    {
        creatures.put(nameCreature, creatureToAdd);
    }

    public void removeObject(String objectToRemove)
    {
        // Check borders
        //System.out.println(borders);
        // Fast one liner from stackexchange to remove from map by value
        while (borders.values().remove(objectToRemove));
        //System.out.println(borders);

        if (items.containsKey(objectToRemove))
        {
            items.remove(objectToRemove);
            //System.out.println("Removed item " + objectToRemove + " from " + name);
        }
        if (creatures.containsKey(objectToRemove))
        {
            creatures.remove(objectToRemove);
            //System.out.println("Removed creature " + objectToRemove + " from " + name);
        }
        if (containers.containsKey(objectToRemove))
        {
            containers.remove(objectToRemove);
            //System.out.println("Removed container " + objectToRemove + " from " + name);
        }
    }

    public ArrayList<Trigger> getTriggers()
    {
        return triggers;
    }

    public boolean hasObject (String objectToFind)
    {
        boolean has = false;
        has = items.containsKey(objectToFind);
        has = creatures.containsKey(objectToFind);
        has = containers.containsKey(objectToFind);
        return has;
    }

    public HashMap<String, Creature> getCreatures() {
        return creatures;
    }
}
