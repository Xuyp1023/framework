package com.betterjr.common.selectkey;

import java.util.List;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.security.shiro.cache.RedisManager;
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

	private static Map<String, SnoGeneralInfo> dataMap = new ConcurrentHashMap();
	private static Object obj = new Object();
	private static ArrayBlockingQueue abq = new ArrayBlockingQueue(10000);

	private static final String DefaultIdRedisKeyPrefix="betterjr.id.";
	
	@Autowired
	private RedisManager redis;
	
	@Autowired
	private SqlSessionFactoryBean sqlSessionFactory;
	private int checkTimeOut = 10;

	public int getCheckTimeOut() {
		return checkTimeOut;
	}

	public void setCheckTimeOut(int anCheckTimeOut) {
		checkTimeOut = anCheckTimeOut;
	}

	public SelectKeyGenIDService() {

	}

	public void init() {
		System.out.println("this is init KeyGenService");

		SqlSession sqlSession = null;
		Map<String, SnoGeneralInfo> tmpMap = new ConcurrentHashMap();
		try {
			sqlSession = sqlSessionFactory.getObject().openSession();
			SnoGeneralInfoMapper snoInfoMapper = sqlSession.getMapper(SnoGeneralInfoMapper.class);
			Example exa = new Example(SnoGeneralInfo.class);
			List<SnoGeneralInfo> snoList = snoInfoMapper.selectByExample(exa);
			for (SnoGeneralInfo snoInfo : snoList) {
				tmpMap.put(snoInfo.getOperType(), snoInfo);
				snoInfo.updateOldNo(snoInfo.getLastNo());
		//		System.out.println("对象: " + snoInfo);
			}
		//	System.out.println("数据量是: " + tmpMap.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		synchronized (dataMap) {
			dataMap = tmpMap;
			//init redis
			this.initRedis(tmpMap);
		}
		// 初始化完毕后，启动服务
		this.start();
	}

	/**
	 * 检查数据重新加载
	 * @param 最后检查时间
	 * @return 检查完毕的时间
	 * @throws Exception 
	 */
	private long checkReload(long anLast) throws Exception {
 		if ((anLast + this.checkTimeOut * 1000) < System.currentTimeMillis()) {
			SqlSession sqlSession = null;
			sqlSession = sqlSessionFactory.getObject().openSession();
			SnoGeneralInfoMapper snoInfoMapper = sqlSession.getMapper(SnoGeneralInfoMapper.class);
			Example exa = new Example(SnoGeneralInfo.class);
			List<SnoGeneralInfo> snoList = snoInfoMapper.selectByExample(exa);
			Map<String, SnoGeneralInfo> tmpMap =  dataMap;
			for (SnoGeneralInfo snoInfo : snoList) {
				SnoGeneralInfo oldSnoInfo = tmpMap.get(snoInfo.getOperType());
				
				//存在就检查更新，不存在就加入
				if (oldSnoInfo != null){
					oldSnoInfo.updateLastNo(snoInfo.getLastNo());
				}
				else{
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
				System.out.println("waiting for data");
				if (hasError == false) {
					abq.take();
				}
				hasError = false;
			  
				lastTime = checkReload(lastTime);
			 
				Map<String, SnoGeneralInfo> tmpMap = dataMap;
				List<SnoGeneralInfo> workList = new ArrayList();

				// 获取需要更新的列表
				for (Map.Entry<String, SnoGeneralInfo> ent : tmpMap.entrySet()) {
					SnoGeneralInfo snoInfo = ent.getValue();
					if (snoInfo.hasSave()) {
						workList.add(snoInfo.clone());
					}
				}
				
				if (workList.size() > 0) {
					sqlSession = sqlSessionFactory.getObject().openSession();
					// sqlSession = tmpFactory.openSession();
					SnoGeneralInfoMapper snoInfoMapper = sqlSession.getMapper(SnoGeneralInfoMapper.class);

					// 更新列表
					for (SnoGeneralInfo snoInfo : workList) {
						int xx = snoInfoMapper.updateSimple(snoInfo);
					//	System.out.println("this is update count :" + xx);
					}
					sqlSession.commit();
					sqlSession.close();
					// 更新旧的序号
					for (SnoGeneralInfo snoInfo : workList) {
						SnoGeneralInfo tmpSnoInfo = tmpMap.get(snoInfo.getOperType());
						if (tmpSnoInfo != null) {
							tmpSnoInfo.updateOldNo(snoInfo.getLastNo());
						}
					}
				}
			} catch (Exception e1) {
				hasError = true;
				try {
					Thread.sleep(5000);
					e1.printStackTrace();
					if (sqlSession != null) {

						sqlSession.rollback();
						sqlSession.close();

					}
				} catch (Exception e) {

				}
			}
		}
	}

	public long getLongValue(String anKey) {
		SnoGeneralInfo snoInfo = dataMap.get(anKey);
		if (snoInfo != null) {
			synchronized (snoInfo) {
				if (abq.size() < 1000) {
					try {
						abq.put(obj);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return this.incrby(snoInfo);
			}
		}

		return 0;
	}
	
	private String buildKey(String key){
	    if(StringUtils.isBlank(key)){
	        return key;
	    }
	    return DefaultIdRedisKeyPrefix+key;
	}
	
	private void initRedis(Map<String, SnoGeneralInfo> tmpMap){
	    if(tmpMap==null){
	        return ;
	    }
	    for(SnoGeneralInfo info:tmpMap.values()){
	        String type=info.getOperType();
	        long no=info.getLastNo();
	        
	        String key=this.buildKey(type);
	        boolean exists=this.redis.exists(key);
	        if(!exists){
	            this.redis.set(key, String.valueOf(no));
	        }
	    }
	    
	}
	
	private long incrby(SnoGeneralInfo anInfo){
	    String type=anInfo.getOperType();
	    
	    String key=this.buildKey(type);
	    long re=this.redis.incrby(key, 1);
	    anInfo.addValue();
	    if(anInfo.getLastNo()!=re){
	        throw new BytterException("for key="+key+",memory's id!= redis's id;mem="+anInfo.getLastNo()+",redis="+re);
	    }
	    return re;
	}

	public static void main(String[] args) {

	}
}
