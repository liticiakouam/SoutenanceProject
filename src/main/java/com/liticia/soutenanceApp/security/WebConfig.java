package com.liticia.soutenanceApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ErrorViewResolver thymeleafErrorViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setCharacterEncoding("UTF-8");
        return new DefaultErrorViewResolver(resolver);
    }

    private static class DefaultErrorViewResolver implements ErrorViewResolver {

        private final ThymeleafViewResolver resolver;

        public DefaultErrorViewResolver(ThymeleafViewResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
            if (status == HttpStatus.NOT_FOUND) {
                ModelAndView modelAndView = new ModelAndView("notfound");
                modelAndView.setStatus(HttpStatus.NOT_FOUND);
                return modelAndView;
            } else if (status == HttpStatus.BAD_REQUEST) {
                ModelAndView modelAndView = new ModelAndView("serverError");
                modelAndView.setStatus(HttpStatus.BAD_REQUEST);
                return modelAndView;
            } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                ModelAndView modelAndView = new ModelAndView("serverError");
                modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                return modelAndView;
            }
            return null;
        }
    }
}
