package com.example.quily.model;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Entity
@Table(name = "key_indices")
public class KeyIndices {
    @jakarta.persistence.Id
    @Id
    @Column("id")
    private Long id;

    private int index1;

    private int index2;

    private int index3;

    private int index4;

    private int index5;

    private int index6;

    public KeyIndices(Long id, int index1, int index2, int index3, int index4, int index5, int index6) {
        this.id = id;
        this.index1 = index1;
        this.index2 = index2;
        this.index3 = index3;
        this.index4 = index4;
        this.index5 = index5;
        this.index6 = index6;
    }

    public KeyIndices() {}

    @Override
    public String toString() {
        return "KeyGeneratorIndices{" +
                "firstIndex=" + index1 +
                ", secondIndex=" + index2 +
                ", thirdIndex=" + index3 +
                ", forthIndex=" + index4 +
                ", fifthIndex=" + index5 +
                ", sixthIndex=" + index6 +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex1() {
        return index1;
    }

    public void setIndex1(int firstIndex) {
        this.index1 = firstIndex;
    }

    public int getIndex2() {
        return index2;
    }

    public void setIndex2(int secondIndex) {
        this.index2 = secondIndex;
    }

    public int getIndex3() {
        return index3;
    }

    public void setIndex3(int thirdIndex) {
        this.index3 = thirdIndex;
    }

    public int getIndex4() {
        return index4;
    }

    public void setIndex4(int forthIndex) {
        this.index4 = forthIndex;
    }

    public int getIndex5() {
        return index5;
    }

    public void setIndex5(int fifthIndex) {
        this.index5 = fifthIndex;
    }

    public int getIndex6() {
        return index6;
    }

    public void setIndex6(int sixthIndex) {
        this.index6 = sixthIndex;
    }
}
