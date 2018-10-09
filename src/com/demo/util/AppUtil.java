package com.demo.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;
import org.imgscalr.Scalr;

public class AppUtil {

	/**
	 * Convenience method to determine if a Object is null or blank For Integer
	 * objects -1 is considered to be blank
	 * 
	 * @param Object
	 * @return boolean
	 */
	public static boolean isBlank(Object o) {
		if (o == null)
			return true;
		else if (o instanceof String) {
			if (((String) o).trim().equals(""))
				return true;
		} else if (o instanceof Collection<?>) {
			if (((Collection<?>) o).isEmpty())
				return true;
		} else if (o instanceof Integer) {
			if (((Integer) o) <= 0)
				return true;
		} else if (o instanceof Long) {
			if (((Integer) o) <= 0)
				return true;
		} else if (o instanceof Map<?, ?>) {
			if (((Map<?, ?>) o).isEmpty())
				return true;
		}
		return false;
	}
	
	
	/**
	 * Convenience method to determine if a string is null or blank
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean isBlank(String value) {
		return (value == null || value.trim().length() == 0 || value.trim().equals("") || value.equals("null"));
	}
	
	public static String getBaseUrl(HttpServletRequest request) {
		String context = request.getContextPath(); // this will return context
		// path of URL.
		String schema = request.getScheme(); // will return http or https
		String host = request.getServerName(); // will return domain name e.g.
		// localhost
		int port = request.getServerPort(); // will return server port
		return schema + "://" + host + (port == 80 ? "" : ":" + port) + "" + context + "/";

	}
	
	public static int parseInt(Object o) {
		String str = o == null ? "0" : o.toString().trim();
		if (isBlank(str))
			return 0;
		else
			try {
				return (int) Float.parseFloat(str);
			} catch (Exception e) {
				// e.printStackTrace();
				return 0;
			}
	}

	public static byte parseByte(String str) {
		if (isBlank(str))
			return 0;
		else
			try {
				return Byte.parseByte(str);
			} catch (Exception e) {

				// e.printStackTrace();
				return 0;
			}
	}

	public static Boolean parseBoolean(String str) {
		if (AppUtil.parseInt(str) == 1)
			return true;
		if (isBlank(str))
			return false;
		else
			try {
				return Boolean.parseBoolean(str);
			} catch (Exception e) {
				// e.printStackTrace();
				return false;
			}
	}

	public static Boolean parseBoolean(Object str) {
		if (AppUtil.parseInt(str) == 1)
			return true;
		return parseBoolean(str == null ? null : str.toString());
	}

	public static short parseShort(String str) {
		if (isBlank(str))
			return 0;
		else
			try {
				return Short.parseShort(str);
			} catch (Exception e) {

				// e.printStackTrace();
				return 0;
			}
	}

	public static double parseAndRoundDouble(Object o) {
		double d = parseDouble(o);
		return round(d, 2);
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static double parseDouble(Object obj) {
		if (obj == null)
			return 0;
		else
			return parseDouble(obj.toString());
	}

	public static double parseDouble(String str) {
		if (isBlank(str))
			return 0d;
		else
			try {
				return Double.parseDouble(str);
			} catch (Exception e) {

				// e.printStackTrace();
				return 0d;
			}
	}

	public static long parseLong(String str) {
		if (isBlank(str))
			return 0;
		else
			try {
				return Long.parseLong(str);
			} catch (Exception e) {

				// e.printStackTrace();
				return 0;
			}
	}

	public static float parseFloat(String str) {
		if (isBlank(str))
			return 0f;
		else
			try {
				return Float.parseFloat(str);
			} catch (Exception e) {
				return 0f;
			}
	}
	
	/**
	 * Copies entire directory from one location to another
	 * 
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyDirectory(File sourceDir, File destDir) throws IOException {
		if (!destDir.exists()) {
			destDir.getParentFile().mkdirs();
			destDir.mkdir();
		}
		File[] children = sourceDir.listFiles();
		if (children != null)
			for (File sourceChild : children) {
				String name = sourceChild.getName();
				File destChild = new File(destDir, name);
				if (sourceChild.isDirectory()) {
					copyDirectory(sourceChild, destChild);
				} else {
					copyFile(sourceChild, destChild, false);
				}
			}
	}

	/**
	 * Copies single file from one location to another
	 * 
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFile(File source, File dest, boolean deleteSource) throws IOException {
		if (!dest.exists()) {
			dest.getParentFile().mkdirs();
			dest.createNewFile();
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} finally {
			in.close();
			out.close();
			if (deleteSource) {
				source.delete();
			}
		}

	}
	
	/**
	 * This method takes the Image object and creates BufferedImage of it and
	 * returns the buffered image object
	 * 
	 * @param img
	 * @return
	 */
	public static BufferedImage getBufferedImageFromImageFilePath(String filePath) throws Exception {
		Image img = Toolkit.getDefaultToolkit().createImage(filePath);
		// This line is important, this makes sure that the image is
		// loaded fully
		img = new ImageIcon(img).getImage();

		// Create the BufferedImage object with the width and height of the
		// Image
		BufferedImage bufferedImage = ImageIO.read(new File(filePath));
		// ImageIO.read(new File(filePath));
		// new BufferedImage(img.getWidth(null),img.getHeight(null),
		// BufferedImage.TYPE_INT_RGB);

		// Create the graphics object from the BufferedImage
		Graphics g = bufferedImage.createGraphics();

		// Draw the image on the graphics of the BufferedImage
		g.drawImage(img, 0, 0, null);

		// Dispose the Graphics
		g.dispose();

		// return the BufferedImage
		return bufferedImage;
	}
	
	/**
	 * Return the file extension by passing the file name
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static String getFileExtentionByName(String name) throws Exception {
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			return "";
		else
			return name.substring(dotIndex + 1, name.length());
	}

	
	/**
	 * Which scales the image file in the src location with the given width and
	 * height and saves in the destination location Here we used
	 * imgscalr-lib-3.2.jar to do this
	 * 
	 * @param srcImagePath
	 * @param dstImagePath
	 * @param paramHeight
	 * @param paramWidth
	 * @throws Exception
	 */
	public static void resizeImage(String srcImagePath, String dstImagePath, String paramHeight, String paramWidth)
			throws Exception {
		if (AppUtil.isBlank(srcImagePath) || AppUtil.isBlank(dstImagePath)) {
			return;
		}
		File srcFile = new File(srcImagePath);
		if (!srcFile.exists()) {
			return;
		}
		File file = new File(dstImagePath);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		int height = AppUtil.parseInt(paramHeight);
		int width = AppUtil.parseInt(paramWidth);
		BufferedImage bfi = getBufferedImageFromImageFilePath(srcImagePath);
		BufferedImage buffImage = Scalr.resize(bfi, height, width, new BufferedImageOp[] {});
		ImageIO.write(buffImage, getFileExtentionByName(srcImagePath), file);
	}
	
	/**
	 * Trim String and handle if null
	 * 
	 * @param String
	 *            value to trim
	 * @return String
	 */
	public static String trim(String value) {
		return trim(value, "");
	}

	/**
	 * Trim String and handle if null
	 * 
	 * @param String
	 *            value to trim
	 * @return String
	 */
	public static String htmlTrim(String value) {
		return trim(value, "&nbsp");
	}

	/**
	 * Trim String and handle if null
	 * 
	 * @param String
	 *            value to trim
	 * @param String
	 *            - default value if the value is null
	 * @return String
	 */
	public static String trim(String value, String defaultString) {
		return ((value == null || value.trim().length() == 0) ? defaultString : value.trim());
	}
	
	public static Date getDateFromString(String strDate, String formate) throws ParseException {
		// System.out.println(strDate);
		// System.out.println(formate);
		if (strDate == null || isBlank(strDate))
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(formate);
		return formatter.parse(AppUtil.trim(strDate));
	}

	public static Date getTimeFromString(String strDate, String formate) throws ParseException {
		if (strDate == null || isBlank(strDate))
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(formate);
		return formatter.parse(AppUtil.trim(strDate));
	}
	
	public static String truncate(String s, int...len) {
		if(len == null || len.length == 0)len = new int[] {1000};
		return s == null ? null : (s.length() <= len[0] ? s : s.substring(0, len[0]));
	}
	
	/**
	 * Converts the given input string to TitleCase, which is to remove underscores and replace it with spaces
	 * 	and Capitalize the first letter of all words.
	 *
	 * @param input
	 * @return String, Title case of the given input
	 * @since 2018-07-20
	 */
	public static String titleCase(String input) {
		if(input==null) return null;
		if(input.trim().equals("")) return "";
		input=input.replace("_", " ").toLowerCase();
		String result = "";
        char firstChar = input.charAt(0);
        result = result + Character.toUpperCase(firstChar);
        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            char previousChar = input.charAt(i - 1);
            if (previousChar == ' ') {
                result = result + Character.toUpperCase(currentChar);
            } else {
                result = result + currentChar;
            }
        }
		return result;
	}
	
	/**
	 * Converts the given String to pascalCase
	 * 
	 * @param input
	 * @return String, Pascal case of the given input
	 * @since 2018-07-20
	 */
	public static String pascalCase(String input) {
		if(input==null) return null;
		if(input.trim().equals("")) return "";
		input=titleCase(input);
		input=input.replaceAll("\\s+", "");
		char c=(input.charAt(0)+"").toLowerCase().charAt(0);
		return c+input.substring(1);
	}
	
	
	public static String toSentenceCase(String input) {
		if(input==null) return "";
		input=input.trim();
	    StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;
	    boolean isDot=false;
	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	        	nextTitleCase = isDot;	
	        } else if (c=='.') {
	            nextTitleCase = isDot = true;
	        } else if (nextTitleCase) {
	            c = Character.toUpperCase(c);
	            nextTitleCase = isDot = false;
	        }else {
	        	c=Character.toLowerCase(c);
	        	isDot=false;
	        }
	        titleCase.append(c);
	    }
	    return titleCase.toString();
	}
	
	
	/**
	 * Creates a file if it does not exists in the given directory
	 * 
	 * @param path
	 * @return File
	 * @throws Exception
	 * @author Jagadeesh.T
	 * @since 2018-07-20
	 */
	public static File saveFileIfNotExist(String path) throws Exception {
		if (isBlank(path)) {
			return null;
		}
		File file = new File(path);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return file;
	}
	
	/**
     * Method to format bytes in human readable format
     * 
     * @param bytes
     *            - the value in bytes
     * @param digits
     *            - number of decimals to be displayed
     * @return human readable format string
     */
    public static String getFileSizeInReadable(double bytes, int digits) {
        String[] dictionary = { "bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
        int index = 0;
        for (index = 0; index < dictionary.length; index++) {
            if (bytes < 1024) {
                break;
            }
            bytes = bytes / 1024;
        }
        return String.format("%." + digits + "f", bytes) + " " + dictionary[index];
    }
    
	/**
	 * This method takes String as input and gives UTC formatted date in return
	 * 
	 * @param date
	 * @return UTC formatted date
	 * @throws Exception when the given input String is in not parsable to UTC date then it will through exception
	 * @author Jagadeesh.T
	 * @since 2018-07-20 
	 */
	public static Date getDateFromUtcString(String date)throws Exception {
		if(date==null) return null;
		try {
			return Date.from(Instant.parse(date));
		}catch (Exception e) {
			throw new Exception("Unable to parse given utc Date ... : "+date);
		}
	}
    
    
	public static void main(String[] args) {
		System.out.println(toSentenceCase("praneeth kumar reddy"));
	}
}
