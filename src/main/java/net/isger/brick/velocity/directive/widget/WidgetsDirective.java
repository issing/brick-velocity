package net.isger.brick.velocity.directive.widget;

import net.isger.brick.velocity.VelocityContext;

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

    public VelocityContext getWidgetContext(Node node,
            InternalContextAdapter context) {
        VelocityContext widgetContext = super.getWidgetContext(node, context);
        /* 添加内容节点 */
        Node screenNode = node.jjtGetChild(getPropertyCount(node));
        widgetContext.put(SCREEN, new WidgetScreen(this, screenNode,
                new InternalContextAdapterImpl(widgetContext)));
        return widgetContext;

    }
}
