package net.isger.velocity.bean;

/**
 * 布局
 * 
 * @author issing
 * 
 */
public class LayoutBean {

    private boolean support;

    private String name;

    private String screen;

    private String path;

    public boolean isSupport() {
        return support;
    }

    public void setSupport(boolean support) {
        this.support = support;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
