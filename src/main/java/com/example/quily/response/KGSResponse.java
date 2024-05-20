package com.example.quily.response;

public class KGSResponse {
    private int [] indices;

    private String hashKey;

    public KGSResponse(int[] indices, String hashKey) {
        this.indices = indices;
        this.hashKey = hashKey;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }
}