package com.midea.cmms.rest.proxy.server.context;

import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;
import com.midea.cmms.rest.proxy.server.util.CopyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.validation.DataBinder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理方法BeanDefinition解析器
 *
 * <p>解析ApplicationContext配置文件中，自定义命名空间<b>http://www.midea.com/cmms/dubbo-config</b>的 &lt;service&gt; 元素</p>
 *
 * <p>生成URL与Dubbo Service形成映射关系的<b>HandleMethod</b>对象</p>
 *
 * @see HandleMethod
 *
 * Created by liyilin on 2017/7/17.
 */
public class HandleMethodBeanDefinitionParser implements BeanDefinitionParser {

    /**
     * 用于给匿名Bean进行编号的变量
     */
    private static int count = 0;

    public BeanDefinition parse(Element element, ParserContext context) {
        RootBeanDefinition def = new RootBeanDefinition();

        // 注册属性
        String className = element.getAttribute("class");

        context.pushContainingComponent(new CompositeComponentDefinition("service", element));

        try {
            Class<?> serviceClass = Class.forName(className);
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (isActionNode(node)) {
                    parseAction((Element) node, context, serviceClass);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        context.popContainingComponent();

        return def;
    }

    private boolean isActionNode(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("action");
    }

    /**
     * 解析&lt;action&gt;元素
     * @param element
     * @param context
     * @param serviceClass
     */
    private void parseAction(Element element, ParserContext context, Class<?> serviceClass) {
        RootBeanDefinition def = new RootBeanDefinition();
        def.setBeanClass(HandleMethod.class);

        Action action = new Action();
        setPropertiesByElemAttrs(action, element);

        Method method = findMethodByUniqueName(serviceClass, action.name);

        def.getPropertyValues().addPropertyValue("httpMethod", action.httpMethod);
        def.getPropertyValues().addPropertyValue("sign", action.sign);
        def.getPropertyValues().addPropertyValue("serviceClassName", serviceClass.getName());
        def.getPropertyValues().addPropertyValue("method", method);
        if (action.corsRef != null)
            def.getPropertyValues().addPropertyValue("corsConfig", new RuntimeBeanReference(action.corsRef));

        // 处理RequestPattern
        Element parentElem = (Element) context.getContainingComponent().getSource();
        String serviceMapping = parentElem.getAttribute("mapping");
        String actionMapping = action.mapping;
        String requestPattern = buildRequestPattern(serviceMapping, actionMapping);

        def.getPropertyValues().addPropertyValue("requestPattern", requestPattern);

        List<HttpParameterInfo> list = new ArrayList<>();
        NodeList childNodes = element.getChildNodes();
        for (int i = 0, idx = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (isArgElement(node)) {
                HttpParameterInfo httpParameterInfo = parseArg((Element) node, idx, method);
                list.add(httpParameterInfo);
                idx++;
            }
        }
        HttpParameterInfo[] httpParameterInfos = list.toArray(new HttpParameterInfo[0]);
        def.getPropertyValues().addPropertyValue("httpParameterInfos", httpParameterInfos);

        count++;
        BeanDefinitionHolder idHolder = new BeanDefinitionHolder(def, HandleMethod.class.getName() + "_" + count);
        BeanDefinitionReaderUtils.registerBeanDefinition(idHolder, context.getRegistry());
    }

    private boolean isArgElement(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("arg");
    }

    /**
     * 解析&lt;arg&gt;元素
     *
     * <p>生成HandleMethod的参数定义对象({@code HttpParameterInfo})</p>
     *
     * @param element
     * @param index
     * @param method
     * @return
     * @see HttpParameterInfo
     */
    private HttpParameterInfo parseArg(Element element, int index, Method method) {
        HttpParameterInfo httpParameterInfo = new HttpParameterInfo(index);

        Arg arg = new Arg();
        arg.index = index;
        setPropertiesByElemAttrs(arg, element);

        CopyUtils.copy(arg, httpParameterInfo);
        httpParameterInfo.setType(method.getParameterTypes()[index]);

        return httpParameterInfo;
    }

    private String buildRequestPattern(String serviceMapping, String actionMapping) {
        String requestPattern = "";
        if (serviceMapping != null && !serviceMapping.isEmpty()) {
            if (!serviceMapping.startsWith("/")) requestPattern += "/" + serviceMapping;
            else requestPattern += serviceMapping;
        }
        if (actionMapping != null && !actionMapping.isEmpty()) {
            if (!actionMapping.startsWith("/")) requestPattern += "/" + actionMapping;
            else requestPattern += actionMapping;
        }
        return requestPattern;
    }

    private void setPropertiesByElemAttrs(Object obj, Element element) {
        DataBinder binder = new DataBinder(null);
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            XmlAttr annotation = field.getAnnotation(XmlAttr.class);
            if (annotation != null) {
                try {
                    String xmlAttr = annotation.value();
                    String attribute = element.getAttribute(xmlAttr);
                    Class<?> type = field.getType();
                    field.setAccessible(true);
                    if (attribute != null && !attribute.isEmpty())
                        field.set(obj, binder.convertIfNecessary(attribute, type));
                } catch (IllegalAccessException ignored) {}
            }
        }
    }

    private Method findMethodByUniqueName(Class<?> clazz, String methodName) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method ans = null;
        for (Method method : declaredMethods) {
            if (method.getName().equals(methodName)) {
                if (ans == null) ans = method;
                else throw new IllegalArgumentException("类[" + clazz.getName() + "]中有同名方法: " + methodName);
            }
        }
        if (ans != null) return ans;
        throw new IllegalArgumentException("类[" + clazz.getName() + "]中找不到方法: " + methodName);
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface XmlAttr {
        String value();
    }

    static class Action {
        @XmlAttr("name") String name;
        @XmlAttr("method") String httpMethod;
        @XmlAttr("sign") boolean sign = false;
        @XmlAttr("mapping") String mapping;
        @XmlAttr("cors-ref") String corsRef;
    }

    static class Arg {
        @XmlAttr("index") Integer index;
        @XmlAttr("param") String paramName;
        @XmlAttr("pathVariable") boolean pathVariable = false;
        @XmlAttr("required") boolean required = false;
        @XmlAttr("requestBody") boolean requestBody = false;
        @XmlAttr("default") String defaultValue;
        @XmlAttr("appSign") boolean appSign = false;
        @XmlAttr("dateFormat") String dateFormat;
    }
}
