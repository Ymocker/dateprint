/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tv.dateprint;

import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ComboBox;
//import javafx.scene.control.SpinnerValueFactory;
//import java.net.URL;
//import java.util.ResourceBundle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.FileOutputStream;
import static java.lang.Math.floor;
import java.time.LocalDate;
import java.util.Properties;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
//import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
//import javafx.scene.image.PixelReader;
//import javafx.scene.image.PixelWriter;
//import javafx.scene.image.WritableImage;
//import javafx.scene.paint.Color;
import javafx.stage.Stage;

//import javafx.embed.swing.*;
//import javafx.scene.control.SpinnerValueFactory;//.IntegerSpinnerValueFactory;

public class DateprintController {

@FXML
public AnchorPane fxPane;
@FXML
public ImageView fxImg;
@FXML
public Canvas fxCn;
@FXML
public DatePicker fxDate;
@FXML
public Label fxPrintDate;
@FXML
public Label fxInfo;
@FXML
public Spinner fxFontSize;
@FXML
public ComboBox<String> fxFontList;
@FXML
public ComboBox<String> fxPaperOrient;
@FXML
public CheckBox fxDateCheck;
@FXML
public CheckBox fxImgCheck;
@FXML
public CheckBox fxFontWeight;
@FXML
public CheckBox fxFontPosture;
@FXML
public CheckBox fxMarksCheck;

Properties appProps = new Properties();
FileChooser fileChooser = new FileChooser();
File file, saveFile;
int imW, imH;
int W = 200, H = 50;
double kW, kH, lbX, lbY;
ScrDateText dateText;
ExitImage exImage;


@FXML
private Button fxMakeFile;
@FXML
private Button fxPrint;
@FXML
private Button fxOpen;
@FXML
private Button fxExit;
@FXML
public Spinner fxXaxis; //
@FXML
public Spinner fxYaxis;
@FXML
public Spinner fxXgap;
@FXML
public Spinner fxYgap;
@FXML
public Spinner fxDpi;

    

public void initialize() throws FileNotFoundException {

    try {
        //appProps.load(new FileInputStream("dateprint.prop"));
        appProps.loadFromXML(new FileInputStream("dateprintprop.xml"));
    } catch(IOException e){
        appProps.setProperty("dir", "c:");
    }
    
    fxCn.widthProperty().bind(fxPane.widthProperty().subtract(400));
    fxCn.heightProperty().bind(fxPane.heightProperty().subtract(30));
    //fxPrintDate.prefWidthProperty().bind(fxCn.widthProperty().divide(4.0));
    //fxInfo.textProperty().bind(fxPrintDate.prefWidthProperty().asString());
    
    fxDate.setValue(LocalDate.now());
    
    dateText = new ScrDateText();
    exImage = new ExitImage();
    
    dateText.setValue(fxDate.getValue());
    dateText.setLabelWidth(fxPrintDate.getPrefWidth());
    dateText.setLabelHeight(fxPrintDate.getPrefHeight());
       
    fxFontSize.getValueFactory().setValue(dateText.getFontSize());
    fxPrintDate.setFont(dateText.getFont());
    fxPrintDate.setText(dateText.getValue());
    
    ObservableList<String> systemFonts = FXCollections.observableArrayList(Font.getFamilies());
    fxFontList.setItems(systemFonts);
    fxFontList.setValue("Arial");
    
    fxFontSize.valueProperty().addListener(new ChangeListener<Number>() {
	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {		
            dateText.setFontSize((int)newValue);
            fxPrintDate.setFont(dateText.getFont());
	}
    });
    
    
    fxXaxis.valueProperty().addListener(new ChangeListener<Number>() {
	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {		
            exImage.qtyW = (int)newValue;
	}
    });
    fxYaxis.valueProperty().addListener(new ChangeListener<Number>() {
	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {		
            exImage.qtyH = (int)newValue;
	}
    });
    fxXgap.valueProperty().addListener(new ChangeListener<Number>() {
	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {		
            exImage.gapW = (double)newValue/2.54*exImage.dpi;
	}
    });
    fxYgap.valueProperty().addListener(new ChangeListener<Number>() {
	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {		
		exImage.gapH = (double)newValue/2.54*exImage.dpi;
	}
    });
    fxDpi.valueProperty().addListener(new ChangeListener<Number>() {
	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            exImage.resetDpi((int)newValue);
            exImage.gapW = (double)fxXgap.getValue()/2.54*exImage.dpi;
            exImage.gapH = (double)fxYgap.getValue()/2.54*exImage.dpi;
	}
    });
    
    fxPrintDate.setOnMousePressed(e -> {
        lbX = fxPrintDate.getTranslateX()-e.getSceneX();
        lbY = fxPrintDate.getTranslateY()-e.getSceneY();
    });
        
    fxPrintDate.setOnMouseDragged(e -> {        
        if (lbX+e.getSceneX() < 0) {
            fxPrintDate.setTranslateX(0);
        } else if (lbX+e.getSceneX() > W - dateText.getLabelWidth()) {
            fxPrintDate.setTranslateX(W - dateText.getLabelWidth());
            //fxPrintDate.setTextAlignment(TextAlignment.RIGHT);
        } else {   
            fxPrintDate.setTranslateX(lbX+e.getSceneX());
        }
        
        if (lbY+e.getSceneY() < 0) {
            fxPrintDate.setTranslateY(0);
        } else if (lbY+e.getSceneY() > H - dateText.getLabelHeight()) {
            fxPrintDate.setTranslateY(H - dateText.getLabelHeight());
        } else {   
            fxPrintDate.setTranslateY(lbY+e.getSceneY());
        }

        dateText.setX(fxPrintDate.getTranslateX());
        dateText.setY(fxPrintDate.getTranslateY());
    });
}    


@FXML
private void clickOpen(ActionEvent event) {
    Stage s = App.getPrimaryStage();
    
    fileChooser.setTitle("Open Image File");
    FileChooser.ExtensionFilter extFilterO = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
    fileChooser.getExtensionFilters().add(extFilterO);
    
    if (new File(appProps.getProperty("dir")).isDirectory()) { // if someone sets illegal directory name in the properties file
        fileChooser.setInitialDirectory(new File(appProps.getProperty("dir")));
    } else {
        fileChooser.setInitialDirectory(new File("c:"));
    }
    
    file = fileChooser.showOpenDialog(s); //choose file with fileOpenDialog
    
    if (file != null) {
        fileChooser.setInitialDirectory(new File(file.getParent()));
        appProps.setProperty("dir", file.getParent());
        
        
        Image im = new Image("file:" + file.toString());
        
//        ImageView iv = fxImg;
        Canvas cn = fxCn;
        GraphicsContext gc = cn.getGraphicsContext2D();
        gc.clearRect(0, 0, cn.getWidth(), cn.getHeight());
        
        // calculate the rate to scale image to put it into the canvas        
        imW = (int)im.getWidth();
        imH = (int)im.getHeight();        
        kW = (floor(cn.getWidth()*100/imW))/100;
        kH = (floor(cn.getHeight()*100/imH))/100;
        if (kW > kH) { kW = kH; } // kW - scale ratio
        W = (int)(imW * kW);
        H = (int)(imH * kW);
        
        exImage.file = file;
        exImage.imgW = im.getWidth();
        exImage.imgH = im.getHeight();
        exImage.screenToReal = kW;
        
        gc.drawImage(im, 0, 0, W, H);
        
        fxPrintDate.setTranslateX(0);
        fxPrintDate.setTranslateY(0);
        fxPrintDate.setPrefWidth(fxCn.getWidth()/4);
//        fxInfo.setText(String.valueOf(fxPrintDate.getLayoutBounds().getMinX()));
        //fxInfo.setText(file.getName());
       
/*        
        PixelReader pixelReader = im.getPixelReader();
        WritableImage wim = new WritableImage(pixelReader, imW, imH);
        PixelWriter writer = wim.getPixelWriter();
        
        for(int y = 0; y < imH; y++) { 
            for(int x = 0; x < imW; x++) { 
               //Retrieving the color of the pixel of the loaded image   
               Color color = pixelReader.getColor(x, y);                
               //Setting the color to the writable image 
               writer.setColor(x, y, color);              
            }
        }
        
        //file = new File("A:\\ttttttest.jpg");
        //ImageIO.write(wim, "jpg", new File("/tttest.jpg"));
        
        try {
            //BufferedImage awtImage = new BufferedImage((int)img.getWidth(), (int)img.getHeight(), BufferedImage.TYPE_INT_RGB);
            
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "jpeg", new FileOutputStream("3test.jpg"));
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", new File("A:/3test.png"));
    
        }catch (Exception e){
            System.out.println (e.getMessage());
        }
*/
    }
}

@FXML
private void clickMake(ActionEvent event) {
    exImage.dateText = this.dateText;
    //exImage.withDate = !fxEmpty.isSelected();
    exImage.withImg = fxImgCheck.isSelected();
    exImage.withMarks = fxMarksCheck.isSelected();
    
    exImage.MakeImage();
    
    Stage s = App.getPrimaryStage();
    fileChooser.setTitle("Сохранить файл");
//    FileChooser.ExtensionFilter extFilterS = new FileChooser.ExtensionFilter("Image Files", "*.jpg");
//    fileChooser.getExtensionFilters().add(extFilterS);
    fileChooser.setInitialDirectory(file.getParentFile());
    fileChooser.setInitialFileName(exImage.fileName);
    saveFile = fileChooser.showSaveDialog(s); //file save dialog
    if (saveFile != null) {
        exImage.printToFile(saveFile.getAbsolutePath());
    } 
}

@FXML
private void clickPrint(ActionEvent event) {
    
//   send the job to printer
    exImage.dateText = this.dateText;
    exImage.withDate = fxDateCheck.isSelected();

    exImage.MakeImage();
    exImage.printToPrinter();


    
    //PrinterJob printJob = PrinterJob.getPrinterJob();
    /*
    PrinterJob job = PrinterJob.createPrinterJob();
 if (job != null) {
    boolean success = job.printPage(fxCn);
    if (success) {
        job.endJob();
    }
*/
      
}

@FXML
private void clickDate(ActionEvent event) {
    dateText.setValue(fxDate.getValue());
    fxPrintDate.setText(dateText.getValue());
}

@FXML
private void clickDateCheck(ActionEvent event) {
    if (fxDateCheck.isSelected()) { // selected is No Date
        exImage.withDate = true;
        fxPrintDate.visibleProperty().setValue(Boolean.TRUE);
    } else {
        exImage.withDate = false;
        fxPrintDate.visibleProperty().setValue(Boolean.FALSE);
    }
    //exImage.withDate = !fxEmpty.isSelected();
}

@FXML
private void clickFont(ActionEvent event) {
    dateText.setFontName(fxFontList.getValue());
    fxPrintDate.setFont(Font.font(dateText.getFontName(), dateText.getFontWeight(),dateText.getFontPosture(), dateText.getFontSize()));
}

@FXML
private void clickFontWeight(ActionEvent event) {
    if (fxFontWeight.isSelected()) {
        dateText.setFontWeight(FontWeight.BOLD);
        //fxPrintDate.setStyle("-fx-font-weight: bold; -fx-background-color: #FDD;");
    } else {
        dateText.setFontWeight(FontWeight.NORMAL);
        //fxPrintDate.setStyle("-fx-font-weight: normal; -fx-background-color: #FDD;");
    }
    fxPrintDate.setFont(Font.font(dateText.getFontName(), dateText.getFontWeight(),dateText.getFontPosture(), dateText.getFontSize()));
}
@FXML
private void clickFontPosture(ActionEvent event) {
    if (fxFontPosture.isSelected()) {
        dateText.setFontPosture(FontPosture.ITALIC);
    } else {
        dateText.setFontPosture(FontPosture.REGULAR);
    }
    fxPrintDate.setFont(Font.font(dateText.getFontName(), dateText.getFontWeight(),dateText.getFontPosture(), dateText.getFontSize()));
}

@FXML
private void clickOrientation(ActionEvent event) {
    exImage.setPageOrient(fxPaperOrient.getValue());
}

@FXML
private void menuVa4_2x3(ActionEvent event) {
    fxPaperOrient.setValue("вертикально");
      exImage.setPageOrient("вертикально");
    fxXaxis.getValueFactory().setValue(2);
      exImage.qtyW = 2;
    fxYaxis.getValueFactory().setValue(3);
      exImage.qtyH = 3;
    fxXgap.getValueFactory().setValue(1.5);
      exImage.gapW = (double)1.2/2.54*exImage.dpi;
    fxYgap.getValueFactory().setValue(1.5);
      exImage.gapH = (double)1.2/2.54*exImage.dpi;
    fxDpi.getValueFactory().setValue(300);
      exImage.dpi = 300;
}

@FXML
private void menuHa4_2x2(ActionEvent event) {
    fxPaperOrient.setValue("горизонтально");
      exImage.setPageOrient("горизонтально");
    fxXaxis.getValueFactory().setValue(2);
      exImage.qtyW = 2;
    fxYaxis.getValueFactory().setValue(2);
      exImage.qtyH = 2;
    fxXgap.getValueFactory().setValue(0.8);
      exImage.gapW = (double)0.8/2.54*exImage.dpi;
    fxYgap.getValueFactory().setValue(1.0);
      exImage.gapH = (double)1/2.54*exImage.dpi;
    fxDpi.getValueFactory().setValue(300);
      exImage.dpi = 300;
}

@FXML
private void menuVa4_2x5(ActionEvent event) {
    fxPaperOrient.setValue("вертикально");
      exImage.setPageOrient("вертикально");
    fxXaxis.getValueFactory().setValue(2);
      exImage.qtyW = 2;
    fxYaxis.getValueFactory().setValue(5);
      exImage.qtyH = 5;
    fxXgap.getValueFactory().setValue(1.7);
      exImage.gapW = (double)1.7/2.54*exImage.dpi;
    fxYgap.getValueFactory().setValue(0.75);
      exImage.gapH = (double)0.75/2.54*exImage.dpi;
    fxDpi.getValueFactory().setValue(300);
      exImage.dpi = 300;
}


@FXML
private void buttonExitClick(ActionEvent actionEvent) {
    try {
        //appProps.load(new FileInputStream(appConfigPath));
        //appProps.store(new FileWriter("dateprint.prop"), "store to properties file");
        
        //String newAppConfigXmlFile = rootPath + "newApp.xml";
        appProps.storeToXML(new FileOutputStream("dateprintprop.xml"), "store to xml file");
    } catch(IOException e){
    // если файл не существует, будут использоваться значения по умолчанию 
    }
    Platform.exit();
    //System.exit(0);
  }

//--- menu ----



//===== menu =====

//===============================
}  // end of DateprintController
