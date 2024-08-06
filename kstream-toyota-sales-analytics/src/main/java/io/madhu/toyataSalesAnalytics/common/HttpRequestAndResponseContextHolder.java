/**
 * Author: Madhu
 * User:madhu
 * Date:9/9/24
 * Time:11:54 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.common;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@RequiredArgsConstructor
@Getter
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class HttpRequestAndResponseContextHolder {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @PostConstruct
    public void init() {
        log.info("Init {}", httpServletRequest.getServletPath());
    }
}
