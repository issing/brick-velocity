package net.isger.velocity.directive;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import net.isger.util.Helpers;
import net.isger.util.Strings;
import net.isger.velocity.VelocityConstants;
import net.isger.velocity.VelocityContext;

/**
 * 抽象指令
 * 
 * @author issing
 * 
 */
public abstract class AbstractDirective extends Directive {

    /** 指令名称 */
    private String name;

    public String getName() {
        synchronized (this) {
            if (Strings.isEmpty(this.name)) {
                this.name = Helpers.getAliasName(this.getClass(), "Directive$");
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
        Context internalContext = context.getInternalUserContext();
        if (internalContext instanceof VelocityContext) {
            engine = ((VelocityContext) internalContext).getEngine();
        } else {
            engine = (VelocityEngine) context.get(VelocityConstants.KEY_ENGINE);
        }
        return engine;
    }

    /**
     * 获取属性总数
     * 
     * @param node
     * @return
     */
    protected int getPropertyCount(Node node) {
        int count = node.jjtGetNumChildren();
        if (getType() == BLOCK) {
            count--;
        }
        return count;
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
    protected Map<String, Object> createPropertyMap(InternalContextAdapter context, Node node) throws ParseErrorException, MethodInvocationException {
        Map<String, Object> propertyMap;
        int propCount = getPropertyCount(node);
        Node firstChild = null;
        Object firstValue = null;
        if (propCount == 2 && null != (firstChild = node.jjtGetChild(1)) && (firstValue = firstChild.value(context)) instanceof Map) {
            propertyMap = (Map<String, Object>) firstValue;
        } else {
            propertyMap = new HashMap<String, Object>();
            for (int index = 1; index < propCount; index++) {
                setProperty(propertyMap, context, node.jjtGetChild(index));
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
    protected void setProperty(Map<String, Object> propertyMap, InternalContextAdapter context, Node node) throws ParseErrorException, MethodInvocationException {
        String param = node.value(context).toString();
        int idx = param.indexOf("=");
        if (idx == -1) {
            throw new ParseErrorException("(X) The " + this.getName() + " directive arguments must include" + " an assignment operator" + " [eg: #widget(\"ui.Panel\", \"title=Brick\"," + " \"key=value\")] or"
                    + " [eg: #widget(\"ui.Panel\", { \"title\" : \"Brick\"," + " \"key\" : \"value\" } )]");
        }
        propertyMap.put(param.substring(0, idx), param.substring(idx + 1));
    }

    /**
     * 获取配置属性
     * 
     * @param engine
     * @param key
     * @param def
     * @return
     */
    protected String getProperty(VelocityEngine engine, String key, String def) {
        String val = (String) engine.getProperty(key);
        if (val == null) {
            val = def;
        }
        return val;
    }

}
