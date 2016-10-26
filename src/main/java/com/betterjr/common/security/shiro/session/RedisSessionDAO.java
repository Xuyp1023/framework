package com.betterjr.common.security.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.security.shiro.cache.RedisManager;
import com.betterjr.common.security.shiro.cache.SerializeUtils;

public class RedisSessionDAO extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
    /**
     * shiro-redis的session对象前缀
     */
    private RedisManager redisManager;

    /**
     * The Redis key prefix for the sessions
     */
    private String keyPrefix = "shiro_redis_session:";

    @Override
    public void update(final Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(final Session session) throws UnknownSessionException{
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }

        final byte[] key = getByteKey(session.getId());
        final byte[] value = SerializeUtils.serialize(session);
        session.setTimeout(redisManager.getExpire()*1000);
        this.redisManager.set(key, value, redisManager.getExpire());
    }

    @Override
    public void delete(final Session session) {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        redisManager.del(this.getByteKey(session.getId()));

    }

    @Override
    public Collection<Session> getActiveSessions() {
        final Set<Session> sessions = new HashSet<Session>();

        final Set<byte[]> keys = redisManager.keys(this.keyPrefix + "*");
        if(keys != null && keys.size()>0){
            for(final byte[] key:keys){
                final Session s = (Session)SerializeUtils.deserialize(redisManager.get(key));
                sessions.add(s);
            }
        }

        return sessions;
    }

    @Override
    protected Serializable doCreate(final Session session) {
        final Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    public Session doReadSession(final Serializable sessionId) {
        if(sessionId == null){
            logger.debug("session id is null");
            return null;
        }
        final byte[] sessionByte=redisManager.get(this.getByteKey(sessionId));
        final Session s = (Session)SerializeUtils.deserialize(sessionByte);
        return s;
    }

    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private byte[] getByteKey(final Serializable sessionId){
        final String preKey = this.keyPrefix + sessionId;
        return preKey.getBytes();
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(final RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    /**
     * Returns the Redis session keys
     * prefix.
     * @return The prefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * Sets the Redis sessions key
     * prefix.
     * @param keyPrefix The prefix
     */
    public void setKeyPrefix(final String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }


}
