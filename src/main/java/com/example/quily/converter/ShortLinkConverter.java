package com.example.quily.converter;

import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.util.CommonUtil;
import org.springframework.stereotype.Component;

@Component
public class ShortLinkConverter implements Converter<CreateShortLinkRequest, ShortLinkResponse, ShortLink> {
   @Override
   public ShortLink convertRequest(CreateShortLinkRequest createShortLinkRequest) {
      return new ShortLink(
         createShortLinkRequest.getUserID(),
         createShortLinkRequest.getOriginalLink(),
         "",
         CommonUtil.getCurrentDateTimeInFormat(),
         createShortLinkRequest.getExpiryDate(),
         true);
   }

   @Override
   public ShortLinkResponse convertModel(ShortLink shortLink) {
      return new ShortLinkResponse(shortLink.getUserID(), shortLink.getOriginalLink(),
         shortLink.getShortedLink(), shortLink.getExpiryDate(), shortLink.getCreationDate());
   }
}
