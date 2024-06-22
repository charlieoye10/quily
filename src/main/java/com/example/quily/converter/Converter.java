package com.example.quily.converter;

public interface Converter<Req, Res, Model> {
   Model convertRequest(Req req);
   Res convertModel(Model model);
}
