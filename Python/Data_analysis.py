"""
By: Severi Friman
severi.friman@gmail.com

Accelerometer data analysis.
Reads accelerometer data from a file and plots the data.
Later some analysis will be implemented.

The test data was recorded with Nokia 5.3 accelerometer with 50 Hz sampling rate.
The app usd for recording is given in the project.
The signal is 3-axis accelerometer data of chest seismography.


"""



import csv
from matplotlib import pyplot as plt
import numpy as np

def main():

    # These will contain the sensor data
    Acc_X = []
    Acc_Y = []
    Acc_Z = []
    fs = 50 # Hz

    # Lets open, read and save the data to the initialized vectors
    with open("sensors_Test1.csv", newline='') as csvfile:

        reader = csv.reader(csvfile, delimiter=',')

        for row in reader:
            if (row[0]== "X"):
                pass
            else:
                Acc_X.append(float(row[0]))
                Acc_Y.append(float(row[1]))
                Acc_Z.append(float(row[2]))

    samples_amount = len(Acc_X)
    seconds = samples_amount/fs
    increment = 1/50
    time_vector = np.arange(0,seconds,increment)

    print("############################################")
    print("#                 Messages                 #")
    print("############################################\n")


    print("Amount of samples per channel: ", samples_amount)
    print("sampling rate: ", fs ," Hz")
    print("Recording time: ", seconds , " seconds")

    plt.figure()
    ax1 = plt.subplot(3,1,1)
    plt.plot(time_vector,Acc_X)
    plt.title("X")
    plt.ylabel("Acceleration (m/s2)")
    plt.subplot(3,1,2, sharex=ax1)
    plt.plot(time_vector,Acc_Y)
    plt.title("Y")
    plt.ylabel("Acceleration (m/s2)")
    plt.subplot(3, 1, 3, sharex=ax1)
    plt.plot(time_vector, Acc_Z)
    plt.title("Z")
    plt.xlabel("Time (s)")
    plt.ylabel("Acceleration (m/s2)")
    plt.show()

main()
