package com.betterjr.common.utils;

import java.io.Serializable;

public class IP2LocationInfo implements Serializable {
	private static final long serialVersionUID = -4168759373537404474L;
	private String city;
	private String city_id;
	private String country_id;
	private String ip;
	private String isp;
	private String region;

	public String getRegion() {
		return region;
	}

	public void setRegion(String anRegion) {
		region = anRegion;
	}

	public String getRegion_id() {
		return region_id;
	}

	public void setRegion_id(String anRegion_id) {
		region_id = anRegion_id;
	}

	private String region_id;

	public String getCity() {
		return city;
	}

	public void setCity(String anCity) {
		city = anCity;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String anCity_id) {
		city_id = anCity_id;
	}

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String anCountry_id) {
		country_id = anCountry_id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String anIp) {
		ip = anIp;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String anIsp) {
		isp = anIsp;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(region).append(" ").append(this.city).append(" ")
				.append(this.isp).append("(").append(this.ip).append(",")
				.append(city_id).append(")");
		return sb.toString();
	}
}
