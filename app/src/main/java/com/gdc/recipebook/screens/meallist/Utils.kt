package com.gdc.recipebook.screens.meallist

import com.gdc.recipebook.database.dataclasses.MealFunction


fun convertListToString(list: List<String>): String {
    val string = list.toString()
    val stringLength = string.length - 2
    return string.slice(IntRange(1,stringLength))
}

fun convertFuncsToNameList(mealFunction: MealFunction): List<String> {
    val stringList = mutableListOf<String>()
    if(mealFunction.beverage) {stringList.add(" \uD83C\uDF79 Beverage")}
    if(mealFunction.dessert) {stringList.add(" \uD83C\uDF69 Dessert")}
    if(mealFunction.dip) {stringList.add(" \uD83E\uDD63 Dip")}
    if(mealFunction.dressing) {stringList.add(" \uD83E\uDD57 Dressing")}
    if(mealFunction.ingredient) { stringList.add(" \uD83C\uDF36 ️Ingredient")}
    if(mealFunction.protein) {stringList.add(" \uD83C\uDF56 Protein")}
    if(mealFunction.starch) {stringList.add(" \uD83E\uDD56 Starch")}
    if(mealFunction.veg) {stringList.add(" \uD83E\uDD66 Vegetable")}

    return stringList
}