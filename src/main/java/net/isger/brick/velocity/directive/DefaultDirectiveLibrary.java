package net.isger.brick.velocity.directive;

import java.util.Arrays;
import java.util.List;

import net.isger.brick.velocity.directive.widget.WidgetDirective;

/**
 * 默认指令库
 * 
 * @author issing
 * 
 */
public class DefaultDirectiveLibrary implements DirectiveLibrary {

    /** 默认库 */
    private static final Class<?>[] DEF_LIB;

    static {
        DEF_LIB = new Class<?>[] { WidgetDirective.class };
    }

    public List<Class<?>> getDirectiveClasses() {
        return Arrays.asList(DEF_LIB);
    }

}
