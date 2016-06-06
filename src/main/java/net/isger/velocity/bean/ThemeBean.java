package net.isger.velocity.bean;

public class ThemeBean {

    private String path;

    private String name;

    private String location;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        StringBuffer buffer = new StringBuffer(128);
        buffer.append(path);
        buffer.append("/").append(name);
        buffer.append("/").append(location);
        this.location = buffer.toString();
    }

}
