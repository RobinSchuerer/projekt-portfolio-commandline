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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
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

        String path = args[0];
        XSSFWorkbook workbook = null;
        try {
            workbook = getWorkbook(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigurableApplicationContext context = SpringApplication.run(ProjektPortfolioApplication.class, args);

        ProjektPortfolioExcelReader reader = context.getBean(ProjektPortfolioExcelReader.class);
        ProjektPortfolioVorschlagService service = context.getBean(ProjektPortfolioVorschlagService.class);
        PortfolioWriter writer = context.getBean(PortfolioWriter.class);

        ProjektPortfolioEingabeDaten eingabeDaten = reader.read(workbook);
        ProjektPortfolioVorschlag vorschlag = service.berechne(eingabeDaten);
        writer.write(workbook, vorschlag);

        try {
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static XSSFWorkbook getWorkbook(String path) throws URISyntaxException, IOException {
        Path resPath = Paths.get(path);
        return new XSSFWorkbook(Files.newInputStream(resPath));
    }
}

