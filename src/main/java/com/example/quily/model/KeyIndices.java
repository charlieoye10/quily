package com.example.quily.model;

import com.example.quily.request.KGSRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table(name = "key_indices")
public class KeyIndices {
    @Id
    private Long id;

    private int index1;

    private int index2;

    private int index3;

    private int index4;

    private int index5;

    private int index6;

    public KGSRequest toKGSRequest() {
        return new KGSRequest(id, index1, index2, index3, index4, index5, index6);
    }
}