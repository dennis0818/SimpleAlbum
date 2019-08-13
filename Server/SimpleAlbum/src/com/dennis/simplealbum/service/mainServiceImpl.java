package com.dennis.simplealbum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dennis.simplealbum.mapper.imageMapper;
import com.dennis.simplealbum.mapper.mainMapper;
import com.dennis.simplealbum.po.entryVo;
import com.dennis.simplealbum.po.image;
import com.dennis.simplealbum.po.limitParam;
import com.dennis.simplealbum.po.main;



public class mainServiceImpl implements mainService {
	
	@Autowired
	private mainMapper mainMapper;
	@Autowired
	private imageMapper imageMapper;

	@Override
	public void insertEntry(main m) throws Exception {
		// TODO Auto-generated method stub
		mainMapper.insertEntry(m);
	}

	@Override
	public void insertImage(image i) throws Exception {
		// TODO Auto-generated method stub
		imageMapper.insertImage(i);
		
	}

	@Override
	public List<entryVo> showEntries(limitParam lp) throws Exception {
		// TODO Auto-generated method stub
		
		return mainMapper.showEntries(lp);
	}

	@Override
	public int getCount() throws Exception {
		// TODO Auto-generated method stub
		return mainMapper.getCount();
	}
	

}
