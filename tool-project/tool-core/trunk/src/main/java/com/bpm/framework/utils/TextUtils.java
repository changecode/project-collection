package com.bpm.framework.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * 文本工具类
 * 
 * @author lixx
 * @since 1.0
 * @version 2015-01-26 18:26:00
 */
public class TextUtils {

	public final static String htmlEncode(String s) {
		return htmlEncode(s, true);
	}

	/**
	 * Escape html entity characters and high characters (eg "curvy" Word
	 * quotes). Note this method can also be used to encode XML.
	 * 
	 * @param s
	 *            the String to escape.
	 * @param encodeSpecialChars
	 *            if true high characters will be encode other wise not.
	 * @return the escaped string
	 */
	public final static String htmlEncode(String s, boolean encodeSpecialChars) {
		s = noNull(s);

		StringBuffer str = new StringBuffer();

		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);

			// encode standard ASCII characters into HTML entities where needed
			if (c < '\200') {
				switch (c) {
				case '"':
					str.append("&quot;");

					break;

				case '&':
					str.append("&amp;");

					break;

				case '<':
					str.append("&lt;");

					break;

				case '>':
					str.append("&gt;");

					break;

				default:
					str.append(c);
				}
			}
			// encode 'ugly' characters (ie Word "curvy" quotes etc)
			else if (encodeSpecialChars && (c < '\377')) {
				String hexChars = "0123456789ABCDEF";
				int a = c % 16;
				int b = (c - a) / 16;
				String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
				str.append("&#x" + hex + ";");
			}
			// add other characters back in - to handle charactersets
			// other than ascii
			else {
				str.append(c);
			}
		}

		return str.toString();
	}
	
	/**
	 * Escape html entity characters and high characters (eg "curvy" Word
	 * quotes). Note this method can also be used to encode XML.
	 * 
	 * @param s
	 *            the String to escape.
	 * @param encodeSpecialChars
	 *            if true high characters will be encode other wise not.
	 * @return the escaped string
	 */ 
	public static String htmlDecode(String s, boolean encodeSpecialChars) {
		s = noNull(s);
		s = s.replaceAll("&quot;", "\"") ;
		s = s.replaceAll("&amp;", "&") ;
		s = s.replaceAll("&lt;", "<") ;
		s = s.replaceAll("&gt;", ">") ;
		return s ;
	}

	/**
	 * Join an Iteration of Strings together.
	 * 
	 * <h5>Example</h5>
	 * 
	 * <pre>
	 * // get Iterator of Strings (&quot;abc&quot;,&quot;def&quot;,&quot;123&quot;);
	 * Iterator i = getIterator();
	 * out.print(TextUtils.join(&quot;, &quot;, i));
	 * // prints: &quot;abc, def, 123&quot;
	 * </pre>
	 * 
	 * @param glue
	 *            Token to place between Strings.
	 * @param pieces
	 *            Iteration of Strings to join.
	 * @return String presentation of joined Strings.
	 */
	public final static String join(String glue, Iterator<?> pieces) {
		StringBuffer s = new StringBuffer();

		while (pieces.hasNext()) {
			s.append(pieces.next().toString());

			if (pieces.hasNext()) {
				s.append(glue);
			}
		}

		return s.toString();
	}

	/**
	 * Join an array of Strings together.
	 * 
	 * @param glue
	 *            Token to place between Strings.
	 * @param pieces
	 *            Array of Strings to join.
	 * @return String presentation of joined Strings.
	 * 
	 * @see #join(String, java.util.Iterator)
	 */
	public final static String join(String glue, String[] pieces) {
		return join(glue, Arrays.asList(pieces).iterator());
	}

	/**
	 * Join a Collection of Strings together.
	 * 
	 * @param glue
	 *            Token to place between Strings.
	 * @param pieces
	 *            Collection of Strings to join.
	 * @return String presentation of joined Strings.
	 * 
	 * @see #join(String, java.util.Iterator)
	 */
	public final static String join(String glue, Collection<?> pieces) {
		return join(glue, pieces.iterator());
	}

	/**
	 * Return <code>string</code>, or <code>defaultString</code> if
	 * <code>string</code> is <code>null</code> or <code>""</code>. Never
	 * returns <code>null</code>.
	 * 
	 * <p>
	 * Examples:
	 * </p>
	 * 
	 * <pre>
	 * // prints "hello"
	 * String s=null;
	 * System.out.println(TextUtils.noNull(s,"hello");
	 * 
	 * // prints "hello"
	 * s="";
	 * System.out.println(TextUtils.noNull(s,"hello");
	 * 
	 * // prints "world"
	 * s="world";
	 * System.out.println(TextUtils.noNull(s, "hello");
	 * </pre>
	 * 
	 * @param string
	 *            the String to check.
	 * @param defaultString
	 *            The default string to return if <code>string</code> is
	 *            <code>null</code> or <code>""</code>
	 * @return <code>string</code> if <code>string</code> is non-empty, and
	 *         <code>defaultString</code> otherwise
	 * @see #stringSet(java.lang.String)
	 */
	public final static String noNull(String string, String defaultString) {
		return (stringSet(string)) ? string : defaultString;
	}

	/**
	 * Return <code>string</code>, or <code>""</code> if <code>string</code> is
	 * <code>null</code>. Never returns <code>null</code>.
	 * <p>
	 * Examples:
	 * </p>
	 * 
	 * <pre>
	 * // prints 0
	 * String s = null;
	 * System.out.println(TextUtils.noNull(s).length());
	 * 
	 * // prints 1
	 * s = &quot;a&quot;;
	 * System.out.println(TextUtils.noNull(s).length());
	 * </pre>
	 * 
	 * @param string
	 *            the String to check
	 * @return a valid (non-null) string reference
	 */
	public final static String noNull(String string) {
		return noNull(string, "");
	}

	/**
	 * Check whether <code>string</code> has been set to something other than
	 * <code>""</code> or <code>null</code>.
	 * 
	 * @param string
	 *            the <code>String</code> to check
	 * @return a boolean indicating whether the string was non-empty (and
	 *         non-null)
	 */
	public final static boolean stringSet(String string) {
		return (string != null) && !"".equals(string);
	}

	/**
	 * Verify That the given String is in valid URL format.
	 * 
	 * @param url
	 *            The url string to verify.
	 * @return a boolean indicating whether the URL seems to be incorrect.
	 */
	public final static boolean verifyUrl(String url) {
		if (url == null) {
			return false;
		}

		if (url.startsWith("https://")) {
			// URL doesn't understand the https protocol, hack it
			url = "http://" + url.substring(8);
		}

		try {
			new URL(url);

			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

}
