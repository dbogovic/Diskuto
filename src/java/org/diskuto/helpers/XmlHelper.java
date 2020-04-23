/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.helpers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author dario
 */
public class XmlHelper {

    XPath xp;
    String xml;

    public XmlHelper(String xml) {
        this.xml = xml;
        this.xp = XPathFactory.newInstance().newXPath();
    }

    public Object makeObject(String object) throws XPathExpressionException {
        return xp.evaluate("/" + object, new InputSource(new StringReader(xml)), XPathConstants.NODE);
    }

    public String makeValue(String attribute, Object object) throws XPathExpressionException {
        return xp.evaluate(attribute, object);
    }

    public List<String> makeRawValue(String expression) throws XPathExpressionException {
        List<String> list = new ArrayList();
        NodeList nodes = (NodeList) xp.evaluate(expression, new InputSource(new StringReader(xml)), XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            list.add(nodes.item(i).getTextContent());
        }
        
        return list;
    }
}
