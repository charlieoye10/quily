package com.example.quily.converter;

import com.example.quily.response.ResponseBody;

public interface Converter<Req, Res, Model> {
   Model convertRequestToModel(Req req);
   ResponseBody<Res> convertModelToResponse(Model model);
}
