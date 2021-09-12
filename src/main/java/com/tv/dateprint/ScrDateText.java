/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tv.dateprint;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 *
 * @author VVV
 */
public class ScrDateText {
    LocalDate ld = LocalDate.now();
    private String val = "Дата";
    private double lbX = 0;
    private double lbY = 0;
    private String fontName = "Arial";
    private FontWeight fWeight = FontWeight.BOLD;
     boolean fontBold = true;
    private FontPosture fPosture = FontPosture.REGULAR;
     boolean fontItalic = false;
    private int fontSize = 24;
    private Font labelFont = new Font(fontName, fontSize);
    private double labelWidth = 150;
    private double labelHeight = 35;
    
    
    public void setValue(LocalDate d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.val = formatter.format(d);
        this.ld = d;

    }
    public String getValue() {
        return val;
    }
    
    public void setX(double x) {
        this.lbX = x;
    }
    public double getX() {
        return lbX;
    }
    
    public void setY(double y) {
        this.lbY = y;
    }
    public double getY() {
        return lbY;
    }
       
    public void setLabelWidth(double w) {
        this.labelWidth = w;
    }
    public double getLabelWidth() {
        return labelWidth;
    }
    
    public void setLabelHeight(double h) {
        this.labelHeight = h;
    }
    public double getLabelHeight() {
        return labelHeight;
    }
    
    public void setFontName(String fontName) {
        this.fontName = fontName;    }    
    public String getFontName() {
        return fontName;
    }
    
    public void setFontSize(int s) {
        this.fontSize = s;
    }
    public int getFontSize() {
        return fontSize;
    }
    
    public void setFontWeight(FontWeight fw) {
        this.fWeight = fw;
        this.fontBold = fw == FontWeight.BOLD;
    }
    public FontWeight getFontWeight() {
        return fWeight;
    }
    
    public void setFontPosture(FontPosture fp) {
        this.fPosture = fp;
        this.fontItalic = fp == FontPosture.ITALIC;
    }
    public FontPosture getFontPosture() {
        return fPosture;
    }
    
    public Font getFont() {
        Font font = Font.font(this.fontName, this.fWeight, this.fPosture, this.fontSize);
        //labelFont.font(Font.font(this.fontName, this.fWeight, this.fontSize));
        labelFont = font;
        return labelFont;
    }
    
}