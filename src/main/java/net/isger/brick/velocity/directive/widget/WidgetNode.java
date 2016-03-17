package net.isger.brick.velocity.directive.widget;

import net.isger.brick.velocity.VelocityContext;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.Node;

public class WidgetNode {

    private String name;

    // private Node node;
    //
    // private InternalContextAdapter innerContext;
    //
    // private VelocityEngine engine;

    private VelocityContext context;

    public WidgetNode(String name, Node node,
            InternalContextAdapter innerContext) {
        this.name = name;
        // this.node = node;
        // this.innerContext = innerContext;
        // this.engine = getEngine(innerContext);
        // this.context = new VelocityContext(getEngine(innerContext),
        // createPropertyMap(innerContext, node), innerContext);
    }

    // private VelocityEngine getEngine(InternalContextAdapter context) {
    // VelocityEngine engine;
    // Context ic = context.getInternalUserContext();
    // if (ic instanceof VelocityContext) {
    // engine = ((VelocityContext) ic).getEngine();
    // } else {
    // engine = (VelocityEngine) context.get(VelocityConstants.KEY_ENGINE);
    // }
    // return engine;
    // }

    public String getName() {
        return name;
    }

    public VelocityContext getContext() {
        return context;
    }

    public static String getName(Node node, InternalContextAdapter context) {
        int childCount = node.jjtGetNumChildren() - 1;
        if (childCount > 0) {
            Object firstChild = node.jjtGetChild(0).value(context);
            if (firstChild instanceof String) {
                return (String) firstChild;
            }
        }
        return null;
    }

}
