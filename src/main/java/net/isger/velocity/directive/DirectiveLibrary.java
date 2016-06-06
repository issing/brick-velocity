package net.isger.velocity.directive;

import java.util.List;

/**
 * 指令库
 * 
 * @author issing
 * 
 */
public interface DirectiveLibrary {

    /**
     * 获取指令类型集合
     * 
     * @return
     */
    public List<Class<?>> getDirectiveClasses();

}
