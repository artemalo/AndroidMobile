package com.example.walkofinterest.models.adapters;

public class CategoryModel {
    String name;
    String description;
    int image;
    boolean isChecked;

    public CategoryModel(String name, String description, int image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isSelected) {
        this.isChecked = isSelected;
    }
}
