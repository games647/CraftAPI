package com.github.games647.craftapi.model.skin;

import com.google.gson.annotations.SerializedName;

/**
 * Skin texture models
 */
public enum Model {

    /**
     * Default model with square arms (Steven style)
     */
    SQUARE,

    /**
     * Slim model with slim arms (Alex style)
     */
    @SerializedName("slim")
    SLIM

}
