package net.isger.velocity.sql;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import net.isger.util.Strings;
import net.isger.util.anno.Ignore;
import net.isger.util.sql.PageSql;
import net.isger.util.sql.SqlEntry;
import net.isger.util.sql.SqlTransformerAdapter;
import net.isger.velocity.VelocityConstants;
import net.isger.velocity.VelocityContext;
import net.isger.velocity.directive.BaseDirectiveLibrary;

import org.apache.velocity.app.VelocityEngine;

@Ignore
public class VelocityTransformer extends SqlTransformerAdapter implements
        VelocityConstants {

    public static final String SQL_VALUE = "value";

    public static final String VELOCITY_TRANSFORMER = "brick.velocity.sql.transformer";

    private static final String KEY_DIRECTIVE = "userdirective";

    private VelocityEngine engine;

    private BaseDirectiveLibrary library;

    public void initial() {
        engine = new VelocityEngine();
        if (library == null) {
            library = new BaseDirectiveLibrary();
        }
        Properties properties = loadConfiguration();
        try {
            engine.init(properties);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "(X) Unable to instantiate VelocityEngine", e);
        }
    }

    protected Properties loadConfiguration() {
        Properties props = new Properties();
        // 添加默认指令
        library.addDirectiveClasses(SeizeDirective.class);
        // 自定义砖头指令集
        StringBuffer buffer = new StringBuffer(512);
        List<Class<?>> directives = library.getDirectiveClasses();
        if (directives != null) {
            for (Class<?> d : directives) {
                append(buffer, d.getName());
            }
        }
        String directive = props.getProperty(KEY_DIRECTIVE);
        if (Strings.isEmpty(directive)) {
            buffer.setLength(buffer.length() - 2);
        } else {
            buffer.append(directive);
        }
        props.setProperty(KEY_DIRECTIVE, buffer.toString());
        // 初始默认配置
        initProperty(props, KEY_LAYOUT_PATH, LAYOUT_PATH);
        initProperty(props, KEY_LAYOUT_NAME, LAYOUT_NAME);
        initProperty(props, KEY_THEME_NAME, THEME_NAME);
        initProperty(props, KEY_WIDGET_PATH, WIDGET_PATH);
        return props;
    }

    private void initProperty(Properties props, String key, String def) {
        String value = props.getProperty(key);
        if (Strings.isEmpty(value)) {
            props.setProperty(key, def);
        }
    }

    private void append(StringBuffer buffer, String value) {
        buffer.append(value).append(", ");
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
        widgetContext.put(SQL_VALUE, value);
        StringWriter writer = new StringWriter(sql.length());
        engine.evaluate(widgetContext, writer, VELOCITY_TRANSFORMER, sql);
        List<?> seizes = SeizeDirective.getSeizes(widgetContext);
        return super.transform(writer.getBuffer().toString(),
                seizes.size() > 0 ? seizes.toArray() : value);
    }

    public void destroy() {
    }

}
