package pojo;

public class Equipment {
    private int id;
    private String name;    // 设备名称
    private int isRent;  // 是否被人租借
    private String orderNumber;  // 订单编号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isIdle() {
        return isRent;
    }

    public void setIdle(int idle) {
        isRent = idle;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }


}
