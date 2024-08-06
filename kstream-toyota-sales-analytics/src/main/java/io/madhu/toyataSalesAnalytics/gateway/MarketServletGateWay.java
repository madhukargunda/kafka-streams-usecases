/**
 * Author: Madhu
 * User:madhu
 * Date:10/9/24
 * Time:1:51 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.gateway;

import io.madhu.toyataSalesAnalytics.dto.MarketDataServletDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.ZonedDateTime;

@Component
@Slf4j
public class MarketServletGateWay {

    @Autowired
    ServletContext servletContext;

    public MarketDataServletDTO getMarketData() throws ServletException, IOException {
        log.info("Calling getMarketData Gateway class ");
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/MarketServlet?ric=INR,USD");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // Use the request here
        requestDispatcher.forward(request,responseWrapper);
        byte[] responseContent = responseWrapper.getContentAsByteArray();
        log.info("The ServletResponse which was sent is : {} ,{}", responseContent.length, new String(responseContent));
       // responseWrapper.copyBodyToResponse(); this one commits the servlet response to actual HttpServletResponse
        return new MarketDataServletDTO("Message Written Successfully - {}........"+ ZonedDateTime.now().toString());
    }
}
