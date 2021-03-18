// IServiceAidlInterface.aidl
package com.example.mypedometer;

// Declare any non-default types here with import statements

interface IServiceAidlInterface {

        //get steps count
        int getStepsCount();
        //reset count
        void resetCount();
        //start count
        void startStepsCount();
        //stop count
        void stopStepsCount();
        //get calorie
        double getCalorie();
        //get distance
        double getDistance();
        //save data
        void saveData();
        //set sensitivity
        void setSensitivity(float sensitivity);
        //get sensitivity
        double getSensitivity();
        //get interval
        int getInterval();
        //set interval
        void setInterval(int interval);
        //get timestamp
        long getStartTimestamp();
        //get service running status
        int getServiceRunningStatus();
}
