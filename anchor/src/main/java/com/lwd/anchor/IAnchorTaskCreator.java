package com.lwd.anchor;

/**
 * @author: liweidong
 * @data: 2023/9/26
 */
public interface IAnchorTaskCreator {

    /**
     * 根据task名称，创建爱你task实例，这个接口需要使用者自己实现，创建后的实例会缓存起来
     * @param taskName
     * @return
     */
    AnchorTask createTask(String taskName);

}
