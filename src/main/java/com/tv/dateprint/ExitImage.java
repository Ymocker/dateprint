/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tv.dateprint;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.time.format.DateTimeFormatter;
//import javafx.scene.text.FontWeight;
import javax.imageio.ImageIO;

/**
 *
 * @author VVV
 */
public class ExitImage {
    
    BufferedImage bi, resultBi;
    File file;
    int dpi = 300;
    double screenToReal; // screen to real ratio
    ScrDateText dateText;
    int fontSize;
    int fontStyle = 1; // 0-plain, 1-bold, 2-italic, 3-bold-italic
    //double pageW = 21, pageH = 29.7;   page dimensions
    boolean keepRate = true;
    boolean pageVert = true;
    boolean withDate = true;
    boolean withMarks = true;
    boolean withImg = true;
    double imgW, imgH; // original image dimensions
    double whImgW, whImgH; // dimensions of place for image, 19x27.7 cm (in px)
    int qtyW, qtyH; //quantity images in Hoisontal and Vertical directions
    //double leftM = 1, rightM = 1, topM = 1, bottomM = 1;
    double gapW, gapH;
    String fileName;
    
    
    public ExitImage() {
    //public ExitImage(File file, double screenRatio, ScrDateText dateText, int dpi, double imgW, double imgH) {
        //this.file = file;
        //this.dpi = ;
        //this.screenRatio = screenRatio;
        //this.dateText = dateText;
        this.whImgW = 19/2.54*this.dpi;
        this.whImgH = 27.7/2.54*this.dpi;
        this.qtyW = 2;
        this.qtyH = 3;
        this.gapW = 1.5/2.54*this.dpi;
        this.gapH = 1.5/2.54*this.dpi;
        
        
    } 
    
    public void setPageOrient(String page) {
        this.pageVert = page.equalsIgnoreCase("вертикально");
        if ((this.pageVert) ^ (this.whImgW < this.whImgH)) { 
            double tmp;
            tmp = this.whImgW;
            this.whImgW = this.whImgH;
            this.whImgH = tmp;
        }
    }
    
    public void resetDpi(int dpi) {
        this.dpi = dpi;
        this.whImgW = 19/2.54*this.dpi;
        this.whImgH = 27.7/2.54*this.dpi;
    }
    
    public void MakeImage() {
        try {
            bi = ImageIO.read(this.file);
        } catch (Exception e) {
            System.out.println (e.getMessage());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
        this.fileName = formatter.format(dateText.ld) + "_" + file.getName();

        double allWidthWoGaps = imgW*qtyW; // image width * 2
        double allHeightWoGaps = imgH*qtyH; // image height * 3
        
        double endImgWidthWoGaps = this.whImgW - gapW*(qtyW - 1);  // 19cm*118cpi-gap*qty_of_gaps
        double endImgHeightWoGaps = this.whImgH - gapH*(qtyH - 1); // 27.7cm*118cpi-gap*qty_of_gaps

        double k = (allWidthWoGaps/endImgWidthWoGaps > allHeightWoGaps/endImgHeightWoGaps) ? allWidthWoGaps/endImgWidthWoGaps : allHeightWoGaps/endImgHeightWoGaps;
        
        int newImgW = (int)Math.floor(imgW/k);
        int newImgH = (int)Math.floor(imgH/k);
        
        int newAllImgW = (int)(newImgW*qtyW + gapW*(qtyW - 1));
        int newAllImgH = (int)(newImgH*qtyH + gapH*(qtyH - 1));
        
        fontStyle = 0;
        if (dateText.fontBold) {
            fontStyle++;
        }
        if (dateText.fontItalic) {
            fontStyle = fontStyle + 2;
        }
        
        resultBi = new BufferedImage(newAllImgW, newAllImgH, BufferedImage.TYPE_INT_RGB);
        Graphics result = resultBi.getGraphics();
        result.setColor(Color.WHITE);
        result.fillRect(0, 0, newAllImgW, newAllImgH);
 //------------------------
        Image scaledImg = bi.getScaledInstance(1, 1, Image.SCALE_SMOOTH); // minimal initialisation
        BufferedImage resizedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB); // minimal initialisation
    
        if (k>0.7) {
            Graphics2D g = bi.createGraphics();
            if (!withImg) {
                g.fillRect(0, 0, (int)imgW, (int)imgH);
            }
            if (withDate) {
               g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                //Font fnt = new Font("SANS_SERIF", Font.BOLD, (int)(this.dateText.getFontSize()*k));
                this.fontSize = (int)(this.dateText.getFontSize()/screenToReal);
                Font fnt = new Font(this.dateText.getFontName(), fontStyle, this.fontSize);
                g.setFont(fnt);
                g.setColor(Color.BLACK);
                g.drawString(this.dateText.getValue(), (int)(this.dateText.getX()/screenToReal), (int)(this.dateText.getY()/screenToReal + this.fontSize));
                //g.dispose(); 
            }
            scaledImg = bi.getScaledInstance(newImgW, newImgH, Image.SCALE_SMOOTH);
        } else {
            resizedImage = new BufferedImage(newImgW, newImgH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (withImg) {
                g.drawImage(bi, 0, 0, newImgW, newImgH, null);
            } else {
                g.fillRect(0, 0, newImgW, newImgH);
            }
            
            if (withDate) {
                //Font fnt = new Font("SANS_SERIF", Font.BOLD, (int)(this.dateText.getFontSize()*k));
                double newK = screenToReal*k;
                this.fontSize = (int)(this.dateText.getFontSize()/newK);
                Font fnt = new Font(this.dateText.getFontName(), fontStyle, this.fontSize);
                g.setFont(fnt);
                g.setColor(Color.BLACK);
                g.drawString(this.dateText.getValue(), (int)(this.dateText.getX()/newK), (int)(this.dateText.getY()/newK + this.fontSize));
            }
        }             
                       
        for (int i = 0; i < qtyH; i++) {
            for (int j = 0; j < qtyW; j++) {
                if (k>0.7) {
                    result.drawImage(scaledImg, newImgW*j+(int)(gapW*j), newImgH*i+(int)(gapH*i), null);
                } else {
                    result.drawImage(resizedImage, newImgW*j+(int)(gapW*j), newImgH*i+(int)(gapH*i), null);
                }
            }
        }
        
        if (withMarks) {
            result.setColor(Color.BLACK);
            int stepX = newImgW + (int)gapW;
            int stepY = newImgH + (int)gapH;
            
            for (int i = 0; i < qtyH+1; i++) {
                for (int j = 0; j < qtyW+1; j++) {
                    result.drawLine(stepX*j - (int)gapW/2, stepY*i - (int)Math.round(gapH*1.5), stepX*j -(int)gapW/2, stepY*i+(int)gapH/2);
                    //result.drawLine(stepX*j - (int)Math.round(gapW*1.5), stepY*i - (int)gapW/2, stepY*i+(int)gapW/2, stepY*i - (int)gapW/2);
                    result.drawLine(stepX*j - (int)Math.round(gapW*1.5), stepY*i - (int)gapH/2, stepX*j +(int)gapW/2, stepY*i - (int)gapH/2);
                }
            }
        }

    }
    
    public void printToFile(String path) {
        try {
           ImageIO.write(resultBi, "jpg", new File(path));
           
//            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "jpeg", new FileOutputStream("3test.jpg"));
//            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", new File("A:/3test.png"));
    
        }catch (Exception e){
            System.out.println (e.getMessage());
        }
    
    }
    
    public void printToPrinter() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        
        PageFormat pf = printJob.defaultPage();
        Paper paper = pf.getPaper();
        if (this.pageVert) {
            pf.setOrientation(PageFormat.PORTRAIT);
        } else {
            pf.setOrientation(PageFormat.LANDSCAPE);
        }
        
        paper.setSize(595.275590551181, 841.8897637795276); // A4 210x297 mm
        paper.setImageableArea(28.346456692913385, 28.346456692913385, 538.5826771653543, 785.1968503937007);  // left 10 top 10 190x297 mm
        pf.setPaper(paper);
//        pf = printJob.pageDialog(pf);
//        Paper paper = pf.getPaper();
        
        
//        System.out.println(paper.getWidth() + " / " + paper.getHeight() + " / " + paper.getImageableWidth() + " / " + paper.getImageableHeight());
        printJob.setPrintable(new ImagePrintable(printJob, resultBi, pf), pf);
        
        try {
            if (printJob.printDialog()) {
                printJob.print();
            }
          
        } catch (PrinterException e1) {
          e1.printStackTrace();
        }
        

    }
    
    
    class ImagePrintable implements Printable {
        private double x, y, width, height; 
        private int orientation; 
        private BufferedImage image; 
        public ImagePrintable(PrinterJob printJob, BufferedImage image, PageFormat pageFormat) {
            //PageFormat pageFormat = printJob.defaultPage(); 
            this.x = pageFormat.getImageableX(); 
            this.y = pageFormat.getImageableY(); 
            this.width = pageFormat.getImageableWidth();
            this.height = pageFormat.getImageableHeight();
            this.orientation = pageFormat.getOrientation(); 
            this.image = image; 
        }
        
        @Override 
        public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException { 
            if (pageIndex == 0) {
                int pWidth = 0; 
                int pHeight = 0; 
                /*if (orientation == PageFormat.PORTRAIT) { */
                    pWidth = (int)this.width; 
                    pHeight = (int)this.height;
                /*} else { 
                    pWidth = (int)this.height; 
                    pHeight = (int)this.width;
                } */
                g.drawImage(image, (int)x, (int)y, pWidth, pHeight, null); 
                return PAGE_EXISTS; 
            } else { 
                return NO_SUCH_PAGE; 
            } 
        }
    }
    
    
    
} // end of class Measure


/*        
        import java.awt.Graphics; 
import java.awt.image.BufferedImage; 
import java.awt.print.PageFormat; 
import java.awt.print.Printable; 
import java.awt.print.PrinterException; 
import java.awt.print.PrinterJob; 

public class PrintActionListener implements Runnable {
    private BufferedImage image; 
    
    public PrintActionListener(BufferedImage image) {
        this.image = image; 
    } 
    @Override public void run() {
        PrinterJob printJob = PrinterJob.getPrinterJob(); 
        printJob.setPrintable(new ImagePrintable(printJob, image)); 
        if (printJob.printDialog()) {
            try { 
                printJob.print(); 
            } catch (PrinterException prt) {
                prt.printStackTrace(); 
            } 
        } 
    } 
    
    public class ImagePrintable implements Printable {
        private double x, y, width; 
        private int orientation; 
        private BufferedImage image; 
        public ImagePrintable(PrinterJob printJob, BufferedImage image) {
            PageFormat pageFormat = printJob.defaultPage(); 
            this.x = pageFormat.getImageableX(); 
            this.y = pageFormat.getImageableY(); 
            this.width = pageFormat.getImageableWidth(); 
            this.orientation = pageFormat.getOrientation(); 
            this.image = image; 
        } 
    }
    
    @Override 
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException { 
        if (pageIndex == 0) {
            int pWidth = 0; 
            int pHeight = 0; 
            if (orientation == PageFormat.PORTRAIT) { 
                pWidth = (int) Math.min(width, (double) image.getWidth()); 
                pHeight = pWidth * image.getHeight() / image.getWidth(); 
            } else { 
                pHeight = (int) Math.min(width, (double) image.getHeight()); 
                pWidth = pHeight * image.getWidth() / image.getHeight(); 
            } 
            g.drawImage(image, (int) x, (int) y, pWidth, pHeight, null); 
            return PAGE_EXISTS; 
        } else { 
            return NO_SUCH_PAGE; 
        } 
    } 
}  
 */       

/*
 
        <ComboBox fx:id="fxFontList" layoutX="754.0" layoutY="166.0" prefHeight="34.0" prefWidth="140.0" AnchorPane.rightAnchor="130.0">
            <items>
                <FXCollections fx:factory="observableArrayList">
                    <String fx:value="вертикально" />
                    <String fx:value="горизонтально" />
                </FXCollections>
            </items>
            <value>
                <String fx:value="вертикально" />
            </value>
        </ComboBox>

*/