package cn.com.szgao.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectUtils {
//	 public static boolean isFieldValueNull(Object obj) {
//	        boolean result = true;
//	        if (obj == null) {
//	            return true;
//	        }
//	        for (Field f : obj.getClass().getDeclaredFields()) {
//	            f.setAccessible(true);
//	            try {
//					if (f.get(obj) == null||f.get(obj) == ""||f.get(obj) == "null"||f.get(obj) == "NULL") { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
//						result = true;
//					}else{
//						return false;
//					}
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				}
//	        }
//	        return result;
//	    }
//	 
	 
	 public static boolean isFieldValueNull(Object bean) {
	        boolean result = true;
	        if (bean == null) {
	            return true;
	        }
	        Class<?> cls = bean.getClass();
	        Method[] methods = cls.getDeclaredMethods();
	        Field[] fields = cls.getDeclaredFields();
	        for (Field field : fields) {
	            try {
	                String fieldGetName = parGetName(field.getName());
	                if (!checkGetMet(methods, fieldGetName)) {
	                    continue;
	                }
	                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
	                Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});
	                if (fieldVal != null) {
	                	fieldVal=String.valueOf(fieldVal)  .replaceAll("[&nbsp;\r\t]", "").replace(" ", "");
	                    if ("".equals(fieldVal)||"null".equals(fieldVal)||"NULL".equals(fieldVal)) {
	                        result = true;
	                    } else {
	                    	return false;
//	                        result = false;
	                    }
	                }else{
	                	 result = true;
	                }
	            } catch (Exception e) {
	                continue;
	            }
	        }
	        return result;
	    }


	    /**
	     * 拼接某属性的 get方法
	     *
	     * @param fieldName
	     * @return String
	     */
	    public static String parGetName(String fieldName) {
	        if (null == fieldName || "".equals(fieldName)) {
	            return null;
	        }
	        int startIndex = 0;
	        if (fieldName.charAt(0) == '_')
	            startIndex = 1;
	        return "get"
	                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
	                + fieldName.substring(startIndex + 1);
	    }

	    /**
	     * 判断是否存在某属性的 get方法
	     *
	     * @param methods
	     * @param fieldGetMet
	     * @return boolean
	     */
	    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
	        for (Method met : methods) {
	            if (fieldGetMet.equals(met.getName())) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    
}
