package ru.hofftech.json;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
public class ParcelJson {
    private List<String> shape;
    private List<Integer> pos;

    @SuppressWarnings("unused")
    public ParcelJson() {
    }

    public ParcelJson(List<String> shape, int x, int y) {
        this.shape = shape;
        this.pos = List.of(x, y);
    }

    public List<String> getShape() {
        return shape;
    }

    @SuppressWarnings("unused")
    public void setShape(List<String> shape) {
        this.shape = shape;
    }

    @SuppressWarnings("unused")
    public List<Integer> getPos() {
        return pos;
    }

    @SuppressWarnings("unused")
    public void setPos(List<Integer> pos) {
        this.pos = pos;
    }

    @JsonIgnore
    public int getX() {
        return pos.getFirst();
    }

    @JsonIgnore
    public int getY() {
        return pos.get(1);
    }
}