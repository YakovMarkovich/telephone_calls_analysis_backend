package io.shelfy.telephone_analysis_application.dto;

public class CallerDto {
    public long id;
    public String email;
    public String first_name;
    public String last_name;
    public String gender;
    public String image;

    public CallerDto(){}

    public CallerDto(long id, String email, String first_name, String last_name, String gender, String image) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.image = image;
    }
}
