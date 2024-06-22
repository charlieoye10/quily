package com.example.quily.converter;

public interface Converter<F, T> {
   T convertFrom(F f);
   F convertTo(T t);
}
