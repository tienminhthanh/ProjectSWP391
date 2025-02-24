/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Macbook
 */
public class DeliveryOption {
    private int deliveryOptionID;
    private String optionName;
    private int estimatedTime;
    private int optionCost;

    public DeliveryOption() {
    }

    public DeliveryOption(int deliveryOptionID, String optionName, int estimatedTime, int optionCost) {
        this.deliveryOptionID = deliveryOptionID;
        this.optionName = optionName;
        this.estimatedTime = estimatedTime;
        this.optionCost = optionCost;
    }

    public int getDeliveryOptionID() {
        return deliveryOptionID;
    }

    public void setDeliveryOptionID(int deliveryOptionID) {
        this.deliveryOptionID = deliveryOptionID;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getOptionCost() {
        return optionCost;
    }

    public void setOptionCost(int optionCost) {
        this.optionCost = optionCost;
    }

    @Override
    public String toString() {
        return "DeliveryOption{" + "deliveryOptionID=" + deliveryOptionID + ", optionName=" + optionName + ", estimatedTime=" + estimatedTime + ", optionCost=" + optionCost + '}';
    }

    
}
