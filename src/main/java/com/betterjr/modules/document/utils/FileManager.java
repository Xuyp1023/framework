package com.betterjr.modules.document.utils;

import java.io.InputStream;

public interface FileManager {

    /**
     * 根据文件路径，保存数据流到存储上
     * @param anFilePath 文件路径
     * @param anIn 输入数据流
     */
    public boolean save(String anFilePath, InputStream anIn);

    /**
     * 从存储上加载数据流
     * @param anFilePath 文件路径
     * @return
     */
    public InputStream load(String anFilePath);

    /**
     * 检查文件是否在存储上存在
     * @param anFilePath 文件路径
     * @return
     */
    public boolean exists(String anFilePath);

    /**
     * 查找文件大小
     * @param anFilePath
     * @return
     */
    public long findSize(String anFilePath);

}
