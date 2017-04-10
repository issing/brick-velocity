package net.isger.velocity.bean;

public class ThemeBean {

    private String path;

    private String name;

    private String action;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLocation() {
        StringBuffer buffer = new StringBuffer(128);
        buffer.append(path);
        buffer.append("/").append(name);
        buffer.append("/").append(action);
        return buffer.toString();
    }

}
