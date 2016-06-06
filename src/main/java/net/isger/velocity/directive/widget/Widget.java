package net.isger.velocity.directive.widget;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

public class Widget {

    private List<Node> nodes;

    public Widget() {
        this.nodes = new ArrayList<Node>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void render(InternalContextAdapter context, Writer writer)
            throws IOException,MethodInvocationException, ParseErrorException, ResourceNotFoundException {
        for (Node node : nodes) {
            node.render(context, writer);
        }
    }

    public int getNodeCount() {
        return nodes.size();
    }

}
