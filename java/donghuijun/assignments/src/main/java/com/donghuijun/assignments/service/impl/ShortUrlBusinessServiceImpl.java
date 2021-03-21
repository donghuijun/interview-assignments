package com.donghuijun.assignments.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.donghuijun.assignments.service.ShortUrlBusinessService;
import com.donghuijun.assignments.utils.RedisUtils;


@Service
public class ShortUrlBusinessServiceImpl implements ShortUrlBusinessService {

	public static final Logger logger = LoggerFactory.getLogger(ShortUrlBusinessServiceImpl.class);
	
	@Autowired
	private RedisUtils redisUtils;
	
	private static final  String KeyPrefix = "short_url_hash_md5_key_";
	 
    private static final  String shortUrlKeyPrefix = "short_url_hash_key_";
 
    private static final  String serialNumberKey = "short_url_serial_number";
 
    /**
     * 在进制表示中的字符集合，0-Z分别用于表示最大为62进制的符号表示
     */
    private static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    @Value("${short.url.prefix}")
    private String shortUrlPrefix;
    @Value("${short.url.timeout}")
    private Long shortUrlTimeout;

	
	public String longUrltoShortUrl(String longUrl) throws Exception{
		// TODO Auto-generated method stub
		 
		String	md5 = DigestUtils.md5DigestAsHex(longUrl.getBytes("UTF-8"));
		
        String shortUrl = (String)redisUtils.get(KeyPrefix + md5);
        if (shortUrl == null){
            synchronized (md5.intern()){
                shortUrl = (String)redisUtils.get(KeyPrefix + md5);
                if (shortUrl == null){
                    Long value = redisUtils.increment(serialNumberKey, 1L);
                    logger.info("====自增数：{}====", value);
                    String longString = toOtherNumberSystem(value,62);
                    shortUrl = shortUrlPrefix + longString;
                    //固定前缀+长链接MD5以后的字符串作为key，值为短连接，存储在redis中
                    redisUtils.set(KeyPrefix + md5, shortUrl, shortUrlTimeout, TimeUnit.DAYS);
                    //固定前缀+短链接作为key,长链接作为value，存储与redis中。
                    redisUtils.set(shortUrlKeyPrefix + shortUrl, longUrl, shortUrlTimeout, TimeUnit.DAYS);
                }
            }
        }

        return shortUrl;
	
	}
	
	/**
     * 将十进制的数字转换为指定进制的字符串
     *
     * @param number 十进制的数字
     * @param seed   指定的进制
     * @return 指定进制的字符串
     */
    public static String toOtherNumberSystem(long number, int seed) {
        if (number < 0) {
            number = ((long) 2 * 0x7fffffff) + number + 2;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((number / seed) > 0) {
            buf[--charPos] = digits[(int) (number % seed)];
            number /= seed;
        }
        buf[--charPos] = digits[(int) (number % seed)];
        return new String(buf, charPos, (32 - charPos));

    }
    
    /**
     * 将其它进制的数字（字符串形式）转换为十进制的数字
     * @param number 其它进制的数字（字符串形式）
     * @param seed 指定的进制，也就是参数str的原始进制
     * @return 十进制的数字
     */
   /*public static long toDecimalNumber(String number, int seed) {
       char[] charBuf = number.toCharArray();
       if (seed == 10) {
           return Long.parseLong(number);
       }

       long result = 0, base = 1;

       for (int i = charBuf.length - 1; i >= 0; i--) {
           int index = 0;
           for (int j = 0, length = digits.length; j < length; j++) {
               //找到对应字符的下标，对应的下标才是具体的数值
               if (digits[j] == charBuf[i]) {
                   index = j;
               }
           }
           result += index * base;
           base *= seed;
       }
       return result;
   }*/

    
	public String findLongUrl(String shortUrl) {
        
        return (String)redisUtils.get(shortUrlKeyPrefix + shortUrl);
    }


}
