package com.dennis.simplealbum.mapper;

import java.util.List;

import com.dennis.simplealbum.po.entryVo;
import com.dennis.simplealbum.po.limitParam;
import com.dennis.simplealbum.po.main;



public interface mainMapper {
	
	void insertEntry(main m) throws Exception;
	
	List<entryVo> showEntries(limitParam lp) throws Exception;
	
	int getCount() throws Exception;

}
