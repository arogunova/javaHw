package ru.hofftech.controller;

import java.util.List;

/**
 * DTO для создания посылки через REST API.
 */
public class CreateParcelRequest {
    private String name;
    private List<String> shape;
    private char symbol;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getShape() { return shape; }
    public void setShape(List<String> shape) { this.shape = shape; }

    public char getSymbol() { return symbol; }
    public void setSymbol(char symbol) { this.symbol = symbol; }
}