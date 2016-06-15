package net.isger.velocity.directive.widget;

import net.isger.velocity.VelocityContext;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * 组件指令
 * 
 * @author issing
 * 
 */
public class WidgetsDirective extends WidgetDirective {

    public int getType() {
        return BLOCK;
    }

    public VelocityContext getWidgetContext(InternalContextAdapter context,
            Node node) {
        VelocityContext widgetContext = super.getWidgetContext(context, node);
        /* 添加内容节点 */
        Node screenNode = node.jjtGetChild(getPropertyCount(node));
        widgetContext.put(SCREEN, new WidgetScreen(this, screenNode,
                new InternalContextAdapterImpl(widgetContext)));
        return widgetContext;

    }
}
