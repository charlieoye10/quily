package com.example.quily.converter;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.request.ExpiryDateRequest;
import com.example.quily.response.ExpiryDateResponse;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.util.CommonUtil;
import org.springframework.stereotype.Component;

@Component
public class ShortLinkConverter implements Converter<CreateShortLinkRequest, ShortLinkResponse, ShortLink> {
   @Override
   public ShortLink getModelFromRequest(CreateShortLinkRequest createShortLinkRequest) {
      return null;
   }

   public ShortLink convertRequestToModel(CreateShortLinkRequest createShortLinkRequest, String shortLink) {
      ExpiryDateRequest expiry = createShortLinkRequest.getExpiryDate();
      String expiryDate = expiry.getDate().concat(" ").concat(expiry.getHour());

      return new ShortLink(
         createShortLinkRequest.getUserID(),
         createShortLinkRequest.getOriginalLink(),
         shortLink,
         CommonUtil.getCurrentDateTimeInFormat(),
         expiryDate,
         true);
   }

   @Override
   public ResponseBody<ShortLinkResponse> getResponseFromModel(ShortLink shortLink) {
      String[] expiryDate = shortLink.getExpiryDate().split(" ");
      String date = expiryDate[0];
      String hour = expiryDate[1];

      ExpiryDateResponse expiryDateResponse = new ExpiryDateResponse();
      expiryDateResponse.setDate(date);
      expiryDateResponse.setHour(hour);

      final ShortLinkResponse shortLinkResponse = new ShortLinkResponse(
         shortLink.getUserID(),
         shortLink.getOriginalLink(),
         shortLink.getShortedLink(),
         expiryDateResponse,
         shortLink.getCreationDate());
      final String message = ShortLinkConstants.SHORT_LINK_CREATED_MESSAGE;
      return new ResponseBody<>(200, message, shortLinkResponse);
   }
}
