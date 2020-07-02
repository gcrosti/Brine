package com.gdc.recipebook.screens.mealeditor.resources

import android.graphics.*
import java.util.*


fun TextToBitmap(text: String): Bitmap {
    val textForBit = text.toUpperCase(Locale.ROOT)
    val conf = Bitmap.Config.ARGB_8888
    val width = 500
    val height = 168
    val bitmap = Bitmap.createBitmap(width,height,conf)
    val typeface = Typeface.create("Helvetica",Typeface.BOLD)

    val paint = Paint()
    paint.style = Paint.Style.FILL
    paint.color = Color.BLACK
    paint.typeface = typeface
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = (200).toFloat()

    val rect = Rect()
    paint.getTextBounds(textForBit,0,textForBit.length,rect)

    val canvas = Canvas(bitmap)
    val xpos = (width/2).toFloat()
    val ypos = (height - 10).toFloat()

    canvas.drawText(textForBit,xpos,ypos,paint)

    return bitmap

}