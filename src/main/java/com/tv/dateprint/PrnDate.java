/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tv.dateprint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.*;
//import java.awt.Graphics2D;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.RenderingHints;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author VVV
 */
public class PrnDate {
    
    File file;
    BufferedImage bi;
    double k;
    ScrDateText dateText;
    int fontSize;
    String paper;
    
    String fileName;
    
    //BufferedImage resultBi;
    
    public PrnDate(File file, double k, ScrDateText dateText, String paper) {
        this.file = file;
                
        try {
            this.bi = ImageIO.read(this.file);
        } catch (Exception e) {
            System.out.println (e.getMessage());
        }
        
//        this.x = x;
//        this.y = y;
        this.dateText = dateText;
        this.k = k;
        this.fontSize = (int)(this.dateText.getFontSize()/this.k);
        this.paper = paper;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
        this.fileName = formatter.format(dateText.ld) + "_" + file.getName();
        
    }
    
   
    
    public void printToFile(String path) {
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //Font fnt = new Font("SANS_SERIF", Font.BOLD, (int)(this.dateText.getFontSize()*k));
        Font fnt = new Font(this.dateText.getFontName(), Font.BOLD, this.fontSize);
        g.setFont(fnt);
        g.setColor(Color.BLACK);
        g.drawString(this.dateText.getValue(), (int)(this.dateText.getX()/k), (int)(this.dateText.getY()/k + this.fontSize));
        //g.dispose();
        
        int paperX = 2244;
        int paperY = 3272;
        if (!this.paper.equalsIgnoreCase("вертикально")) {
            int tmp = paperX;
            paperX = paperY;
            paperY = tmp;
        }
        BufferedImage resultBi = new BufferedImage(paperX, paperY, BufferedImage.TYPE_INT_RGB);
        //resultBi.setRGB(0, 0, 2244, 3272, rgbArray, fontSize, fontSize);
        Graphics result = resultBi.getGraphics();
        result.setColor(Color.WHITE);
        result.fillRect(0, 0, paperX, paperY);
        result.drawImage(bi, 0, 0, null);
        result.drawImage(bi, 1220, 0, null);
        result.drawImage(bi, 0, 1153, null);
        result.drawImage(bi, 1220, 1153, null);
        result.drawImage(bi, 0, 2272, null);
        result.drawImage(bi, 1220, 2272, null);
       
        try {
           ImageIO.write(resultBi, "jpg", new File(path));
           
//            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "jpeg", new FileOutputStream("3test.jpg"));
//            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", new File("A:/3test.png"));
    
        }catch (Exception e){
            System.out.println (e.getMessage());
        }
    }
    
    
}
