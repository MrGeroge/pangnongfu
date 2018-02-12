package com.pangnongfu.server.web.config;

import java.util.List;

import com.pangnongfu.server.web.interceptor.CROSInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
/**
 * Author:zhangyu
 * create on 15/10/1.
 */
@Configuration
@ImportResource("classpath:test-services.xml")
@ComponentScan(basePackages = "com.pangnongfu.server.web")
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CROSInterceptor());
    }


    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        //用于从session中取值
        //argumentResolvers.add(new SessionScopeArgumentResolver());
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //对返回JSON支持
        registry.enableContentNegotiation(new MappingJackson2JsonView());

        //配置thymeleaf为模版引擎
        ServletContextTemplateResolver tplResolver=new ServletContextTemplateResolver();
        tplResolver.setPrefix("/WEB-INF/template/");
        tplResolver.setSuffix(".html");
        tplResolver.setCharacterEncoding("UTF-8");
        tplResolver.setTemplateMode("HTML5");

        SpringTemplateEngine engine=new SpringTemplateEngine();
        engine.setTemplateResolver(tplResolver);

        ThymeleafViewResolver viewResolver=new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(engine);
        viewResolver.setCharacterEncoding("UTF-8");

        registry.viewResolver(viewResolver);
    }
}
