package net.isger.brick.velocity.directive.widget;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import net.isger.brick.velocity.VelocityContext;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.Node;

public class WidgetScreen {

    private WidgetDirective directive;

    private ASTBlock node;

    // private Writer writer;

    private InternalContextAdapter context;

    private WidgetLayout layout;

    public WidgetScreen(WidgetDirective directive, ASTBlock node,
            Writer writer, InternalContextAdapter context) {
        this.directive = directive;
        this.node = node;
        // this.writer = writer;
        this.context = context;
        Object layout = this.context.get("layout");
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

    public void render(Writer writer) throws IOException,
            ResourceNotFoundException, ParseErrorException,
            MethodInvocationException {
        // int count = node.jjtGetNumChildren();
        // for (int i = 0; i < count; i++) {
        // this.layout.lay(node.jjtGetChild(i));
        // }
        // node.render(context, writer);
        if (layout != null) {
            layout.render(this, writer);
            return;
        }
        node.render(context, writer);
    }

    public VelocityContext getContext(Node node) {
        return directive.getContext(node, context);
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
