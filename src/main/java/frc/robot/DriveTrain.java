package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    public Limelight limelight;
    
    boolean brakeMode = true;
    public CANSparkMax frontLeft = new CANSparkMax(1, MotorType.kBrushless); 
    //fake id -- need to change when we have a real chassi
    public CANSparkMax backLeft = new CANSparkMax(2, MotorType.kBrushless);
    //fake id -- need to change when we have a real chassi
    public CANSparkMax frontRight = new CANSparkMax(3, MotorType.kBrushless);
    //fake id -- need to change when we have a real chassi
    public CANSparkMax backRight = new CANSparkMax(4, MotorType.kBrushless);
    //fake id -- need to change when we have a real chassi

    public static XboxController driveController = new XboxController(0); 
    MecanumDrive robotDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
    //we probably need to invert some motors

    public RelativeEncoder frontLeftEncoder = frontLeft.getEncoder();
    public RelativeEncoder backLeftEncoder = backLeft.getEncoder();
    public RelativeEncoder frontRightEncoder = frontRight.getEncoder();
    public RelativeEncoder backRightEncoder = backRight.getEncoder();
    public double gyroDifference;

    public DriveTrain() {
        resetEncoders();
        frontRight.setInverted(true); //need to change inversions with testing
        backRight.setInverted(true);
        frontLeft.setInverted(false);
        backLeft.setInverted(false);
    }

    public void resetEncoders() { //makes the position at 0 when teleop and autonomous starts 
        frontLeftEncoder.setPosition(0); 
        backLeftEncoder.setPosition(0);
        frontRightEncoder.setPosition(0);
        backRightEncoder.setPosition(0);
    }

    public double getFrontLeftEncoder() {
        return frontLeftEncoder.getPosition() / 12.75 * Constants.WHEEL_CIRCUMFERENCE;
        //might need to change gear ratio
    }

    public double getFrontRightEncoder() {
        return frontRightEncoder.getPosition() / 12.75 * Constants.WHEEL_CIRCUMFERENCE;
        //might need to change gear ratio
    }

    public double getBackLeftEncoder() {
        return backLeftEncoder.getPosition() / 12.75 * Constants.WHEEL_CIRCUMFERENCE;
        //might need to change gear ratio
    }

    public double getBackRightEncoder() {
        return backRightEncoder.getPosition() / 12.75 * Constants.WHEEL_CIRCUMFERENCE;
        //might need to change gear ratio
    }

    public void update() {
        SmartDashboard.putNumber("Front Left Encoder" , getFrontLeftEncoder());
        SmartDashboard.putNumber("Back Left Encoder" , getBackLeftEncoder());
        SmartDashboard.putNumber("Front Right Encoder" , getFrontRightEncoder());
        SmartDashboard.putNumber("Back Right Encoder" , getBackRightEncoder());
        //for testing

        SmartDashboard.putNumber("DriveTrain Front Left" , frontLeft.get());
        SmartDashboard.putNumber("DriveTrain Back Left" , backLeft.get());
        SmartDashboard.putNumber("DriveTrain Front Right" , frontRight.get());
        SmartDashboard.putNumber("DriveTrain Back Right" , backRight.get());
        //for testing
    }

    public void setAuto() { //brake mode for precision
        frontLeft.setIdleMode(IdleMode.kBrake);
        backLeft.setIdleMode(IdleMode.kBrake);
        frontRight.setIdleMode(IdleMode.kBrake);
        backRight.setIdleMode(IdleMode.kBrake);
    }

    public void setTeleop() {
        if (brakeMode) {
            frontLeft.setIdleMode(IdleMode.kBrake);
            backLeft.setIdleMode(IdleMode.kBrake);
            frontRight.setIdleMode(IdleMode.kBrake);
            backRight.setIdleMode(IdleMode.kBrake);
            SmartDashboard.putBoolean("Brake Mode", true); //green box if it is in brake mode
        } else {
            frontLeft.setIdleMode(IdleMode.kBrake);
            backLeft.setIdleMode(IdleMode.kBrake);
            frontRight.setIdleMode(IdleMode.kBrake);
            backRight.setIdleMode(IdleMode.kBrake);
            SmartDashboard.putBoolean("Brake Mode", false); //red box if it is in coast mode
        }

        if (driveController.getXButtonPressed()) {
            brakeMode = !brakeMode; //this looks odd and is confusin but it works :>
        }
    }

    public void drive(double normalSpeed, double crawlSpeed, boolean noDeadZone) { //scaling

        double forwardSpeed = driveController.getLeftY();
        double sideSpeed = driveController.getLeftX();
        double rotationSpeed = driveController.getRightX();

        if (!driveController.getRightBumper()) { //so basically if you are clicking the right bumper, you go at full speed (-100% to 100%)
            forwardSpeed = driveController.getLeftY() * normalSpeed; //if you aren't clicking any bumper, you go "normal speed" (-50% to 50%)
            sideSpeed = driveController.getLeftX() * normalSpeed; //LOWERED SPEEDS IN ROBOT
            rotationSpeed = driveController.getRightX() * normalSpeed;
        }

        if (driveController.getLeftBumper()) {
            forwardSpeed = driveController.getLeftY() * crawlSpeed; //if you click the left bumper you go at a slow scaled speed
            sideSpeed = driveController.getLeftX() * crawlSpeed;
            rotationSpeed = driveController.getRightX() * crawlSpeed;
        }

        double deadZone = Constants.DRIVE_CONTROLLER_DEADZONE;

        if (noDeadZone) { // dont want deadzone for auto aka boolean; true=don't use deadzone
            deadZone = 0;
        }
        if (Math.abs(driveController.getLeftY()) < deadZone) { //in the future use a better variable (should be all caps and use tolerance) but we made this in the beginning of the season when -- again -- Lila had no idea what she was doing
            forwardSpeed = 0;
        }
        if (Math.abs(driveController.getLeftX()) < deadZone) {
            sideSpeed = 0;
        }
        if (Math.abs(driveController.getRightX()) < deadZone) {
            rotationSpeed = 0;
        } 

        robotDrive.driveCartesian(forwardSpeed, sideSpeed, rotationSpeed);
    }

    private double targetAngle;
    private double targetDistance;
    private double difference;
    private double driveDifference;

    AHRS gyro = new AHRS(SPI.Port.kMXP);


    public void autoUpdate() {
        SmartDashboard.putNumber("GyroAngle Turn", getFacingAngle());
        
    }

    public double getFacingAngle(){
        return gyro.getAngle();
    }

    public boolean turnComplete() {
        difference = (getFacingAngle() - targetAngle);
        if (difference < Constants.TURN_TOLERANCE && difference > -Constants.TURN_TOLERANCE) {
            return true;
        }
        return false;
    } 

    public boolean driveComplete() {
        driveDifference = targetDistance = getFrontLeftEncoder();
        if (Math.abs(driveDifference) < Constants.DISTANCE_TOLERANCE) {
            return true;
        }
        return false;
    }

    public void startTurn(double angle) {
        this.targetAngle = (angle + getFacingAngle());
    }

    public void resetGyro() {
        gyro.reset();
    }

    public void startDrive(double distanceInches) {
        resetEncoders();
        targetDistance = distanceInches;
    }

    public void gyroTurn() {
        double rotationSpeed = 0;
        difference = (getFacingAngle() - targetAngle);

        if (difference < Constants.TURN_TOLERANCE && difference > -Constants.TURN_TOLERANCE) {
            limelight.rotationSpeed = 0;
        } else if (difference < 0) {
            rotationSpeed = 0.005 * Math.abs(difference) + 0.05;
        } else if (difference > 0) {
            rotationSpeed = -0.005 * Math.abs(difference) - 0.05;
        }
        robotDrive.driveCartesian(0, 0, rotationSpeed);
    }
    
    public void autoDrive() {
        driveDifference = targetDistance - getFrontLeftEncoder();
        double speed = 0.5; //will likely change

        if (Math.abs(getFrontLeftEncoder()) < 10) {
            speed = 0.2;
        }

        if (driveDifference > 0) {
            robotDrive.driveCartesian(speed, 0, 0);
        } else if (driveDifference < 0) {
            robotDrive.driveCartesian(-speed, 0, 0);
        }
    }

    public void square() { // turn robot to 0... straight assuming the robot is lined up with the goal to start
      frontLeft.setIdleMode(IdleMode.kBrake);
      backLeft.setIdleMode(IdleMode.kBrake);
      frontRight.setIdleMode(IdleMode.kBrake);
      backRight.setIdleMode(IdleMode.kBrake);

      gyroDifference = Robot.gyroAngle % 360; //divides over and over again until there is a remainder
      if (gyroDifference < 0) {
        gyroDifference += 360;
      }
      
      if (gyroDifference < Constants.SQUARE_TOLERANCE || gyroDifference > 360 - Constants.SQUARE_TOLERANCE) {
            robotDrive.driveCartesian(0, 0, 0);
        } else {
            if (gyroDifference > 180) {
                if (gyroDifference > 345) {
                    robotDrive.driveCartesian(0, 0, 0.070);
                } else {
                    robotDrive.driveCartesian(0, 0, 0.2);
                }
            } else if (gyroDifference < 180) {
                if (gyroDifference < 15) {
                    robotDrive.driveCartesian(0, 0, -0.070);
                } else {
                    robotDrive.driveCartesian(0,0, -0.2);
                }
            }
        }

    }

    public boolean squareComplete() {
        if(Math.abs(Robot.gyroAngle) < Constants.TURN_TOLERANCE) {
            return true;
        }
        return false;
    }
     

}
