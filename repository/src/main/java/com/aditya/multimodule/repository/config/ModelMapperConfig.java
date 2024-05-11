package com.aditya.multimodule.repository.config;

import com.aditya.multimodule.model.Gender;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(enumToStringConverter());
        return modelMapper;
    }

    private Converter<Gender, String> enumToStringConverter() {
        return new AbstractConverter<Gender, String>() {
            @Override
            protected String convert(Gender source) {
                return source.toString();
            }
        };
    }
}
