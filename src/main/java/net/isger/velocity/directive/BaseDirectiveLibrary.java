package net.isger.velocity.directive;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.runtime.directive.Directive;

/**
 * 指令库基类
 * 
 * @author issing
 * 
 */
public class BaseDirectiveLibrary implements DirectiveLibrary {

    private List<Class<?>> directives;

    public BaseDirectiveLibrary() {
        directives = new ArrayList<Class<?>>();
    }

    /**
     * 添加指令类型
     * 
     * @param directive
     */
    public void addDirectiveClass(Class<?> directive) {
        if (Directive.class.isAssignableFrom(directive)
                && !directives.contains(directive)) {
            directives.add(directive);
        }
    }

    /**
     * 获取指令类型集合
     */
    public List<Class<?>> getDirectiveClasses() {
        return directives;
    }

    /**
     * 设置指令类型集合
     * 
     * @param directives
     */
    public void setDirectiveClasses(List<Class<?>> directives) {
        this.directives = directives;
    }

}
