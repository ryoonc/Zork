/**
 * Created with IntelliJ IDEA.
 * User: Richard
 * Date: 11/4/12
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Zork {
    private Map<String, Room> roomList = new HashMap<String, Room>();                   // Original room references
    private Map<String, Item> itemList = new HashMap<String, Item>();                   // Global item list
    private Map<String, Container> containerList = new HashMap<String, Container>();    // Global container list
    private Map<String, Creature> creatureList = new HashMap<String, Creature>();       // Global creature list
    private Room currentRoom;                                                      // Current room
    private Map<String, Item> currentInventory = new HashMap<String, Item>();      // User inventory
    private Map<String, GameObject> objects = new HashMap<String, GameObject>();   // Global object list
    private boolean gameStarted;

    public static void main(String argv[]) {

        try
        {
            // Create an instance of this class, since main() is
            // created with the class instead of with the
            // instance, it can do this. This is so that non-static
            // access is possible
            Zork obj = new Zork();
            obj.session();
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
    }

    public void session()
    {
        System.out.println("Game Started");
        gameStarted = false;
        boolean running = true;
        boolean overwrite;
        while (running)
        {
            try{
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                String inputString = bufferRead.readLine();
                overwrite = false;
                if (gameStarted)
                {
                    overwrite = executeTrigger(inputString);
                }
                if (!overwrite)
                {
                    running = executeCommand(inputString);
                }
                else
                {
                    running = true;
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public boolean executeTrigger(String command)
    {
        String[] commandTerms = command.split(" ");

        boolean conditionsSatisfied = false;
        for (Trigger t : currentRoom.getTriggers())
        {
            if (t.getTriggercommand().equals(commandTerms[0]))
            {

                ArrayList<Condition> triggerConditions = t.getTriggercond();

                for (Condition thisCondition : triggerConditions)
                {

                    if (thisCondition.getHas() != null)
                    {
                        if (thisCondition.getHas().equals("yes"))
                        {
                            if (!thisCondition.getOwner().equals("inventory"))
                            {
                                GameObject referencedObj = objects.get(thisCondition.getOwner());
                                conditionsSatisfied = referencedObj.hasObject(thisCondition.getObject());
                            }
                            else
                            {
                                conditionsSatisfied = currentInventory.containsKey(thisCondition.getObject());
                            }
                        }
                        else
                        {
                            if (!thisCondition.getOwner().equals("inventory"))
                            {
                                GameObject referencedObj = objects.get(thisCondition.getOwner());
                                conditionsSatisfied = !referencedObj.hasObject(thisCondition.getObject());
                            }
                            else
                            {
                                conditionsSatisfied = !currentInventory.containsKey(thisCondition.getObject());
                            }
                        }
                    }

                    // For a object-status condition, usually within Items
                    else if (thisCondition.getStatus() != null)
                    {
                        GameObject referencedObj = objects.get(thisCondition.getObject());
                        conditionsSatisfied = referencedObj.getStatus().equals(thisCondition.getStatus());
                    }
                }
                if (t.getTriggerprint() != null && conditionsSatisfied)
                    System.out.println(t.getTriggerprint());
            }
        }

        return conditionsSatisfied;
    }

    public boolean executeTriggerEffects(String objectToConsider)
    {
        boolean conditionsSatisfied = false;
        GameObject effectObject = objects.get(objectToConsider);
        for (Trigger t : effectObject.getTriggers())
        {
            ArrayList<Condition> triggerConditions = t.getTriggercond();
            for (Condition thisCondition : triggerConditions)
            {
                // For a has-type trigger
                if (thisCondition.getHas() != null)
                {
                    if (thisCondition.getHas().equals("yes"))
                    {
                        if (!thisCondition.getOwner().equals("inventory"))
                        {
                            GameObject referencedObj = objects.get(thisCondition.getOwner());
                            conditionsSatisfied = referencedObj.hasObject(thisCondition.getObject());
                        }
                        else
                        {
                            conditionsSatisfied = currentInventory.containsKey(thisCondition.getObject());
                        }
                    }
                    else
                    {
                        if (!thisCondition.getOwner().equals("inventory"))
                        {
                            GameObject referencedObj = objects.get(thisCondition.getOwner());
                            conditionsSatisfied = !referencedObj.hasObject(thisCondition.getObject());
                        }
                        else
                        {
                            conditionsSatisfied = !currentInventory.containsKey(thisCondition.getObject());
                        }
                    }
                }

                // For a object-status condition, usually within Items
                else if (thisCondition.getStatus() != null)
                {
                    GameObject referencedObj = objects.get(thisCondition.getObject());
                    conditionsSatisfied = !referencedObj.getStatus().equals(thisCondition.getStatus());
                }
            }
            if (!conditionsSatisfied)
            {
                if (t.getTriggerprint() != null)
                    System.out.println(t.getTriggerprint());
                doAction(t.getTriggeraction());
            }
        }

        if (objects.get(objectToConsider) instanceof Item)
        {

            Item item = (Item)objects.get(objectToConsider);

            for (Trigger t : item.getTriggers())
            {
                ArrayList<Condition> triggerConditions = t.getTriggercond();
                for (Condition thisCondition : triggerConditions)
                {
                    // For a has-type trigger
                    if (thisCondition.getHas() != null)
                    {
                        if (thisCondition.getHas().equals("yes"))
                        {
                            if (!thisCondition.getOwner().equals("inventory"))
                            {
                                GameObject referencedObj = objects.get(thisCondition.getOwner());
                                conditionsSatisfied = referencedObj.hasObject(thisCondition.getObject());
                            }
                            else
                            {
                                conditionsSatisfied = currentInventory.containsKey(thisCondition.getObject());
                            }
                        }
                        else
                        {
                            if (!thisCondition.getOwner().equals("inventory"))
                            {
                                GameObject referencedObj = objects.get(thisCondition.getOwner());
                                conditionsSatisfied = !referencedObj.hasObject(thisCondition.getObject());
                            }
                            else
                            {
                                conditionsSatisfied = !currentInventory.containsKey(thisCondition.getObject());
                            }
                        }
                    }
                }
                if (conditionsSatisfied)
                {
                    if (t.getTriggerprint() != null)
                        System.out.println(t.getTriggerprint());
                    doAction(t.getTriggeraction());
                }
            }
        }


        return conditionsSatisfied;
    }

    public boolean executeCommand(String command)
    {
        String[] commandTerms = command.split(" ");

        if (commandTerms[0].equals("IPA1"))
        {
            generateGame(commandTerms[1]);
            currentRoom = roomList.get("Entrance");
            System.out.println(currentRoom.getDescription());
        }
        else if (commandTerms[0].equals("n"))
        {
            String north = currentRoom.canGoNorth();
            if (north != null)
            {
                currentRoom = roomList.get(north);
                System.out.println(currentRoom.getDescription());
            }
            else
            {
                System.out.println("Can't go that way.");
            }
        }
        else if (commandTerms[0].equals("s"))
        {
            String south = currentRoom.canGoSouth();
            if (south != null)
            {
                currentRoom = roomList.get(south);
                System.out.println(currentRoom.getDescription());
            }
            else
            {
                System.out.println("Can't go that way.");
            }
        }
        else if (commandTerms[0].equals("e"))
        {
            String east = currentRoom.canGoEast();
            if (east != null)
            {
                currentRoom = roomList.get(east);
                System.out.println(currentRoom.getDescription());
            }
            else
            {
                System.out.println("Can't go that way.");
            }
        }
        else if (commandTerms[0].equals("w"))
        {
            String west = currentRoom.canGoWest();
            if (west != null)
            {
                currentRoom = roomList.get(west);
            }
            else
            {
                System.out.println("Can't go that way.");
            }
        }
        else if (commandTerms[0].equals("attack") && commandTerms[2].equals("with"))
        {
            if (currentRoom.getCreatures().containsKey(commandTerms[1]))
            {
                Creature roomCreature = currentRoom.getCreatures().get(commandTerms[1]);  // get the current room's specified creature
                boolean overridden = executeTriggerEffects(commandTerms[1]);

                if (currentInventory.containsKey(commandTerms[3]))
                {
                    if (roomCreature.isVulnerable(commandTerms[3]))
                    {
                        Attack attack = roomCreature.getAttack();
                        Condition attackCondition = attack.getCondition();
                        String attackObjectStatus = attackCondition.getStatus();
                        String invItemStatus = currentInventory.get(attackCondition.getObject()).getStatus();
                        if (attackObjectStatus.equals(invItemStatus))
                        {
                            //System.out.println("Status has matched");
                            if (attack.getPrint() != null)
                                System.out.println(attack.getPrint());

                            for (String a : attack.getActions())
                                doAction(a);
                        }
                    }
                }
                else
                {
                    System.out.println("Item " + commandTerms[3] + " is not in your inventory");
                }
            }
            else
            {
                System.out.println("Creature " + commandTerms[1] + " does not exist");
            }
        }
        else if (commandTerms[0].equals("turn") && commandTerms[1].equals("on"))
        {
            if (currentInventory.containsKey(commandTerms[2]))
            {
                Item specificItem = currentInventory.get(commandTerms[2]);
                if (specificItem.getTurnon() != null)
                {
                    boolean overridden = executeTriggerEffects(specificItem.getName());
                    if (!overridden)
                    {
                        TurnOn specificTurnOn = specificItem.getTurnon();
                        System.out.println("You activate the " + specificItem.getName());
                        System.out.println(specificTurnOn.getPrint());
                        doAction(specificTurnOn.getActions());
                    }
                }
                else
                {
                    System.out.println(specificItem.getName() + "can not be turned on");
                }
            }
            else
            {
                System.out.println("Item " + commandTerms[2] + " is not in your inventory");
            }

        }
        else if (commandTerms[0].equals("l") || commandTerms[0].equals("look"))
        {
            Iterator entries = currentRoom.getItems().entrySet().iterator();
            if (currentRoom.getItems().entrySet().size() != 0)
            {
                System.out.print("You look around and see: ");
                while (entries.hasNext()) {
                    Map.Entry thisEntry = (Map.Entry) entries.next();
                    String key = (String)thisEntry.getKey();
                    System.out.print(key);
                    if (entries.hasNext())
                    {
                        System.out.print(", ");
                    }
                    else
                    {
                        System.out.println();
                    }
                }
            }
            else
            {
                System.out.println("You look around the room but see nothing else");
            }
        }
        else if (commandTerms[0].equals("read"))
        {
            if (currentInventory.containsKey(commandTerms[1]))
            {
                Item specificItem = currentInventory.get(commandTerms[1]);
                if (specificItem.getWriting() != null)
                {
                    System.out.println(specificItem.getWriting());
                }
                else
                {
                    System.out.println("Nothing Written");
                }
            }
            else
            {
                System.out.println("Item not found");
            }
        }
        else if (commandTerms[0].equals("put"))
        {
            if (currentInventory.containsKey(commandTerms[1]) && currentRoom.hasContainer(commandTerms[3]))
            {
                // Container exists, proceed
                Container specificContainer = currentRoom.getSpecificContainer(commandTerms[3]);
                if (specificContainer.acceptItem(commandTerms[1]))
                {
                    // The container accepts this item
                    boolean overridden = executeTriggerEffects(specificContainer.getName());
                    if (!overridden)
                    {
                        specificContainer.addItem(commandTerms[1], currentInventory.get(commandTerms[1]));
                        currentInventory.remove(commandTerms[1]);
                    }
                }
                else
                {
                    System.out.println("Cannot put " + commandTerms[1] + " in " + commandTerms[3]);
                }
            }
            else
            {
                if (!currentInventory.containsKey(commandTerms[1]))
                    System.out.println("Item " + commandTerms[1] + " not in inventory");
                if (!currentRoom.hasContainer(commandTerms[3]))
                    System.out.println("Container " + commandTerms[3] + " not in room");
            }
        }
        else if (commandTerms[0].equals("open") && commandTerms[1].equals("exit"))
        {
            if (currentRoom.getType() != null && currentRoom.getType().equals("exit"))
            {
                System.out.println("Game Over");
                return false;   // end the game
            }
            else
            {
                System.out.println("This room is not an exit!");
            }
        }
        else if (commandTerms[0].equals("open"))
        {
            if (currentRoom.getContainers().containsKey(commandTerms[1]))
            {
                Container roomContainer = currentRoom.getContainers().get(commandTerms[1]);  // get the current room's specified container
                boolean overridden = executeTriggerEffects(commandTerms[1]);
                if (!overridden)
                {
                    Map<String, Item> containerItems = roomContainer.getItem();                  // get all items in the container
                    if (!containerItems.entrySet().isEmpty())
                    {
                        System.out.print(roomContainer.getName() + " contains ");

                        Iterator entries = containerItems.entrySet().iterator();
                        while (entries.hasNext()) {                                              // iterate through the items
                            Map.Entry thisEntry = (Map.Entry) entries.next();
                            String key = (String)thisEntry.getKey();
                            System.out.print(key);
                            if (entries.hasNext())
                            {
                                System.out.print(", ");
                            }
                            else
                            {
                                System.out.println();
                            }
                        }
                        currentRoom.getItems().putAll(containerItems);
                        containerItems.clear();
                    }
                    else
                    {
                        System.out.println(commandTerms[1] + " is empty");
                    }
                }
            }
        }
        else if (commandTerms[0].equals("take"))
        {
            if (currentRoom.getItems().containsKey(commandTerms[1]))
            {
                currentInventory.put(commandTerms[1], currentRoom.getItems().get(commandTerms[1]));
                boolean overridden = executeTriggerEffects(commandTerms[1]);
                if (!overridden)
                {
                    System.out.println("Item " + commandTerms[1] + " added to inventory");

                }
                currentRoom.removeItem(commandTerms[1]);
            }
            else
            {
                System.out.println("Cannot take item");
            }
        }
        else if (commandTerms[0].equals("drop"))
        {
            if (currentInventory.containsKey(commandTerms[1]))
            {
                System.out.println("Item " + commandTerms[1] + " dropped on floor");
                currentRoom.addItem(commandTerms[1], currentInventory.get(commandTerms[1]));
                currentInventory.remove(commandTerms[1]);
            }
            else
            {
                System.out.println("Item " + commandTerms[1] + " not in inventory");
            }
        }

        // inventory
        else if (commandTerms[0].equals("i") && commandTerms.length == 1)
        {

            if (!currentInventory.entrySet().isEmpty())
            {
                Iterator entries = currentInventory.entrySet().iterator();
                System.out.print("Inventory: ");
                while (entries.hasNext()) {
                    Map.Entry thisEntry = (Map.Entry) entries.next();
                    String key = (String)thisEntry.getKey();
                    System.out.print(key);
                    if (entries.hasNext())
                    {
                        System.out.print(", ");
                    }
                    else
                    {
                        System.out.println();
                    }
                }
            }
            else
            {
                System.out.println("Inventory: empty");
            }
        }
        else
        {
            System.out.println("Error");
        }
        return true;
    }

    public boolean doAction(String action)
    {
        if (action != null)
        {
            String[] actionTerms = action.split(" ");

            if (actionTerms[0].equals("Add"))
            {
                GameObject targetObject = objects.get(actionTerms[3]);
                if (targetObject instanceof Room)
                {
                    GameObject sourceObject = objects.get(actionTerms[1]);
                    if (sourceObject instanceof Item)
                    {
                        ((Room) targetObject).addItem(sourceObject.getName(), (Item)sourceObject);
                    }
                    else if (sourceObject instanceof Container)
                    {
                        ((Room) targetObject).addContainer(sourceObject.getName(), (Container) sourceObject);
                    }
                    else if (sourceObject instanceof Creature)
                    {
                        ((Room) targetObject).addCreature(sourceObject.getName(), (Creature) sourceObject);
                    }
                    else
                    {
                        System.out.println("ERROR: Invalid object to add to Room");
                    }
                }
                else if (targetObject instanceof Container)
                {
                    GameObject sourceObject = objects.get(actionTerms[1]);
                    if (sourceObject instanceof Item)
                    {
                        ((Container) targetObject).addItem(sourceObject.getName(), (Item)sourceObject);
                    }
                    else
                    {
                        System.out.println("ERROR: Invalid object to add to Container");
                    }
                }
                else
                {
                    System.out.println("ERROR: Cannot add object"); // should never see this with correct XML
                }
            }
            else if (actionTerms[0].equals("Delete"))
            {
                // Go through every room
                Iterator entries = roomList.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry thisEntry = (Map.Entry) entries.next();
                    Room roomToModify = (Room)thisEntry.getValue();
                    roomToModify.removeObject(actionTerms[1]);
                }
                // Remove from inventory
                if (currentInventory.containsKey(actionTerms[1]))
                {
                    currentInventory.remove(actionTerms);
                }
            }
            else if (actionTerms[0].equals("Update"))
            {
                GameObject targetObject = objects.get(actionTerms[1]);
                targetObject.setStatus(actionTerms[3]);
            }
            else if (actionTerms[0].equals("Game") && actionTerms[1].equals("Over"))
            {
                System.out.println("Flawless Victory!");
                return false;
            }
        }
        return true;
    }

    public void generateGame(String filename) {
        try {

            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            ////////////////////////////////////////////////////////////

            NodeList elementitemList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < elementitemList.getLength(); temp++) {
                String name = null;
                String description = null;
                String writing = null;
                String status = null;
                TurnOn turnons = null;
                ArrayList<Trigger> triggers = new ArrayList<Trigger>();

                Node nNode = elementitemList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    name = getTagValue("name", eElement);
                    writing = getTagValue("writing", eElement);
                    description = getTagValue("description", eElement);
                    status = getTagValue("status", eElement);
                    if (name != null)
                    {
//                        System.out.println("Name : " + name);
//                        if (writing != null)
//                            System.out.println("Writing : " + writing);
//                        if (description != null)
//                            System.out.println("Desc : " + description);
//                        if (status != null)
//                            System.out.println("Stat : " + status);

                        NodeList nList = eElement.getElementsByTagName("turnon");
//                        System.out.println("Turnon");
                        for (int x = 0; x < nList.getLength(); x++)
                        {
                            Node xNode = nList.item(x);
                            if (xNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element xElement = (Element) xNode;
                                String turnonprint = getTagValue("print", xElement);
                                String turnonaction = getTagValue("action", xElement);

                                turnons = new TurnOn(turnonprint, turnonaction);
//                                System.out.println("--Print : " + turnonprint);
//                                System.out.println("--Action : " + turnonaction);
                            }
                        }

                        NodeList triggerList = eElement.getElementsByTagName("trigger");
                        for (int x = 0; x < triggerList.getLength(); x++)
                        {
                            String triggercommand = null;
                            String triggertype = null;
                            String triggerprint = null;
                            String triggeraction = null;
                            ArrayList<Condition> triggercond = new ArrayList<Condition>();
                            Node xNode = triggerList.item(x);
                            if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element xElement = (Element) xNode;
                                triggercommand = getTagValue("command", xElement);
                                triggertype = getTagValue("type", xElement);
                                triggerprint = getTagValue("print", xElement);
                                triggeraction = getTagValue("action", xElement);

//                                System.out.println("--Command : " + triggercommand);
//                                System.out.println("--Type : " + triggertype);
//                                System.out.println("--Print : " + triggerprint);

                                NodeList list12 = eElement.getElementsByTagName("condition");
//                                System.out.println("--Condition");
                                for (int n = 0; n < list12.getLength(); n++)
                                {
                                    Node jNode = list12.item(n);
                                    if (jNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element jElement = (Element) jNode;
                                        String conditionobject = getTagValue("object", jElement);
                                        String conditionowner = getTagValue("owner", jElement);
                                        String conditionstatus = getTagValue("status", jElement);
                                        String conditionhas = getTagValue("has", jElement);
                                        Condition objCond = new Condition(conditionobject, conditionstatus, conditionhas, conditionowner);
                                        triggercond.add(objCond);
//                                        System.out.println("----Object : " + conditionobject);
//                                        System.out.println("----Owner : " + conditionowner);
//                                        System.out.println("----Status : " + conditionstatus);
//                                        System.out.println("----Has : " + conditionhas);
                                    }
                                }
                            }
                            Trigger objTrig = new Trigger(triggercommand, triggertype, triggerprint, triggercond, triggeraction);
                            triggers.add(objTrig);
                        }

                        Item objItem = new Item(name, description, writing, status, turnons, triggers);
                        this.itemList.put(name, objItem);
                        this.objects.put(name, objItem);
                    }
                }
            }

            ////////////////////////////////////////////////////////////

            NodeList elementCreatureList = doc.getElementsByTagName("creature");
//            System.out.println("Creature");
//            System.out.println("-----------------------");

            for (int temp = 0; temp < elementCreatureList.getLength(); temp++) {
                String name = null;
                String description = null;
                String status = null;
                ArrayList<String> vulnerability = new ArrayList<String>();
                ArrayList<Trigger> triggers = new ArrayList<Trigger>();
                Attack attack = null;

                Node nNode = elementCreatureList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    name = getTagValue("name", eElement);
                    description = getTagValue("description", eElement);
                    status = getTagValue("status", eElement);
                    if (name != null)
                    {
//                        System.out.println("Name : " + name);
//                        if (description != null)
//                            System.out.println("Desc : " + description);
//                        if (status != null)
//                            System.out.println("Stat : " + status);

                        NodeList nList = eElement.getElementsByTagName("attack");
//                        System.out.println("Attack");
                        for (int x = 0; x < nList.getLength(); x++)
                        {
                            Node xNode = nList.item(x);
                            if (xNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element xElement = (Element) xNode;
                                String printaction = getTagValue("print", xElement);

                                ArrayList<String> attackaction = new ArrayList<String>();

                                NodeList nodeList = eElement.getElementsByTagName("action");
                                for (int i = 0; i < nodeList.getLength(); i++) {
                                    NodeList node = nodeList.item(i).getChildNodes();
                                    Node nValue = (Node) node.item(0);
                                    attackaction.add(nValue.getNodeValue());
//                                    System.out.println("Action : " + nValue.getNodeValue());
                                }

                                 nodeList = eElement.getElementsByTagName("vulnerability");
                                for (int i = 0; i < nodeList.getLength(); i++) {
                                    NodeList node = nodeList.item(i).getChildNodes();
                                    Node nValue = (Node) node.item(0);
                                    vulnerability.add(nValue.getNodeValue());
//                                    System.out.println("Vulnerability : " + nValue.getNodeValue());
                                }

                                NodeList conditionList = eElement.getElementsByTagName("condition");
//                                System.out.println("Condition");
                                Condition condition = null;
                                for (int m = 0; m < conditionList.getLength(); m++)
                                {
                                    Node gNode = conditionList.item(m);
                                    if (gNode.getNodeType() == Node.ELEMENT_NODE) {

                                        Element iElement = (Element) gNode;
                                        String conditionobject = getTagValue("object", iElement);
                                        String conditionstatus = getTagValue("status", iElement);
                                        String conditionhas = getTagValue("status", iElement);
                                        String conditionowner = getTagValue("status", iElement);

                                        condition = new Condition(conditionobject, conditionstatus, conditionhas, conditionowner);
                                    }
                                }

                                attack = new Attack(printaction, attackaction, condition);
//                                System.out.println("--Print : " + attackaction);
//                                System.out.println("--Action : " + attackaction);
                            }
                        }

                        NodeList triggerList = eElement.getElementsByTagName("trigger");
//                        System.out.println("Trigger");
                        for (int x = 0; x < triggerList.getLength(); x++)
                        {
                            String triggercommand = null;
                            String triggertype = null;
                            String triggerprint = null;
                            String triggeraction = null;
                            ArrayList<Condition> triggercond = new ArrayList<Condition>();
                            Node xNode = triggerList.item(x);
                            if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element xElement = (Element) xNode;
                                triggercommand = getTagValue("command", xElement);
                                triggertype = getTagValue("type", xElement);
                                triggerprint = getTagValue("print", xElement);
                                triggeraction = getTagValue("action", xElement);

//                                System.out.println("--Command : " + triggercommand);
//                                System.out.println("--Type : " + triggertype);
//                                System.out.println("--Print : " + triggerprint);

                                NodeList list12 = eElement.getElementsByTagName("condition");
//                                System.out.println("--Condition");
                                for (int n = 0; n < list12.getLength(); n++)
                                {
                                    Node jNode = list12.item(n);
                                    if (jNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element jElement = (Element) jNode;
                                        String conditionobject = getTagValue("object", jElement);
                                        String conditionowner = getTagValue("owner", jElement);
                                        String conditionstatus = getTagValue("status", jElement);
                                        String conditionhas = getTagValue("has", jElement);
                                        Condition objCond = new Condition(conditionobject, conditionstatus, conditionhas, conditionowner);
                                        triggercond.add(objCond);
//                                        System.out.println("----Object : " + conditionobject);
//                                        System.out.println("----Owner : " + conditionowner);
//                                        System.out.println("----Status : " + conditionstatus);
//                                        System.out.println("----Has : " + conditionhas);
                                    }
                                }
                            }
                            Trigger objTrig = new Trigger(triggercommand, triggertype, triggerprint, triggercond, triggeraction);
                            triggers.add(objTrig);
                        }

                        Creature objCreature = new Creature(name, description, status, attack, vulnerability, triggers);
                        this.creatureList.put(name, objCreature);
                        this.objects.put(name, objCreature);
                    }
                }
            }

            ////////////////////////////////////////////////////////////

            NodeList elementContainerList = doc.getElementsByTagName("container");
//            System.out.println("Container");
//            System.out.println("-----------------------");

            for (int temp = 0; temp < elementContainerList.getLength(); temp++) {
                String name = null;
                String description = null;
                ArrayList<String> accept = new ArrayList<String>();
                HashMap<String, Item> items = new HashMap<String, Item>();
                String status = null;
                ArrayList<Trigger> triggers = new ArrayList<Trigger>();

                Node nNode = elementContainerList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    name = getTagValue("name", eElement);
                    description = getTagValue("description", eElement);
                    status = getTagValue("status", eElement);
                    if (name != null)
                    {
//                        System.out.println("Name : " + name);
//                        if (description != null)
//                            System.out.println("Desc : " + description);
//                        if (status != null)
//                            System.out.println("Stat : " + status);

                        NodeList nodeAcceptList = eElement.getElementsByTagName("accept");
                        for (int i = 0; i < nodeAcceptList.getLength(); i++) {
                            NodeList node = nodeAcceptList.item(i).getChildNodes();
                            Node nValue = (Node) node.item(0);
                            accept.add(nValue.getNodeValue());
//                            System.out.println("Accept : " + nValue.getNodeValue());
                        }

                        NodeList nodeItemList = eElement.getElementsByTagName("item");
                        for (int i = 0; i < nodeItemList.getLength(); i++) {
                            NodeList node = nodeItemList.item(i).getChildNodes();
                            Node nValue = (Node) node.item(0);
                            items.put(nValue.getNodeValue(), itemList.get(nValue.getNodeValue()));   // take from global item list
//                            System.out.println("Item : " + nValue.getNodeValue());
                        }

                        NodeList triggerList = eElement.getElementsByTagName("trigger");
//                        System.out.println("Trigger");
                        for (int x = 0; x < triggerList.getLength(); x++)
                        {
                            String triggercommand = null;
                            String triggertype = null;
                            String triggerprint = null;
                            String triggeraction = null;
                            ArrayList<Condition> triggercond = new ArrayList<Condition>();
                            Node xNode = triggerList.item(x);
                            if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element xElement = (Element) xNode;
                                triggercommand = getTagValue("command", xElement);
                                triggertype = getTagValue("type", xElement);
                                triggerprint = getTagValue("print", xElement);
                                triggeraction = getTagValue("action", xElement);

//                                System.out.println("--Command : " + triggercommand);
//                                System.out.println("--Type : " + triggertype);
//                                System.out.println("--Print : " + triggerprint);

                                NodeList list12 = eElement.getElementsByTagName("condition");
//                                System.out.println("--Condition");
                                for (int n = 0; n < list12.getLength(); n++)
                                {
                                    Node jNode = list12.item(n);
                                    if (jNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element jElement = (Element) jNode;
                                        String conditionobject = getTagValue("object", jElement);
                                        String conditionowner = getTagValue("owner", jElement);
                                        String conditionstatus = getTagValue("status", jElement);
                                        String conditionhas = getTagValue("has", jElement);
                                        Condition objCond = new Condition(conditionobject, conditionstatus, conditionhas, conditionowner);
                                        triggercond.add(objCond);
//                                        System.out.println("----Object : " + conditionobject);
//                                        System.out.println("----Owner : " + conditionowner);
//                                        System.out.println("----Status : " + conditionstatus);
//                                        System.out.println("----Has : " + conditionhas);
                                    }
                                }
                            }
                            Trigger objTrig = new Trigger(triggercommand, triggertype, triggerprint, triggercond, triggeraction);
                            triggers.add(objTrig);
                        }

                        Container objContainer = new Container(name, description, status, accept, items, triggers);
                        this.containerList.put(name, objContainer);
                        this.objects.put(name, objContainer);
//                        System.out.println(">>>>>>>>>>>>" + objItem.getName());
                    }
                }
            }

            ////////////////////////////////////////////////////////////

//            System.out.println("Room");
            NodeList nodeRoomList = doc.getElementsByTagName("room");
//            System.out.println("-----------------------");

            for (int temp = 0; temp < nodeRoomList.getLength(); temp++) {
                String name = null;
                String desc = null;
                String type = null;
                String status = null;
                ArrayList<Trigger> triggers = new ArrayList<Trigger>();
                HashMap<String, Container> containers = new HashMap<String, Container>();
                HashMap<String, Item> items = new HashMap<String, Item>();
                HashMap<String, Creature> creatures = new HashMap<String, Creature>();
                HashMap<String,String> borders = new HashMap<String, String>();

                Node nNode = nodeRoomList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    name = getTagValue("name", eElement);
                    desc = getTagValue("description", eElement);
                    status = getTagValue("status", eElement);
                    
                    Node typeNode = getNodeByParent(eElement, "type", "room");

//                    System.out.println("Room Name : " + name);
//                    System.out.println("Description : " + desc);

                    if (typeNode != null)
                    {
                        type = typeNode.getNodeValue();
//                        System.out.println("Type : " + type);
                    }

                    NodeList nList = eElement.getElementsByTagName("border");
//                    System.out.println("Border");
                    for (int x = 0; x < nList.getLength(); x++)
                    {
                        Node xNode = nList.item(x);
                        if (xNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element xElement = (Element) xNode;
                            String bordername = getTagValue("name", xElement);
                            String borderdir = getTagValue("direction", xElement);
                            borders.put(borderdir, bordername);

//                            System.out.println("--Name : " + bordername);
//                            System.out.println("--Direction : " + borderdir);
                        }
                    }


                    NodeList triggerList = eElement.getElementsByTagName("trigger");
//                    System.out.println("Trigger");
                    for (int x = 0; x < triggerList.getLength(); x++)
                    {
                        String triggercommand = null;
                        String triggertype = null;
                        String triggerprint = null;
                        String triggeraction = null;
                        ArrayList<Condition> triggercond = new ArrayList<Condition>();
                        Node xNode = triggerList.item(x);
                        if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element xElement = (Element) xNode;
                            triggercommand = getTagValue("command", xElement);
                            triggertype = getTagValue("type", xElement);
                            triggerprint = getTagValue("print", xElement);
                            triggeraction = getTagValue("action", xElement);

//                            System.out.println("--Command : " + triggercommand);
//                            System.out.println("--Type : " + triggertype);
//                            System.out.println("--Print : " + triggerprint);

                            NodeList list12 = eElement.getElementsByTagName("condition");
//                            System.out.println("--Condition");
                            for (int n = 0; n < list12.getLength(); n++)
                            {
                                Node jNode = list12.item(n);
                                if (jNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element jElement = (Element) jNode;
                                    String conditionobject = getTagValue("object", jElement);
                                    String conditionowner = getTagValue("owner", jElement);
                                    String conditionstatus = getTagValue("status", jElement);
                                    String conditionhas = getTagValue("has", jElement);
                                    Condition objCond = new Condition(conditionobject, conditionstatus, conditionhas, conditionowner);
                                    triggercond.add(objCond);
//                                    System.out.println("----Object : " + conditionobject);
//                                    System.out.println("----Owner : " + conditionowner);
//                                    System.out.println("----Status : " + conditionstatus);
//                                    System.out.println("----Has : " + conditionhas);
                                }
                            }
                        }
                        Trigger objTrig = new Trigger(triggercommand, triggertype, triggerprint, triggercond, triggeraction);
                        triggers.add(objTrig);
                    }

                    NodeList nodeList = eElement.getElementsByTagName("item");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        NodeList node = nodeList.item(i).getChildNodes();
                        Node nValue = (Node) node.item(0);
                        items.put(nValue.getNodeValue(), itemList.get(nValue.getNodeValue()));   // take from global item list
//                        System.out.println("Item : " + nValue.getNodeValue());
                    }

                    nodeList = eElement.getElementsByTagName("container");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        NodeList node = nodeList.item(i).getChildNodes();
                        Node nValue = (Node) node.item(0);
                        containers.put(nValue.getNodeValue(), containerList.get(nValue.getNodeValue()));
//                        System.out.println("Container : " + nValue.getNodeValue());
                    }

                    nodeList = eElement.getElementsByTagName("creature");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        NodeList node = nodeList.item(i).getChildNodes();
                        Node nValue = (Node) node.item(0);
                        creatures.put(nValue.getNodeValue(), creatureList.get(nValue.getNodeValue()));
//                        System.out.println("Creature : " + nValue.getNodeValue());
                    }
                }
                // Create a new room
                Room objRoom = new Room(name, desc, type, status, triggers, containers, items, creatures, borders);
                // Place it in Room array
                this.roomList.put(name, objRoom);
                this.objects.put(name, objRoom);
            }

            gameStarted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private static String getTagValue(String sTag, Element eElement) {
//        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
//
//        Node nValue = (Node) nlList.item(0);
//
//        return nValue.getNodeValue();
//    }
    private static String getTagValue(String sTag, Element eElement) {
        Node elementNodes = eElement.getElementsByTagName(sTag).item(0);
        if (elementNodes != null)
        {
            NodeList nlList = elementNodes.getChildNodes();
            Node nValue = (Node) nlList.item(0);
            return nValue.getNodeValue();
        }
        else
            return null;
    }

    /**
     * Find direct descendant tag of another tag
     * @param eElement Starting point for our search
     * @param tagName Node name to look up
     * @param parent Parent node name
     * @return matching Node (null if none)
     */
    public static Node getNodeByParent(Element eElement, String tagName, String parent) {
        NodeList elementNodes = eElement.getElementsByTagName(tagName);
        if (elementNodes != null)
        {
            for (int i = 0; i < elementNodes.getLength(); i++)
            {
                Node nValue = (Node) elementNodes.item(i);
                if(nValue.getParentNode().getNodeName().equals(parent)){
                    return nValue.getChildNodes().item(0);
                }
            }
        }
        return null;
    }

    /**
     * Find direct descendant tag of another tag
     * @param eElement Starting point for our search
     * @param tagName Node name to look up
     * @param parent Parent node name
     * @return matching Node (null if none)
     */
    public static Node getNodeByParent(Element eElement, String tagName, Node parent) {
        NodeList elementNodes = eElement.getElementsByTagName(tagName);
        if (elementNodes != null)
        {
            for (int i = 0; i < elementNodes.getLength(); i++)
            {
                Node nValue = (Node) elementNodes.item(i);
                if(nValue.getParentNode().getNodeName().equals(parent)){
                    return nValue.getChildNodes().item(0);
                }
            }
        }
        return null;
    }
}

