package com.example.quily.converter;

import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.ShortLinkResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.quily.constants.CommonConstants.MAX_LOCAL_TIME;
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
         LocalDateTime.now(),
         createShortLinkRequest.getIsQrRequest());
   }

   @Override
   public ResponseBody<ShortLinkResponse> getResponseFromModel(ShortLink shortLink, int statusCode, String message) {
      final ShortLinkResponse shortLinkResponse = new ShortLinkResponse(
         shortLink.getUserEmail(),
         shortLink.getOriginalLink(),
         shortLink.getShortedLink(),
         handleExpiryDateResponse(shortLink.getExpiryDate()),
         shortLink.getCreationDate(),
         shortLink.getUpdatedTime(),
         shortLink.isQrCreated());
      return new ResponseBody<>(statusCode, message, shortLinkResponse);
   }

   public static final LocalDateTime handleExpiryDateResponse(LocalDateTime expiryDate) {
      if (expiryDate.equals(LocalDateTime.parse(MAX_LOCAL_TIME, DateTimeFormatter.ISO_LOCAL_DATE_TIME))) {
         return null;
      } else {
         return expiryDate;
      }
   }
}
