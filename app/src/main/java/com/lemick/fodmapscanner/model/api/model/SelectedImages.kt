package com.lemick.fodmapscanner.model.api.model

import com.fasterxml.jackson.annotation.JsonProperty


data class SelectedImages (

    @JsonProperty("front") var front : Front?,
    @JsonProperty("ingredients") var ingredients : Ingredients?,
    @JsonProperty("nutrition") var nutrition : Nutrition?

)