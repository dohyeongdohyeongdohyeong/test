/*
 * Copyright 2009 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



/**
 * Shared String utility methods.
 */
public class StringUtil {

  /**
   * Determined a short name from a class.
   * @param clazz the Java class
   * @return a short name for the class
   */
  public static String getShortTypeName(Class<?> clazz) {
    String typeName = clazz.getName();
    return typeName.substring(typeName.lastIndexOf('.') + 1);
  }

  /**
   * Return short classname of <code>obj</code>.
   * 
   * @param obj the object whose name is to be determined
   * @return the short class name
   */
  public static String getShortTypeName(Object obj) {
    String typeName = obj.getClass().getName();
    return typeName.substring(typeName.lastIndexOf('.') + 1);
  }
  
  public static String replaceAll(String text, String oldChar, String newChar) {
      String newText = text;
      if (text != null) {
          StringBuffer sb = new StringBuffer((int)(text.length() * 1.5));
          int index = 0;
          while ( (index = text.indexOf(oldChar)) > -1) {
              sb.append(text.substring(0, index));
              sb.append(newChar);
              if (index + oldChar.length() < text.length()) {
                  text = text.substring(index + oldChar.length());    
              } else {
                  text = "";
                  break;
              }
          }
          sb.append(text);
          newText = sb.toString();
          
      }
      return newText;
  }

	public static String crop(String source, int length, String tail, String charset) throws UnsupportedEncodingException {
		if (source == null) return null;
		String result = source;
		int sLength = 0;
		int bLength = 0;
		char c;
		
		if ( result.getBytes(charset).length > length) {
		    length = length - tail.length();
			while ( (bLength + 1) <= length) {
				c = result.charAt(sLength);
				bLength++;
				sLength++;
				if (c > 127) bLength++;
			}
			result = result.substring(0, sLength) + tail;
		}
		return result;
	}

	public static String crop(String source, int length, String tail) {
		if (source == null) return null;
		
		String result = source;
		int sLength = 0;
		int bLength = 0;
		char c;
		
		if ( result.getBytes().length > length) {
		    length = length - tail.length();
			while ( (bLength + 1)  <= length) {
				c = result.charAt(sLength);
				bLength++;
				sLength++;
				if (c > 127) bLength++;
			}
			result = result.substring(0, sLength) + tail;
		}
		return result;
		
	}
	
	public static String htmlCrop(String str, int length, String tail) {
		StringBuffer result = new StringBuffer();

		int remain = length;
		int position = 0;
		int bIndex = -1;
		int eIndex = -1;
		String temp = str;
		
		while (position < temp.length()) {
			bIndex = temp.indexOf("<");
			eIndex = temp.indexOf(">", bIndex);
			
			if (bIndex > -1) {
				if (remain > 0) {
					result.append(crop(temp.substring(0, bIndex), remain, tail));
				}
				if (bIndex < eIndex) {
					position = eIndex + 1;
					result.append(temp.substring(bIndex, position));
					temp = temp.substring(position);
				} else {
					if (remain > 0) {
						result.append(crop(temp, remain, tail));
						temp = "";
					}
				}	
			} else {
				if (remain > 0) {
					result.append(crop(temp, remain, tail));
						temp = "";
				}
				position = temp.length();
			}
		}

		if (remain > 0) {
			result.append(crop(temp, remain, tail));
		}
		
		return result.toString();
	}

	
	public static boolean isNull(String value) {
		if(value != null && !value.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isEmpty(String value) {
		if(value != null && !value.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isEmpty(Object obj) {
		if(obj != null){
			String value = obj.toString();
			if(value != null && !value.trim().equals("")) {
				return false;
			} else {
				return true;
			}
		}else{
			return true;
		}
	}
	
	public static Object defaultSet(Object obj, Object value) {
		if(isEmpty(obj)){
			obj = value;
		}
		return obj;
	}
	
	
	public static String makeRandomString(int strLength)
	{

		String result = "";
		String letter[] = {"A","B","C","D","E","F","G","H","I","J","K","M","N","P","Q","R","S","T","U","V","W","X","Y","2","3","4","5","6","7","8","9"};
		Random rn = new Random();
		
		for(int i=0; i<strLength; i++)
		{
			result = result + letter[rn.nextInt(letter.length)];
		}
		
		return result;
	}
	
	/**
	 * string split, godbasic[2010.03.09]
	 * @param sourceString
	 * @param token
	 * @return
	 */
	public static String[] split( String sourceString, String token) {
		String[] strData = sourceString.split("[" + token + "]");
		return strData;
	}
	
	public static String[] toStringArray(String str, String delimiter) {
        String[] result = null;
        if (str != null) {
            TokenizerUtils t = new TokenizerUtils(str, delimiter);
            result = new String[t.countTokens()];
            for( int i = 0; t.hasMoreTokens(); i++) {
                result[i] = t.nextToken();
            }            
        }
		return result;
	}

	
	/**
	 * 문자열 길이 체크
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean checkLength(String str, int length){
	   if(isEmpty(str)) return false;
	   if(str.length() ==length ){
		   return true;
	   }else{
		   return false;
	   }
	}
	
	 /**
	  * String을 int형으로
	  * @param value
	  * @return
	  */
	 public static int parseInt(String value) {
	  return parseInt(value, 0);
	 }
	 /**
	  * Object를 int형으로
	  * defaultValue는 0이다.
	  * @param value
	  * @return
	  */
	 public static int parseInt(Object value) {
	  String valueStr = replaceNull(value);
	  return parseInt(valueStr, 0);
	 }
	 /**
	  * Object를 int형으로
	  * Object가 null이면 defaultValue return
	  * @param value
	  * @param defaultValue
	  * @return
	  */
	 public static int parseInt(Object value, int defaultValue) {
	  String valueStr = replaceNull(value);
	  return parseInt(valueStr, defaultValue);
	 }
	 /**
	  * String을 int형으로
	  * String이 숫자 형식이 아니면 defaultValue return
	  * @param value
	  * @param defaultValue
	  * @return
	  */
	 public static int parseInt(String value, int defaultValue) {
	  int returnValue = 0;
	  if( isNull(value) ) {
	   returnValue = defaultValue;
	  } else if( !isNumeric(value) ) {
	   returnValue = defaultValue;
	  } else {
	   returnValue = Integer.parseInt(value);
	  }
	  return returnValue;
	 }
	 /**
	  * String을 long형으로
	  * defaultValue는 0이다.
	  * @param value
	  * @return
	  */
	 public static long parseLong(String value) {
	  return parseLong(value, 0);
	 }
	 
	 /**
	  * String을 long형으로
	  * 잘못된 데이타 일시 return은 defaultValue
	  * @param value
	  * @return
	  */
	 public static long parseLong(String value, long defaultValue) {
	  long returnValue = 0;
	  if( isNull(value) ) {
	   returnValue = defaultValue;
	  } else if( !isNumeric(value) ) {
	   returnValue = defaultValue;
	  } else {
	   returnValue = Long.parseLong(value);
	  }
	  return returnValue;
	 }
	 
	 public static String replaceNull(String value) {
		  return replaceNull(value, "");
		 }
		 /**
		  * Object를 받아서 String 형이 아니거나 NULL이면 ""를 return
		  * String 형이면 형 변환해서 넘겨준다.
		  * @param value
		  * @return
		  */
		 public static String replaceNull(Object value) {
		  Object rtnValue = value;
		  if( rtnValue == null || !"java.lang.String".equals(rtnValue.getClass().getName())) {
		   rtnValue = "";
		  }
		  return replaceNull((String)rtnValue, "");
		 }
		 /**
		  * 파라미터로 넘어온 값이 null 이거나 공백이 포함된 문자라면
		  * defaultValue를 return
		  * 아니면 값을 trim해서 넘겨준다.
		  * @param value
		  * @param repStr
		  * @return
		  */
		 public static String replaceNull(String value, String defaultValue) {
		  if (isNull(value)) {
		   return defaultValue;
		  }
		  return value.trim();
		 }
		 /**
		  * Object를 받아서 String 형이 아니거나 NULL이면 defaultValue를 return
		  * String 형이면 형 변환해서 넘겨준다.
		  * @param value
		  * @param repStr
		  * @return
		  */
		 public static String replaceNull(Object value, String defaultValue) {
		  String valueStr = replaceNull(value);
		  if ( isNull(valueStr) ) {
		   return defaultValue;
		  }
		  return valueStr.trim();
		 }
	
		 
		 public static boolean isNumeric(String value) {
			 return true;
		}
  
}
