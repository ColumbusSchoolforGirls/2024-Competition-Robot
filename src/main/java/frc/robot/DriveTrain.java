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

    private double targetAngle;
    private double targetDistance;
    private double difference;
    private double driveDifference;
    private double speed;
    private double rotationSpeed;
    double startAutoDriveTime;

    AHRS gyro = new AHRS(SPI.Port.kMXP);

    public Limelight limelight;

    public CANSparkMax frontLeft = new CANSparkMax(Constants.FRONT_LEFT_ID, MotorType.kBrushless);
    public CANSparkMax backLeft = new CANSparkMax(Constants.BACK_LEFT_ID, MotorType.kBrushless);
    public CANSparkMax frontRight = new CANSparkMax(Constants.FRONT_RIGHT_ID, MotorType.kBrushless);
    public CANSparkMax backRight = new CANSparkMax(Constants.BACK_RIGHT_ID, MotorType.kBrushless);

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
        frontRight.setInverted(true);
        backRight.setInverted(true);

        frontLeft.setInverted(false);
        backLeft.setInverted(false);
    }

    // makes the position at 0 when teleop and autonomous starts
    public void resetEncoders() {
        frontLeftEncoder.setPosition(0);
        backLeftEncoder.setPosition(0);
        frontRightEncoder.setPosition(0);
        backRightEncoder.setPosition(0);
    }

    public double getFrontLeftEncoder() {
        return frontLeftEncoder.getPosition() / Constants.GEAR_RATIO * Constants.WHEEL_CIRCUMFERENCE;
    }

    public double getFrontRightEncoder() {
        return frontRightEncoder.getPosition() / Constants.GEAR_RATIO * Constants.WHEEL_CIRCUMFERENCE;
    }

    public double getBackLeftEncoder() {
        return backLeftEncoder.getPosition() / Constants.GEAR_RATIO * Constants.WHEEL_CIRCUMFERENCE;
    }

    public double getBackRightEncoder() {
        return backRightEncoder.getPosition() / Constants.GEAR_RATIO * Constants.WHEEL_CIRCUMFERENCE;
    }

    public void update() {
        SmartDashboard.putNumber("Front Left Encoder", getFrontLeftEncoder());
        SmartDashboard.putNumber("Back Left Encoder", getBackLeftEncoder());
        SmartDashboard.putNumber("Front Right Encoder", getFrontRightEncoder());
        SmartDashboard.putNumber("Back Right Encoder", getBackRightEncoder());

        SmartDashboard.putNumber("DriveTrain Front Left", frontLeft.get());
        SmartDashboard.putNumber("DriveTrain Back Left", backLeft.get());
        SmartDashboard.putNumber("DriveTrain Front Right", frontRight.get());
        SmartDashboard.putNumber("DriveTrain Back Right", backRight.get());

    }

    // green box if it is in brake mode
    void setBrakeMode() {
        frontLeft.setIdleMode(IdleMode.kBrake);
        backLeft.setIdleMode(IdleMode.kBrake);
        frontRight.setIdleMode(IdleMode.kBrake);
        backRight.setIdleMode(IdleMode.kBrake);
        SmartDashboard.putBoolean("Brake Mode", true);
    }

    // red box if it is in coast mode
    private void setCoastMode() {
        frontLeft.setIdleMode(IdleMode.kCoast);
        backLeft.setIdleMode(IdleMode.kCoast);
        frontRight.setIdleMode(IdleMode.kCoast);
        backRight.setIdleMode(IdleMode.kCoast);
        SmartDashboard.putBoolean("Brake Mode", false);
    }

    // brake mode for precision
    public void setAuto() {
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

    public void drive(double normalSpeed, double crawlSpeed, boolean noDeadZone) { // scaling

        double forwardSpeed = -driveController.getLeftY();
        double sideSpeed = driveController.getLeftX();
        double rotationSpeed = driveController.getRightX();

        if (!driveController.getRightBumper()) { // so basically if you are clicking the right bumper, you go at full
                                                 // speed (-100% to 100%)
            forwardSpeed = -driveController.getLeftY() * normalSpeed; // if you aren't clicking any bumper, you go
                                                                      // "normal speed" (-50% to 50%)
            sideSpeed = driveController.getLeftX() * normalSpeed; // LOWERED SPEEDS IN ROBOT
            rotationSpeed = driveController.getRightX() * normalSpeed;
        }

        // if you click the left bumper you go at a slow scaled speed
        if (driveController.getLeftBumper()) {
            forwardSpeed = -driveController.getLeftY() * crawlSpeed;
            sideSpeed = driveController.getLeftX() * crawlSpeed;
            rotationSpeed = driveController.getRightX() * crawlSpeed;
        }

        double deadZone = Constants.DRIVE_CONTROLLER_DEADZONE;
        double driftDeadZone = Constants.DRIVE_CONTROLLER_DRIFT_DEADZONE;

        // dont want deadzone for auto aka boolean; true=don't use deadzone
        if (noDeadZone) {
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

    public double getFacingAngle() {
        return gyro.getAngle();
    }

    public boolean turnComplete() {
        difference = (getFacingAngle() - targetAngle);

        return Math.abs(difference) < Constants.TURN_TOLERANCE;
        // if this doesn't work, its because we just changed it - 3/15/2024
    }

    public boolean driveComplete() {
        driveDifference = targetDistance - getFrontLeftEncoder();
        if (Math.abs(driveDifference) < Constants.DISTANCE_TOLERANCE) {
            if (frontLeftEncoder.getVelocity() < 0.03) { // to prevent skidding bc of turning before drive is complete
                return true;
            }
        } else if (Math.abs(gyro.getVelocityY()) < Constants.AUTO_DRIVE_VELOCITY_THRESHHOLD) { // change: test //change
                                                                                               // add a time delay 0.5
            System.out.println("Drive stalled - TESTING");
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
            rotationSpeed = 0.0035 * Math.abs(difference) + 0.05;
        } else if (difference > 0) {
            rotationSpeed = -0.0035 * Math.abs(difference) - 0.05;
        }
        robotDrive.driveCartesian(0, 0, rotationSpeed);
    }

    public void autoDrive() {
        driveDifference = targetDistance - getFrontLeftEncoder();
        startAutoDriveTime = Timer.getFPGATimestamp(); // this one - ok...

        // this is the old auto drive in case the trapezoid one breaks things
        // if (Math.abs(driveDifference) < Constants.DISTANCE_TOLERANCE) {
        // speed = 0;

        // }
        // else if (driveDifference > 0) { // && Math.abs(getFrontLeftEncoder()) > 5
        // if (Math.abs(getFrontLeftEncoder()) < 3) {
        // speed = 0.15;
        // } else {
        // speed = 0.00625 * Math.abs(driveDifference) + 0.05; //changed from 0.0075 to
        // 0.0065
        // }
        // } else if (driveDifference < 0) { // && Math.abs(getFrontLeftEncoder()) > 5
        // if (Math.abs(getFrontLeftEncoder()) < 3) {
        // speed = -0.15;
        // } else {
        // speed = -0.00625 * Math.abs(driveDifference) - 0.05;
        // }
        // }

        double vMax = 0.20;
        double speedUpDistance = 3;
        double position = Math.abs(getFrontLeftEncoder());
        double minSpeed = 0.1;
        double slowDownDistance = 3; // might need to change

        // testing needed for trapezoidal
        // this is a trapezoidal autodrive, which means that it starts slower and gets
        // faster and reaches a certain top speedand then gets slower as it approaches
        // the end
        if (Math.abs(driveDifference) < Constants.DISTANCE_TOLERANCE) {
            speed = 0;
        } else {
            if (position < speedUpDistance) {
                speed = Math.max(vMax * (position / speedUpDistance), minSpeed);
            } else if (driveDifference > slowDownDistance) {
                speed = vMax;
            } else {
                speed = Math.max(vMax * (driveDifference / slowDownDistance), minSpeed);
            }
            if (driveDifference < 0) {
                speed *= -1;
            }
        }
        System.out.println("(testing) AUTODRIVE SPEED: " + speed);
        robotDrive.driveCartesian(speed, 0, 0);
    }

    public void stopDriveTrain() {
        robotDrive.driveCartesian(0, 0, 0);
    }

}
