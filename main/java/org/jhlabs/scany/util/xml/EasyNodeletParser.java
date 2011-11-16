package org.jhlabs.scany.util.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

/**
 * The NodeletParser is a callback based parser similar to SAX.  The big
 * difference is that rather than having a single callback for all nodes,
 * the NodeletParser has a number of callbacks mapped to
 * various nodes.   The callback is called a Nodelet and it is registered
 * with the NodeletParser against a specific XPath.
 */
public class EasyNodeletParser extends AbstractNodeletParser {
	
	private final Log log = LogFactory.getLog(EasyNodeletParser.class);

	private final static Properties EMPTY_ATTRIBUTES = new Properties();
	
	private Map<String, EasyNodelet> nodeletMap = new HashMap<String, EasyNodelet>();
	
	/**
	 * Registers a nodelet for the specified XPath. Current XPaths supported
	 * are:
	 * <ul>
	 * <li> Text Path - /rootElement/childElement/text()
	 * <li> Attribute Path  - /rootElement/childElement/@theAttribute
	 * <li> Element Path - /rootElement/childElement/theElement
	 * <li> All Elements Named - //theElement
	 * </ul>
	 *
	 * @param xpath the xpath
	 * @param nodelet the nodelet
	 */
	public void addNodelet(String xpath, EasyNodelet nodelet) {
		nodeletMap.put(xpath, nodelet);
	}
	
	/**
	 * Registers a nodelet for the specified XPath. Current XPaths supported
	 * are:
	 * <ul>
	 * <li> Text Path - /rootElement/childElement/text()
	 * <li> Attribute Path  - /rootElement/childElement/@theAttribute
	 * <li> Element Path - /rootElement/childElement/theElement
	 * <li> All Elements Named - //theElement
	 * </ul>
	 *
	 * @param prefix the prefix xpath
	 * @param xpath the xpath
	 * @param nodelet the nodelet
	 */
	public void addNodelet(String prefix, String xpath, EasyNodelet nodelet) {
		addNodelet(new StringBuilder(prefix).append(xpath).toString(), nodelet);
	}

	/**
	 * Adds the nodelet.
	 *
	 * @param xpath the xpath
	 * @param nodeletAdder the nodelet adder
	 * @throws Exception the exception
	 */
	public void addNodelet(String xpath, EasyNodeletAdder nodeletAdder) {
		nodeletAdder.process(xpath, this);
	}
	
	/**
	 * Adds the nodelet.
	 *
	 * @param prefix the prefix
	 * @param xpath the xpath
	 * @param nodeletAdder the nodelet adder
	 * @throws Exception the exception
	 */
	public void addNodelet(String prefix, String xpath, EasyNodeletAdder nodeletAdder) {
		addNodelet(new StringBuilder(prefix).append(xpath).toString(), nodeletAdder);
	}

	protected void processNodelet(Node node, String pathString) {
		EasyNodelet nodelet = nodeletMap.get(pathString);
		
		if(nodelet != null) {
			try {
				Properties attributes;
				String text;

				if(!pathString.endsWith("end()")) {
					attributes = NodeletUtils.parseAttributes(node);
					text = NodeletUtils.getText(node);

					if(log.isTraceEnabled()) {
						StringBuilder sb = new StringBuilder(pathString);
						
						if(attributes != null && attributes.size() > 0) {
							sb.append(" ").append(attributes);
						}
						
						if(text != null && text.length() > 0) {
							sb.append(" ").append(text);
						}
						
						log.trace(sb.toString());
					}
				} else {
					attributes = EMPTY_ATTRIBUTES;
					text = null;
				}

				nodelet.process(attributes, text);
			} catch(Exception e) {
				throw new RuntimeException("Error parsing XPath '" + pathString + "'. Cause: " + e, e);
			}
		}
	}

}