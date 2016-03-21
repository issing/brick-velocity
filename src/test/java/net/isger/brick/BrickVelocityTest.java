package net.isger.brick;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.isger.brick.velocity.VelocityContext;
import net.isger.brick.velocity.directive.widget.WidgetDirective;
import net.isger.brick.velocity.directive.widget.WidgetsDirective;

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
                + ", " + WidgetsDirective.class.getName());
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
        assertTrue(true);
    }

}
