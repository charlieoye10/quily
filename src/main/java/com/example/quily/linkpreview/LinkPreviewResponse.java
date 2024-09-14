package com.example.quily.linkpreview;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LinkPreviewResponse {
   private String title;
   private String description;
   private String url;
   private int error;
   private String errorMessage;
}
