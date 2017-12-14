package com.bpm.framework.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Miscellaneous string utility methods. Mainly for internal use within the
 * framework; consider Jakarta's Commons Lang for a more comprehensive suite of
 * string utilities.
 * 
 * <p>
 * This class delivers some simple functionality that should really be provided
 * by the core Java String and StringBuffer classes, such as the ability to
 * replace all occurrences of a given substring in a target string. It also
 * provides easy-to-use methods to convert between delimited strings, such as
 * CSV strings, and collections and arrays.
 * 
 * <p>
 * 
 * <p>
 * 
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author lixx
 * @since 16 April 2001
 * @see org.apache.commons.lang.StringUtils
 */
@SuppressWarnings("unchecked")
public abstract class StringUtils {

	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	public static final String PASSWOARD = "[[a-z]|[A-Z]|[0-9]]*";

	public static final String USERNAME = "[[a-z]|[A-Z]|[0-9]]*";

	public static final String NUMBER = "[0-9]*";

	public static final String LETTER = "[[A-Z]|[a-z]]*";

	public static final String PHONE_NO = "0[0-9]{2,3}-[0-9]{7,8}";

	public static final String MOBILE_PHONE_NO = "13[0-9][0-9]{8}";

	// ---------------------------------------------------------------------
	// General convenience methods for working with Strings
	// ---------------------------------------------------------------------

	/**
	 * Check that the given String is neither <code>null</code> nor of length
	 * 0. Note: Will return <code>true</code> for a String that purely
	 * consists of whitespace.
	 * <p>
	 * 
	 * <pre>
	 *                                                                                                StringUtils.hasLength(null) = false
	 *                                                                                                StringUtils.hasLength(&quot;&quot;) = false
	 *                                                                                                StringUtils.hasLength(&quot; &quot;) = true
	 *                                                                                                StringUtils.hasLength(&quot;Hello&quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(String str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check whether the given String has actual text. More specifically,
	 * returns <code>true</code> if the string not <code>null<code>,
	 * its length is greater than 0, and it contains at least one non-whitespace character.
	 * <p><pre>
	 *                                                                                                StringUtils.hasText(null) = false
	 *                                                                                                StringUtils.hasText(&quot;&quot;) = false
	 *                                                                                                StringUtils.hasText(&quot; &quot;) = false
	 *                                                                                                StringUtils.hasText(&quot;12345&quot;) = true
	 *                                                                                                StringUtils.hasText(&quot; 12345 &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not <code>null</code>, its
	 *         length is greater than 0, and is does not contain whitespace only
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean hasText(String str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given String contains any whitespace characters.
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not empty and contains at
	 *         least 1 whitespace character
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean containsWhitespace(String str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Trim leading and trailing whitespace from the given String.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		while (buf.length() > 0
				&& Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim leading whitespace from the given String.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Trim trailing whitespace from the given String.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0
				&& Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim <i>all</i> whitespace from the given String: leading, trailing, and
	 * inbetween characters.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		int index = 0;
		while (buf.length() > index) {
			if (Character.isWhitespace(buf.charAt(index))) {
				buf.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return buf.toString();
	}

	/**
	 * Test if the given String starts with the specified prefix, ignoring
	 * upper/lower case.
	 * 
	 * @param str
	 *            the String to check
	 * @param prefix
	 *            the prefix to look for
	 * @see java.lang.String#startsWith
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * Test if the given String ends with the specified suffix, ignoring
	 * upper/lower case.
	 * 
	 * @param str
	 *            the String to check
	 * @param suffix
	 *            the suffix to look for
	 * @see java.lang.String#endsWith
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}

		String lcStr = str.substring(str.length() - suffix.length())
				.toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	/**
	 * Count the occurrences of the substring in string s.
	 * 
	 * @param str
	 *            string to search in. Return 0 if this is null.
	 * @param sub
	 *            string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0
				|| sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * Replace all occurences of a substring within a string with another
	 * string.
	 * 
	 * @param inString
	 *            String to examine
	 * @param oldPattern
	 *            String to replace
	 * @param newPattern
	 *            String to insert
	 * @return a String with the replacements
	 */
	public static String replace(String inString, String oldPattern,
			String newPattern) {
		if (inString == null) {
			return null;
		}
		if (oldPattern == null || newPattern == null) {
			return inString;
		}

		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		// remember to append any characters to the right of a match
		return sbuf.toString();
	}

	/**
	 * Delete all occurrences of the given substring.
	 * 
	 * @param pattern
	 *            the pattern to delete all occurrences of
	 */
	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	/**
	 * Delete any character in a given string.
	 * 
	 * @param charsToDelete
	 *            a set of characters to delete. E.g. "az\n" will delete 'a's,
	 *            'z's and new lines.
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (inString == null || charsToDelete == null) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with formatted Strings
	// ---------------------------------------------------------------------

	/**
	 * Quote the given String with single quotes.
	 * 
	 * @param str
	 *            the input String (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or
	 *         <code>null<code> if the input was <code>null</code>
	 */
	public static String quote(String str) {
		return (str != null ? "'" + str + "'" : null);
	}

	/**
	 * Turn the given Object into a String with single quotes if it is a String;
	 * keeping the Object as-is else.
	 * 
	 * @param obj
	 *            the input Object (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or the input object as-is
	 *         if not a String
	 */
	public static Object quoteIfString(Object obj) {
		return (obj instanceof String ? quote((String) obj) : obj);
	}

	/**
	 * Unqualify a string qualified by a '.' dot character. For example,
	 * "this.name.is.qualified", returns "qualified".
	 * 
	 * @param qualifiedName
	 *            the qualified name
	 */
	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	/**
	 * Unqualify a string qualified by a separator character. For example,
	 * "this:name:is:qualified" returns "qualified" if using a ':' separator.
	 * 
	 * @param qualifiedName
	 *            the qualified name
	 * @param separator
	 *            the separator
	 */
	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName
				.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	/**
	 * Capitalize a <code>String</code>, changing the first letter to upper
	 * case as per {@link Character#toUpperCase(char)}. No other letters are
	 * changed.
	 * 
	 * @param str
	 *            the String to capitalize, may be <code>null</code>
	 * @return the capitalized String, <code>null</code> if null
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * Uncapitalize a <code>String</code>, changing the first letter to lower
	 * case as per {@link Character#toLowerCase(char)}. No other letters are
	 * changed.
	 * 
	 * @param str
	 *            the String to uncapitalize, may be <code>null</code>
	 * @return the uncapitalized String, <code>null</code> if null
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str,
			boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

	/**
	 * Extract the filename from the given path, e.g. "mypath/myfile.txt" ->
	 * "myfile.txt".
	 * 
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1)
				: path);
	}

	/**
	 * Extract the filename extension from the given path, e.g.
	 * "mypath/myfile.txt" -> "txt".
	 * 
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the extracted filename extension, or <code>null</code> if none
	 */
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
	}

	/**
	 * Strip the filename extension from the given path, e.g.
	 * "mypath/myfile.txt" -> "mypath/myfile".
	 * 
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the path with stripped filename extension, or <code>null</code>
	 *         if none
	 */
	public static String stripFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	/**
	 * Apply the given relative path to the given path, assuming standard Java
	 * folder separation (i.e. "/" separators);
	 * 
	 * @param path
	 *            the path to start from (usually a full file path)
	 * @param relativePath
	 *            the relative path to apply (relative to the full file path
	 *            above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner
	 * simple dots.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that
	 * Windows separators ("\") are replaced by simple slashes.
	 * 
	 * @param path
	 *            the original path
	 * @return the normalized path
	 */

	public static String cleanPath(String path) {
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR,
				FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse,
				FOLDER_SEPARATOR);
		List pathElements = new LinkedList();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pathArray[i])) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(pathArray[i])) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with corresponding to top path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, pathArray[i]);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix
				+ collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * Compare two paths after normalization of them.
	 * 
	 * @param path1
	 *            First path for comparizon
	 * @param path2
	 *            Second path for comparizon
	 * @return whether the two paths are equivalent after normalization
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	/**
	 * Parse the given locale string into a <code>java.util.Locale</code>.
	 * This is the inverse operation of Locale's <code>toString</code>.
	 * 
	 * @param localeString
	 *            the locale string, following <code>java.util.Locale</code>'s
	 *            toString format ("en", "en_UK", etc). Also accepts spaces as
	 *            separators, as alternative to underscores.
	 * @return a corresponding Locale instance
	 */
	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = (parts.length > 0 ? parts[0] : "");
		String country = (parts.length > 1 ? parts[1] : "");
		String variant = (parts.length > 2 ? parts[2] : "");
		return (language.length() > 0 ? new Locale(language, country, variant)
				: null);
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with String arrays
	// ---------------------------------------------------------------------

	/**
	 * Append the given String to the given String array, returning a new array
	 * consisting of the input array contents plus the given String.
	 * 
	 * @param array
	 *            the array to append to (can be <code>null</code>)
	 * @param str
	 *            the String to append
	 * @return the new array (never <code>null</code>)
	 */
	public static String[] addStringToArray(String[] array, String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] { str };
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	/**
	 * Concatenate the given String arrays into one, with overlapping array
	 * elements included twice.
	 * <p>
	 * The order of elements in the original arrays is preserved.
	 * 
	 * @param array1
	 *            the first array (can be <code>null</code>)
	 * @param array2
	 *            the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were
	 *         <code>null</code>)
	 */
	public static String[] concatenateStringArrays(String[] array1,
			String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	/**
	 * Merge the given String arrays into one, with overlapping array elements
	 * only included once.
	 * <p>
	 * The order of elements in the original arrays is preserved (with the
	 * exception of overlapping elements, which are only included on their first
	 * occurence).
	 * 
	 * @param array1
	 *            the first array (can be <code>null</code>)
	 * @param array2
	 *            the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were
	 *         <code>null</code>)
	 */
	public static String[] mergeStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		List result = new ArrayList();
		result.addAll(Arrays.asList(array1));
		for (int i = 0; i < array2.length; i++) {
			String str = array2[i];
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

	/**
	 * Turn given source String array into sorted array.
	 * 
	 * @param array
	 *            the source array
	 * @return the sorted array (never <code>null</code>)
	 */
	public static String[] sortStringArray(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}

	/**
	 * Copy the given Collection into a String array. The Collection must
	 * contain String elements only.
	 * 
	 * @param collection
	 *            the Collection to copy
	 * @return the String array (<code>null</code> if the Collection was
	 *         <code>null</code> as well)
	 */
	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	/**
	 * Remove duplicate Strings from the given array. Also sorts the array, as
	 * it uses a TreeSet.
	 * 
	 * @param array
	 *            the String array
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}
		Set set = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return toStringArray(set);
	}

	/**
	 * Split a String at the first occurrence of the delimiter. Does not include
	 * the delimiter in the result.
	 * 
	 * @param toSplit
	 *            the string to split
	 * @param delimiter
	 *            to split the string up with
	 * @return a two element array with index 0 being before the delimiter, and
	 *         index 1 being after the delimiter (neither element includes the
	 *         delimiter); or <code>null</code> if the delimiter wasn't found
	 *         in the given input String
	 */
	public static String[] splitFirst(String toSplit, String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	/**
	 * Take an array Strings and split each element based on the given
	 * delimiter. A <code>Properties</code> instance is then generated, with
	 * the left of the delimiter providing the key, and the right of the
	 * delimiter providing the value.
	 * <p>
	 * Will trim both the key and value before adding them to the
	 * <code>Properties</code> instance.
	 * 
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @return a <code>Properties</code> instance representing the array
	 *         contents, or <code>null</code> if the array to process was null
	 *         or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array,
			String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	/**
	 * Take an array Strings and split each element based on the given
	 * delimiter. A <code>Properties</code> instance is then generated, with
	 * the left of the delimiter providing the key, and the right of the
	 * delimiter providing the value.
	 * <p>
	 * Will trim both the key and value before adding them to the
	 * <code>Properties</code> instance.
	 * 
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @param charsToDelete
	 *            one or more characters to remove from each element prior to
	 *            attempting the split operation (typically the quotation mark
	 *            symbol), or <code>null</code> if no removal should occur
	 * @return a <code>Properties</code> instance representing the array
	 *         contents, or <code>null</code> if the array to process was
	 *         <code>null</code> or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array,
			String delimiter, String charsToDelete) {

		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		Properties result = new Properties();
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			if (charsToDelete != null) {
				element = deleteAny(array[i], charsToDelete);
			}
			String[] splittedElement = splitFirst(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1]
					.trim());
		}
		return result;
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * 
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those
	 *            characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * 
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those
	 *            characters is individually considered as delimiter)
	 * @param trimTokens
	 *            trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens
	 *            omit empty tokens from the result array (only applies to
	 *            tokens that are empty after trimming; StringTokenizer will not
	 *            consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters,
			boolean trimTokens, boolean ignoreEmptyTokens) {

		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of
	 * potential delimiter characters - in contrast to
	 * <code>tokenizeToStringArray</code>.
	 * 
	 * @param str
	 *            the input String
	 * @param delimiter
	 *            the delimiter between elements (this is a single delimiter,
	 *            rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str,
			String delimiter) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List result = new ArrayList();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(str.substring(i, i + 1));
			}
		} else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(str.substring(pos, delPos));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(str.substring(pos));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Convert a CSV list into an array of Strings.
	 * 
	 * @param str
	 *            CSV list
	 * @return an array of Strings, or the empty array if s is null
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * Convenience method to convert a CSV string list to a set. Note that this
	 * will suppress duplicates.
	 * 
	 * @param str
	 *            CSV String
	 * @return a Set of String entries in the list
	 */
	public static Set commaDelimitedListToSet(String str) {
		Set set = new TreeSet();
		String[] tokens = commaDelimitedListToStringArray(str);
		for (int i = 0; i < tokens.length; i++) {
			set.add(tokens[i]);
		}
		return set;
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * 
	 * @param coll
	 *            Collection to display
	 * @param delim
	 *            delimiter to use (probably a ",")
	 * @param prefix
	 *            string to start each element with
	 * @param suffix
	 *            string to end each element with
	 */
	public static String collectionToDelimitedString(Collection coll,
			String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * 
	 * @param coll
	 *            Collection to display
	 * @param delim
	 *            delimiter to use (probably a ",")
	 */
	public static String collectionToDelimitedString(Collection coll,
			String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convenience method to return a Collection as a CSV String. E.g. useful
	 * for <code>toString()</code> implementations.
	 * 
	 * @param coll
	 *            Collection to display
	 */
	public static String collectionToCommaDelimitedString(Collection coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * Convenience method to return a String array as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * 
	 * @param arr
	 *            array to display. Elements may be of any type (toString will
	 *            be called on each element).
	 * @param delim
	 *            delimiter to use (probably a ",")
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String arrayToDelimitedString(String[] arr, String delim) {
		if (arr == null || arr.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			if (arr[i] == null) {
				sb.append("");
			} else {
				sb.append(arr[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * Convenience method to return a String array as a CSV String. E.g. useful
	 * for <code>toString()</code> implementations.
	 * 
	 * @param arr
	 *            array to display. Elements may be of any type (toString will
	 *            be called on each element).
	 */
	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	// -------------------------------------------------------------------------------------------

	/**
	 * 判断str字符串是否符合我们定义的标准格式，pattern表示格式(正则表达式)。
	 * 
	 * @param str
	 * @param pattern
	 */
	public static boolean isDefinedPattern(String str, String pattern) {
		Assert.notNull(str);
		Assert.notNull(pattern);

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 使用str2代替str1中的pattern字符串（pattern可以是正则表达式）
	 * 
	 * @param str1
	 *            将被替换的源字符串
	 * @param pattern
	 *            指定的将被替换的字符串符合的模式（pattern）
	 * @param str2
	 *            用来替换掉符合模式字符串的字符串
	 */
	public static String replaceByPattern(String str1, String pattern,
			String str2) {
		Assert.notNull(str1);
		Assert.notNull(pattern);
		Assert.notNull(str2);

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str1);
		return m.replaceAll(str2);
	}

	/**
	 * 通过指定字符串模式来截取字符串，并且返回切断后的字符串数组
	 * 
	 * <p>
	 * 该方法同StringUtils.split方法有一些区别，StringUtils.split是在分割字符第一次出现的地方把字符串分割成两段，
	 * 形成一个length为2的数组。而本方法是在字符串中任何出现符合切割模式 的地方进行切割，形成一个长度不定的字符串数组
	 * 
	 * @param str
	 * @param pattern
	 */
	public static String[] splitByPattern(String str, String pattern) {
		Assert.notNull(pattern);

		if (StringUtils.hasLength(str)) {
			Pattern p = Pattern.compile(pattern);
			return p.split(str);
		} else {
			return null;
		}
	}

	/**
	 * 把字符串数组转化为数字型(Long,Integer,Float,Double)数组
	 * 
	 * @param <T>
	 *            要转化成的目标数组的类型
	 * @param source
	 *            需要被转化的源字符串数组
	 * @param destination
	 *            要转成的目标类型数组的一个实例，用来表示需要把源数组转化成什么类型的数组
	 * @return
	 */
	public static <T> T[] strArrayToNumArray(String[] source, T[] destination) {

		Assert.notNull(source);
		Assert.notNull(destination);

		Class clazz = destination.getClass().getComponentType();

		// 转换成Integer数组
		if (ClassTypeUtil.isInteger(clazz)) {
			Integer[] temp = new Integer[source.length];
			for (int i = 0; i < source.length; i++) {
				temp[i] = Integer.parseInt(source[i]);
			}
			return (T[]) temp;
		}

		// 转换成Long数组
		if (ClassTypeUtil.isLong(clazz)) {
			Long[] temp = new Long[source.length];
			for (int i = 0; i < source.length; i++) {
				temp[i] = Long.parseLong(source[i]);
			}
			return (T[]) temp;
		}

		// 转换成Float数组
		if (ClassTypeUtil.isFloat(clazz)) {
			Float[] temp = new Float[source.length];
			for (int i = 0; i < source.length; i++) {
				temp[i] = Float.parseFloat(source[i]);
			}
			return (T[]) temp;
		}

		// 转换成Double数组
		if (ClassTypeUtil.isDouble(clazz)) {
			Double[] temp = new Double[source.length];
			for (int i = 0; i < source.length; i++) {
				temp[i] = Double.parseDouble(source[i]);
			}
			return (T[]) temp;
		}

		throw new RuntimeException("type error,can't surport from string[] to "
				+ clazz.getName() + "[].");
	}

	/**
	 * 把字符串转化为数字型(Long,Integer,Float,Double)数组
	 * 
	 * @param <T>
	 *            要转化成的目标数组的类型
	 * @param source
	 *            需要被转化的源字符串
	 * @param destination
	 *            要转成的目标类型数组的一个实例，用来表示需要把源数组转化成什么类型的数组
	 * @return
	 */
	public static <T> T[] splitToNumArray(String source, String pattern,
			T[] destination) {

		Assert.notNull(source);
		Assert.notNull(pattern);
		Assert.notNull(destination);

		String[] arr = StringUtils.splitByPattern(source, pattern);
		return StringUtils.strArrayToNumArray(arr, destination);
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(String str) {
		if (str == null) {
			return true;
		} else if (str.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEmpty(String str) {
		return isNullOrBlank(str);
	}
	
	public static boolean isNotEmpty(String str) {
		return !isNullOrBlank(str);
	}

	/**
	 * 将字符串分割成长整型数组
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static <T> T[] splitToArray(String str, String pattern,
			Class<T> clazz) {
		Assert.notNull(str);
		Assert.notNull(pattern);
		String[] source = str.split(pattern);
		// 转换成Long数组
		// 转换成Integer数组
		if (ClassTypeUtil.isInteger(clazz)) {
			Integer[] temp = new Integer[source.length];
			for (int i = 0; i < source.length; i++) {
				temp[i] = Integer.parseInt(source[i]);
			}
			return (T[]) temp;
		}

		// 转换成Long数组
		if (ClassTypeUtil.isLong(clazz)) {
			Long[] temp = new Long[source.length];
			for (int i = 0; i < source.length; i++) {
				temp[i] = Long.parseLong(source[i]);
			}
			return (T[]) temp;
		}

		// 转换成Float数组
		if (ClassTypeUtil.isFloat(clazz)) {
			Float[] temp = new Float[source.length];
			for (int i = 0; i < source.length; i++) {
				temp[i] = Float.parseFloat(source[i]);
			}
			return (T[]) temp;
		}

		// 转换成Double数组
		if (ClassTypeUtil.isDouble(clazz)) {
			Double[] temp = new Double[source.length];
			for (int i = 0; i < source.length; i++) {
				temp[i] = Double.parseDouble(source[i]);
			}
			return (T[]) temp;
		}

		throw new RuntimeException("type error,can't surport from string[] to "
				+ clazz.getName() + "[].");
	}

	/**
	 * 去掉字符串中 回车符 换行符 制表符
	 * 
	 * @param str
	 * @return
	 */
	public static String parseToString(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("\r|\n|\t", " ");
	}

	public static String getText(String info, Object... arg) {

		if (arg != null && arg.length > 0) {
			for (int i = 0; i < arg.length; i++) {
				String val = arg[i] == null ? "" : arg[i].toString();
				// String replaceAll(regex, replacement)函数 ,
				// 由于第一个参数支持正则表达式，replacement中出现"$",会按照$1$2的分组
				// 模式进行匹配，当编译器发现"$"后跟的不是整数的时候，就会抛出"非法的组引用"的异常。
				// 所以我们在使用replaceAll(regex,replacement)函数的时候,
				// 要把String中的字符替换成"$AAA"的话，可以对replacement进行"$"的转义处理,filterDollarStr就是用来做转义处理的函数
				info = info.replaceAll("(\\{" + i + "\\})",
						filterDollarStr(val));
			}
		}
		return info;
	}

	/**
	 * String replaceAll(regex, replacement)函数 ,
	 * 由于第一个参数支持正则表达式，replacement中出现"$",会按照$1$2的分组
	 * 模式进行匹配，当编译器发现"$"后跟的不是整数的时候，就会抛出"非法的组引用"的异常。
	 * 所以我们在使用replaceAll(regex,replacement)函数的时候,
	 * 要把String中的字符替换成"$AAA"的话，可以对replacement进行
	 * "$"的转义处理,filterDollarStr就是用来做转义处理的函数
	 * 
	 * @param str
	 * @return
	 */
	public static String filterDollarStr(String str) {
		StringBuffer sReturn = new StringBuffer("");
		if (str.indexOf('$', 0) > -1) {
			while (str.length() > 0) {
				if (str.indexOf('$', 0) > -1) {
					sReturn.append(str.subSequence(0, str.indexOf('$', 0)));
					sReturn.append("\\$");
					str = str.substring(str.indexOf('$', 0) + 1, str.length());
				} else {
					sReturn.append(str);
					str = "";
				}
			}
		} else {
			sReturn = new StringBuffer(str);
		}
		return sReturn.toString();
	}

	/**
	 * 分割字符串,查询的时候用ID的时候超过1000个会出问题的解决方法
	 * 
	 * @param strs
	 * @return
	 */
	public static List<String> getSplitStr(String strs) {
		List<String> list = new ArrayList<String>();
		StringBuffer strTemp = new StringBuffer();
		if (strs == null) {
			return list;
		}
		String[] strGroup = strs.split(",");
		for (int i = 0; i < strGroup.length; i++) {
			if (i != 0 && i % 1000 == 0) {
				list.add(strTemp.toString().substring(0,
						strTemp.toString().length() - 1));
				strTemp = new StringBuffer();
				strTemp.append(strGroup[i]).append(",");
			} else {
				strTemp.append(strGroup[i]).append(",");
			}
		}
		list.add(strTemp.toString().substring(0,
				strTemp.toString().length() - 1));
		return list;
	}

	/**
	 * 用户String提供的split方法会过滤掉后面的空字符串，所以提供本方法，严格按照分隔符个数形成数组
	 * 
	 * 例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}，
	 * 而用本函数则返回数组{"1","2","3","4","",""}
	 * 
	 * @param input
	 *            本分割的字符串或者StringBuffer对象
	 * @param delimiterPattern
	 *            分割字符
	 * @param limit
	 *            参数控制模式应用的次数，因此影响结果数组的长度。如果该限制 n 大于 0，则模式将被最多应用 n - 1
	 *            次，数组的长度将不会大于 n，而且数组的最后项将包含超出最后匹配的定界符的所有输入。如果 n
	 *            为非正，则模式将被应用尽可能多的次数，而且数组可以是任意长度。如果 n
	 *            为零，则模式将被应用尽可能多的次数，数组可有任何长度，并且结尾空字符串将被丢弃。
	 * @return
	 */
	public static String[] split(CharSequence input, String delimiterPattern,
			int limit) {
		if (input == null)
			return new String[] {};
		int index = 0;
		boolean matchLimited = limit > 0;
		ArrayList matchList = new ArrayList();
		Pattern p = Pattern.compile(delimiterPattern);
		Matcher m = p.matcher(input);

		// Add segments before each match found
		while (m.find()) {
			if (!matchLimited || matchList.size() < limit - 1) {
				String match = input.subSequence(index, m.start()).toString();
				matchList.add(match);
				index = m.end();
			} else if (matchList.size() == limit - 1) { // last one
				String match = input.subSequence(index, input.length())
						.toString();
				matchList.add(match);
				index = m.end();
			}
		}

		// If no match was found, return this
		if (index == 0)
			return new String[] { input.toString() };

		// Add remaining segment
		if (!matchLimited || matchList.size() < limit)
			matchList.add(input.subSequence(index, input.length()).toString());

		// Construct result
		int resultSize = matchList.size();
		// 去掉的代码是同jdk原生split方法的不同之处，例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}，而用本函数则返回数组{"1","2","3","4","",""}
		// if (limit == 0)
		// while (resultSize > 0 && matchList.get(resultSize - 1).equals(""))
		// resultSize--;
		String[] result = new String[resultSize];
		return (String[]) matchList.subList(0, resultSize).toArray(result);
	}

	/**
	 * 判断一个字符串中是否包含一个子串,不区分大小写
	 * 
	 * @param str
	 *            String
	 * @param subString
	 *            String
	 * @return boolean
	 */
	public static boolean isIncludeSubString(String str, String subString) {
		boolean result = false;
		if (str == null || subString == null) {
			return false;
		}
		int strLength = str.length();
		int subStrLength = subString.length();
		String tmpStr = null;
		for (int i = 0; i < strLength; i++) {
			if (strLength - i < subStrLength) {
				return false;
			}
			tmpStr = str.substring(i, subStrLength + i);
			if (tmpStr.endsWith(subString)) {
				return true;
			}
		}
		return result;
	}

	/**
	 * /** 用户String提供的split方法会过滤掉后面的空字符串，所以提供本方法，严格按照分隔符个数形成数组
	 * 
	 * 例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}，
	 * 而用本函数则返回数组{"1","2","3","4","",""}
	 * 
	 * @param input
	 *            本分割的字符串或者StringBuffer对象
	 * @param delimiterPattern
	 *            分割字符
	 * @return
	 */
	public static String[] split(CharSequence input, String delimiterPattern) {
		return split(input, delimiterPattern, 0);
	}

	/**
	 * 将字符串str 按照length长度为一个字符串平均分割。 如果length<=0，则返回null。 wuwm
	 * 
	 * @param str
	 * @param length
	 * @return String[]
	 */
	public static String[] splitSameLegthArray(String str, int length) {
		if (length <= 0 || str == null) {
			return null;
		}
		String[] strArr = new String[str.length() / length + 1];

		char[] cStr = str.toCharArray();
		StringBuffer buf = new StringBuffer();
		int count = 0;
		int leg = 0;
		for (char s : cStr) {
			if (count != length) {
				buf.append(String.valueOf(s));
				count++;
			} else {
				strArr[leg++] = buf.toString();
				buf.delete(0, buf.length());
				buf.append(String.valueOf(s));
				count = 1;
			}
		}
		if (buf.length() != 0) {
			strArr[leg++] = buf.toString();
		}
		return strArr;
	}

	public static Properties toProperties(String str) {
		return toProperties(str, ",");
	}

	public static Properties toProperties(String str, String split) {
		Properties props = new Properties();
		if (!StringUtils.hasLength(str)) {
			return props;
		}
		String[] lines = str.split(split);
		for (String line : lines) {
			if (line == null) {
				return null;
			}
			line = StringUtils.trimLeadingWhitespace(line);
			if (line.length() > 0) {
				char firstChar = line.charAt(0);
				if (firstChar != '#' && firstChar != '!') {
					int separatorIndex = line.indexOf("=");
					if (separatorIndex == -1) {
						separatorIndex = line.indexOf(":");
					}
					String key = (separatorIndex != -1 ? line.substring(0,
							separatorIndex) : line);
					String value = (separatorIndex != -1) ? line
							.substring(separatorIndex + 1) : "";
					key = StringUtils.trimTrailingWhitespace(key);
					value = StringUtils.trimLeadingWhitespace(value);
					props.put(key, value);
				}
			}
		}
		return props;
	}

	public static String fillToFront(Object obj, int length, char fileStr) {

		StringBuffer buf = new StringBuffer(obj == null ? "" : obj.toString());
		if (buf.length() > length) {
			throw new RuntimeException(StringUtils.getText(
					"value length great maxLength:{0}", length));
		}
		for (int i = length - buf.length(); i > 0; i--) {
			buf.insert(0, fileStr);
		}
		return buf.toString();
	}

	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	/**
	 * 搜索一个字符串（sourcestr）中任意中的任意两个字符之间的内容 eg sourcestr="123{456}7{890} dlimStart="{" dlimEdn="}", 则返回数组｛"456","890"｝
	 * @param sourcestr
	 * @param dlimStart
	 * @param dlimEnd
	 * @return
	 */
	public static List<String> analyBracket(String sourcestr, String dlimStart,
			String dlimEnd) {
		List<String> list = new ArrayList() ;
		int begin = 0 ;
		int end = 0 ;
		while( (begin=sourcestr.indexOf(dlimStart))>=0){
			String temp = sourcestr.substring(begin+dlimStart.length()) ;
			end = temp.indexOf(dlimEnd) ;
			if(end<0)
				return list ;
			list.add(temp.substring(0,end)) ;
			sourcestr = temp.substring(end+dlimEnd.length()) ;
		}
		return list ;
	}

	public static String subString(String array,int start,int end){
    	int length = array.length();
    	if(start > length || end > length){
    		throw new RuntimeException(
    				"[NumberTooLarge] : String index out of range");
    	}
    	return array.substring(start,end);
    }
	
	/**
	 * 
	 * 字符串是否包含小写字母
	 * 
	 * true:包含
	 * false：不包含
	 * 
	 * @param s
	 * @return
	 */
	public static boolean containsLowerCase(String s) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isLowerCase(c)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 字符串是否包含大写字母
	 * 
	 * true:包含
	 * false：不包含
	 * 
	 * @param s
	 * @return
	 */
	public static boolean containsUpperCase(String s) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isUpperCase(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String... args) {
		// Properties p =
		// toProperties("mail.smtp.auth=sdds,mail.smtp.auth0=fasle");
		// System.out.println(p.getProperty("mail.smtp.auth"));
		// System.out.println(p.getProperty("mail.smtp.auth0"));
	}
}
