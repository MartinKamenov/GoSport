package com.kamenov.martin.gosportbg.models.engine;

import com.kamenov.martin.gosportbg.constants.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Martin on 1.10.2018 г..
 */

public class ImageBorderService {
    public static void addBorders(CircleImageView img) {
        img.setBorderColor(Constants.SECONDCOLOR);
        img.setBorderWidth(3);
    }
}
