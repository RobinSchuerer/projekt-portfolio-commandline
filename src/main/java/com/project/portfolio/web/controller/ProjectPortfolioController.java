package com.project.portfolio.web.controller;

import com.project.portfolio.domain.ProjectPortfolio;
import com.project.portfolio.integration.service.ProjectPortfolioService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;


@Controller
public class ProjectPortfolioController {
	
	@Inject
	ProjectPortfolioService portfolioService;
	
	@RequestMapping(value="/portfolio/input", method = RequestMethod.POST)
	public @ResponseBody ProjectPortfolio get(@RequestParam(name = "file") MultipartFile file)  throws Exception{
		System.out.println("File size: "+ file.getSize());
		byte[] bytes = file.getBytes();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		XSSFWorkbook portflioWorkBook = new XSSFWorkbook(bis);
		return portfolioService.processPortfolio(portflioWorkBook);
	}

}
