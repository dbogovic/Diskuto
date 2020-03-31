/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.helpers;

import java.io.StringReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

/**
 *
 * @author dario
 */
public class XmlHelper {
    
    InputSource source;
    XPath xp;
    
    public XmlHelper(String xml) {
        source = new InputSource(new StringReader(xml));
        xp = XPathFactory.newInstance().newXPath();
    }
    
    public Object makeObject(String object) throws XPathExpressionException{
        return xp.evaluate("/"+object, source, XPathConstants.NODE);
    }
    
    public String makeValue(String attribute, Object object) throws XPathExpressionException {
        return xp.evaluate(attribute, object);
    }
    
}
