package com.gfeed.sudhaseshu.gitamfeed;

class NewsModel {
    String description, image, heading;

    public String getDescription() {
        return description;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public NewsModel(){

    }
}
