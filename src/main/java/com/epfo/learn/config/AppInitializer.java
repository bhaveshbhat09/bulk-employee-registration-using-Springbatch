package com.epfo.learn.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Root context: shared beans such as DataSource and Batch (added in later steps)
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class, BatchConfig.class, ReadJobConfig.class, ValidateJobConfig.class, ApproveJobConfig.class };
    }
    // Web context: controllers and MVC settings
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    // The DispatcherServlet handles every URL under the app
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(
                new MultipartConfigElement("", 10L * 1024 * 1024, 12L * 1024 * 1024, 0));
    }
}