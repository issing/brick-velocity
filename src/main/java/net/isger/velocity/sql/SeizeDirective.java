package net.isger.velocity.sql;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.isger.velocity.directive.AbstractDirective;

import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * 占位指令
 * 
 * @author issing
 *
 */
public class SeizeDirective extends AbstractDirective {

    public static final String KEY_SEIZES = "brick.stub.seizes";

    public int getType() {
        return LINE;
    }

    /**
     * 占位渲染
     */
    public boolean render(InternalContextAdapter context, Writer writer,
            Node node) throws IOException, ResourceNotFoundException,
            ParseErrorException, MethodInvocationException {
        List<Object> seizes = getSeizes(context);
        if (seizes == null) {
            seizes = new ArrayList<Object>();
            context.put(KEY_SEIZES, seizes);
        }
        Object seize = getSeize(context, node);
        writer.write("?");
        seize: {
            if (seize instanceof Collection) {
                seize = ((Collection<?>) seize).toArray();
            } else if (!(seize instanceof Object[])) {
                seizes.add(seize);
                break seize;
            }
            Object[] values = (Object[]) seize;
            int size = values.length;
            seizes.add(values[0]);
            for (int i = 1; i < size; i++) {
                writer.write(", ?");
                seizes.add(values[i]);
            }
        }
        writer.flush();
        return true;
    }

    /**
     * 获取占位值
     * 
     * @param context
     * @param node
     * @return
     */
    public final Object getSeize(InternalContextAdapter context, Node node) {
        if (getPropertyCount(node) >= 1) {
            return node.jjtGetChild(0).value(context);
        }
        throw new ParseErrorException("(X) The " + this.getName()
                + " directive's name must be configured"
                + " [eg: #seize(\"name\")]");
    }

    @SuppressWarnings("unchecked")
    public static List<Object> getSeizes(Context context) {
        List<Object> seizes = (List<Object>) context.get(KEY_SEIZES);
        if (seizes == null) {
            seizes = new ArrayList<Object>();
            context.put(KEY_SEIZES, seizes);
        }
        return seizes;
    }
}
