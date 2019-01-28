package com.rhodonite.deletelistview;

public class CustomListView {
    private String name;
    private int imageId;
    private boolean selected;

    public CustomListView(String name, int imageId, boolean selected) {
        this.name = name;
        this.imageId = imageId;
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

}
