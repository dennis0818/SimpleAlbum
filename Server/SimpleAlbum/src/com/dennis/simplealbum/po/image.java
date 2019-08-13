package com.dennis.simplealbum.po;
public class image {
	
	private int img_id;
	
	private int num_entry;
	
	private String img_path;
	
	

	public int getImg_id() {
		return img_id;
	}

	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}

	public int getNum_entry() {
		return num_entry;
	}

	public void setNum_entry(int num_entry) {
		this.num_entry = num_entry;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	@Override
	public String toString() {
		return "image [img_id=" + img_id + ", num_entry=" + num_entry + ", img_path=" + img_path + "]";
	}
	
	

}
