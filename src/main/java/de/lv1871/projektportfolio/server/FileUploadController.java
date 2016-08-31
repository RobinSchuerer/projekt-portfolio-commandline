package de.lv1871.projektportfolio.server;


import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.reader.ProjektPortfolioExcelReader;
import de.lv1871.projektportfolio.service.ProjektPortfolioVorschlagService;
import de.lv1871.projektportfolio.writer.PortfolioWriter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class FileUploadController {

    @Autowired
    private ProjektPortfolioExcelReader reader;

    @Autowired
    private ProjektPortfolioVorschlagService service;

    @Autowired
    private PortfolioWriter writer;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                              RedirectAttributes redirectAttributes) {

        if (!file.isEmpty()) {
            try {

                XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                ProjektPortfolioEingabeDaten eingabeDaten = reader.read(workbook);
                ProjektPortfolioVorschlag vorschlag = service.berechne(eingabeDaten);
                writer.write(workbook, vorschlag);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                workbook.write(byteArrayOutputStream);


                byte[] byteArray = byteArrayOutputStream.toByteArray();


                HttpHeaders header = new HttpHeaders();
                header.set("Content-Disposition", "attachment; filename=" + file.getOriginalFilename().replace(" ", "_"));
                header.setContentLength(byteArray.length);

                return ResponseEntity
                        .ok()
                        .headers(header)
                        .contentLength(byteArray.length)
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(new ByteArrayResource(byteArray));


            } catch (IOException | RuntimeException e) {
                redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " because it was empty");
        }

        redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " because it was empty");

        return null;
    }

}