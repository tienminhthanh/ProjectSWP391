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
    private boolean expiry;
    private String voucherType;
    private Double maxDiscountAmount;
    private String dateStarted;

    public Voucher() {
    }

    public Voucher(String voucherName, double voucherValue, int quantity, int minimumPurchaseAmount, String dateCreated, int duration, int adminID, boolean isActive, boolean expiry, String voucherType, Double maxDiscountAmount, String dateStarted) {
        this.voucherName = voucherName;
        this.voucherValue = voucherValue;
        this.quantity = quantity;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.adminID = adminID;
        this.isActive = isActive;
        this.expiry = expiry;
        this.voucherType = voucherType;
        this.maxDiscountAmount = maxDiscountAmount;
        this.dateStarted = dateStarted;
    }

    public Voucher(int voucherID, String voucherName, double voucherValue, int quantity, int minimumPurchaseAmount, String dateCreated, int duration, int adminID, boolean isActive, boolean expiry, String voucherType, Double maxDiscountAmount, String dateStarted) {
        this.voucherID = voucherID;
        this.voucherName = voucherName;
        this.voucherValue = voucherValue;
        this.quantity = quantity;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.adminID = adminID;
        this.isActive = isActive;
        this.expiry = expiry;
        this.voucherType = voucherType;
        this.maxDiscountAmount = maxDiscountAmount;
        this.dateStarted = dateStarted;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
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

    public void setVoucherValue(double voucherValue) {
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

    public boolean isExpiry() {
        return expiry;
    }

    public void setExpiry(boolean expiry) {
        this.expiry = expiry;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public Double getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(Double maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    @Override
    public String toString() {
        return "Voucher{" + "voucherID=" + voucherID + ", voucherName=" + voucherName + ", voucherValue=" + voucherValue + ", quantity=" + quantity + ", minimumPurchaseAmount=" + minimumPurchaseAmount + ", dateCreated=" + dateCreated + ", duration=" + duration + ", adminID=" + adminID + ", isActive=" + isActive + ", expiry=" + expiry + ", voucherType=" + voucherType + ", maxDiscountAmount=" + maxDiscountAmount + ", dateStarted=" + dateStarted + '}';
    }

}
