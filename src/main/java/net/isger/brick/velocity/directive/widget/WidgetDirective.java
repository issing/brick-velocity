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
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * 组件指令
 * 
 * @author issing
 * 
 */
public final class WidgetDirective extends AbstractDirective {

    private Map<Node, VelocityContext> contexts;

    public WidgetDirective() {
        contexts = new HashMap<Node, VelocityContext>();
    }

    public int getType() {
        return BLOCK;
    }

    /**
     * 渲染指令
     * 
     */
    public boolean render(InternalContextAdapter context, Writer writer,
            Node node) throws IOException, ResourceNotFoundException,
            ParseErrorException, MethodInvocationException {
        WidgetNode widgetNode = makeWidgetNode(node, context);
        if (widgetNode != null) {
            String widgetName = widgetNode.getName();
            // 生成组件内容
            VelocityContext widgetContext = getContext(node, context);
            widgetContext.put(
                    "screen",
                    new WidgetScreen(this, (ASTBlock) node.jjtGetChild(node
                            .jjtGetNumChildren() - 1), writer,
                            new InternalContextAdapterImpl(widgetContext)));
            VelocityEngine engine = getEngine(context);
            Template template = engine.getTemplate(
                    getProperty(engine, VelocityConstants.KEY_WIDGET_PATH,
                            VelocityConstants.WIDGET_PATH)
                            + "/"
                            + getProperty(engine,
                                    VelocityConstants.KEY_THEME_NAME,
                                    VelocityConstants.THEME_NAME)
                            + "/"
                            + widgetName.replaceFirst("(ui.)", "") + ".vm",
                    getProperty(engine, VelocityConstants.KEY_ENCODING,
                            VelocityConstants.ENCODING));
            template.merge(widgetContext, writer);
            writer.flush();
            contexts.remove(node);
        }
        return true;
    }

    private WidgetNode makeWidgetNode(Node node, InternalContextAdapter context) {
        WidgetNode widgetNode = null;
        String widgetName = WidgetNode.getName(node, context);
        if (widgetName != null) {
            widgetNode = new WidgetNode(widgetName, node, context);
        }
        return widgetNode;
    }

    public VelocityContext getContext(Node node, InternalContextAdapter context) {
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
