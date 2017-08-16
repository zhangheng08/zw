package com.ouchn.lib.typesetting;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class XMLTypesettingHelper {
	public final String TAG = "XMLTypesettingHelper";
	public final String FILENAME = "note.ann";
	public Document mDom;
	
	private Context mContext;
	private DocumentBuilderFactory mDocBuilderFactory;
	private DocumentBuilder mDocBuilder;
	private String mFilePath;
	private String htmlContent;

	public XMLTypesettingHelper(Context c, String content) {
		mContext = c;
		htmlContent = content;
		try {
			mDocBuilderFactory = DocumentBuilderFactory.newInstance();
			mDocBuilder = mDocBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			errorPrinter(e, "Note4XmlParser-constructor");
		}
	}
	
	public void loadDom() {
		
		InputStream is = new ByteArrayInputStream(htmlContent.getBytes());
		try {
			mDom = mDocBuilder.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}//loadDom(.)
	
	public void read() {
		NodeList pageNodes = mDom.getElementsByTagName("P");
		if(null == pageNodes || pageNodes.getLength() == 0 ) return;
		
		for(int i = 0;i < pageNodes.getLength(); i ++) {
			
		}
		
	}//parse(.)
	
	public void appendLineHeight() {
		NodeList nodes = mDom.getElementsByTagName("p");
		if(null == nodes || nodes.getLength() == 0 ) return;
		
		for(int i = 0; i < nodes.getLength(); i ++) {
			Element ele = (Element) nodes.item(i);
			Log.v(TAG, "current node is : " + ele.getNodeName());
			ele.setAttribute("style", "line-height:150%");
			ele.setAttribute("align", "left");
		}
		
		NodeList spanNodes = mDom.getElementsByTagName("span");
		if(null == spanNodes || spanNodes.getLength() == 0 ) return;
		
		for(int i = 0; i < spanNodes.getLength(); i ++) {
			Element ele = (Element) spanNodes.item(i);
			Log.v(TAG, "current node is : " + ele.getNodeName());
			ele.setAttribute("style", "line-height:150%");
		}
		
	}
	
	public void changeImgPath(String path) {
		NodeList nodes = mDom.getElementsByTagName("img");
		if(null == nodes || nodes.getLength() == 0 ) return;
		
		for(int i = 0; i < nodes.getLength(); i ++) {
			Element ele = (Element) nodes.item(i);
			Log.v(TAG, "current node is : " + ele.getNodeName());
			String src = ele.getAttribute("src");
			String imgName = src.substring(src.lastIndexOf("/") + 1);
			String newPath = path + File.separator + imgName;
			ele.setAttribute("src", newPath);
		}
	}
	
	public void changeImageWH(String width, String height, String name) {
		NodeList nodes = mDom.getElementsByTagName("img");
		if(null == nodes || nodes.getLength() == 0 ) return;
		
		for(int i = 0; i < nodes.getLength(); i ++) {
			Element ele = (Element) nodes.item(i);
			Log.v(TAG, "current node is : " + ele.getNodeName());
			String src = ele.getAttribute("src");
			String imgName = src.substring(src.lastIndexOf("/") + 1);
			if(name.equals(imgName)) {
				ele.setAttribute("width", width);
				ele.setAttribute("height", height);
			}
		}
	}
	
	public Element doPage(String pageNo) {
		Element pageElement = mDom.createElement("Page");
		//e.g. <Page Type="5" PageNum="1" Insert="0" Width="2114" Height="3008">
		pageElement.setAttribute("Type", "");
		pageElement.setAttribute("PageNum", pageNo);
		pageElement.setAttribute("Insert", "0");
		pageElement.setAttribute("Width", "");
		pageElement.setAttribute("Height", "");
		return pageElement;
	}
	
	public void remove() {
//		if(noteLayer !=  null && noteLayer.mXmlSelf != null && noteLayer.mXmlParent != null) {
//			noteLayer.mXmlParent.removeChild(noteLayer.mXmlSelf);
//		}
//		if(tagLayer !=  null && tagLayer.mXmlSelf != null && tagLayer.mXmlParent != null) {
//			tagLayer.mXmlParent.removeChild(tagLayer.mXmlSelf);
//		}
	}//remove(.)
	
	private void doAppend(Element pageElement) {
		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Element child = null;
//			child = mDom.createElement("HighLight");
//			child.setAttribute("Relative", "");
//			child.setAttribute("datetime", dateFormat.format(new Date()));
//			pageElement.appendChild(child);
//			
//			child = null;
//			child = mDom.createElement("Note");
//			child.setAttribute("Relative", "");
//			child.setAttribute("Width", "72");
//			child.setAttribute("Height", "72");
//			child.setAttribute("Color", "");
//			child.setAttribute("datetime", dateFormat.format(new Date()));
//			pageElement.appendChild(child);
			
	}//doAppend(...)
	
	public String getProcessedContent() {
		DOMSource domSource = new DOMSource(mDom);  
        StringWriter writer = new StringWriter();  
        StreamResult result = new StreamResult(writer);  
        TransformerFactory tf = TransformerFactory.newInstance();  
        Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
        return writer.toString();  
	}//save()
	
	private String doCreate() {
		XmlSerializer mSerializer = null;  
		StringWriter mWriter = null;
		SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			mSerializer = Xml.newSerializer();  
			mWriter = new StringWriter();
			mSerializer.setOutput(mWriter);
			mSerializer.startDocument("UTF-8", true);
			mSerializer.startTag("", "Notation");
			mSerializer.attribute("", "Version", "1.0");
			
			mSerializer.startTag("", "Book");
				mSerializer.startTag("", "BookName");
				mSerializer.attribute("", "value", "");
				mSerializer.endTag("", "BookName");
				mSerializer.startTag("", "Author");
				mSerializer.attribute("", "value", "");
				mSerializer.endTag("", "Author");
				mSerializer.startTag("", "Publisher");
				mSerializer.attribute("", "value", "");
				mSerializer.endTag("", "Publisher");
				mSerializer.startTag("", "PublishTime");
				mSerializer.attribute("", "value", "");
				mSerializer.endTag("", "PublishTime");
				mSerializer.startTag("", "SSnum");
				mSerializer.endTag("", "SSnum");
			mSerializer.endTag("", "Book");
			
			mSerializer.startTag("", "Noter");
				mSerializer.startTag("", "UserID");
				mSerializer.attribute("", "value", "");
				mSerializer.endTag("", "UserID");
				mSerializer.startTag("", "CreateTime");
				mSerializer.attribute("", "value", dateFromat.format(new Date()));
				mSerializer.endTag("", "CreateTime");
				mSerializer.startTag("", "ModifyTime");
				mSerializer.attribute("", "value", "");
				mSerializer.endTag("", "ModifyTime");
			mSerializer.endTag("", "Noter");
			
			mSerializer.startTag("", "TotalPage");
			mSerializer.attribute("", "NUM", "0");
			mSerializer.endTag("", "TotalPage");
			mSerializer.endTag("", "Notation");
			mSerializer.endDocument();
		} catch(Exception e) {
			errorPrinter(e, "onCreate");
			try {
				mSerializer.flush();
				mWriter.flush();
				if(mWriter != null) mWriter.close();
				mSerializer = null;
				mWriter = null;
			} catch (IOException e1) {
				errorPrinter(e1, "onCreate");
			}
			return null;
		}
		return mWriter.toString();
	}//doCreate()
	
	private void errorPrinter(Exception e, String methodNm) {
		try {
			Log.e(TAG + ":" + methodNm, e.getMessage());
			StackTraceElement[] errors = e.getStackTrace();
			for(int i = 0; i < errors.length; i ++) Log.e(TAG + ":" + methodNm, errors[i].toString());
		} catch(Exception ee) {
			Log.e(TAG + ":errorPrinter", ee.getMessage());
		}
	}//errorPrinter(..)
	
}//class
