package net.isger.brick.velocity;

import net.isger.brick.velocity.bean.LayoutBean;
import net.isger.brick.velocity.bean.ThemeBean;
import net.isger.brick.velocity.bean.WidgetBean;
import net.isger.util.Reflects;

/**
 * 模板上下文策略
 * 
 * @author issing
 * 
 */
public class ContextSecretary {

    private ThemeBean theme;

    private WidgetBean widget;

    private LayoutBean layout;

    public ThemeBean getTheme() {
        if (this.theme == null) {
            this.theme = new ThemeBean();
        }
        return theme;
    }

    public void setTheme(ThemeBean theme) {
        this.theme = theme;
    }

    public WidgetBean getWidget() {
        if (this.widget == null) {
            this.widget = new WidgetBean();
        }
        return widget;
    }

    public void setWidget(WidgetBean widget) {
        this.widget = widget;
    }

    public LayoutBean getLayout() {
        if (this.layout == null) {
            this.layout = new LayoutBean();
        }
        return layout;
    }

    public void setLayout(LayoutBean layout) {
        this.layout = layout;
    }

    /**
     * 镜像
     * 
     * @param name
     * @return
     */
    public static Object mirror(String name) {
        return mirror(name, false);
    }

    /**
     * 镜像
     * 
     * @param name
     * @param isCreate
     * @return
     */
    public static Object mirror(String name, boolean isCreate) {
        Class<?> type = Reflects.getClass(name);
        if (type == null) {
            type = Reflects.getClass("java.lang." + name);
        }
        return isCreate ? Reflects.newInstance(type) : type;
    }
}
