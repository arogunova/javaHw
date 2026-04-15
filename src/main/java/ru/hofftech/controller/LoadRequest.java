package ru.hofftech.controller;

import java.util.List;

public class LoadRequest {
    private List<String> parcelNames;
    private String algorithm;
    private int maxTrucks;

    public List<String> getParcelNames() { return parcelNames; }
    public void setParcelNames(List<String> parcelNames) { this.parcelNames = parcelNames; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public int getMaxTrucks() { return maxTrucks; }
    public void setMaxTrucks(int maxTrucks) { this.maxTrucks = maxTrucks; }
}