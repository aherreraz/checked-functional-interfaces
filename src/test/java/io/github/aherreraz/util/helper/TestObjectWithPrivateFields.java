package io.github.aherreraz.util.helper;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class TestObjectWithPrivateFields {
    private Integer integerField;
    private Integer integerField2;
    private Integer integerField3;
    @Getter(AccessLevel.NONE)
    private Integer privateIntegerField;
}