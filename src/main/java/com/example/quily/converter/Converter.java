package com.example.quily.converter;

import com.example.quily.response.ResponseBody;

public interface Converter<Req, Res, Model> {
   Model getModelFromRequest(Req req);
   ResponseBody<Res> getResponseFromModel(Model model);
}
