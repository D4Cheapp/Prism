package com.prism.messenger.model;

import lombok.Data;

@Data
public class TextResponseModel {
    private String info;
    private String error;

    public static TextResponseModel toTextResponseModel(String text, boolean success) {
        TextResponseModel model = new TextResponseModel();
        if (success) {
            model.setInfo(text);
        } else {
            model.setError(text);
        }
        return model;
    }
}
