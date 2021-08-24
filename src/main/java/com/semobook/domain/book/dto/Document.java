package com.semobook.domain.book.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Document {
    private ArrayList<String> authors = new ArrayList();
    private String contents;
    private String datetime;
    private String isbn;
    private String price;
    private String publisher;
    private String sale_price;
    private String status;
    private String thumbnail;
    private String title;
    private ArrayList<String> translators = new ArrayList<>();
    private String url;
}
