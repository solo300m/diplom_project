package com.example.MyProjectWithSecurity.data;

public class SearchIdDto {
    private Integer idParam;

    public SearchIdDto() {
    }

    public SearchIdDto(Integer idParam) {
        this.idParam = idParam;
    }

    public Integer getIdParam() {
        return idParam;
    }

    public void setIdParam(Integer idParam) {
        this.idParam = idParam;
    }
}
