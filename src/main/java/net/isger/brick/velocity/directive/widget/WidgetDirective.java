package net.isger.brick.velocity.directive.widget;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import net.isger.brick.velocity.VelocityConstants;
import net.isger.brick.velocity.VelocityContext;
import net.isger.brick.velocity.directive.AbstractDirective;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * 组件指令
 * 
 * @author issing
 * 
 */
public class WidgetDirective extends AbstractDirective {

    public static final String SCREEN = "screen";

    private Map<Node, VelocityContext> contexts;

    public WidgetDirective() {
        contexts = new HashMap<Node, VelocityContext>();
    }

    public int getType() {
        return LINE;
    }

    /**
     * 渲染指令
     * 
     */
    public final boolean render(InternalContextAdapter context, Writer writer,
            Node node) throws IOException, ResourceNotFoundException,
            ParseErrorException, MethodInvocationException {
        /* 生成组件内容 */
        Template template = getWidgetTemplate(getEngine(context),
                getWidgetName(node, context)); // 获取组件模板
        try {
            template.merge(getWidgetContext(node, context), writer); // 输出组件内容
            writer.flush();
        } finally {
            contexts.remove(node);
        }
        return true;
    }

    /**
     * 获取节点名
     * 
     * @param node
     * @param context
     * @return
     */
    public final String getWidgetName(Node node, InternalContextAdapter context) {
        if (getPropertyCount(node) >= 1) {
            Object firstChild = node.jjtGetChild(0).value(context);
            if (firstChild instanceof String) {
                return (String) firstChild;
            }
        }
        throw new ParseErrorException("(X) The " + this.getName()
                + " directive's component name must be configured"
                + " [eg: #widget(\"ui.Newline\")]");
    }

    /**
     * 获取模板
     * 
     * @param engine
     * @param widgetName
     * @return
     */
    public Template getWidgetTemplate(VelocityEngine engine, String widgetName) {
        return engine.getTemplate(
                getProperty(engine, VelocityConstants.KEY_WIDGET_PATH,
                        VelocityConstants.WIDGET_PATH)
                        + "/"
                        + getProperty(engine, VelocityConstants.KEY_THEME_NAME,
                                VelocityConstants.THEME_NAME)
                        + "/"
                        + widgetName.replaceFirst("(ui.)", "") + ".vm",
                getProperty(engine, VelocityConstants.KEY_ENCODING,
                        VelocityConstants.ENCODING));
    }

    /**
     * 获取上下文
     * 
     * @param node
     * @param context
     * @return
     */
    public VelocityContext getWidgetContext(Node node,
            InternalContextAdapter context) {
        VelocityContext widgetContext = contexts.get(node);
        if (widgetContext == null) {
            widgetContext = new VelocityContext(getEngine(context),
                    createPropertyMap(context, node), context);
            contexts.put(node, widgetContext);
        }
        return widgetContext;
    }

    /**
     * 获取配置属性
     * 
     * @param engine
     * @param key
     * @param def
     * @return
     */
    private String getProperty(VelocityEngine engine, String key, String def) {
        String val = (String) engine.getProperty(key);
        if (val == null) {
            val = def;
        }
        return val;
    }

}
