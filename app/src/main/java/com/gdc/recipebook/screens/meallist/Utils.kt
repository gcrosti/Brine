package com.gdc.recipebook.screens.meallist

import com.gdc.recipebook.database.dataclasses.MealFunction


fun convertListToString(list: List<String>): String {
    val stringLength = list.size - 2
    return list.toString().slice(IntRange(1,stringLength))
}

fun convertFuncsToNameList(mealFunction: MealFunction): List<String> {
    val stringList = mutableListOf<String>()
    if(mealFunction.beverage) {stringList.add("beverage")}
    if(mealFunction.dessert) {stringList.add("dessert")}
    if(mealFunction.dip) {stringList.add("dip")}
    if(mealFunction.dressing) {stringList.add("dressing")}
    if(mealFunction.ingredient) { stringList.add("ingredient")}
    if(mealFunction.protein) {stringList.add("protein")}
    if(mealFunction.starch) {stringList.add("starch")}
    if(mealFunction.veg) {stringList.add("vegetable")}

    return stringList
}