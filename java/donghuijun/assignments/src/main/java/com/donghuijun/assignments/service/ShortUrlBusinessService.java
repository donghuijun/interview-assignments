package com.donghuijun.assignments.service;

public interface ShortUrlBusinessService {
	/**
	 * 短域名存储接口：接受长域名信息，返回短域名信息
	 * @param longUrl
	 * @return
	 */
	public String longUrltoShortUrl(String longUrl) throws Exception;
	/**
	 * 短域名读取接口：接受短域名信息，返回长域名信息。
	 * @param shortUrl
	 * @return
	 */
	public String findLongUrl(String shortUrl) throws Exception;
        

}
