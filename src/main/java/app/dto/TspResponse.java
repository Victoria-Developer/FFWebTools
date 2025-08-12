package app.dto;

import java.util.LinkedList;
import java.util.List;

public class TspResponse {
    private Area area;
    private List<Coordinate> solution;
    
    public TspResponse(Area area, LinkedList<Coordinate> solution) {
        this.area = area;
        this.solution = solution;
    }

    public Area getArea() {
        return area;
    }

    public List<Coordinate> getSolution() {
        return solution;
    }

    public void setArea(Area area) {
        this.area = area;
    }
        
    public void setSolution(List<Coordinate> solution) {
        this.solution = solution;
    }
}