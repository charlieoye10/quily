package com.example.quily.DAO;

import com.example.quily.model.KeyIndices;

public interface KeyGeneratorDAO {
    final int hashStringLength = 6; // put value in application
    final int baseSize = 62; // same

    final String base62_1 = "mkKVUs0DQjxfMOLhe2HclrS183ZCPydYEwNJRt7IvuqWF9z65pbonBa4AiGgXT"; // same if possible
    final String base62_2 = "Y7LnRwDTbzio5Vjhf8GZN0HmcFEyp9Xuga6W2UPql3ABJdrCSI4tQO1MKeskvx";
    final String base62_3 = "SWUKYIEkjNo9lVyhzZv1mg0Tipf2wHrC5anx3RuOcG4PdsebF8JXQDt7MLAq6B";
    final String base62_4 = "rgv0ubZdSf21WtUAsGh7p3YNax5QBcTPXR4LKk8HoeMjm6znIqV9OywlDCJEFi";
    final String base62_5 = "CFVTYfpIqo6zkGPOAiLWHcemdrRMnStv7lBDJa2Z1jsyKu84wE5xhQNX93gU0b";
    final String base62_6 = "EqSIFVbuXDZPlpWYnKTmOi7cA3ew0Uhy1Qt84gkBdz2RfJoGaxv5s6Cjr9HNLM";

    int [] toArray(KeyIndices keyIndices);

    void updateIndices(int [] indexArray, int currentIndex, int carry);

    KeyIndices getUpdatedIndices(KeyIndices keyIndices);

    String giveSixLengthHash(KeyIndices keyIndices);
}
