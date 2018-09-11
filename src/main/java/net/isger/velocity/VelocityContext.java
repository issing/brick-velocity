package net.isger.velocity;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import net.isger.util.Helpers;

/**
 * 模板上下文
 * 
 * @author issing
 * 
 */
public class VelocityContext extends org.apache.velocity.VelocityContext {

    /** 模板实例名 */
    private String name;

    /** 模板引擎 */
    private VelocityEngine engine;

    /** 模板上下文策略（拓展） */
    private ContextSecretary secretary;

    public VelocityContext(VelocityEngine engine) {
        this(engine, null, null);
    }

    public VelocityContext(VelocityEngine engine, Context innerContext) {
        this(engine, null, innerContext);
    }

    public VelocityContext(VelocityEngine engine, Map<String, Object> context,
            Context innerContext) {
        super(context, innerContext);
        this.engine = engine;
        if (innerContext instanceof VelocityContext) {
            VelocityContext velocityContext = (VelocityContext) innerContext;
            this.secretary = velocityContext.secretary;
            this.name = velocityContext.name;
        } else {
            this.secretary = new ContextSecretary();
            this.name = Helpers.getAliasName(this.getClass(), "Context$",
                    "Brick");
        }
        this.put(getName(), getSecretary());
    }

    /**
     * 实例名称
     * 
     * @return
     */
    protected String getName() {
        return name;
    }

    /**
     * 获取引擎
     * 
     * @return
     */
    public VelocityEngine getEngine() {
        return engine;
    }

    /**
     * 获取策略
     * 
     * @return
     */
    public ContextSecretary getSecretary() {
        return secretary;
    }

    public void put(Class<?> clazz) {
        this.put(clazz.getSimpleName(), clazz);
    }

    /**
     * 获取资源
     */
    public Object internalGet(String key) {
        return super.internalContainsKey(key) ? super.internalGet(key)
                : ContextSecretary.mirror(key);
    }

}
