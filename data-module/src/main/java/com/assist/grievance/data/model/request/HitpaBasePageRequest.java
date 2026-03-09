package com.assist.grievance.data.model.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class HitpaBasePageRequest {

    @JsonSetter(nulls = Nulls.SKIP)
    private int page = 0;

    @JsonSetter(nulls = Nulls.SKIP)
    private int size = 10;

    private List<String> sort = List.of("id");

    private String sortDirection = "desc";
}
