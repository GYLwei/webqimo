package cn.enilu.flash.utils;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

/**
 * Created by huan on 2017/1/7.
 */
public class XmlHelper {

    private final XPath path;
    private final Document doc;

    private XmlHelper(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = getDocumentBuilderFactory();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(inputSource);
        path = getXPathFactory().newXPath();
    }

    private static XmlHelper create(InputSource inputSource) {
        try {
            return new XmlHelper(inputSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static XmlHelper of(InputStream is) {
        InputSource inputSource = new InputSource(is);
        return create(inputSource);
    }

    public static XmlHelper of(String xmlStr) {
        StringReader sr = new StringReader(xmlStr.trim());
        InputSource inputSource = new InputSource(sr);
        XmlHelper xmlHelper = create(inputSource);
        IOUtils.closeQuietly(sr);
        return xmlHelper;
    }

    private Object evalXPath(String expression, Object item, QName returnType) {
        item = null == item ? doc : item;
        try {
            return path.evaluate(expression, item, returnType);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ??????String
     * @param expression ??????
     * @return String
     */
    public String getString(String expression) {
        return (String) evalXPath(expression, null, XPathConstants.STRING);
    }

    /**
     * ??????Boolean
     * @param expression ??????
     * @return String
     */
    public Boolean getBoolean(String expression) {
        return (Boolean) evalXPath(expression, null, XPathConstants.BOOLEAN);
    }

    /**
     * ??????Number
     * @param expression ??????
     * @return {Number}
     */
    public Number getNumber(String expression) {
        return (Number) evalXPath(expression, null, XPathConstants.NUMBER);
    }

    /**
     * ??????????????????
     * @param expression ??????
     * @return {Node}
     */
    public Node getNode(String expression) {
        return (Node) evalXPath(expression, null, XPathConstants.NODE);
    }

    /**
     * ???????????????
     * @param expression ??????
     * @return NodeList
     */
    public NodeList getNodeList(String expression) {
        return (NodeList) evalXPath(expression, null, XPathConstants.NODESET);
    }


    /**
     * ??????String
     * @param node ??????
     * @param expression ?????????node?????????
     * @return String
     */
    public String getString(Object node, String expression) {
        return (String) evalXPath(expression, node, XPathConstants.STRING);
    }

    /**
     * ??????
     * @param node ??????
     * @param expression ?????????node?????????
     * @return String
     */
    public Boolean getBoolean(Object node, String expression) {
        return (Boolean) evalXPath(expression, node, XPathConstants.BOOLEAN);
    }

    /**
     * ??????
     * @param node ??????
     * @param expression ?????????node?????????
     * @return {Number}
     */
    public Number getNumber(Object node, String expression) {
        return (Number) evalXPath(expression, node, XPathConstants.NUMBER);
    }

    /**
     * ??????????????????
     * @param node ??????
     * @param expression ??????
     * @return {Node}
     */
    public Node getNode(Object node, String expression) {
        return (Node) evalXPath(expression, node, XPathConstants.NODE);
    }

    /**
     * ???????????????
     * @param node ??????
     * @param expression ?????????node?????????
     * @return NodeList
     */
    public NodeList getNodeList(Object node, String expression) {
        return (NodeList) evalXPath(expression, node, XPathConstants.NODESET);
    }

    /**
     * ???????????????????????????????????????
     * @return map??????
     */
    public Map<String, String> toMap() {
        Element root = doc.getDocumentElement();
        Map<String, String> params = Maps.newHashMap();

        // ??????????????????map??????
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                params.put(node.getNodeName(), node.getTextContent());
            }
        }
        return params;
    }

    private static DocumentBuilderFactory getDocumentBuilderFactory(){
        return XmlHelper.XmlHelperHolder.documentBuilderFactory;
    }

    private static XPathFactory getXPathFactory() {
        return  XmlHelper.XmlHelperHolder.xPathFactory;
    }

    /**
     * ???????????????
     */
    private static class XmlHelperHolder {
        private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        private static XPathFactory xPathFactory = XPathFactory.newInstance();
    }
}
