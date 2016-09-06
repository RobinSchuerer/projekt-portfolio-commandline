package de.lv1871.projektportfolio.application;

import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.reader.ProjektPortfolioExcelReader;
import de.lv1871.projektportfolio.service.ProjektPortfolioVorschlagService;
import de.lv1871.projektportfolio.writer.PortfolioWriter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by SchuererR on 02.08.2016.
 */
@SpringBootApplication
@ComponentScan(basePackages = "de.lv1871.projektportfolio")
public class ProjektPortfolioApplication {


    public static void main(String[] args) {

        String dateiname = args[0];

        ConfigurableApplicationContext context = SpringApplication.run(ProjektPortfolioApplication.class, args);
        ProjektPortfolioExcelReader reader = context.getBean(ProjektPortfolioExcelReader.class);
        ProjektPortfolioVorschlagService service = context.getBean(ProjektPortfolioVorschlagService.class);
        PortfolioWriter writer = context.getBean(PortfolioWriter.class);

        try {
            Path resPath = Paths.get(dateiname);
            InputStream fileInputStream = Files.newInputStream(resPath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            ProjektPortfolioEingabeDaten eingabeDaten = reader.read(workbook);
            ProjektPortfolioVorschlag vorschlag = service.berechne(eingabeDaten);
            writer.write(workbook,vorschlag);

            OutputStream stream = Files.newOutputStream(resPath);
            workbook.write(stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

