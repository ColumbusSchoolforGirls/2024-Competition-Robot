package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
    public Limelight limelight;

    double targetOffsetAngle_Vertical = limelight.ty.getDouble(0.0);
    double limelightMountAngleDegrees = 45.0; //will need to change
    double limelightLensHeight = 6.0; //in inches -- will need to change
    //distance from center of the limelight lens tot he floor
    double goalHeight = 24.0; //might need to change // in inches //goal = april tag NOT speaker
    //this is for the speaker
    double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
    double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

    double distanceFromGroundToAprilTag = 57.125;

    double distanceFromLimelightToGoal = (goalHeight - limelightLensHeight) / Math.tan(angleToGoalRadians);
    //calculates distance
    double groundDistanceToSpeaker = Math.sqrt(Math.pow(distanceFromLimelightToGoal, 2) - Math.pow(distanceFromGroundToAprilTag, 2));

//default intake (arm down)
//1 motor to drive arm up
//activate intake, move arm up, hold in place using other "intake", speed up Shooter, move note using Holder
//square to SPEAKER using apriltag
//trig the angle and speed to shoot

//button to activate INTAKE (not dragging, slightly off ground) **IF has note THEN arm up (parallel with robot chassis)**
// 

//mech will decide locking mechanism so arm can stop at multiple angles

//add brake
}
