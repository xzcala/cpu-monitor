/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcbrzfcpumonitorfxml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 *
 * @author Jon
 */
public class FXMLDocumentController implements Initializable, PropertyChangeListener {

    private double angle;
    private int lap = 1, loop = 0;

    @FXML
    public ImageView hand;

    @FXML
    public Text percentage;

    @FXML
    public Text shadow;

    @FXML
    public Text record1;

    @FXML
    public Text record2;

    @FXML
    public Text record3;

    @FXML
    public Button start;

    @FXML
    public Button record;
    
    CPUMonitorModel cpuMonitorModel; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cpuMonitorModel = new CPUMonitorModel();
        cpuMonitorModel.setUp();
        cpuMonitorModel.addPropertyChangeListener(this);
    }

    @FXML
    public void start(ActionEvent event) {
        if (!cpuMonitorModel.isRunning()) {
            cpuMonitorModel.start();
            start.setText("Stop");
            record.setText("Record");
        } else {
            cpuMonitorModel.pause();
            start.setText("Start");
            record.setText("Reset");
        }
    }

    ;

    @FXML
    public void record(ActionEvent event) {
        if (!cpuMonitorModel.isRunning()) {
            
            lap = 1;
            loop = 0;
            percentage.setText("0%");
            shadow.setText("0%");
            record1.setText("--.--%");
            record2.setText("--.--%");
            record3.setText("--.--%");
            hand.setRotate(0);
            cpuMonitorModel.reset();
            
        } else {

            Date date = new Date();

            switch (loop) {
                case 0:
                    record1.setText("Record " + lap++ + ": " + percentage.getText() + "% " + DateFormat.getTimeInstance().format(date));
                    loop++;
                    break;
                case 1:
                    record2.setText("Record " + lap++ + ": " + percentage.getText() + "% " + DateFormat.getTimeInstance().format(date));
                    loop++;
                    break;
                case 2:
                    record3.setText("Record " + lap++ + ": " + percentage.getText() + "% " + DateFormat.getTimeInstance().format(date));
                    loop = 0;
                    break;
            }
            record.setText("Record");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        double cpu = (double) evt.getNewValue();
        
        percentage.setText(cpu + "%");
        shadow.setText(cpu + "%");

        if (cpu > 10) {
            angle = (cpu / 100) * 300;
            hand.setRotate(angle);
        } else {
            hand.setRotate(0);
        }
    }
    
}
