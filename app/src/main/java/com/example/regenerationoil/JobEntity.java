package com.example.regenerationoil;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "jobs")
public class JobEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String oilType;
    private String operationCycle;
    private double volume;
    private int cleanlinessClass;
    private double waterContent;
    private double gasContent;
    private double acidity;
    private double dielectricLoss;
    private double breakdownVoltage;

    public JobEntity(String oilType, String operationCycle, double volume, int cleanlinessClass, double waterContent, double gasContent, double acidity, double dielectricLoss, double breakdownVoltage) {
        this.oilType = oilType;
        this.operationCycle = operationCycle;
        this.volume = volume;
        this.cleanlinessClass = cleanlinessClass;
        this.waterContent = waterContent;
        this.gasContent = gasContent;
        this.acidity = acidity;
        this.dielectricLoss = dielectricLoss;
        this.breakdownVoltage = breakdownVoltage;
    }

    @Override
    public String toString() {
        return "JobEntity{" +
                "oilType='" + oilType + '\'' +
                ", operationCycle='" + operationCycle + '\'' +
                ", volume=" + volume +
                ", cleanlinessClass=" + cleanlinessClass +
                ", waterContent=" + waterContent +
                ", gasContent=" + gasContent +
                ", acidity=" + acidity +
                ", dielectricLoss=" + dielectricLoss +
                ", breakdownVoltage=" + breakdownVoltage +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }

    public String getOperationCycle() {
        return operationCycle;
    }

    public void setOperationCycle(String operationCycle) {
        this.operationCycle = operationCycle;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getCleanlinessClass() {
        return cleanlinessClass;
    }

    public void setCleanlinessClass(int cleanlinessClass) {
        this.cleanlinessClass = cleanlinessClass;
    }

    public double getWaterContent() {
        return waterContent;
    }

    public void setWaterContent(double waterContent) {
        this.waterContent = waterContent;
    }

    public double getGasContent() {
        return gasContent;
    }

    public void setGasContent(double gasContent) {
        this.gasContent = gasContent;
    }

    public double getAcidity() {
        return acidity;
    }

    public void setAcidity(double acidity) {
        this.acidity = acidity;
    }

    public double getDielectricLoss() {
        return dielectricLoss;
    }

    public void setDielectricLoss(double dielectricLoss) {
        this.dielectricLoss = dielectricLoss;
    }

    public double getBreakdownVoltage() {
        return breakdownVoltage;
    }

    public void setBreakdownVoltage(double breakdownVoltage) {
        this.breakdownVoltage = breakdownVoltage;
    }
}
