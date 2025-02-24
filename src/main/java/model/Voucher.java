package model;


/**
 *
 * @author trungtinh
 */
public class Voucher {

    private int voucherID;
    private String voucherName;
    private double voucherValue;
    private int quantity;
    private int minimumPurchaseAmount;
    private String dateCreated;
    private int duration;
    private int adminID;
    private boolean isActive;

    public Voucher(int voucherID, String voucherName, double voucherValue, int quantity, int minimumPurchaseAmount, String dateCreated, int duration, int adminID, boolean isActive) {
        this.voucherID = voucherID;
        this.voucherName = voucherName;
        this.voucherValue = voucherValue;
        this.quantity = quantity;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.adminID = adminID;

        this.isActive = isActive;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }


    public double getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(int voucherValue) {
        this.voucherValue = voucherValue;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(int minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }


    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }


    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Voucher() {
    }
}
