package top.ytazwc.remoting.constants;

/**
 * @author 花木凋零成兰
 * @title RpcConstants
 * @date 2024/6/15 16:46
 * @package top.ytazwc.remoting.constants
 * @description 环境变量
 */
public class RpcConstants {

    /**
     * 魔法数：一般为4个字节，用于筛选来到服务端的数据包，用于判断是否为有效数据包
     */
    public static final byte[] MAGIC_NUMBER = {(byte) 'g', (byte) 'r', (byte) 'p', (byte) 'c'};

    // 版本信息
    public static final byte VERSION = 1;
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    /**
     * 版本信息-总长度
     */
    public static final byte TOTAL_LENGTH = 16;

    // 检测信号请求类型-请求
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    // 检测信号请求类型-响应
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;

    public static final String PING = "ping";
    public static final String PONG = "pong";

    /**
     * 最大帧长度
     */
    public static final int MAX_FRANE_LENGTH = 8 * 1024 * 1024;

    /**
     * 消息头长度
     */
    public static final int HEAD_LENGTH = 16;

}
