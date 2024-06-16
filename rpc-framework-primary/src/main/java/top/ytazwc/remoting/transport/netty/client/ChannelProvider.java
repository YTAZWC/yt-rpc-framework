package top.ytazwc.remoting.transport.netty.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 花木凋零成兰
 * @title ChannelProvider
 * @date 2024/6/15 16:05
 * @package top.ytazwc.remoting.transport.netty.client
 * @description 存储和获取 Channel 对象
 */
@Slf4j
public class ChannelProvider {
    // 缓存Channel对象
    private final Map<String, Channel> channelMap;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
    }

    /**
     * 根据连接地址从缓存中获取连接通道
     * @param inetSocketAddress 连接地址
     * @return  连接通道
     */
    public Channel get(InetSocketAddress inetSocketAddress) {
        // 根据套接字地址来获取对应的 Channel
        String key = inetSocketAddress.toString();
        // 确定是否存在相应地址的连接
        if (channelMap.containsKey(key)) {
           Channel channel = channelMap.get(key);
           // 确定连接是否可用
           if (channel != null && channel.isActive()) {
               return channel;
           } else {
               channelMap.remove(key);
           }
        }
        return null;
    }

    /**
     * 缓存连接地址对应的通道
     * @param inetSocketAddress 连接地址
     * @param channel 对应通道
     */
    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        channelMap.put(key, channel);
    }

    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
        log.info("Channel map size: [{}]", channelMap.size());
    }

}
