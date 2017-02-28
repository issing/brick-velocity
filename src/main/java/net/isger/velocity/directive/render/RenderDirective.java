package net.isger.velocity.directive.render;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import net.isger.velocity.VelocityContext;
import net.isger.velocity.directive.AbstractDirective;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

public class RenderDirective extends AbstractDirective {

    private Map<Node, VelocityContext> contexts;

    public RenderDirective() {
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
        VelocityEngine engine = getEngine(context); // 获取引擎
        try {
            engine.evaluate(getWidgetContext(context, node), writer, "render",
                    getRenderContent(context, node)); // 渲染组件内容
            writer.flush();
        } finally {
            contexts.remove(node);
        }
        return true;
    }

    /**
     * 获取节点名
     * 
     * @param context
     * @param node
     * @return
     */
    public final String getRenderContent(InternalContextAdapter context,
            Node node) {
        if (getPropertyCount(node) >= 1) {
            Object firstChild = node.jjtGetChild(0).value(context);
            if (firstChild != null) {
                return firstChild.toString();
            }
        }
        throw new ParseErrorException("(X) The " + this.getName()
                + " directive's content must be configured"
                + " [eg: #render(\"content...\")]");
    }

    /**
     * 获取上下文
     * 
     * @param context
     * @param node
     * @return
     */
    public VelocityContext getWidgetContext(InternalContextAdapter context,
            Node node) {
        VelocityContext widgetContext = contexts.get(node);
        if (widgetContext == null) {
            widgetContext = new VelocityContext(getEngine(context),
                    createPropertyMap(context, node),
                    context.getInternalUserContext());
            contexts.put(node, widgetContext);
        }
        return widgetContext;
    }

}
