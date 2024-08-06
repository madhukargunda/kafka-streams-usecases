/**
 * Author: Madhu
 * User:madhu
 * Date:10/9/24
 * Time:12:52 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.madhu.toyataSalesAnalytics.dto.InstrumentDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@WebServlet(name = "MarketServlet", urlPatterns = {"/MarketServlet"})
@Slf4j
public class MarketServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InstrumentDTO instrumentDTO = new InstrumentDTO("BankofAMerica-",100l,"BANK OF AMERICA USA");
        log.info("The values which are coming pa {}", request.getParameterValues("ric"));
        String[] currency = request.getParameterValues("ric");
        ObjectMapper objectMapper = new ObjectMapper();
        String asString = objectMapper.writeValueAsString(instrumentDTO);
        response.setContentType("application/json; charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
//        outputStreamWriter.write(asString);
//        outputStreamWriter.flush();
//        outputStreamWriter.close();

        PrintWriter writer = new PrintWriter(outputStreamWriter);
        response.getWriter().write(asString);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
