package net.isger.velocity.directive.widget;

import java.io.Writer;

import org.apache.velocity.runtime.parser.node.Node;

public interface WidgetLayout {

    public Widget getWidget(Object constraints);

    public void lay(Node node, Object constraints);

    public void render(WidgetScreen screen, Writer writer);

}
