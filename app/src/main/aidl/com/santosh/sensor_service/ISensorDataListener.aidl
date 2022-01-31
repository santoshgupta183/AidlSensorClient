// ISensorDataListener.aidl
package com.santosh.sensor_service;

// Declare any non-default types here with import statements

interface ISensorDataListener {
    void onSensorDataChanged(float x, float y, float z);
}