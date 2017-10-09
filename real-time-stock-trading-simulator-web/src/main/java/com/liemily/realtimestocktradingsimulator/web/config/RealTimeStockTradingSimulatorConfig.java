package com.liemily.realtimestocktradingsimulator.web.config;

import com.liemily.broker.config.BrokerConfig;
import com.liemily.company.domain.Company;
import com.liemily.company.service.CompanyService;
import com.liemily.report.config.ReportGenerationConfig;
import com.liemily.stock.generation.StockGenerationService;
import com.liemily.stock.generation.config.StockGenerationConfig;
import com.liemily.stock.generation.exceptions.StockGenerationException;
import com.liemily.trade.config.TradeConfig;
import com.liemily.user.config.UserConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Configuration
@Import({BrokerConfig.class, EmailConfig.class, ReportGenerationConfig.class, StockGenerationConfig.class, TradeConfig.class, UserConfig.class})
class RealTimeStockTradingSimulatorConfig {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private CompanyService companyService;
    @Autowired
    private StockGenerationService stockGenerationService;

    @PostConstruct
    public void populateStocks() throws IOException {
        Collection<Company> companies = generateCompanies();
        generateStocks(companies);
    }

    private Collection<Company> generateCompanies() throws IOException {
        Collection<Company> companies = new HashSet<>();
        List<String> companyLines = new ArrayList<>();

        URL resource = getClass().getClassLoader().getResource("static/company.psv");
        try (InputStream is = resource.openStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is));
             Stream<String> lines = br.lines()) {
            lines.forEach(companyLines::add);
        }

        for (String companyLine : companyLines) {
            String[] companyToSymbol = companyLine.split("\\|");
            if (companyToSymbol.length == 2) {
                Company company = new Company(companyToSymbol[1], companyToSymbol[0]);
                companies.add(company);
                logger.info("Found company " + company + " to save to table");
            }
        }
        return companyService.save(companies);
    }

    private void generateStocks(Collection<Company> companies) {
        for (Company company : companies) {
            try {
                stockGenerationService.generateStock(company.getSymbol());
            } catch (StockGenerationException sge) {
                logger.info("Stock already exists for " + company + " so will not generate");
            }
        }
    }
}
