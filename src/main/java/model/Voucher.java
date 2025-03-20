package model;

/**
 *
 * @author trungtinh
 */
public class Voucher {

    private int voucherID;
    private String voucherName;
    private double voucherValue;
    private int voucherQuantity;
    private int minimumPurchaseAmount;
    private String voucherDateCreated;
    private int voucherDuration;
    private int adminID;
    private boolean voucherIsActive;
    private boolean expiry;
    private String voucherType;
    private Double maxDiscountAmount;
    private String voucherDateStarted;

    public Voucher() {
    }

    public Voucher(String voucherName, double voucherValue, int voucherQuantity, int minimumPurchaseAmount, String voucherDateCreated, int voucherDuration, int adminID, boolean voucherIsActive, boolean expiry, String voucherType, Double maxDiscountAmount, String voucherDateStarted) {
        this.voucherName = voucherName;
        this.voucherValue = voucherValue;
        this.voucherQuantity = voucherQuantity;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.voucherDateCreated = voucherDateCreated;
        this.voucherDuration = voucherDuration;
        this.adminID = adminID;
        this.voucherIsActive = voucherIsActive;
        this.expiry = expiry;
        this.voucherType = voucherType;
        this.maxDiscountAmount = maxDiscountAmount;
        this.voucherDateStarted = voucherDateStarted;
    }

    public Voucher(int voucherID, String voucherName, double voucherValue, int voucherQuantity, int minimumPurchaseAmount, String voucherDateCreated, int voucherDuration, int adminID, boolean voucherIsActive, boolean expiry, String voucherType, Double maxDiscountAmount, String voucherDateStarted) {
        this.voucherID = voucherID;
        this.voucherName = voucherName;
        this.voucherValue = voucherValue;
        this.voucherQuantity = voucherQuantity;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.voucherDateCreated = voucherDateCreated;
        this.voucherDuration = voucherDuration;
        this.adminID = adminID;
        this.voucherIsActive = voucherIsActive;
        this.expiry = expiry;
        this.voucherType = voucherType;
        this.maxDiscountAmount = maxDiscountAmount;
        this.voucherDateStarted = voucherDateStarted;
    }

    public String getVoucherDateCreated() {
        return voucherDateCreated;
    }

    public void setVoucherDateCreated(String voucherDateCreated) {
        this.voucherDateCreated = voucherDateCreated;
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

    public int getVoucherQuantity() {
        return voucherQuantity;
    }

    public void setVoucherQuantity(int voucherQuantity) {
        this.voucherQuantity = voucherQuantity;
    }

    public int getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(int minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public int getVoucherDuration() {
        return voucherDuration;
    }

    public void setVoucherDuration(int voucherDuration) {
        this.voucherDuration = voucherDuration;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public boolean isVoucherIsActive() {
        return voucherIsActive;
    }

    public void setVoucherIsActive(boolean voucherIsActive) {
        this.voucherIsActive = voucherIsActive;
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

    public String getVoucherDateStarted() {
        return voucherDateStarted;
    }

    public void setVoucherDateStarted(String voucherDateStarted) {
        this.voucherDateStarted = voucherDateStarted;
    }

}
