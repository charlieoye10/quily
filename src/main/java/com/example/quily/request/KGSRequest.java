package com.example.quily.request;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class KGSRequest {

    @NonNull private Long  id;

    @NonNull private int index1;

    @NonNull private int index2;

    @NonNull private int index3;

    @NonNull private int index4;

    @NonNull private int index5;

    @NonNull private int index6;
}


