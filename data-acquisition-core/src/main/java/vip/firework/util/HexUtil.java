package vip.firework.util;

import vip.firework.constants.RouterConstans;

import java.io.ByteArrayOutputStream;

/**
 * 16进制工具类
 *
 * @author yongjieshi1
 * @date 2019/3/25 2:08 PM
 */
public class HexUtil {
    // 转化字符串为十六进制编码
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4 + " ";
        }
        return str;
    }

    //16进制字符串转换成byte数组
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /** */
    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    // 转化十六进制编码为字符串
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
            }
        }
        try {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            s = "";
        }
        return s;
    }

    /*
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        //将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2) {
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        }
        return new String(baos.toByteArray());
    }

    /*
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String encode(String str) {
        //根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        //将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
//        System.out.println(encode("中文"));
//        System.out.println(decode(encode("中文")));
//
//        System.out.println(Utils.toHexString("1"));// 转化字符串为十六进制编码
//        System.out.println(Utils.toStringHex("31"));// 转化十六进制编码为字符串
//
//        System.out.println(Utils.toHexString("NAK"));
//        System.out.println(Utils.toHexString("ACK"));
//
        System.out.println(HexUtil.toHexString("&123456789140080060201606011002@"));
        ;
        byte[] source = {0x02, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x34, 0x30, 0x30, 0x38,
                0x30, 0x30, 0x36, 0x30, 0x32, 0x30, 0x31, 0x36, 0x30, 0x36, 0x30, 0x31, 0x31, 0x30, 0x30, 0x32};
//        System.out.println();
        System.out.println(HexUtil.toStringHex(HexUtil.bytesToHexString(source)));

//        System.out.println(Utils.toHexString("01303031303030315DC6"));
        System.out.println(HexUtil.toHexString("03 30 30 30 31 30 30 30 30 30 31 33 37 30 76 DE"));
//        System.out.println(Utils.toHexString(Constants.SPLIT_START+Constants.COMMOND_BOX_FIRST_BACK+"5001121545122515009915441225150102154212251501021541122515010015411225150101153712251501051536122515009915361225150126121212251501201138122515012911371225150136113612251500581927122115010419061221150042175412211501651749122115005116301221150119142812211501221210122115013011281221150136112812211501311127122115012711271221150097112612211500870928121115008409271211150089181812101500811817121015008118161210150082181612101500840933120215015310311117150129135911061501201357110615011013561106150184101711051501521028110315009815241031150098144010311501011013103115009809531031150099095310311501000952103115009409511031150093094910311501111014102915008619341028150089193110281500891802102815009120311027150;1>#"));
//        System.out.println(Utils.toHexString(Constants.SPLIT_START+Constants.COMMOND_BOX_FIRST_BACK+"5001121545122515009915441225150102154212251501021541122515010015411225150101153712251501051536122515009915361225150126121212251501201138122515012911371225150136113612251500581927122115010419061221150042175412211501651749122115005116301221150119142812211501221210122115013011281221150136112812211501311127122115012711271221150097112612211500870928121115008409271211150089181812101500811817121015008118161210150082181612101500840933120215015310311117150129135911061501201357110615011013561106150184101711051501521028110315009815241031150098144010311501011013103115009809531031150099095310311501000952103115009409511031150093094910311501111014102915008619341028150089193110281500891931102815008919311028150;1>@"));
//        System.out.println(Utils.toHexString(Constants.SPLIT_START+Constants.COMMOND_BOX_FIRST_BACK+"0925130369203367117008607190285694284600401231001220000000000000000000,\"0582,34,00,460,00,12,30fd,13,00,108a,2555309@"));
//        System.out.println(Utils.toHexString(Constants.SPLIT_START+Constants.COMMOND_BOX_FIRST_BACK+"1020000250507160319112202058050000000XGJ2132AR0021303692030<=9@"));
//        System.out.println(Utils.toHexString(Constants.SPLIT_START+Constants.COMMOND_BOX_FIRST_BACK+"12.23-P00000016013104071701701700CEMU212-M1038007103888831?1:;@"));
        System.out.println(HexUtil.toHexString(RouterConstans.SPLIT_START + RouterConstans.COMMOND_BOX_FIRST_BACK + "0400000000000000000000000000000000000000000000000111038888312<85@"));
    }

    /*
     * 16进制数字字符集
     */
    private static String hexString = "0123456789ABCDEF";


}
