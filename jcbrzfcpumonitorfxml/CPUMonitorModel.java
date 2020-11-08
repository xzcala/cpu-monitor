/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcbrzfcpumonitorfxml;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 *
 * @author Jon
 */
public class CPUMonitorModel {
    
    protected PropertyChangeSupport propertyChangeSupport;
    
    private Timeline timeline;
    private KeyFrame keyFrame;

    public static double cpu = 0;
    private final DecimalFormat df = new DecimalFormat("#.####");

    public CPUMonitorModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    public void setUp() {
        keyFrame = new KeyFrame(Duration.millis(100), (ActionEvent) -> {
            update();
        });

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
    }
    
    public void update() {

        double old = cpu;
        cpu = 100 * getCPUUsage();
        String format = df.format(cpu);
        cpu = Double.valueOf(format);
        firePropertyChange("Model",old,cpu);
    }
    
    public void start() {
        timeline.play();
    }
    
    public void pause() {
        timeline.pause();
    }
    
    private static double getCPUUsage() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        double value = 0;
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("getSystemCpuLoad")
                    && Modifier.isPublic(method.getModifiers())) {
                try {
                    value = (double) method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = 0;
                }
                return value;
            }
        }
        return value;
    }

    public boolean isRunning() {
        if (timeline != null) {
            if (timeline.getStatus() == Animation.Status.RUNNING) {
                return true;
            }
        }
        return false;
    }

    void reset() {
        timeline.pause();
        cpu=0;
    }
}
