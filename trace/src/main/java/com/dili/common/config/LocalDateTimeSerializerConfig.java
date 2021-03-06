package com.dili.common.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@JsonComponent
public class LocalDateTimeSerializerConfig {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//        return builder -> builder.serializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(this.pattern));
//    }

    //
//    @Bean
//    public LocalDateTimeSerializer localDateTimeDeserializer() {
//        if (StringUtils.isBlank(this.pattern)) {
//            return LocalDateTimeSerializer.INSTANCE;
//        }
//        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
//    }
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder() {
//
//        return builder -> {
//            TimeZone tz = TimeZone.getTimeZone("GMT+8");
//            DateFormat df = new SimpleDateFormat(pattern);
//            df.setTimeZone(tz);
//            builder.failOnEmptyBeans(false)
//                    .failOnUnknownProperties(false)
//                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                    .dateFormat(df);
//        };
//    }

//    @Bean
//    public LocalDateTimeSerializer localDateTimeDeserializer() {
//        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
//    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());

        return builder -> {
            builder.serializerByType(ZonedDateTime.class, new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            builder.serializerByType(LocalDateTime.class,  new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)));
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        };
    }
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//
//
//        return builder -> {
//            builder.serializerByType(ZonedDateTime.class, new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        };
//    }

}