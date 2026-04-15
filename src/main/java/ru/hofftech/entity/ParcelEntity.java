package ru.hofftech.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "parcels")
public class ParcelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private char symbol;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false)
    private List<String> shape;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int area;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ParcelEntity() {}

    public ParcelEntity(String name, char symbol, List<String> shape, int width, int height, int area) {
        this.name = name;
        this.symbol = symbol;
        this.shape = shape;
        this.width = width;
        this.height = height;
        this.area = area;
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public char getSymbol() { return symbol; }
    public void setSymbol(char symbol) { this.symbol = symbol; }

    public List<String> getShape() { return shape; }
    public void setShape(List<String> shape) { this.shape = shape; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getArea() { return area; }
    public void setArea(int area) { this.area = area; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}