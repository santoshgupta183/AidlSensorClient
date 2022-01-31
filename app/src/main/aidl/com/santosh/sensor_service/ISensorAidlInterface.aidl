// ISensorAidlInterface.aidl
package com.santosh.sensor_service;

import com.santosh.sensor_service.ISensorDataListener;

// Declare any non-default types here with import statements

interface ISensorAidlInterface {
    void setListener(ISensorDataListener listener);
}