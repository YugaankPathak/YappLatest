package com.example.userloginsqlite;

public class Apparel {
    private String id;
    private String type;
    private String color;
    private String material;
    private String upperLower;
    private String imageUrl;  // For storing image URL or base64 string
    private String ownership; // For storing ownership status

    // Default constructor
    public Apparel() {
    }

    // Constructor with parameters
    public Apparel(String id, String type, String color, String material, String upperLower, String imageUrl, String ownership) {
        this.id = id;
        this.type = type;
        this.color = color;
        this.material = material;
        this.upperLower = upperLower;
        this.imageUrl = imageUrl;
        this.ownership = ownership;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return upperLower;
    }

    public void setUpperLower(String upperLower) {
        this.upperLower = upperLower;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOwnership() {
        return ownership;
    }



    @Override
    public String toString() {
        return "Apparel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", material='" + material + '\'' +
                ", upperLower='" + upperLower + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ownership='" + ownership + '\'' +
                '}';
    }

    // Set the image from a byte array (convert to base64 if needed)
    public void setImage(byte[] imageBlob) {
        if (imageBlob != null) {
            // Convert byte array to base64 string
            String base64Image = android.util.Base64.encodeToString(imageBlob, android.util.Base64.DEFAULT);
            this.imageUrl = "data:image/png;base64," + base64Image;
        } else {
            this.imageUrl = null;  // Handle null case
        }
    }

    // Set the ownership status
    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }
}
