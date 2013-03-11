/**

	BlackBoard BreadBoard Designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.core.html;

public class Link extends AbstractHTMLElement implements IHTMLElement {
	
	public static interface Target {
		public static String BLANK = "_blank";
		public static String SELF = "_self";
	}

	private String charset;    
	private String type;       
	private String name;      
	private String href;       
	private String hreflang;   
	private String rel;        
	private String rev;        
	private String accesskey;  
	private String shape;      
	private String coords;     
	private String tabindex;   
	private String onfocus;    
	private String onblur; 	
	private String target;
	private String data;
	
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * @return the hreflang
	 */
	public String getHreflang() {
		return hreflang;
	}

	/**
	 * @param hreflang the hreflang to set
	 */
	public void setHreflang(String hreflang) {
		this.hreflang = hreflang;
	}

	/**
	 * @return the rel
	 */
	public String getRel() {
		return rel;
	}

	/**
	 * @param rel the rel to set
	 */
	public void setRel(String rel) {
		this.rel = rel;
	}

	/**
	 * @return the rev
	 */
	public String getRev() {
		return rev;
	}

	/**
	 * @param rev the rev to set
	 */
	public void setRev(String rev) {
		this.rev = rev;
	}

	/**
	 * @return the accesskey
	 */
	public String getAccesskey() {
		return accesskey;
	}

	/**
	 * @param accesskey the accesskey to set
	 */
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	/**
	 * @return the shape
	 */
	public String getShape() {
		return shape;
	}

	/**
	 * @param shape the shape to set
	 */
	public void setShape(String shape) {
		this.shape = shape;
	}

	/**
	 * @return the coords
	 */
	public String getCoords() {
		return coords;
	}

	/**
	 * @param coords the coords to set
	 */
	public void setCoords(String coords) {
		this.coords = coords;
	}

	/**
	 * @return the tabindex
	 */
	public String getTabindex() {
		return tabindex;
	}

	/**
	 * @param tabindex the tabindex to set
	 */
	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}

	/**
	 * @return the onfocus
	 */
	public String getOnfocus() {
		return onfocus;
	}

	/**
	 * @param onfocus the onfocus to set
	 */
	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	/**
	 * @return the onblur
	 */
	public String getOnblur() {
		return onblur;
	}

	/**
	 * @param onblur the onblur to set
	 */
	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}


	  
	@Override
	public String render() {

		StringBuffer s = new StringBuffer();
		
		s.append(Tags.LINK_OPEN);	
		s.append(getUniversalAttributes());
		
		if (href != null && href.length() > 0)
			s.append(Attribute.ATT_HREF+"="+Tags.QUOTE+href+Tags.QUOTE);
		if (rel != null && rel.length() > 0)
			s.append(Attribute.ATT_REL+"="+Tags.QUOTE+rel+Tags.QUOTE);
		if (target != null && target.length() > 0)
			s.append(Attribute.ATT_TARGET+"="+Tags.QUOTE+target+Tags.QUOTE);
		if (name != null && name.length() > 0)
			s.append(Attribute.ATT_NAME+"="+Tags.QUOTE+name+Tags.QUOTE);
		
		s.append(Tags.TAG_FINISH);		
		
		if (data != null && data.length() > 0)
			s.append(data);
		
		s.append(Tags.LINK_CLOSE);
		
		return s.toString();
		
	}

}
