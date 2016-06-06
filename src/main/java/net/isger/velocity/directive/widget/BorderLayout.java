package net.isger.velocity.directive.widget;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import net.isger.velocity.VelocityContext;

import org.apache.velocity.runtime.parser.node.Node;

public class BorderLayout extends AbstractLayout {

    public static final String CONSTRAINTS = "constraints";

    private Map<Object, Widget> widgets;

    public BorderLayout(WidgetScreen screen) {
        super(screen);
        widgets = new HashMap<Object, Widget>();
    }

    public void lay(Node node, Object constraints) {
        if (isWidgetNode(node)) {
            VelocityContext context = screen.getWidgetContext(node);
            if (constraints != null
                    && constraints.equals(context.get(CONSTRAINTS))) {
                Widget widget = widgets.get(constraints);
                if (widget == null) {
                    widget = new Widget();
                }
            }
        }
    }

    public Widget getWidget(Object constraints) {
        return widgets.get(constraints);
    }

    public void render(WidgetScreen screen, Writer writer) {

    }

}
