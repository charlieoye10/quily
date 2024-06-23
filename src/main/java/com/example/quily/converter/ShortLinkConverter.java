package com.example.quily.converter;

import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.util.CommonUtil;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.stereotype.Component;

@Component
public class ShortLinkConverter implements Converter<CreateShortLinkRequest, ShortLinkResponse, ShortLink> {
   @Override
   public ShortLink convertRequestToModel(CreateShortLinkRequest createShortLinkRequest) {
      return null;
   }

   public ShortLink convertRequestToModel(CreateShortLinkRequest createShortLinkRequest, String shortLink) {
      return new ShortLink(
         createShortLinkRequest.getUserID(),
         createShortLinkRequest.getOriginalLink(),
         shortLink,
         CommonUtil.getCurrentDateTimeInFormat(),
         createShortLinkRequest.getExpiryDate(),
         true);
   }

   @Override
   public ResponseBody<ShortLinkResponse> convertModelToResponse(ShortLink shortLink) {
      final ShortLinkResponse shortLinkResponse = new ShortLinkResponse(
         shortLink.getUserID(),
         shortLink.getOriginalLink(),
         shortLink.getShortedLink(),
         shortLink.getExpiryDate(),
         shortLink.getCreationDate());
      final String message = ShortLinkUtil.SHORT_LINK_CREATED_MESSAGE;
      return new ResponseBody<>(200, message, shortLinkResponse);
   }
}
