package com.bonaparte.util;

import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangmingquan on 2018/10/11.
 */
public class StringTools {

    /**
     * @Description
     *
     * @param name
     * @param extra
     *
     * @return boolean
     * @version        1.0
     */
    public static boolean checkSpecialChar(String name, String extra) {
        if (name == null) {
            return false;
        }

        String regEx = "[^\\p{L}\\p{Nd}" + extra + "]";
        Pattern p     = Pattern.compile(regEx);
        Matcher m     = p.matcher(name.toLowerCase());

        return m.find();
    }

    /**
     * 字符解码
     *
     * @param namelist
     * @return
     */
    public static String listToString(List<String> namelist) {
        if (namelist.size() <= 0) {
            return null;
        }

        // 对集合进行排序
        Collections.sort(namelist,
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });

        // 规则化歌手字符串
        StringBuffer sb = new StringBuffer();

        for (String artist : namelist) {
            sb.append(artist + ";");
        }

        return sb.substring(0, sb.lastIndexOf(";"));
    }


    /**
     * 字符解码
     * @param str   原始字符串
     * @return
     */
    public static String stringDecode(String str) {
        if (str == null) {
            return null;
        }

        str = org.springframework.util.StringUtils.trimWhitespace(str);
        str = HtmlUtils.htmlUnescape(str);

        return str;
    }

    /**
     * @Description
     *
     * @param str
     *
     * @return String
     * @version        1.0
     */
    public static String toSearchUnicode(String str) {
        String result = "";

        str = str.toLowerCase();

        for (int i = 0; i < str.length(); i++) {
            char sChar = str.charAt(i);

            if (checkSpecialChar(String.valueOf(sChar), "")) {
                result += String.valueOf(sChar);
            } else {
                int    chr1     = (char) sChar;
                String hexStr   = Integer.toHexString(chr1);
                String startStr = "";

                for (int j = 0; j < (4 - hexStr.length()); j++) {
                    startStr += "0";
                }

                result += "u" + startStr + hexStr;
            }
        }

        return result;
    }

    /**
     * 删除所有特殊字符
     * @param name   原始字符串
     * @param extra   指定不删除字符串
     * @return
     */
    public static String trimSpecialChar(String name, String extra) {
        if (name == null) {
            return null;
        }

        String regEx = "[^\\p{L}\\p{Nd}" + extra + "]";
        Pattern p     = Pattern.compile(regEx);
        Matcher m     = p.matcher(name.toLowerCase());
        String re    = m.replaceAll("").trim();

        if (re.equals("")) {
            return name;
        }

        return re;
    }

    public static String spliceUrl(String url, Map<String, String> params){
        if(params == null){
            return url;
        }
        url += "?";
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry)iter.next();
            String value = entry.getValue();
            url += entry.getKey() + "=" + value + "&";
        }
        url = url.substring(0, url.length() - 1);
        System.out.println("远程请求地址为"+url);
        return url;
    }
}
