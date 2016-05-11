package com.betterjr.common.security;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private String startWith = "pppp";

	private String endWith = "ppppppppp";

	private String[] propArray = new String[] {};
	protected Resource[] locations;

	public String[] getPropArray() {
		return propArray;
	}

	public void setPropArray(String[] anPropArray) {
		propArray = anPropArray;
	}

	public String getEndWith() {
		return endWith;
	}

	public void setEndWith(String anEndWith) {
		endWith = anEndWith;
	}

	public String getStartWith() {
		return startWith;
	}

	public void setStartWith(String anStartWith) {
		startWith = anStartWith;
	}

	@Override
	public void setLocations(Resource[] locations) { // 由于location是父类私有，所以需要记录到本类的locations中
		super.setLocations(locations);
		this.locations = locations;
	}

	@Override
	public void setLocation(Resource location) { // 由于location是父类私有，所以需要记录到本类的locations中
		super.setLocation(location);
		this.locations = new Resource[] { location };
	}

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		System.out.println(propertyName + " = " + propertyValue + ", mytype="
				+ (isEncryptPropertyVal(propertyName) && SignHelper.isAesEncrypt(propertyValue)));
		String tmpValue;
		if (isEncryptPropertyVal(propertyName) && SignHelper.isAesEncrypt(propertyValue)) {
			tmpValue = SignHelper.aesDecrypt(propertyValue);
			System.out.println("this is decode string =" + tmpValue);
		} else {
			tmpValue = propertyValue;

		}
		return super.convertProperty(propertyName, tmpValue);
	}

	private void encrypt(File file) throws FileNotFoundException, IOException {
		System.out.println("this is load Properties");
		String tmpValue = null;
		boolean hasEncrypt = false;

		Properties props = new Properties();
		Reader rr = new FileReader(file);
		props.load(rr);
		IOUtils.closeQuietly(rr);

		for (Enumeration propertyNames = props.propertyNames(); propertyNames.hasMoreElements();) {
			String propertyName = (String) propertyNames.nextElement();
			String propertyValue = props.getProperty(propertyName);
			if (isEncryptPropertyVal(propertyName)) {
				if (SignHelper.isAesEncrypt(propertyValue)) {
					tmpValue = propertyValue;
				} else {
					tmpValue = SignHelper.aesEncrypt(propertyValue);
					hasEncrypt = true;
				}
			} else {
				tmpValue = propertyValue;
			}
			props.setProperty(propertyName, tmpValue);
			System.out.println(propertyName + " = " + tmpValue + ", |||" + propertyValue);
		}

		if (hasEncrypt) {
			System.out.println("this is data Encrypt");
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(file));
				props.store(writer, "file Encrypt");
			} catch (IOException e) {

			}
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		super.postProcessBeanFactory(beanFactory);

		for (Resource location : locations) {

			// 加载完后，遍历location，对properties进行加密
			try {
				final File file = location.getFile();
				if (file.isFile()) { // 如果是一个普通文件

					if (file.canWrite()) { // 如果有写权限
						encrypt(file); // 调用文件加密方法
					} else {
						if (logger.isWarnEnabled()) {
							logger.warn("File '" + location + "' can not be write!");
						}
					}
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn("File '" + location + "' is not a normal file!");
					}
				}

			} catch (IOException e) {
				if (logger.isWarnEnabled()) {
					logger.warn("File '" + location + "' is not a normal file!");
				}
			}
		}

	}

	private boolean isEncryptPropertyVal(String propertyName) {

		if (propertyName.startsWith(this.startWith) || propertyName.endsWith(endWith)) {

			return true;
		} else {
			for (String tmpStr : this.propArray) {
				if (propertyName.equalsIgnoreCase(tmpStr)) {

					return true;
				}
			}
		}
		return false;
	}
}
