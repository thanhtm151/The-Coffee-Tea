package com.backend.dream.service.imp;

import com.backend.dream.service.UploadService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class UploadServiceImp implements UploadService {
	@Autowired
	ServletContext context;
	
	@Override
	public File save(MultipartFile file, String folder) { 
		File dir = new File(context.getRealPath("/img/" + folder));
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String s = System.currentTimeMillis() + file.getOriginalFilename();
		String name = Integer.toHexString(s.hashCode()) + s.substring(s.lastIndexOf("."));
		try {
			File saveFile = new File(dir,name);
			file.transferTo(saveFile);
			return saveFile;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
