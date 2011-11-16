package org.jhlabs.scany.util.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * The NodeletParser is a callback based parser similar to SAX.  The big
 * difference is that rather than having a single callback for all nodes,
 * the NodeletParser has a number of callbacks mapped to
 * various nodes.   The callback is called a Nodelet and it is registered
 * with the NodeletParser against a specific XPath.
 */
public class NodeletParser extends AbstractNodeletParser {
	
	private Map<String, Nodelet> nodeletMap = new HashMap<String, Nodelet>();

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
	public void addNodelet(String xpath, Nodelet nodelet) {
		nodeletMap.put(xpath, nodelet);
	}

	protected void processNodelet(Node node, String pathString) {
		Nodelet nodelet = nodeletMap.get(pathString);
		
		if(nodelet != null) {
			try {
				nodelet.process(node);
			} catch(Exception e) {
				throw new RuntimeException("Error parsing XPath '" + pathString + "'. Cause: " + e, e);
			}
		}
	}

}