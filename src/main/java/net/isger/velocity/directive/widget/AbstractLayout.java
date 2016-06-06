package net.isger.velocity.directive.widget;

import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.Node;

public abstract class AbstractLayout implements WidgetLayout {

    protected WidgetScreen screen;

    public AbstractLayout(WidgetScreen screen) {
        this.screen = screen;
    }

    public boolean isWidgetNode(Node node) {
        return node instanceof ASTDirective
                && screen.getDirective().getName()
                        .equals(((ASTDirective) node).getDirectiveName());
    }

    // private WidgetNode getWidgetNode(Node node) {
    // ASTDirective directiveNode = (ASTDirective) node;
    // return null;
    // }

}
