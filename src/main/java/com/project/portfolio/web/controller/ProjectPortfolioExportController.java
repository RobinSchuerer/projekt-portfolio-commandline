package com.project.portfolio.web.controller;

import java.io.ByteArrayInputStream;

import javax.inject.Inject;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.portfolio.domain.ProjectPortfolio;
import com.project.portfolio.integration.service.ProjectPortfolioService;

@Controller
public class ProjectPortfolioExportController {
	@Inject
	ProjectPortfolioService portfolioService;

	@RequestMapping(value = "/portfolio/inputexport", method = RequestMethod.POST)
	@ResponseBody
	public String writeToWorkBook(@RequestParam MultipartFile file) throws Exception {
		System.out.println("File size: " + file.getSize());
		byte[] bytes = file.getBytes();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		XSSFWorkbook portflioWorkBook = new XSSFWorkbook(bis);

		ProjectPortfolio portFolio = portfolioService
				.processPortfolio(portflioWorkBook);
		portfolioService.writeToWorkBook(portFolio, portflioWorkBook);
		return "successS";
	}
}