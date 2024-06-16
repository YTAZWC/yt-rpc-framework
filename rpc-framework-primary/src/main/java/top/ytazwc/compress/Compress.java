package top.ytazwc.compress;

import top.ytazwc.extension.SPI;

/**
 * @author 花木凋零成兰
 * @title Compress
 * @date 2024/6/16 16:44
 * @package top.ytazwc.compress
 * @description 压缩接口
 */
@SPI
public interface Compress {

    // 压缩
    byte[] compress(byte[] bytes);

    // 解压缩
    byte[] decompress(byte[] bytes);

}
