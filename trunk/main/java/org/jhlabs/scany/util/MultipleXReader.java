/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.scany.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 다중 컬럼 XML 데이터 파서
 * @author gulendol
 * @version <pre>
 * 2007/10/18 SimpleXReader를 기능을 확장
 * 2006/06/17</pre>
 */
public class MultipleXReader extends DefaultHandler {

	protected String tableName;

	protected String recordName;

	protected String[] columnNames;

	private Record record;

	private String columnName;

	private Attributes attributes;

	private StringBuffer buffer;

	private boolean isInsideTable;

	private boolean isInsideRecord;

	protected List records;

	/**
	 * 테이블의 레코드
	 * @author Gulendol
	 */
	public static class Record {

		private List columns;

		private Map attributes;

		public Record() {
			columns = new ArrayList();
		}

		public void addColumn(Column column) {
			columns.add(column);
		}

		public Column[] getColumns() {
			return (Column[])columns.toArray(new Column[columns.size()]);
		}

		public Column[] getColumns(String columnName) {
			List list = new ArrayList();

			for(int i = 0; i < columns.size(); i++) {
				Column column = (Column)columns.get(i);

				if(columnName.equals(column.getName()))
					list.add(column);
			}

			return (Column[])list.toArray(new Column[list.size()]);
		}

		public int getColumnCount() {
			return columns.size();
		}

		public Column getColumn(String columnName) {
			for(int i = 0; i < columns.size(); i++) {
				Column column = (Column)columns.get(i);

				if(columnName.equals(column.getName()))
					return column;
			}

			return null;
		}

		public String getColumnValue(String columnName) {
			Column column = getColumn(columnName);

			if(column == null)
				return null;

			return column.getValue();
		}

		public String getColumnAttributeValue(String columnName, String attributeName) {
			Column column = getColumn(columnName);
			return column.getAttributeValue(attributeName);
		}

		public Map getAttributes() {
			return attributes;
		}

		public void setAttributes(Map attributes) {
			this.attributes = attributes;
		}

		public String getAttributeValue(String attributeName) {
			if(attributes == null)
				return null;

			return (String)attributes.get(attributeName);
		}
	}

	/**
	 * 레코드의 컬럼
	 * @author Gulendol
	 */
	public static class Column {

		private String name;

		private String value;

		private Map attributes;

		public Column() {
		}

		public Column(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Map getAttributes() {
			return attributes;
		}

		public void setAttributes(Map attributes) {
			this.attributes = attributes;
		}

		public String getAttributeValue(String attributeName) {
			return (String)attributes.get(attributeName);
		}
	}

	/**
	 * 생성자.
	 */
	public MultipleXReader() {
	}

	/**
	 * 생성자.
	 * @param tableName 데이터베이스로 말하자면 테이블명이다.
	 * @param recordName 데이터베이스로 말하자면 하나의 레코드이고, 그 레코드에 이름이 부여되었다.
	 * @param typeColumns 데이터베이스로 말하자면 필드명이다.
	 */
	public MultipleXReader(String tableName, String recordName, String[] columnNames) {
		setMetadata(tableName, recordName, columnNames);
	}

	/**
	 * XML 파싱을 위한 Metadata를 설정한다. 
	 * @param tableName 데이터베이스로 말하자면 테이블명이다.
	 * @param recordName 데이터베이스로 말하자면 하나의 레코드이고, 그 레코드에 이름이 부여되었다.
	 * @param typeColumns 데이터베이스로 말하자면 필드명이다.
	 */
	public void setMetadata(String tableName, String recordName, String[] columnNames) {
		this.tableName = tableName;
		this.recordName = recordName;
		this.columnNames = columnNames;

		isInsideTable = false;
		isInsideRecord = false;
	}

	/**
	 * 파싱 후 결과값을 배열 형태로 반환합니다.<br>
	 * ArrayList가 포함하는 Item 배열구조는 item 인자의 배열구조와 동일합니다. 
	 * @param filename XML 파일
	 * @return ArrayList
	 * @throws CommonException
	 */
	//	public List read(String filename) throws CommonException {
	//		parse(new File(filename));
	//		return records;
	//	}
	/**
	 * 파싱 후 결과값을 배열 형태로 반환합니다.<br>
	 * ArrayList가 포함하는 Item 배열구조는 item 인자의 배열구조와 동일합니다. 
	 * @param data XML 데이터 문자열
	 * @return ArrayList
	 * @throws CommonException
	 */
	public List read(String data) throws ParserConfigurationException, SAXException, IOException {
		InputStream is = new ByteArrayInputStream(data.getBytes());
		parse(is);
		return records;
	}

	/**
	 * 파싱 후 결과값을 배열 형태로 반환합니다.<br>
	 * ArrayList가 포함하는 Item 배열구조는 item 인자의 배열구조와 동일합니다. 
	 * @param file File
	 * @return ArrayList
	 * @throws CommonException
	 */
	public List read(File file) throws ParserConfigurationException, SAXException, IOException {
		FileInputStream fis = new FileInputStream(file);
		parse(new BufferedInputStream(fis));
		return records;
	}

	/**
	 * 파싱 후 결과값을 배열 형태로 반환합니다.<br>
	 * ArrayList가 포함하는 Item 배열구조는 item 인자의 배열구조와 동일합니다. 
	 * @param url URL
	 * @return ArrayList
	 * @throws CommonException
	 */
	public List read(URL url) throws ParserConfigurationException, SAXException, IOException {
		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		parse(bis);
		return records;
	}

	/**
	 * 파싱 후 결과값을 배열 형태로 반환합니다.<br>
	 * ArrayList가 포함하는 Item 배열구조는 item 인자의 배열구조와 동일합니다. 
	 * @param InputStream is
	 * @return ArrayList
	 * @throws CommonException
	 */
	public List read(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		parse(bis);
		return records;
	}

	/**
	 * 파싱 후 결과값을 배열 형태로 반환합니다.<br>
	 * ArrayList가 포함하는 Item 배열구조는 item 인자의 배열구조와 동일합니다. 
	 * @param file XML FILE.
	 * @return ArrayList
	 * @throws CommonException
	 */
	protected void parse(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		if(tableName == null || recordName == null)
			throw new IllegalArgumentException("XML 데이터를 분석하기 위한 Metadata 정의가 이루어지지 않았습니다.");

		if(records != null)
			records.clear();

		if(buffer != null)
			buffer.setLength(0);

		records = new ArrayList();
		buffer = new StringBuffer();
		columnName = null;

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(is, this);
	}

	protected void addRecord() {
		records.add(record);
	}

	/**
	 * processingInstruction을 만날 때마다 호출됩니다.
	 * @param target
	 * @param data
	 */
	public void processingInstruction(String target, String data) {
	}

	/**
	 * document가 시작될 때 호출됩니다.
	 */
	public void startDocument() {
	}

	/**
	 * element가 시작될 때 호출됩니다.
	 */
	public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) {
		if(qName.equals(tableName)) {
			isInsideTable = true;
			return;
		}

		if(!isInsideTable)
			return;

		if(!isInsideRecord) {
			if(qName.equals(recordName)) {
				record = new Record();
				record.setAttributes(parseAttributes(attrs));
				isInsideRecord = true;
				return;
			}

			return;
		}

		columnName = null;

		if(columnNames != null) {
			for(int i = 0; i < columnNames.length; i++) {
				if(columnNames[i].equals(qName)) {
					columnName = qName;
					break;
				}
			}
		}

		attributes = attrs;
	}

	/**
	 * 캐릭터 문자를 만날 때마다 호출된다. element 의 value를 읽을 때 씁니다.
	 */
	public void characters(char ch[], int start, int length) {
		if(columnName != null) {
			buffer.append(new String(ch, start, length));
			//itemIndex = -1;
			//System.out.println(new String(ch, start, length));
		}
	}

	/**
	 * 분리 문자로만 되어 있는 텍스트를 만날 때 마다(DTD가 있는 경우에) 호출된다.
	 */
	public void ignorableWhitespace(char ch[], int start, int length) {
		//System.out.print("[" + new String(ch, start, length) + "]");
		//characters(ch, start, length); 
	}

	/**
	 * element 의 끝을 만날 때마다 호출된다.
	 */
	public void endElement(String namespaceURI, String localName, String qName) {
		if(qName.equals(tableName)) {
			isInsideTable = false;
			isInsideRecord = false;
			return;
		}

		if(!isInsideTable)
			return;

		if(isInsideRecord) {
			if(qName.equals(recordName)) {
				addRecord();
				attributes = null;
				columnName = null;
				isInsideRecord = false;
				return;
			}
		}

		if(columnName != null && columnName.equals(qName)) {
			Column column = new Column(columnName, buffer.toString());
			column.setAttributes(parseAttributes(attributes));

			record.addColumn(column);

			buffer.setLength(0);
			attributes = null;
			columnName = null;
		}
	}

	/**
	 * Document의 끝을 만나면 호출된다.
	 */
	public void endDocument() {
		// No need to do anything. 
	}

	/**
	 * ErrorHandler 처리 부분: Warning
	 */
	public void warning(SAXParseException ex) {
		throw new RuntimeException("[Warning] " + getLocationString(ex), ex);
	}

	/**
	 * ErrorHandler 처리 부분: Error
	 */
	public void error(SAXParseException ex) {
		throw new RuntimeException("[Error] " + getLocationString(ex), ex);
	}

	/**
	 * ErrorHandler 처리 부분: Fatal error
	 */
	public void fatalError(SAXParseException ex) throws SAXException {
		throw new RuntimeException("[Fatal Error] " + getLocationString(ex), ex);
	}

	/**
	 * XML 의 위치 정보 반환
	 */
	private String getLocationString(SAXParseException ex) {
		StringBuffer str = new StringBuffer();

		//같은 시스템에 있는 xml 문서의 이름이 반환된다. 
		//getPublicId()는 다른 시스템에 있는 xml 문서의 정보가 반환되고
		//getSystemId가 존재하면 getPublicId가 null이고 
		//getPublicId가 존재하면 getSystemId가 null이다.
		String systemId = ex.getSystemId();

		if(systemId != null) {
			int index = systemId.lastIndexOf('/');

			if(index != -1)
				systemId = systemId.substring(index + 1);

			str.append(systemId);
		}

		str.append(':');
		str.append(ex.getLineNumber());
		str.append(':');
		str.append(ex.getColumnNumber());

		return str.toString();
	}

	/**
	 * XML Element의 속성을 반환한다.
	 * @return
	 */
	private Map parseAttributes(Attributes attrs) {
		if(attrs == null)
			return null;

		Map map = new HashMap();

		for(int i = 0; i < attrs.getLength(); i++) {
			map.put(attrs.getQName(i), attrs.getValue(i));
			//System.out.println(attributes.getQName(i));
		}

		return map;
	}

	public static void main(String argv[]) {
		String[] columnNames = { "title", "link", "source", "author", "pubDate", "description", "category", "enclosure" };

		//String data = MyUtil.getText("http://ch.allblog.net/thankyou/samsung/rss/").toString();
		//System.out.println(data);

		try {
			MultipleXReader xreader = new MultipleXReader();
			xreader.setMetadata("channel", "item", columnNames);
			List list = xreader.read(new URL("http://ch.allblog.net/thankyou/samsung/rss/"));

			if(list != null) {
				for(int i = 0; i < list.size(); i++) {
					MultipleXReader.Record record = (MultipleXReader.Record)list.get(i);
					System.out.println(record.getColumnValue("title"));
					System.out.println(record.getColumnAttributeValue("source", "url"));

					//MultipleXReader.Column[] columns = (MultipleXReader.Column[])list.get(i);
					//System.out.println(record.getColumn("title").getValue());
					//System.out.println(column.getName() + ": " + column.getValue());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
