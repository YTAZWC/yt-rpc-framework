package top.ytazwc.remoting.transport.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import top.ytazwc.compress.Compress;
import top.ytazwc.enums.CompressType;
import top.ytazwc.enums.SerializationType;
import top.ytazwc.extension.ExtensionLoader;
import top.ytazwc.remoting.constants.RpcConstants;
import top.ytazwc.remoting.dto.RpcMessage;
import top.ytazwc.serializer.Serializer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  *   0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 *  *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 *  *   |   magic   code        |version | full length         | messageType| codec|compress|    RequestId       |
 *  *   +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
 *  *   |                                                                                                       |
 *  *   |                                         body                                                          |
 *  *   |                                                                                                       |
 *  *   |                                        ... ...                                                        |
 *  *   +-------------------------------------------------------------------------------------------------------+
 *  * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 *  * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
 * @author 花木凋零成兰
 * @title RpcMessageEncoder
 * @date 2024/6/16 16:18
 * @package top.ytazwc.remoting.transport.netty.codec
 * @description 自定义编码器；负责处理“出站”消息，将消息格式转换字节数组然后写入到字节数组容器对象中
 * // TODO 待学习理解
 */
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    /**
     * 使用AtomicInteger来实现线程安全的计数器。
     * AtomicInteger是一个提供原子操作的整数类，适用于需要高并发操作整数的场景。
     * 在这里，我们初始化为0，表示一个全局的计数器。
     */
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf out) {
        try {
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            out.writeByte(RpcConstants.VERSION);
            // leave a place to write the value of full length
            out.writerIndex(out.writerIndex() + 4);
            byte messageType = rpcMessage.getMessageType();
            out.writeByte(messageType);
            out.writeByte(rpcMessage.getCodec());
            out.writeByte(CompressType.GZIP.getCode());
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            // build full length
            byte[] bodyBytes = null;
            int fullLength = RpcConstants.HEAD_LENGTH;
            // if messageType is not heartbeat message,fullLength = head length + body length
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE
                    && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                // serialize the object
                String codecName = SerializationType.getName(rpcMessage.getCodec());
                log.info("codec name: [{}] ", codecName);
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                        .getExtension(codecName);
                bodyBytes = serializer.serialize(rpcMessage.getData());
                // compress the bytes
                String compressName = CompressType.getName(rpcMessage.getCompress());
                Compress compress = ExtensionLoader.getExtensionLoader(Compress.class)
                        .getExtension(compressName);
                bodyBytes = compress.compress(bodyBytes);
                fullLength += bodyBytes.length;
            }

            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
            int writeIndex = out.writerIndex();
            out.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            log.error("Encode request error!", e);
        }
    }
}
