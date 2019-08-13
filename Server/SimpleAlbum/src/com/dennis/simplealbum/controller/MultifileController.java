package com.dennis.simplealbum.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dennis.simplealbum.po.entryVo;
import com.dennis.simplealbum.po.image;
import com.dennis.simplealbum.po.limitParam;
import com.dennis.simplealbum.po.main;
import com.dennis.simplealbum.po.transferData;
import com.dennis.simplealbum.service.mainService;



@Controller
@RequestMapping("/Inuyasha")
public class MultifileController {

	private static int num_entry = 1;

	@Autowired
	private mainService mainService;

	@RequestMapping("/ShowMessages")
	public @ResponseBody transferData showEntries(int limitStart, int limitOffset) throws Exception {

		limitParam lp = new limitParam();
		lp.setLimitStart(limitStart);
		lp.setLimitOffset(limitOffset);

		List<entryVo> list = mainService.showEntries(lp);

		for (entryVo e : list) {
			System.out.println(e);
		}

		int count = mainService.getCount();

		num_entry = count;
		System.out.println("88888888888888888888888888888" + num_entry + "88888888888888888888888888");

		transferData data = new transferData();
		data.setCount(count);
		data.setDataList(list);

		return data;
	}

	@RequestMapping("/PhoneUpload")
	public void PhoneReceive(MultipartFile[] files, String message, HttpServletResponse resp) throws Exception {

		
		String server_path = "D:/pictures/SimpleAlbumImages/";
		
		//锟斤拷取锟斤拷前锟斤拷锟斤拷锟斤拷目锟斤拷
		num_entry = mainService.getCount();
		String newMessage = new String(message.getBytes("ISO-8859-1"), "utf-8");
		

		// 锟斤拷锟捷匡拷main锟斤拷锟铰硷拷锟�
		main m = new main();
		m.setMessage(newMessage);
		m.setUsername("me");
		m.setNum_entry(num_entry + 1);
		System.out.println("777777777777777777777777777777" + num_entry + "777777777777777777777777777777777777");
		m.setTime(new Date(System.currentTimeMillis()));// 锟斤拷取系统锟斤拷前时锟斤拷

		mainService.insertEntry(m);

		for (MultipartFile file : files) {
			
			// 图片锟斤拷锟捷达拷锟斤拷
			String originalFilename = file.getOriginalFilename();
			image img = new image();
			img.setNum_entry(num_entry + 1);
			img.setImg_path(originalFilename);

			mainService.insertImage(img);

		}
		
		for(MultipartFile file : files) {
			String originalFilename = file.getOriginalFilename();

			String path_server_file = server_path + originalFilename;

			File server_file = new File(path_server_file);

			file.transferTo(server_file);
		}

		// 锟斤拷锟斤拷锟斤拷锟斤拷
		//num_entry++;
		

		// 锟斤拷应锟斤拷锟街伙拷
		resp.setContentType("text/html");

		PrintWriter out = resp.getWriter();

		out.println("Success!");

	}

}
