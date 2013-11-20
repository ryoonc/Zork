import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Richard
 * Date: 11/6/12
 * Time: 2:54 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class GameObject {
    String name;
    String status;
    String description;
    ArrayList<Trigger> triggers;

    public abstract String getName();
    public abstract String getStatus();
    public abstract ArrayList<Trigger> getTriggers();

    public abstract String getDescription();

    public abstract void setStatus(String status);
    public abstract boolean hasObject (String objectToFind);
}
