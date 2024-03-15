package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    // move to top of file - NC --> done 
    private double targetAngle;
    private double targetDistance;
    private double difference;
    private double driveDifference;
    private double speed;
    private double rotationSpeed;
    double startAutoDriveTime;

    // also move this -NC --> done
    AHRS gyro = new AHRS(SPI.Port.kMXP);

    public Limelight limelight;
    
    // move CAN IDs into a constants -NC --> done 
    public CANSparkMax frontLeft = new CANSparkMax(Constants.FRONT_LEFT_ID, MotorType.kBrushless); 
    //fake id -- need to change when we have a real chassi
    public CANSparkMax backLeft = new CANSparkMax(Constants.BACK_LEFT_ID, MotorType.kBrushless);
    //fake id -- need to change when we have a real chassi
    public CANSparkMax frontRight = new CANSparkMax(Constants.FRONT_RIGHT_ID, MotorType.kBrushless);
    //fake id -- need to change when we have a real chassi
    public CANSparkMax backRight = new CANSparkMax(Constants.BACK_RIGHT_ID, MotorType.kBrushless);
    //fake id -- need to change when we have a real chassi

    public static XboxController driveController = new XboxController(0); 
    MecanumDrive robotDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
    

    public RelativeEncoder frontLeftEncoder = frontLeft.getEncoder();
    public RelativeEncoder backLeftEncoder = backLeft.getEncoder();
    public RelativeEncoder frontRightEncoder = frontRight.getEncoder();
    public RelativeEncoder backRightEncoder = backRight.getEncoder();
    public double gyroDifference;

    public DriveTrain(Limelight limelight) {
        this.limelight = limelight;

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
        return frontLeftEncoder.getPosition() / Constants.GEAR_RATIO * Constants.WHEEL_CIRCUMFERENCE;
        //might need to change gear ratio
    }

    public double getFrontRightEncoder() {
        return frontRightEncoder.getPosition() / Constants.GEAR_RATIO * Constants.WHEEL_CIRCUMFERENCE;
        //might need to change gear ratio
    }

    public double getBackLeftEncoder() {
        return backLeftEncoder.getPosition() / Constants.GEAR_RATIO * Constants.WHEEL_CIRCUMFERENCE;
        //might need to change gear ratio
    }

    public double getBackRightEncoder() {
        return backRightEncoder.getPosition() / Constants.GEAR_RATIO * Constants.WHEEL_CIRCUMFERENCE;
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
    void setBrakeMode() {
        frontLeft.setIdleMode(IdleMode.kBrake);
        backLeft.setIdleMode(IdleMode.kBrake);
        frontRight.setIdleMode(IdleMode.kBrake);
        backRight.setIdleMode(IdleMode.kBrake);
        SmartDashboard.putBoolean("Brake Mode", true); //green box if it is in brake mode
    }

    private void setCoastMode() {
        frontLeft.setIdleMode(IdleMode.kCoast);
        backLeft.setIdleMode(IdleMode.kCoast);
        frontRight.setIdleMode(IdleMode.kCoast);
        backRight.setIdleMode(IdleMode.kCoast);
        SmartDashboard.putBoolean("Brake Mode", false); //red box if it is in coast mode
    }

    public void setAuto() { //brake mode for precision
        setBrakeMode();
    }

    public void setTeleop() {
        if (driveController.getXButtonPressed()) {
            if (frontLeft.getIdleMode() == IdleMode.kCoast) {
                setBrakeMode();
            } else {
            setCoastMode();
            }
        }
    }

    public void drive(double normalSpeed, double crawlSpeed, boolean noDeadZone) { //scaling

        double forwardSpeed = -driveController.getLeftY();
        double sideSpeed = driveController.getLeftX();
        double rotationSpeed = driveController.getRightX();

        if (!driveController.getRightBumper()) { //so basically if you are clicking the right bumper, you go at full speed (-100% to 100%)
            forwardSpeed = -driveController.getLeftY() * normalSpeed; //if you aren't clicking any bumper, you go "normal speed" (-50% to 50%)
            sideSpeed = driveController.getLeftX() * normalSpeed; //LOWERED SPEEDS IN ROBOT
            rotationSpeed = driveController.getRightX() * normalSpeed;
        }

        if (driveController.getLeftBumper()) {
            forwardSpeed = -driveController.getLeftY() * crawlSpeed; //if you click the left bumper you go at a slow scaled speed
            sideSpeed = driveController.getLeftX() * crawlSpeed;
            rotationSpeed = driveController.getRightX() * crawlSpeed;
        }

        double deadZone = Constants.DRIVE_CONTROLLER_DEADZONE;
        double driftDeadZone = Constants.DRIVE_CONTROLLER_DRIFT_DEADZONE;

        if (noDeadZone) { // dont want deadzone for auto aka boolean; true=don't use deadzone
            deadZone = 0;
        }
        if (Math.abs(driveController.getLeftY()) < deadZone) { 
            forwardSpeed = 0;
        }
        if (Math.abs(driveController.getLeftX()) < driftDeadZone) {
            sideSpeed = 0;
        }
        if (Math.abs(driveController.getRightX()) < deadZone) {
            rotationSpeed = 0;
        } 

        robotDrive.driveCartesian(forwardSpeed, sideSpeed, rotationSpeed);
    }


    /*public void autoUpdate() {
        SmartDashboard.putNumber("GyroAngle Turn", getFacingAngle());
        // why update this only in auto? - NC
    } */ // not being called anywhere???

    public double getFacingAngle(){
        return gyro.getAngle();
    }

    public boolean turnComplete() {
        difference = (getFacingAngle() - targetAngle);
        
        // return difference < Constants.TURN_TOLERANCE && difference > -Constants.TURN_TOLERANCE;
        // but this is fine :) 
        if (Math.abs(difference) < Constants.TURN_TOLERANCE) {
            return true;
        }
        return false;
    } 

    public boolean driveComplete() {
        driveDifference = targetDistance - getFrontLeftEncoder();
        if (Math.abs(driveDifference) < Constants.DISTANCE_TOLERANCE) {
            if(frontLeftEncoder.getVelocity() < 0.03){ //to prevent skidding bc of turning before drive is complete
               System.out.println(getFrontLeftEncoder() + "AUTO FRONT LEFT ENCODER");
                return true;
            } 
        } else if (Math.abs(gyro.getVelocityY()) < Constants.VELOCITY_THRESHHOLD) {
            System.out.println("Drive stalled");
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
        difference = (getFacingAngle() - targetAngle);

        if (Math.abs(difference) < Constants.TURN_TOLERANCE) {
            rotationSpeed = 0;
        } else if (difference < 0) {
            rotationSpeed = 0.0035 * Math.abs(difference) + 0.05; //changed from 0.005 to 0.004
        } else if (difference > 0) {
            rotationSpeed = -0.0035 * Math.abs(difference) - 0.05;
        }
        robotDrive.driveCartesian(0, 0, rotationSpeed);
    }
    
    public void autoDrive() {
        driveDifference = targetDistance - getFrontLeftEncoder();
        startAutoDriveTime = Timer.getFPGATimestamp(); //this one

        // if (driveDifference < Constants.SLOWING_DISTANCE) { // magic number >:( -NC
        //     speed = 0.2;
        // } else if (driveDifference > -Constants.SLOWING_DISTANCE) {
        //     speed = -0.2;
        // }

        if (Math.abs(driveDifference) < Constants.DISTANCE_TOLERANCE) {
            speed = 0;
        // } else if (Math.abs(getFrontLeftEncoder()) < 5) {
        //     speed = 0.15;
        } 
        else if (driveDifference > 0) { //  && Math.abs(getFrontLeftEncoder()) > 5
            if (Math.abs(getFrontLeftEncoder()) < 3) {
                speed = 0.15;
            } else {
                speed = 0.00625 * Math.abs(driveDifference) + 0.05; //changed from 0.0075 to 0.0065
            }
        } else if (driveDifference < 0) { //  && Math.abs(getFrontLeftEncoder()) > 5
             if (Math.abs(getFrontLeftEncoder()) < 3) {
                speed = -0.15;
            } else {
                speed = -0.00625 * Math.abs(driveDifference) - 0.05;
            }
        }
        System.out.println("AUTODRIVE SPEED: " + speed);
        robotDrive.driveCartesian(speed, 0, 0);
        // remove printlines -NC
        //System.out.println("ENCODER INCHES: " + getFrontLeftEncoder());
        //System.out.println("DRIVE DIFFERENCE (DIST FROM TARGET): " + driveDifference);
    }

    public void stopDriveTrain() {
        robotDrive.driveCartesian(0, 0 , 0);
    }
     
}
