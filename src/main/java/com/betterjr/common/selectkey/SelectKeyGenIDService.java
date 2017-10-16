package com.betterjr.common.selectkey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.JedisUtils;
import com.betterjr.mapper.entity.Example;
import com.betterjr.modules.sys.dao.SnoGeneralInfoMapper;
import com.betterjr.modules.sys.entity.SnoGeneralInfo;

/**
 * 自动产生自增时序的ID值
 *
 * @author zhoucy
 *
 */

public class SelectKeyGenIDService extends Thread {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private static Map<String, SnoGeneralInfo> dataMap = new ConcurrentHashMap();
    private static Object obj = new Object();
    private static ArrayBlockingQueue abq = new ArrayBlockingQueue(10000);

    private static final String DefaultIdRedisKeyPrefix = "betterjr.id.";
    private static final long IdGap = 10;

    public static final String DEF_DAY_PREX = "LIMIT_";

    @Autowired
    private SqlSessionFactoryBean sqlSessionFactory;
    private int checkTimeOut = 10;

    public int getCheckTimeOut() {
        return checkTimeOut;
    }

    public void setCheckTimeOut(final int anCheckTimeOut) {
        checkTimeOut = anCheckTimeOut;
    }

    public SelectKeyGenIDService() {

    }

    public void init() {
        System.out.println("this is init KeyGenService");

        SqlSession sqlSession = null;
        final Map<String, SnoGeneralInfo> tmpMap = new ConcurrentHashMap();
        try {
            sqlSession = sqlSessionFactory.getObject().openSession();
            final SnoGeneralInfoMapper snoInfoMapper = sqlSession.getMapper(SnoGeneralInfoMapper.class);
            final Example exa = new Example(SnoGeneralInfo.class);
            final List<SnoGeneralInfo> snoList = snoInfoMapper.selectByExample(exa);
            for (final SnoGeneralInfo snoInfo : snoList) {
                tmpMap.put(snoInfo.getOperType(), snoInfo);
                snoInfo.updateOldNo(snoInfo.getLastNo());
                // System.out.println("对象: " + snoInfo);
            }
            // System.out.println("数据量是: " + tmpMap.size());
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }

        synchronized (dataMap) {
            dataMap = tmpMap;
            // init redis
            this.initRedis(tmpMap);
        }
        // 初始化完毕后，启动服务
        this.start();
    }

    /**
     * 检查数据重新加载
     *
     * @param 最后检查时间
     * @return 检查完毕的时间
     * @throws Exception
     */
    private long checkReload(long anLast) throws Exception {
        if ((anLast + this.checkTimeOut * 1000) < System.currentTimeMillis()) {
            SqlSession sqlSession = null;
            sqlSession = sqlSessionFactory.getObject().openSession();
            final SnoGeneralInfoMapper snoInfoMapper = sqlSession.getMapper(SnoGeneralInfoMapper.class);
            final Example exa = new Example(SnoGeneralInfo.class);
            final List<SnoGeneralInfo> snoList = snoInfoMapper.selectByExample(exa);
            final Map<String, SnoGeneralInfo> tmpMap = dataMap;
            for (final SnoGeneralInfo snoInfo : snoList) {
                final SnoGeneralInfo oldSnoInfo = tmpMap.get(snoInfo.getOperType());

                // 存在就检查更新，不存在就加入
                if (oldSnoInfo != null) {
                    oldSnoInfo.updateLastNo(snoInfo.getLastNo());
                } else {
                    dataMap.put(snoInfo.getOperType(), snoInfo);
                }
            }
            anLast = System.currentTimeMillis();
            sqlSession.close();
        }
        return anLast;
    }

    @Override
    public void run() {
        SqlSession sqlSession = null;
        boolean hasError = false;
        long lastTime = System.currentTimeMillis();
        while (true) {
            try {
                // System.out.println("waiting for data");
                if (hasError == false) {
                    abq.take();
                }
                hasError = false;

                lastTime = checkReload(lastTime);

                final Map<String, SnoGeneralInfo> tmpMap = dataMap;
                final List<SnoGeneralInfo> workList = new ArrayList();

                // 获取需要更新的列表
                for (final Map.Entry<String, SnoGeneralInfo> ent : tmpMap.entrySet()) {
                    final SnoGeneralInfo snoInfo = ent.getValue();
                    if (snoInfo.hasSave()) {
                        workList.add(snoInfo.clone());
                    }
                }

                if (workList.size() > 0) {
                    sqlSession = sqlSessionFactory.getObject().openSession();
                    // sqlSession = tmpFactory.openSession();
                    final SnoGeneralInfoMapper snoInfoMapper = sqlSession.getMapper(SnoGeneralInfoMapper.class);

                    // 更新列表
                    for (final SnoGeneralInfo snoInfo : workList) {
                        final int xx = snoInfoMapper.updateSimple(snoInfo);
                        // System.out.println("this is update count :" + xx);
                    }
                    sqlSession.commit();
                    sqlSession.close();
                    // 更新旧的序号
                    for (final SnoGeneralInfo snoInfo : workList) {
                        final SnoGeneralInfo tmpSnoInfo = tmpMap.get(snoInfo.getOperType());
                        if (tmpSnoInfo != null) {
                            tmpSnoInfo.updateOldNo(snoInfo.getLastNo());
                        }
                    }
                }
            }
            catch (final Exception e1) {
                hasError = true;
                try {
                    Thread.sleep(5000);
                    e1.printStackTrace();
                    if (sqlSession != null) {

                        sqlSession.rollback();
                        sqlSession.close();

                    }
                }
                catch (final Exception e) {

                }
            }
        }
    }

    public long getLongValue(final String anKey) {
        final SnoGeneralInfo snoInfo = dataMap.get(anKey);
        if (snoInfo != null) {
            synchronized (snoInfo) {
                if (abq.size() < 1000) {
                    try {
                        abq.put(obj);
                    }
                    catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return this.incrby(snoInfo);
            }
        }

        return 0;
    }

    private String buildKey(final String anKey) {
        if (StringUtils.isBlank(anKey)) {
            return anKey;
        }
        return DefaultIdRedisKeyPrefix + anKey;
    }

    private void initRedis(final Map<String, SnoGeneralInfo> anMap) {
        if (anMap == null) {
            return;
        }
        for (final SnoGeneralInfo info : anMap.values()) {
            final String type = info.getOperType();
            final long no = info.getLastNo();

            final String key = this.buildKey(type);
            final long newid = JedisUtils.checkBigThanAndSet(key, no, IdGap, 0);
            info.updateLastNo(newid);
        }

    }

    /**
     * 1.利用redis 自增1 2.如果内部buffer的id> redis的返回值，为保证id的升序，设置内部id自增到redis 3.回写最终写入redis的值到内部buffer
     *
     * @param anInfo
     * @return
     */
    private long incrby(final SnoGeneralInfo anInfo) {
        final String type = anInfo.getOperType();

        final String key = this.buildKey(type);
        long newid = JedisUtils.incrby(key, 1);

        if (anInfo.getLastNo() >= newid) {
            anInfo.addValue();
            newid = JedisUtils.checkBigThanAndSet(key, anInfo.getLastNo(), IdGap, 0);
        }

        anInfo.updateLastNo(newid);
        return newid;
    }

    public String findAppNoWithDay(final String anWorkType) {
        final String tmpWorkType = DEF_DAY_PREX.concat(anWorkType);
        final SnoGeneralInfo snoInfo = dataMap.get(tmpWorkType);
        if (snoInfo != null) {
            synchronized (snoInfo) {
                if (abq.size() < 1000) {
                    try {
                        abq.put(obj);
                    }
                    catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                final long tmpValue = incrby(snoInfo);

                return snoInfo.findMachValue()
                        .concat(StringUtils.leftPad(Long.toString(tmpValue), snoInfo.getDataLen(), "0"));
            }
        }
        logger.warn("not find anWorkType = " + tmpWorkType);
        return "";
    }
}
