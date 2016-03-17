package net.isger.brick.velocity.directive;

import java.util.HashMap;
import java.util.Map;

import net.isger.brick.velocity.VelocityContext;
import net.isger.brick.velocity.VelocityConstants;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * 抽象指令
 * 
 * @author issing
 * 
 */
public abstract class AbstractDirective extends Directive {

    /** 指令前缀 */
    private static final String DIRECTIVE_PREFIX = "x";

    /** 指令名称 */
    private String name;

    public String getName() {
        synchronized (this) {
            if (this.name == null || !this.name.equals("Directive")) {
                this.name = DIRECTIVE_PREFIX
                        + this.getClass().getSimpleName()
                                .replaceFirst("Directive$", "");
            }
        }
        return this.name;
    }

    /**
     * 获取模板引擎
     * 
     * @param context
     * @return
     */
    protected VelocityEngine getEngine(InternalContextAdapter context) {
        VelocityEngine engine;
        Context ic = context.getInternalUserContext();
        if (ic instanceof VelocityContext) {
            engine = ((VelocityContext) ic).getEngine();
        } else {
            engine = (VelocityEngine) context.get(VelocityConstants.KEY_ENGINE);
        }
        return engine;
    }

    /**
     * 创建属性集合
     * 
     * @param context
     * @param node
     * @return
     * @throws ParseErrorException
     * @throws MethodInvocationException
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> createPropertyMap(
            InternalContextAdapter context, Node node)
            throws ParseErrorException, MethodInvocationException {
        Map<String, Object> propertyMap;
        int childCount = node.jjtGetNumChildren();
        if (getType() == BLOCK) {
            childCount--;
        }
        Node firstChild = null;
        Object firstValue = null;
        if (childCount == 2 && null != (firstChild = node.jjtGetChild(1))
                && null != (firstValue = firstChild.value(context))
                && firstValue instanceof Map) {
            propertyMap = (Map<String, Object>) firstValue;
        } else {
            propertyMap = new HashMap<String, Object>();
            for (int index = 1; index < childCount; index++) {
                this.addProperty(propertyMap, context, node.jjtGetChild(index));
            }
        }
        return propertyMap;
    }

    /**
     * 存放属性
     * 
     * @param propertyMap
     * @param context
     * @param node
     * @throws ParseErrorException
     * @throws MethodInvocationException
     */
    protected void addProperty(Map<String, Object> propertyMap,
            InternalContextAdapter context, Node node)
            throws ParseErrorException, MethodInvocationException {
        String param = node.value(context).toString();
        int idx = param.indexOf("=");
        if (idx == -1) {
            throw new ParseErrorException(
                    "#"
                            + this.getName()
                            + " arguments must include an assignment operator [eg: #xWidget(\"ui.Panel\", \"title=Brick\" )]");
        }
        String key = param.substring(0, idx);
        String value = param.substring(idx + 1);
        propertyMap.put(key, value);
    }
}
