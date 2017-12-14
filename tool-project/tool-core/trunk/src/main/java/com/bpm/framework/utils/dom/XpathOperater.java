package com.bpm.framework.utils.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.xml.sax.InputSource;

/**
 * 
 * jdom解析工具类
 * 
 * @author andyLee
 * @createDate 2015-08-26 13:55:00
 * 
 * 使用方法：
 * XpathOperater xpath = new XpathOperater(xml);
 * xpath.setXPath("/nodeName/nodeName");
 * String nodeValue = xpath.getNodeValue();
 * ...
 */
public class XpathOperater implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4587448148273869609L;

	private Document document;
	private String xPath;

	public XpathOperater(String xml) {
		String trimxml = xml.trim();
		StringReader rd = new StringReader(trimxml);
		InputSource is = new InputSource(rd);
		SAXBuilder sb = new SAXBuilder();

		try {
			document = sb.build(is);
		} catch (Exception e) {
			throw new RuntimeException("创建文档对象Document异常：", e);
		}
	}
	
	public XpathOperater(File file) {
		SAXBuilder sb = new SAXBuilder();

		try {
			document = sb.build(file);
		} catch (Exception e) {
			throw new RuntimeException("创建文档对象Document异常：", e);
		}
	}
	
	public XpathOperater(File file, String encoding) {
		SAXBuilder sb = new SAXBuilder();

		try {
			InputSource is = new InputSource(new InputStreamReader(new FileInputStream(file), encoding));
			is.setEncoding(encoding);
			document = sb.build(is);
		} catch (Exception e) {
			throw new RuntimeException("创建文档对象Document异常：", e);
		}
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getXPath() {

		return xPath;
	}

	public void setXPath(String path) {
		this.xPath = path;

	}

	public String getNodeValue() {
		try {

			XPath xp = XPath.newInstance(this.xPath);

			Element ee = (Element) xp.selectSingleNode(document.getRootElement());
			return ee.getTextTrim();
		} catch (JDOMException e) {
			throw new RuntimeException("解析XPath时异常：" + xPath, e);
		}
	}

	@SuppressWarnings("unchecked")
	public String[] getNodeValuesByString() {
		try {
			XPath xp = XPath.newInstance(xPath);
			List<Element> list = (List<Element>) xp.selectNodes(document.getRootElement());

			String[] values = new String[list.size()];
			int i = 0;
			for (Element e : list) {
				values[i] = e.getTextTrim();
				i++;
			}
			return values;
		} catch (JDOMException e) {
			throw new RuntimeException("解析XPath时异常：" + xPath, e);
		}
	}

	@SuppressWarnings("unchecked")
	public Double[] getNodeValuesByDouble() {
		try {
			XPath xp = XPath.newInstance(xPath);
			List<Element> list = (List<Element>) xp.selectNodes(document.getRootElement());

			Double[] values = new Double[list.size()];
			int i = 0;
			for (Element e : list) {
				values[i] = Double.parseDouble(e.getTextTrim());
				i++;
			}
			return values;
		} catch (JDOMException e) {
			throw new RuntimeException("解析XPath时异常：" + xPath, e);
		}
	}

	@SuppressWarnings("unchecked")
	public Integer[] getNodeValuesByInt() {
		try {
			XPath xp = XPath.newInstance(xPath);
			List<Element> list = (List<Element>) xp.selectNodes(document.getRootElement());

			Integer[] values = new Integer[list.size()];
			int i = 0;
			for (Element e : list) {
				values[i] = Integer.parseInt(e.getTextTrim());
				i++;
			}
			return values;
		} catch (JDOMException e) {
			throw new RuntimeException("解析XPath时异常：" + xPath, e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Element> getNodes() {
		try {
			XPath xp = XPath.newInstance(xPath);

			List<Element> list = (List<Element>) xp.selectNodes(document.getRootElement());
			return list;
		} catch (JDOMException e) {
			throw new RuntimeException("解析XPath时异常：" + xPath, e);
		}
	}

	public Element getNode() {
		try {
			XPath xp = XPath.newInstance(xPath);

			Element el = (Element) xp.selectSingleNode(document.getRootElement());
			return el;
		} catch (JDOMException e) {
			throw new RuntimeException("解析XPath时异常：" + xPath, e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		new XpathOperater(new File("C:/a/BusinessTripRequest.snaker"), "UTF-8");
	}
}
