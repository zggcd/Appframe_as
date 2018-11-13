package com.asiainfo.appframe.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml解析相关功能类
 * @author 黄斌
 * 
 * 
 * 使用说明
 * 将IO流传进来后，将会解析成List形式
 * key为标签,value为值
 *
 */
public class XMLReader {
	static List<Map<String, Object>> appList = null;

	// static XMLReader xmlReader=null;
	private XMLReader() {
	}// 暂时不需要实例

	/**
	 * 按照文件解析xml文件返Map格式回节点列表
	 * @param xmlFile
	 * @return
	 */
	public static List<Map<String, Object>> getXMLList(File xmlFile){
		List<Map<String, Object>> xmlList=null;
		InputStream is = null;
		try {
			is = new FileInputStream(xmlFile);
			xmlList=getXMLList(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return xmlList;
	}
	
	/**
	 * 按照输入流的文件来解析xml返回Map格式回节点列表
	 * @param is
	 * @return
	 */
	public static List<Map<String, Object>> getXMLList(InputStream is){
		
		List<Map<String, Object>> xmlList = null;
		//InputStream is = null;
		try {
			XmlPullParser parser = Xml.newPullParser();
			//List<String> tempParserNameList=new ArrayList<String>();//出现的标签名列表
			List<Map<String, Object>> tempMapList=new ArrayList<Map<String, Object>>();//临时生成的Map列表
			Map<String, Object> startMap=new HashMap<String, Object>();
			tempMapList.add(startMap);
			parser.setInput(is, "utf-8");
			int event = parser.getEventType();//产生第一个事件
			while(event!= XmlPullParser.END_DOCUMENT){
	            switch(event){ 
	            case XmlPullParser.START_DOCUMENT://文档开始事件
	                break; //可以执行初始化操作 
	            case XmlPullParser.START_TAG://标签元素开始事件
	            	Map<String, Object> itemsMap=new HashMap<String, Object>();
	            	String pNameStr=parser.getName();
	            	//tempParserNameList.add(pNameStr);//将改起始标签名称翻入列表
	            	itemsMap.put("xmlParserName", pNameStr);//将开始标签元素放入Map中
                	for(int attrs=0;attrs<parser.getAttributeCount();attrs++){//循环将标签属性放入Map中
                		itemsMap.put(parser.getAttributeName(attrs), parser.getAttributeValue(attrs));
                	}
                	tempMapList.add(itemsMap);//将产生的临时Map存入列表
	                break;
	            case XmlPullParser.END_TAG://标签元素结束事件
	            	Map<String, Object> tempParentMap=tempMapList.get(tempMapList.size()-2);//上一节点对于Map
	            	List<Map<String, Object>> items=(List<Map<String, Object>>) tempParentMap.get("items");//获取上一节点保存的items(子列表)内容
	            	if(items==null){//如果获取其存放子节点的列表不存在则新建并放入items值中。
	            		items=new ArrayList<Map<String, Object>>();
	            		tempParentMap.put("items", items);
	            	}
	            	items.add(tempMapList.get(tempMapList.size()-1));//将子节点放入父节点的items列表中去
	            	tempMapList.remove(tempMapList.size()-1);//队尾的Map对于的xml解析完成并加入其父节点对于Map，将其移除
	                break;
				// case XmlPullParser.END_DOCUMENT:
				// tempMapList.get(tempMapList.size()-1);
	            }
	            event = parser.next();//进入下一个元素并触发相应事件 
	        }
			xmlList=(List<Map<String, Object>>) ((List<Map<String, Object>>) startMap.get("items")).get(0).get("items");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		if(xmlList==null) xmlList=new ArrayList<Map<String, Object>>();
		return xmlList;
	}
}
