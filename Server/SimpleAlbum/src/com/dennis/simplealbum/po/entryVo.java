package com.dennis.simplealbum.po;

import java.util.List;

public class entryVo extends main {
	
	List<image> images;

	public List<image> getImages() {
		return images;
	}

	public void setImages(List<image> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return super.toString() + "entryVo [images=" + images + "]";
	}
	
	
	
	
	

}
