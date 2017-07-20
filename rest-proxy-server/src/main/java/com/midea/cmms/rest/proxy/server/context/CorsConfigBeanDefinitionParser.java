package com.midea.cmms.rest.proxy.server.context;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;
import org.springframework.web.cors.CorsConfiguration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域配置BeanDefinition解析器
 *
 * <p>解析ApplicationContext配置文件中，自定义命名空间<b>http://www.midea.com/cmms/dubbo-config</b>的 &lt;cors&gt; 元素</p>
 *
 * Created by liyilin on 2017/7/18.
 */
public class CorsConfigBeanDefinitionParser implements BeanDefinitionParser {
    /**
     * 用于给匿名Bean进行编号的变量
     */
    private static int count = 0;

    private DataBinder binder = new DataBinder(null);

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition def = new RootBeanDefinition(CorsConfiguration.class);

        String id = element.getAttribute("id");
        if (id.isEmpty()) {
            count++;
            id = CorsConfiguration.class.getName() + "$" + count;
        }

        BeanDefinitionHolder idHolder = new BeanDefinitionHolder(def, id);
        BeanDefinitionReaderUtils.registerBeanDefinition(idHolder, parserContext.getRegistry());

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            // <allow-origins>
            if (isAllowOriginsElement(item)) {
                Element originElem = (Element) item;
                String trim = originElem.getTextContent().trim();
                List<String> list = parseStrToList(trim);
                def.getPropertyValues().addPropertyValue("allowedOrigins", list);
            }
            // <allow-headers>
            else if (isAllowHeadersElement(item)) {
                Element originElem = (Element) item;
                String trim = originElem.getTextContent().trim();
                List<String> list = parseStrToList(trim);
                def.getPropertyValues().addPropertyValue("allowedHeaders", list);
            }
            // <allow-methods>
            else if (isAllowMethodsElement(item)) {
                Element originElem = (Element) item;
                String trim = originElem.getTextContent().trim();
                List<String> list = parseStrToList(trim);
                def.getPropertyValues().addPropertyValue("allowedMethods", list);
            }
            // <allow-credentials>
            else if (isAllowCredentialsElement(item)) {
                Element originElem = (Element) item;
                String trim = originElem.getTextContent().trim();
                boolean allowCredentials = binder.convertIfNecessary(trim, Boolean.class);
                def.getPropertyValues().addPropertyValue("allowCredentials", allowCredentials);
            }
            // <expose-headers>
            else if (isExposeHeadersElement(item)) {
                Element originElem = (Element) item;
                String trim = originElem.getTextContent().trim();
                List<String> list = parseStrToList(trim);
                def.getPropertyValues().addPropertyValue("exposedHeaders", list);
            }
            // max-age
            else if (isMaxAgeElement(item)) {
                Element originElem = (Element) item;
                String trim = originElem.getTextContent().trim();
                long maxAge = binder.convertIfNecessary(trim, Long.class);
                def.getPropertyValues().addPropertyValue("maxAge", maxAge);
            }
        }

        return def;
    }

    private List<String> parseStrToList(String str) {
        String[] allowOrigins = StringUtils.tokenizeToStringArray(str, ",");
        List<String> list = new ArrayList<>(allowOrigins.length);
        for (String s : allowOrigins) {
            list.add(s.trim());
        }
        return list;
    }

    private boolean isAllowOriginsElement(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("allow-origins");
    }

    private boolean isAllowHeadersElement(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("allow-headers");
    }

    private boolean isAllowMethodsElement(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("allow-methods");
    }

    private boolean isAllowCredentialsElement(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("allow-credentials");
    }

    private boolean isExposeHeadersElement(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("expose-headers");
    }

    private boolean isMaxAgeElement(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("max-age");
    }
}
