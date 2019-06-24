package com.nsn.dubbo.dubboinvoker.web.lock;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述:
 *
 * @author donghao
 * @date 2019-06-06 15:46
 */
@Data
@Component
public class Lock {

    private ReentrantLock consumerLock = new ReentrantLock();
    private ReentrantLock configLock = new ReentrantLock();
}
