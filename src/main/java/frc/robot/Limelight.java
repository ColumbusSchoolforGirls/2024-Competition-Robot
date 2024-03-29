package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
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

    public Limelight(DriveTrain drive) {
        this.driveTrain = drive;

    }

    public void updateLimelight() {

    }

    public double getRotation() {
        return pos.getDoubleArray(new double[6])[5]; // get rotation z value from botpose array

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
