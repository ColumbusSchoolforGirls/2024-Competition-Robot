package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.TimedRobot;

public class Limelight {
    public AnalogInput speedSensor;
    public AnalogInput rpmSensor;
    public CAN canBus;
    public int speed;
    public int rpm;
    
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx"); // x axis position 
    NetworkTableEntry ty = table.getEntry("ty"); // y axis position
    NetworkTableEntry tv = table.getEntry("tv"); // is there valid target
    NetworkTableEntry ta = table.getEntry("ta"); // area in view
    NetworkTableEntry ts = table.getEntry("ts0"); // area in view
    NetworkTableEntry pos = table.getEntry("camera-pose_targetspace"); // 3D translation and rotations?
    NetworkTableEntry pos1 = table.getEntry("target-pose_cameraspace");
    NetworkTableEntry pos2 = table.getEntry("target-pose_robotspace");

    DriveTrain driveTrain;
    double ySpeed = 0;
    double rotationSpeed = 0;
    long lastTargetTime = 0;

    private final byte[] msg1 = {};
    private final byte[] msg2 = {0x08, 0x1B, 0x18, 0x1B, 0x01, (byte) 0x8A, (byte) 0x92, 0x01};
    private final byte[] msg3 = {(byte) 0x9C, (byte) 0xCB, 0x40, 0x00, 0x03, 0x00, 0x00, (byte) 0xD7};
    private final byte[] msg4 = {0x1B, 0x30, 0x31, 0x32, (byte) 0xC8, 0x00, (byte) 0x84, 0x00};
    private final byte[] msg7 = {(byte) 0x80, 0x00, 0x06, 0x0C, (byte) 0xE0, 0x00, 0x00, 0x00}; // turn sig+ign
    private final byte[] msg8 = {0x43, 0x11, 0x11, 0x11, 0x50, 0x50, 0x00, 0x00};
    private final byte[] msg9 = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}; // put back to 0
    private final byte[] msg10 = {(byte) 0xC0, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00};
    private final byte[] msg11 = {0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF};

    // please just look away
    private final byte[] s0 = {0x04, 0x44, (byte) 0x98, (byte) 0xC6, (byte) 0xE2, 0x00, 0x00, 0x00}; // mph
    private final byte[] s1 = {0x04, 0x44, (byte) 0x98, (byte) 0xC6, (byte) 0xE2, 0x20, 0x06, 0x49}; // mph
    private final byte[] s2 = {0x04, 0x44, (byte) 0x98, (byte) 0xC6, (byte) 0xE2, 0x40, 0x0C, (byte) 0x92}; // mph
    private final byte[] s3 = {0x04, 0x44, (byte) 0x98, (byte) 0xC6, (byte) 0xE2, 0x61, 0x12, (byte) 0xDC}; // mph
    private final byte[] s4 = {0x04, 0x44, (byte) 0x98, (byte) 0xC6, (byte) 0xE2, (byte) 0x81, 0x19, 0x25}; // mph
    // remaining sX arrays are omitted for brevity

    // similarly, tX arrays are omitted for brevity

    public void setWritePackets() {
        //TODO: fix/modify
        speed = (int) (speedSensor.getVoltage() * 64); // Assuming 5V range
        rpm = (int) (rpmSensor.getVoltage() * 7.82); // Assuming 5V range

        //TODO: fix/modify

        // Write your logic to send CAN messages here based on speed and rpm
        // Example:
        if (speed < 5) {
            canBus.writePacket(s0, 0x202);
        } else if (speed < 10) {
            canBus.writePacket(s0, 0x202);
        }
        // Add more conditions for other speed ranges
        
        // Similarly, add conditions for rpm ranges and send appropriate CAN messages
        
        // Finally, send other CAN messages
        canBus.writePacket(msg1, 0x000);
        canBus.writePacket(msg2, 0x077);
        canBus.writePacket(msg3, 0x156);
        canBus.writePacket(msg4, 0x179);
        canBus.writePacket(msg7, 0x3B2);
        canBus.writePacket(msg8, 0x3B4);
        canBus.writePacket(msg9, 0x3C3);
        canBus.writePacket(msg10, 0x416);
        canBus.writePacket(msg11, 0x416);

    }

    public void setVisionVariables() {
        //TODO: fix and modify
        speedSensor = new AnalogInput(1); // Change channel according to your setup
        rpmSensor = new AnalogInput(0); // Change channel according to your setup
        canBus = new CAN(0); // Change device ID according to your setup
    }

    public Limelight(DriveTrain drive) {
        this.driveTrain = drive;

    }

    public void updateLimelight() {

    }

    public double getRotation() {
        return pos.getDoubleArray(new double[6])[5]; //get rotation z value from botpose array
    
    }

    public double getX() {
        return tx.getDouble(0);
    }

    public double getY() {
        return ty.getDouble(0);

    }

    public double getA() {
        return ta.getDouble(0);

    }
}
