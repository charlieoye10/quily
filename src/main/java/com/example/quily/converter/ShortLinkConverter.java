package com.example.quily.converter;

import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.util.CommonUtil;
import org.springframework.stereotype.Component;

import static com.example.quily.constants.ShortLinkConstants.SHORT_LINK_CREATED_MESSAGE;

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
         CommonUtil.getCurrentDateTimeInFormat(),
         createShortLinkRequest.getExpiryDate(),
         true);
   }

   @Override
   public ResponseBody<ShortLinkResponse> getResponseFromModel(ShortLink shortLink) {
      final ShortLinkResponse shortLinkResponse = new ShortLinkResponse(
         shortLink.getUserEmail(),
         shortLink.getOriginalLink(),
         shortLink.getShortedLink(),
         shortLink.getExpiryDate(),
         shortLink.getCreationDate());
      return new ResponseBody<>(200, SHORT_LINK_CREATED_MESSAGE, shortLinkResponse);
   }
}
