/**
 * Chang Yoon Kim
 * User: Richard
 * Date: 11/6/12
 * Time: 5:03 AM
 */
public class Condition {
    private String object;
    private String status;
    private String has;
    private String owner;

    public Condition(String object, String status, String has, String owner)
    {
        this.object = object;
        this.status = status;
        this.has = has;
        this.owner = owner;
//        System.out.println("Condition <" + object + "> Created");
    }

    public String getObject() {
        return object;
    }

    public String getStatus() {
        return status;
    }

    public String getHas() {
        return has;
    }

    public String getOwner() {
        return owner;
    }
}
