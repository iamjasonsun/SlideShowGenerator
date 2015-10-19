/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slideshowgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;

/**
 *
 * @author Jason
 */
public class SlideShowGenerator extends Application {

    private RadioButton radioBtnNormal;
    private RadioButton radioBtnFancy;
    private Label labelEffect;
    private ComboBox comboBoxEffect1;
    private ComboBox comboBoxEffect2;
    private Label labelTimeInterval;
    private Label labelPicWidth;
    private Label labelPicHeight;
    private TextField textFieldTimeInterval;
    private TextField textFieldWidth;
    private TextField textFieldHeight;
    private Label labelOutputToHead;
    private Label labelOutputToBody;
    private TextArea textAreaOutputToHead;
    private TextArea textAreaOutputToBody;
    private Button btnGenerate;
    private Button btnChooseFile;
    private Label labelNumOfFileChosen;
    private VBox vbEffect;
    private List<File> fileList;
    
    
    private void setUpHBoxOfRadioBtn(HBox hb, ToggleGroup group) {
        this.radioBtnNormal = new RadioButton("Normal Slideshow");
        this.radioBtnFancy = new RadioButton("Fancy Slideshow");
        this.radioBtnNormal.setToggleGroup(group);
        this.radioBtnNormal.setSelected(true);
        this.radioBtnFancy.setToggleGroup(group);
        hb.getChildren().addAll(this.radioBtnNormal, this.radioBtnFancy);
    }

    private void setUpVBoxOfEffect1ComboBox(VBox vb) {
        this.labelEffect = new Label("Please choose the effect you want");
        ObservableList<String> Effect1Options =
                FXCollections.observableArrayList(
                "fadeout",
                "move left",
                "move right",
                "move up",
                "move down",
                "flip up",
                "flip down");
        this.comboBoxEffect1 = new ComboBox(Effect1Options);

        ObservableList<String> Effect2Options =
                FXCollections.observableArrayList(
                "fadeout2",
                "heigh to zero",
                "blow away to left",
                "blow away to right",
                "fall down");
        this.comboBoxEffect2 = new ComboBox(Effect2Options);
        vb.getChildren().clear();
        vb.getChildren().addAll(this.labelEffect, this.comboBoxEffect1);

        /*
         * Add class name for comboBox
         */
        this.comboBoxEffect1.getStyleClass().add("comboBox");
        this.comboBoxEffect2.getStyleClass().add("comboBox");

    }

    private void setUpVBoxOfEffect2ComboBox(VBox vb) {
        this.labelEffect = new Label("Please choose the effect you want");
        ObservableList<String> Effect1Options =
                FXCollections.observableArrayList(
                "random disappear",
                "diagonal disappear 1",
                "diagonal disappear 2");
        this.comboBoxEffect1 = new ComboBox(Effect1Options);

        ObservableList<String> Effect2Options =
                FXCollections.observableArrayList(
                "fadeout2",
                "heigh to zero",
                "blow away to left",
                "blow away to right",
                "fall down");
        this.comboBoxEffect2 = new ComboBox(Effect2Options);
        vb.getChildren().clear();
        vb.getChildren().addAll(this.labelEffect, this.comboBoxEffect1, this.comboBoxEffect2);

        /*
         * Add class name for comboBox
         */
        this.comboBoxEffect1.getStyleClass().add("comboBox");
        this.comboBoxEffect2.getStyleClass().add("comboBox");

    }

    private void setUpGridPaneOfSetTimeWidthHeight(GridPane grid) {
        this.labelTimeInterval = new Label("Time Interval(s)");
        this.labelPicWidth = new Label("Picture Width");
        this.labelPicHeight = new Label("Picture Height");
        this.textFieldTimeInterval = new TextField("5");
        this.textFieldWidth = new TextField("960");
        this.textFieldHeight = new TextField("360");
        grid.add(this.labelTimeInterval, 0, 0);
        grid.add(this.labelPicWidth, 0, 1);
        grid.add(this.labelPicHeight, 0, 2);
        grid.add(this.textFieldTimeInterval, 1, 0);
        grid.add(this.textFieldWidth, 1, 1);
        grid.add(this.textFieldHeight, 1, 2);
    }

    private void setUpGridPaneOutputTextArea(GridPane grid) {
        this.labelOutputToHead = new Label("Paste code below to head tag");
        this.labelOutputToBody = new Label("Paste code below to body");
        this.textAreaOutputToHead = new TextArea();
        this.textAreaOutputToHead.setPrefRowCount(15);
        this.textAreaOutputToHead.setPrefColumnCount(18);
        this.textAreaOutputToHead.setEditable(false);
        this.textAreaOutputToBody = new TextArea();
        this.textAreaOutputToBody.setPrefRowCount(15);
        this.textAreaOutputToBody.setPrefColumnCount(18);
        this.textAreaOutputToBody.setEditable(false);
        grid.add(this.labelOutputToHead, 0, 0);
        grid.add(this.labelOutputToBody, 1, 0);
        grid.add(this.textAreaOutputToHead, 0, 1);
        grid.add(this.textAreaOutputToBody, 1, 1);
    }

    private void setUpGridPaneOfButtons(GridPane grid) {
        this.btnGenerate = new Button("Generate");
        this.btnChooseFile = new Button("Choose file(s)");
        this.labelNumOfFileChosen = new Label("0 file(s) have been choosen");
        grid.add(this.btnGenerate, 0, 0);
        grid.add(this.btnChooseFile, 1, 0);
        grid.add(this.labelNumOfFileChosen, 1, 1);
    }

    private void addClassName() {
        /*
         * Add class name for label.
         */
        this.labelEffect.getStyleClass().add("label");
        this.labelNumOfFileChosen.getStyleClass().add("label");
        this.labelOutputToBody.getStyleClass().add("label");
        this.labelOutputToHead.getStyleClass().add("label");
        this.labelPicHeight.getStyleClass().add("label");
        this.labelPicWidth.getStyleClass().add("label");
        this.labelTimeInterval.getStyleClass().add("label");

        /*
         * Add class name for textfield
         */
        this.textFieldHeight.getStyleClass().add("textField");
        this.textFieldTimeInterval.getStyleClass().add("textField");
        this.textFieldWidth.getStyleClass().add("textField");
        /*
         * Add class name for textarea
         */
        this.textAreaOutputToBody.getStyleClass().add("TextareaOutput");
        this.textAreaOutputToHead.getStyleClass().add("TextareaOutput");
        /*
         * Add class name for button
         */
        this.btnChooseFile.getStyleClass().add("button");
        this.btnGenerate.getStyleClass().add("button");
        /*
         * Add class name for radio button
         */
        this.radioBtnFancy.getStyleClass().add("radioBtn");
        this.radioBtnNormal.getStyleClass().add("radioBtn");
    }

    private static String changeToFunctionName(String str) {
        String functionName;
        if (str.equals("fadeout")) {
            functionName = "fadeOut";
        } else if (str.equals("move left") || str.equals("move right")) {
            functionName = "moveLeftRight";
        } else if (str.equals("move up") || str.equals("move down")) {
            functionName = "moveUpDown";
        } else if (str.equals("flip up")) {
            functionName = "flipUp";
        } else if (str.equals("flip down")) {
            functionName = "flipDown";
        } else if (str.equals("fadeout2")) {
            functionName = "fadeOut2";
        } else if (str.equals("heigh to zero")) {
            functionName = "HeightToZero";
        } else if (str.equals("blow away to left")) {
            functionName = "blowAwayToLeft";
        } else if (str.equals("blow away to right")) {
            functionName = "blowAwayToRight";
        } else if (str.equals("fall down")) {
            functionName = "fallDown";
        } else if (str.equals("random disappear")) {
            functionName = "slicePicShow";
        } else if (str.equals("diagonal disappear 1")) {
            functionName = "slicePicShow_diagonal";
        } else if (str.equals("diagonal disappear 2")) {
            functionName = "slicePicShow_diagonal2";
        } else {
            functionName = "";
        }

        return functionName;
    }

    private static String generateHTML(Scanner in, PrintWriter out, String timeInterval, String picWidth, String picHeight, String effect1, String effect2, String imgTags, boolean isNormal) {
        String functionName;
        String effect1L = changeToFunctionName(effect1);
        String effect2L = changeToFunctionName(effect2);
        if (isNormal) {
            if (effect1.equals("flip up") || effect1.equals("flip down")) {
                functionName = "slideShow_flipUpDown(" + effect1L + ", " + timeInterval + "000, " + picWidth + ", " + picHeight + ");";
            } else if (effect1.equals("move right")) {
                functionName = "slideShow(" + effect1L + ", " + timeInterval + "000, -" + picWidth + ", " + picHeight + ");";
            } else if (effect1.equals("move down")) {
                functionName = "slideShow(" + effect1L + ", " + timeInterval + "000, " + picWidth + ", -" + picHeight + ");";
            } else {
                functionName = "slideShow(" + effect1L + ", " + timeInterval + "000, " + picWidth + ", " + picHeight + ");";
            }
        } else {
            functionName = "slideShow_slicedPic(" + effect1L + ", " + effect2L + ", " + timeInterval + "000, " + picWidth + ", " + picHeight + ");";
        }

        while (in.hasNext()) {
            String tempLine = in.nextLine();
            if (tempLine.indexOf("// mark for function - jiasongSun") >= 0) {
                tempLine = functionName;
            }
            if(tempLine.indexOf("<!-- mark for img tags - jiasongSun -->") >= 0){
                tempLine = imgTags;
            }
            out.println(tempLine);
        }
        
        return functionName;
    }
    
    private static void generateCSS(Scanner in, PrintWriter out, String picWidth, String picHeight){
        while(in.hasNext()){
            String temp = in.nextLine();
            if(temp.indexOf("/*mark for width - jiasongSun*/")>=0){
                temp = "width: "+picWidth+"px;";
            }
            if(temp.indexOf("/*mark for height - jiasongSun*/")>=0){
                temp = "height: "+picHeight+"px;";
            }
            if(temp.indexOf("/*mark for left - jiasongSun*/")>=0){
                temp = "left: "+picWidth+"px;";
            }
            if(temp.indexOf("/*mark for top - jiasongSun height/2-22 */")>=0){
                temp = "top: "+(Integer.parseInt(picHeight)/2 - 22)+"px;";
            }
            out.println(temp);
        }
    }

    @Override
    public void start(final Stage primaryStage) {

        HBox hbRadioBtn = new HBox();
        final ToggleGroup group = new ToggleGroup();
        setUpHBoxOfRadioBtn(hbRadioBtn, group);
        hbRadioBtn.getStyleClass().add("hbRadioBtn");

        this.vbEffect = new VBox();
        setUpVBoxOfEffect1ComboBox(this.vbEffect);
        this.vbEffect.getStyleClass().add("vbEffect");

        GridPane gridPaneSetTimeWidthHeight = new GridPane();
        setUpGridPaneOfSetTimeWidthHeight(gridPaneSetTimeWidthHeight);
        gridPaneSetTimeWidthHeight.getStyleClass().add("gridPaneSetTimeWidthHeight");

        HBox hbGroupEffectAndTimeInterval = new HBox();
        hbGroupEffectAndTimeInterval.getChildren().addAll(this.vbEffect, gridPaneSetTimeWidthHeight);
        hbGroupEffectAndTimeInterval.getStyleClass().add("hbGroupEffectAndTimeInterval");

        GridPane gridPaneOutputTextArea = new GridPane();
        setUpGridPaneOutputTextArea(gridPaneOutputTextArea);
        gridPaneOutputTextArea.getStyleClass().add("gridPaneOutputTextArea");

        GridPane gridButton = new GridPane();
        setUpGridPaneOfButtons(gridButton);
        gridButton.getStyleClass().add("gridButton");

        VBox vb = new VBox();
        vb.getChildren().addAll(hbRadioBtn, hbGroupEffectAndTimeInterval, gridPaneOutputTextArea, gridButton);
        vb.getStyleClass().add("vb");

        addClassName();

        /*
         * Add event listener
         */
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (radioBtnNormal.isSelected()) {
                    setUpVBoxOfEffect1ComboBox(vbEffect);
                } else {
                    setUpVBoxOfEffect2ComboBox(vbEffect);
                }
            }
        });

        this.btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean comboChosen;
                if(radioBtnNormal.isSelected()){
                    comboChosen = comboBoxEffect1.getValue() != null;
                }else{
                    comboChosen = comboBoxEffect1.getValue() != null && comboBoxEffect2.getValue() != null;
                }
                if (fileList != null && comboChosen) {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File fileDirectory = directoryChooser.showDialog(primaryStage);
                    if (fileDirectory != null) {
                        File dir = new File(fileDirectory.getAbsolutePath() + "\\img");
                        dir.mkdir();
                        if (fileDirectory.canWrite()) {
                            /*
                             * Copy user chosen photos to directory
                             */
                            Iterator<File> iter = fileList.iterator();
                            String imgTags = "";
                            boolean bl = true;
                            while (iter.hasNext()) {
                                File temp = iter.next();
                                Path FROM = Paths.get(temp.getPath());
                                Path TO = Paths.get(fileDirectory.getAbsolutePath() + "\\img\\" + temp.getName());
                                try {
                                    Files.copy(FROM, TO, StandardCopyOption.REPLACE_EXISTING);
                                } catch (IOException ex) {
                                    bl = false;
                                    MessageBox.show(primaryStage, "Unable to copy photos!", "Error", MessageBox.OK);
                                }
                                imgTags += "<img src=\"img/" + temp.getName() + "\" alt=\"" + temp.getName() + "\" />\n";
                            }
                            /*
                             * Copy javascript file and images to directory
                             */
                            Path FROM = Paths.get("Jiasong_slideShow_1.1.js");
                            Path FROM1 = Paths.get("next.png");
                            Path FROM2 = Paths.get("prev.png");
                            Path FROM3 = Paths.get("slideIndex.png");
                            Path TO = Paths.get(fileDirectory.getAbsolutePath() + "\\" + "Jiasong_slideShow_1.1.js");
                            Path TO1 = Paths.get(fileDirectory.getAbsolutePath() + "\\img\\" + "next.png");
                            Path TO2 = Paths.get(fileDirectory.getAbsolutePath() + "\\img\\" + "prev.png");
                            Path TO3 = Paths.get(fileDirectory.getAbsolutePath() + "\\img\\" + "slideIndex.png");
                            try {
                                Files.copy(FROM, TO, StandardCopyOption.REPLACE_EXISTING);
                                Files.copy(FROM1, TO1, StandardCopyOption.REPLACE_EXISTING);
                                Files.copy(FROM2, TO2, StandardCopyOption.REPLACE_EXISTING);
                                Files.copy(FROM3, TO3, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ex) {
                                bl = false;
                                MessageBox.show(primaryStage, "Unable to copy original files. It may be removed.", "Error", MessageBox.OK);
                            }
                            /*
                             * Generate html file.
                             */
                            String functionName = "";
                            File htmlFile = new File("slideShowDemo.html");
                            try {
                                Scanner in = new Scanner(htmlFile);
                                PrintWriter out = new PrintWriter(fileDirectory.getAbsolutePath() + "//demoSlideshow.html");
                                
                                
                                String effect2 = "";
                                String effect1 =comboBoxEffect1.getValue().toString();
                                if(comboBoxEffect2.getValue()!=null){
                                    effect2 =comboBoxEffect2.getValue().toString();
                                }
                                
                                functionName = generateHTML(in, out, textFieldTimeInterval.getText(), textFieldWidth.getText(), textFieldHeight.getText(), effect1, effect2, imgTags, radioBtnNormal.isSelected());
                                in.close();
                                out.close();
                            } catch (FileNotFoundException ex) {
                                bl = false;
                                MessageBox.show(primaryStage,"Unable to generate HTML file.","Error",MessageBox.OK);
                            }
                            /*
                             * Generate css file.
                             */
                            File cssFile = new File("slideShowDemo.css");
                            try {
                                Scanner in = new Scanner(cssFile);
                                PrintWriter out = new PrintWriter(fileDirectory.getAbsolutePath() + "//slideShowDemo.css");
                                generateCSS(in, out, textFieldWidth.getText(), textFieldHeight.getText());
                                in.close();
                                out.close();
                            } catch (FileNotFoundException ex) {
                                bl = false;
                                MessageBox.show(primaryStage,"Unable to generate CSS file.","Error",MessageBox.OK);
                            }

                            
                            if (bl) {
                                textAreaOutputToHead.setText("<link rel=\"stylesheet\" type=\"text/css\" href=\"slideShowDemo.css\"/>\n" +
"<script type=\"text/javascript\" src=\"Jiasong_slideShow_1.1.js\"></script>\n" +
"<script>\n" +
"          window.onload = function()\n" +
"          {\n              " +
functionName +
"\n            };\n" +
"</script>");
                                textAreaOutputToBody.setText("<div id=\"slideShow\">\n              " +
"            <div id=\"slideShowPic\">\n" +
imgTags +
"            </div>\n" +
"            <img id=\"next\" src=\"img/next.png\">\n" +
"            <img id=\"prev\" src=\"img/prev.png\">\n" +
"            <ul id=\"slideIndex\"></ul>\n" +
"</div>");
                                MessageBox.show(primaryStage, "Files successfully generated!", "Success", MessageBox.OK);
                            }else{
                                MessageBox.show(primaryStage, "Files are not generated successfully!", "Error", MessageBox.OK);
                            }
                        } else {
                            MessageBox.show(primaryStage, "Unable to write files into the directory!", "Error", MessageBox.OK);
                        }
                    }
                } else {
                    if(comboChosen){
                        MessageBox.show(primaryStage, "Please choose photos first!", "Error", MessageBox.OK);
                    }else{
                        MessageBox.show(primaryStage, "Please choose effect!", "Error", MessageBox.OK);
                    }
                    
                }
            }
        });

        this.btnChooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose photoes you want to use...");
                fileList = fileChooser.showOpenMultipleDialog(primaryStage);
                if (fileList != null) {
                    labelNumOfFileChosen.setText(fileList.size() + " file(s) have been choosen");
                } else {
                    labelNumOfFileChosen.setText("0 file(s) have been choosen");
                }
            }
        });


        Scene scene = new Scene(vb, 800, 680);
        scene.getStylesheets().add("style.css");
        primaryStage.setTitle("Slideshow Generator");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
