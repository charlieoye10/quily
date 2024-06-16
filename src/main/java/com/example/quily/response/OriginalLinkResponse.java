package com.example.quily.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OriginalLinkResponse {
	public String originalLink;

	public ResponseBody<OriginalLinkResponse> toSuccessResponse() {
		return new ResponseBody<>(200, "", this);
	};
}
