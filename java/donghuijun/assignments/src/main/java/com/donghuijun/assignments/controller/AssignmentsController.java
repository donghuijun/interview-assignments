package com.donghuijun.assignments.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.donghuijun.assignments.service.ShortUrlBusinessService;

@RestController
public class AssignmentsController {

	public static final Logger logger = LoggerFactory.getLogger(AssignmentsController.class);
	
	@Autowired
	private ShortUrlBusinessService shortUrlBusinessService;
	
	
	
	@RequestMapping(value="/convertToShortUrl",method=RequestMethod.POST)
    public String convertToShortUrl(String longUrl) throws Exception{
        return this.shortUrlBusinessService.longUrltoShortUrl(longUrl);
    }
	
	
	@RequestMapping(value="/findLongUrl",method=RequestMethod.POST)
    public String findLongUrl(String shortUrl) throws Exception {
        return this.shortUrlBusinessService.findLongUrl(shortUrl);
    }
}
