package com.imooc.pan.lock.core.key;

import com.imooc.pan.lock.core.LockContext;

/**
 * key generator interface for the lock
 */
public interface KeyGenerator {

    /**
     * generate key
     *
     * @param lockContext
     * @return
     */
    String generateKey(LockContext lockContext);

}
