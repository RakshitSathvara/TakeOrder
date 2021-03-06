package in.vaksys.takeorder.dbPojo;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/29/2016.
 */
public class AddOrder extends RealmObject {

    String orderId;
    private String barcode;
    private String quality;
    private String price;
    private String description;
    private Date startDate;
    private String buyerName;
    private boolean flag = false;
    //private RealmResults<AddOrder> addOrderList;

//    public AddOrder(RealmResults<AddOrder> addOrderList, boolean flag) {
//        this.addOrderList = addOrderList;
//        this.flag = flag;
//    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
