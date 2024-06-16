package com.example.quily.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KGSResponse {
    private int [] indices;

    private String hashKey;

    public ResponseBody<KGSResponse> toSuccessResponse() {
        return new ResponseBody<>(200, "", this);
    }
}