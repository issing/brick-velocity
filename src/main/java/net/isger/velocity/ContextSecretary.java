package net.isger.velocity;

import net.isger.util.Helpers;
import net.isger.util.Reflects;
import net.isger.velocity.bean.LayoutBean;
import net.isger.velocity.bean.ThemeBean;
import net.isger.velocity.bean.WidgetBean;

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
        @SuppressWarnings("unchecked")
        Class<?> type = Helpers.coalesce(Reflects.getClass(name), Reflects.getClass("java.lang." + name), Reflects.getClass("java.util." + name), Reflects.getClass("net.isger.util." + name));
        return isCreate ? Reflects.newInstance(type) : type;
    }
}
