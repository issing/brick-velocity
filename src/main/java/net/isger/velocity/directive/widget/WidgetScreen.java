package net.isger.velocity.directive.widget;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import net.isger.velocity.VelocityContext;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

public class WidgetScreen {

    public static final String LAYOUT = "layout";

    private WidgetDirective directive;

    private Node node;

    private InternalContextAdapter context;

    private WidgetLayout layout;

    public WidgetScreen(WidgetDirective directive, Node node,
            InternalContextAdapter context) {
        this.directive = directive;
        this.node = node;
        this.context = context;
        Object layout = this.context.get(LAYOUT);
        if (layout instanceof String) {
            this.layout = new BorderLayout(this);
        } else if (layout instanceof WidgetLayout) {
            this.layout = (WidgetLayout) layout;
        }
    }

    public WidgetDirective getDirective() {
        return this.directive;
    }

    public Widget getWidget(Object constraints) {
        Widget widget = this.layout.getWidget(constraints);
        if (widget == null) {
            int count = node.jjtGetNumChildren();
            for (int i = 0; i < count; i++) {
                this.layout.lay(node.jjtGetChild(i), constraints);
            }
            widget = this.layout.getWidget(constraints);
        }
        return widget;
    }

    public VelocityContext getWidgetContext() {
        return getWidgetContext(node);
    }

    public VelocityContext getWidgetContext(Node node) {
        return directive.getWidgetContext(context, node);
    }

    public void render(Writer writer) throws IOException,
            ResourceNotFoundException, ParseErrorException,
            MethodInvocationException {
        if (layout == null) {
            node.render(context, writer);
        } else {
            layout.render(this, writer);
        }
    }

    public String toString() {
        StringWriter writer = new StringWriter();
        try {
            render(writer);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return writer.getBuffer().toString().trim();
    }

}
