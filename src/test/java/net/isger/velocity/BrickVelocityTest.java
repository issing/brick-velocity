package net.isger.velocity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.isger.util.Sqls;
import net.isger.util.sql.SqlEntry;
import net.isger.velocity.VelocityContext;
import net.isger.velocity.bean.Employ;
import net.isger.velocity.directive.widget.WidgetDirective;
import net.isger.velocity.directive.widget.WidgetsDirective;
import net.isger.velocity.sql.SeizeDirective;
import net.isger.velocity.sql.VelocityTransformer;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

public class BrickVelocityTest extends TestCase {

    public BrickVelocityTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(BrickVelocityTest.class);
    }

    public void testVelocity() {
        Map<String, Object> context = new HashMap<String, Object>();
        VelocityEngine engine = new VelocityEngine();
        Properties props = new Properties();
        props.setProperty("userdirective", WidgetDirective.class.getName()
                + ", " + WidgetsDirective.class.getName() + ", "
                + SeizeDirective.class.getName());
        props.setProperty("brick.widget.path", "/template/isweb");
        engine.init(props);
        VelocityContext widgetContext = new VelocityContext(engine, context,
                null);
        Template template = engine.getTemplate("/template/isweb.vm", "UTF-8");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                System.out));
        template.merge(widgetContext, writer);
        try {
            writer.flush();
        } catch (IOException e) {
        }
        System.out.println("# SQL ************************");
        widgetContext.put("value", new Employ("1", "first"));
        engine.evaluate(widgetContext, writer, Employ.class.getName()
                + ".insert", Sqls.getSQL(Employ.class, "insert"));
        try {
            writer.flush();
        } catch (IOException e) {
        }
        System.out.println(SeizeDirective.getSeizes(widgetContext));
        assertTrue(true);
        VelocityTransformer transformer = new VelocityTransformer();
        transformer.initial();
        SqlEntry entry = transformer.transform(
                Sqls.getSQL(Employ.class, "insert"), new Employ("1", "first"));
        System.out.println(entry);
    }
}
