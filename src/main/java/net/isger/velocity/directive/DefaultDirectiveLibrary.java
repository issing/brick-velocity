package net.isger.velocity.directive;

import java.util.Arrays;
import java.util.List;

import net.isger.velocity.directive.widget.WidgetDirective;
import net.isger.velocity.directive.widget.WidgetsDirective;

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
        DEF_LIB = new Class<?>[] { WidgetDirective.class,
                WidgetsDirective.class };
    }

    public List<Class<?>> getDirectiveClasses() {
        return Arrays.asList(DEF_LIB);
    }

}
