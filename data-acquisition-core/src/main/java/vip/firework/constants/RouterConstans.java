package vip.firework.constants;

/**
 * 路由相关
 *
 * @author yongjieshi1
 * @date 2019/3/25 2:34 PM
 */
public interface RouterConstans {
    static byte[] SPILT_STATR_BYTE = {0x26};

    static byte[] SPILT_ENDBYTE = {0x40};

    static byte[] COMMOND_BOX_INFO_BACK_BYTE = {0x03};

    static byte[] COMMOND_BOX_INFO_FIRST_BYTE = {0x02};
    /**
     * 开始分隔符
     */
    static final String SPLIT_START = new String(SPILT_STATR_BYTE);
    /**
     * 结束分隔符
     */
    static final String SPLIT_END = new String(SPILT_ENDBYTE);


    static final String COMMOND_BOX_INFO_BACK = new String(COMMOND_BOX_INFO_BACK_BYTE);

    static final String COMMOND_BOX_FIRST_BACK = new String(COMMOND_BOX_INFO_FIRST_BYTE);
    /**
     * 正确
     */
    static final String COMMOND_BOX_OK = "ACK";
    /**
     * 错误
     */
    static final String COMMOND_BOX_ERROR = "NAK";
    /**
     * 默认ID
     */
    static final String DEFALUT_ID = "0";

}
