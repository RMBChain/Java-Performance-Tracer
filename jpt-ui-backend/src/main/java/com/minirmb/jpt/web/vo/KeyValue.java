package com.minirmb.jpt.web.vo;

import lombok.Data;

@Data
public class KeyValue {
    private String text;
    private String value;

    public KeyValue(){
    }

    public KeyValue( String text, String value){
        this.text = text;
        this.value = value;
    }
}
