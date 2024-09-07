package com.example.quily.converter;

import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.ShortLinkResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.quily.util.CommonUtil.convertStringToLocalTimeDate;

@Component
public class ShortLinkConverter implements Converter<CreateShortLinkRequest, ShortLinkResponse, ShortLink> {
   @Override
   public ShortLink getModelFromRequest(CreateShortLinkRequest createShortLinkRequest) {
      return null;
   }

   public ShortLink convertRequestToModel(CreateShortLinkRequest createShortLinkRequest,
                                          String shortLink,
                                          String userEmail) {
      return new ShortLink(
         userEmail,
         createShortLinkRequest.getOriginalLink(),
         shortLink,
         LocalDateTime.now(),
         convertStringToLocalTimeDate(createShortLinkRequest.getExpiryDate()),
         true,
         LocalDateTime.now());
   }

   @Override
   public ResponseBody<ShortLinkResponse> getResponseFromModel(ShortLink shortLink, int statusCode, String message) {
      final ShortLinkResponse shortLinkResponse = new ShortLinkResponse(
         shortLink.getUserEmail(),
         shortLink.getOriginalLink(),
         shortLink.getShortedLink(),
         shortLink.getExpiryDate(),
         shortLink.getCreationDate(),
         shortLink.getUpdatedTime());
      return new ResponseBody<>(statusCode, message, shortLinkResponse);
   }
}
