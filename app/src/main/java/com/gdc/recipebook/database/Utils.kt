package com.gdc.recipebook.database

import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.Resource


fun findImagesForDeletion(imagesFromEditor: ImagesFromEditor): List<Image>? {
    val result = imagesFromEditor.loadedImages.toMutableList()
    imagesFromEditor.savedImages.forEach {
        result.remove(it)
    }
    return result
}

fun findImagesForInsertion(imagesFromEditor: ImagesFromEditor): List<Image>? {
    val result = imagesFromEditor.savedImages.toMutableList()
    imagesFromEditor.loadedImages.forEach {
        result.remove(it)
    }
    return result
}


fun findResourcesForDeletion(resourcesFromEditor: ResourcesFromEditor): List<Resource>? {
    val result = resourcesFromEditor.loadedResources.toMutableList()
    resourcesFromEditor.savedResources.forEach {
        result.remove(it)
    }
    return result
}

fun findResourcesForInsertion(resourcesFromEditor: ResourcesFromEditor): List<Resource>? {
    val result = resourcesFromEditor.savedResources.toMutableList()
    resourcesFromEditor.loadedResources.forEach {
        result.remove(it)
    }
    return result
}