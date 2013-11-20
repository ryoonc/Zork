/**
 * Created with IntelliJ IDEA.
 * User: Richard
 * Date: 11/6/12
 * Time: 3:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class Border {
    private String direction;
    private String name;

    public Border(String name, String direction)
    {
        this.name = name;
        this.direction = direction;
        //System.out.println("Created Border object with name: " + name + " direction: " + direction);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
