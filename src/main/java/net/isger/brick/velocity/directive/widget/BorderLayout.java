package net.isger.brick.velocity.directive.widget;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import net.isger.brick.velocity.VelocityContext;

import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.Node;

public class BorderLayout extends AbstractLayout {

    private Map<Object, Widget> widgets;

    public BorderLayout(WidgetScreen screen) {
        super(screen);
        widgets = new HashMap<Object, Widget>();
    }

    public void lay(Node node, Object constraints) {
        if (isWidgetNode(node)) {
            lay(((ASTDirective) node), constraints);
        }
    }

    private void lay(ASTDirective node, Object constraints) {
        VelocityContext context = screen.getContext(node);
        if (constraints != null
                && constraints.equals(context.get("constraints"))) {
            Widget widget = widgets.get(constraints);
            if (widget == null) {
                widget = new Widget();
            }
        }
    }

    public Widget getWidget(Object constraints) {
        return null;
    }

    public void render(WidgetScreen screen, Writer writer) {
        // TODO Auto-generated method stub
        
    }

}
