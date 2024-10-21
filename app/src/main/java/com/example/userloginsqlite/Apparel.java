package com.example.userloginsqlite;

public class Apparel {
    private String color;
    private String material;
    private String upper_lower;
    private String image;  // For storing image URL or base64 string
    private String ownership; // For storing user
private String occasion;
    // Default constructor
    public Apparel() {
    }
//'ownership', 'color','material', 'upper_lower', 'occasion', 'image'
    // Constructor with parameters
    public Apparel(String ownership, String color, String material, String upper_lower, String occasion, String image) {

        this.ownership = ownership;
        this.color = color;
        this.material = material;
        this.upper_lower = upper_lower;
        this.image = image;
        this.occasion = occasion;
    }

    // Getters and setters

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getUpperLower() {
        return upper_lower;
    }

    public void setUpperLower(String upperLower) {
        this.upper_lower = upperLower;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwnership() {
        return ownership;
    }



    @Override
    public String toString() {
        return "Apparel{" +

                ", color='" + color + '\'' +
                ", material='" + material + '\'' +
                ", upper_lower='" + upper_lower + '\'' +
                ", image='" + image + '\'' +
                ", ownership='" + ownership + '\'' +
                '}';
    }



    // Set the owner
    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public void getInfo(Apparel a){
        System.out.println(a.occasion+" "+a.upper_lower+" "+a.color+" "+a.ownership);
    }
}
