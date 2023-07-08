package pojo;

public class Orders {
    private int id;
    private String renderName;  // 租借人名字
    private String renderNumber;  // 租借人联系电话
    private String orderNumber;  // 订单编号
    private int days;   // 租借天数

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRenderName() {
        return renderName;
    }

    public void setRenderName(String renderName) {
        this.renderName = renderName;
    }

    public String getRenderNumber() {
        return renderNumber;
    }

    public void setRenderNumber(String renderNumber) {
        this.renderNumber = renderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

}
