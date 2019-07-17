package net.isger.velocity.sql;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;

import net.isger.util.Asserts;
import net.isger.util.Callable;
import net.isger.util.Helpers;
import net.isger.util.Strings;
import net.isger.util.anno.Ignore;
import net.isger.util.sql.PageSql;
import net.isger.util.sql.SqlEntry;
import net.isger.util.sql.SqlTransformerAdapter;
import net.isger.velocity.VelocityConstants;
import net.isger.velocity.VelocityContext;
import net.isger.velocity.directive.BaseDirectiveLibrary;
import net.isger.velocity.directive.DirectiveLibrary;

@Ignore
public class VelocityTransformer extends SqlTransformerAdapter implements VelocityConstants {

    private static final String TAG = "brick.velocity.sql.transformer";

    private static final String KEY_DIRECTIVE = "userdirective";

    private static final String KEY_VALUE = "value";

    private VelocityEngine engine;

    private DirectiveLibrary library;

    public void initial() {
        engine = new VelocityEngine();
        if (library == null) {
            library = createDirectiveLibrary();
        }
        Properties properties = loadConfiguration();
        try {
            engine.init(properties);
        } catch (Exception e) {
            throw Asserts.state("Unable to instantiate VelocityEngine", e);
        }
    }

    protected DirectiveLibrary createDirectiveLibrary() {
        return new BaseDirectiveLibrary();
    }

    protected Properties loadConfiguration() {
        Properties props = new Properties();
        // 自定义指令集
        if (library != null) {
            List<Class<?>> directives = library.getDirectiveClasses();
            if (directives == null) {
                directives = new ArrayList<Class<?>>();
            } else {
                directives = new ArrayList<Class<?>>(directives);
            }
            if (!directives.contains(SeizeDirective.class)) {
                directives.add(SeizeDirective.class);
            }
            props.setProperty(KEY_DIRECTIVE, Strings.join(true, ",", (Object[]) Helpers.each(true, directives, new Callable<String>() {
                public String call(Object... args) {
                    return ((Class<?>) args[1]).getName();
                }
            })));
        }
        // 初始默认配置
        initProperty(props, KEY_LAYOUT_PATH, LAYOUT_PATH);
        initProperty(props, KEY_LAYOUT_NAME, LAYOUT_NAME);
        initProperty(props, KEY_THEME_NAME, THEME_NAME);
        initProperty(props, KEY_WIDGET_PATH, WIDGET_PATH);
        return props;
    }

    protected void initProperty(Properties props, String key, String def) {
        String value = props.getProperty(key);
        if (Strings.isEmpty(value)) {
            props.setProperty(key, def);
        }
    }

    public SqlEntry transform(SqlEntry entry) {
        if (!(entry instanceof PageSql)) {
            return transform(entry.getSql(), entry.getValues());
        }
        PageSql page = (PageSql) entry;
        page.wrap(transform(page.getOriginSql(), page.getOriginValues()));
        return page;
    }

    public SqlEntry transform(String sql, Object value) {
        VelocityContext widgetContext = new VelocityContext(engine);
        widgetContext.put(KEY_VALUE, value);
        StringWriter writer = new StringWriter(sql.length());
        engine.evaluate(widgetContext, writer, TAG, sql);
        return super.transform(writer.getBuffer().toString(), SeizeDirective.getSeizes(widgetContext).toArray());
    }

    public void destroy() {
    }

}
