package com.dennis.simplealbum.service;

import java.util.List;

import com.dennis.simplealbum.po.entryVo;
import com.dennis.simplealbum.po.image;
import com.dennis.simplealbum.po.limitParam;
import com.dennis.simplealbum.po.main;



public interface mainService {
	
	void insertEntry(main m) throws Exception;
	
	void insertImage(image i) throws Exception;
	
	List<entryVo> showEntries(limitParam lp) throws Exception;
	
	int getCount() throws Exception;
}
