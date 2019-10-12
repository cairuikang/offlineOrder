package com.jhlc.offlineorder.conmmon.utils;


import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by staconfree on 2017/4/2.
 */
public class CommonUtils {



    public static String getClientIp(HttpServletRequest request) {
        try {
            String ip = request.getHeader("x-forwarded-for");
            if((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
                ip = request.getRemoteAddr();
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("http_client_ip");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }

            if(ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress addr = null;
                try {
                    addr = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = addr.getHostAddress() == null ? "" : addr.getHostAddress().trim();
            }
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if(ip!=null && ip.length()>15){ //"***.***.***.***".length() = 15
                if(ip.indexOf(",")>0){
                    ip = ip.substring(0,ip.indexOf(","));
                }
            }
            return ip;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取服务器Ip
     * @return
     */
    public static String getServerIp() {
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements())
            {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements())
                {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address)
                    {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 检验一个字符串有否有值 ，无传false ,有传ture
     * @param str
     * @return
     */
    public static boolean strIsNull(String str){
        return str != null && !"null".equals(str) && !"".equals(str) && !"undefined".equals(str);
    }
    /**
     * 检验多个字符串有否有值 ，有一个无传false ,都有传ture
     * @param strs
     * @return
     */
    public static boolean strIsNulls(String... strs){
        for (String s : strs) {
            if(s==null||"null".equals(s)||"".equals(s)||"undefined".equals(s)){
                return false;
            }
        }
        return true;
    }
    /**
     * 检验list是否为空 ，有传ture
     * @param list
     * @return
     */
    public static boolean ListIsNull(List list){
        return list != null && list.size() != 0;
    }
    
    
    public static String getStr(String str){
    	if(strIsNull(str)){
    		return str;
    	}else{
    		return "";
    	}
    }




    static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

    //判断所有的参数有值，返回true
    public static boolean  checkParmByAll(Map parm,String... keys){
        boolean flag = true;
        for (String key : keys) {
            if(!CommonUtils.strIsNull(parm.get(key)+"")){
                flag = false;
                break;
            }
        }
        return flag;
    }
    public static boolean isInteger(String str) {
        return pattern.matcher(str).matches();
    }


    /**
     * 获取一个随驾数
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(str.length() - 1);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
