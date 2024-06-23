package com.example.quily.converter;

public interface Converter<Req, Res, Model> {
   Model convertRequestToModel(Req req);
   Res convertModelToResponse(Model model);
}
