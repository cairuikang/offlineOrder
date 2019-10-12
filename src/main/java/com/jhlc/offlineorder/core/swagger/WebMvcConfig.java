package com.jhlc.offlineorder.core.swagger;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // 解决静态资源无法访问（可选）
        /*registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");*/
        // 直接在浏览器访问：根目录/swagger-ui.html
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 需要用到的webjars（包含js、css等）
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    //设置swagger里面返回的responsebody的值为json字符串
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> list=new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        list.add(new MediaType("text","html", Charset.forName("UTF-8")));
        list.add(new MediaType("application","*+json",Charset.forName("UTF-8")));
        converter.setSupportedMediaTypes(list);
        converters.add(converter);
    }
}