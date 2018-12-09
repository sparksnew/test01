package com.oristartech.cinema.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class RequestToBeanUtil {
	 public static Object turn(HttpServletRequest req,Class cls){  
	        /** 
	         * turn方法的核心思想是找相同：req和cls一定有相同的部分。因为要将req的参数值和cls的属性值进行自动设置 
	         * request中的参数名称和class中的属性名称一样 
	         * 接下来可以获取所有的request请求参数和值。 再接下来将request请求中获取到的参数，和class中的属性对应上，并且 
	         * 将request取到的值设置到class中对应的属性上即可 
	         */  
	        Object obj=null;  
	        try {  
	            obj=cls.newInstance();  
	            //获取表单中所有的参数和参数对应的值  
	            //并不是获取类中的所有属性，因为类中属性有的表单中的参数不一定都有  
	            //获取所有显示提交的参数名  
	            Enumeration<String> ens=req.getParameterNames();  
	            while(ens.hasMoreElements()){  
	                String key=ens.nextElement();  
	                String val=req.getParameter(key);  
	                //查找class中对应的setxxx方法  
	                Method method[]=cls.getDeclaredMethods();  
	                for(Method m:method){  
	                    //忽略大小写进行匹配  
	                    if(m.getName().equalsIgnoreCase("set"+key)){  
	                        Class pc[]=m.getParameterTypes();  
	                        if(pc[0].getName().equalsIgnoreCase("float")){  
	                            m.invoke(obj, Float.parseFloat(val));  
	                        }else if(pc[0].getName().equalsIgnoreCase("int")){  
	                            m.invoke(obj, Integer.parseInt(val));  
	                        }else {  
	                            m.invoke(obj,val);  
	                        }  
	                    }  
	                }  
	            }  
	        } catch (InstantiationException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IllegalAccessException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (NumberFormatException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IllegalArgumentException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        return obj;  
	    }  
}
