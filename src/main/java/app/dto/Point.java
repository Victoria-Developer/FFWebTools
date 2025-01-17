package app.dto;

// Created to avoid using java.awt.
public record Point(double x, double y) {
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
