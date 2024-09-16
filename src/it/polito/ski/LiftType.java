package it.polito.ski;

public class LiftType {
    String category, code;
    int capacity;
    public LiftType(String code, String category, int capacity) {
        this.code = code;
        this.category = category;
        this.capacity = capacity;
    }
    public String getCategory() {
        return category;
    }
    public int getCapacity() {
        return capacity;
    }
    public String getCode() {
        return code;
    }
    
}
