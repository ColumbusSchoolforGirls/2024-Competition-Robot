package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
    public Limelight limelight;

    public Arm (Limelight limelight) {
    this.limelight = limelight;

    //limelight stuff to calculate distance with april tags
    double targetOffsetAngle_Vertical = limelight.ty.getDouble(0.0); //getting the vertical angle that the limelight is off from the april tag
    double limelightMountAngleDegrees = 45.0; //will need to change
    double limelightLensHeight = 6.0; //in inches -- will need to change
    //distance from center of the limelight lens tot he floor
    double goalHeight = 24.0; //might need to change // in inches //goal = april tag NOT speaker
    //this is for the speaker
    double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
    double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);
    double distanceFromAprilTagToSpeaker = 57.125;
    double distanceFromLimelightToGoal = (goalHeight - limelightLensHeight) / Math.tan(angleToGoalRadians);
    //calculates distance
    double groundDistanceToSpeaker = Math.sqrt(Math.pow(distanceFromLimelightToGoal, 2) - Math.pow(distanceFromAprilTagToSpeaker, 2));
    //double shooterAngle = something where distanceFromLimelightToGoal is the independent variable
    System.out.println("Distance on ground from limelight to april tag:" + groundDistanceToSpeaker);
    }
//testing neos
    public CANSparkMax shootMotor= new CANSparkMax(2, MotorType.kBrushless);
    //public CANSparkMax holdDrum = new CANSparkMax(3, MotorType.kBrushless);
    public CANSparkMax intakeMotor = new CANSparkMax(4, MotorType.kBrushless);

    public static XboxController aux = new XboxController(1); // 1 is the zux controller
    
    
    public void armFunctions(double normalShootSpeed, double normalIntakeSpeed) {
        double shootSpeed = -aux.getLeftY();
        double intakeSpeed = aux.getRightY();

        if (Math.abs(intakeSpeed) < Constants.AUX_DEADZONE){
            intakeMotor.set(0);
        } else{
            intakeMotor.set(intakeSpeed * normalIntakeSpeed);
         }

        if (Math.abs(shootSpeed) < Constants.AUX_DEADZONE){
            shootMotor.set(0);
        } else{
            shootMotor.set(shootSpeed * normalShootSpeed);
         }   
    }


//color sensor
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);

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
