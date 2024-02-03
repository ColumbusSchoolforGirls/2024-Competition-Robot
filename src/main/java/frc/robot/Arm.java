package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
    private SparkPIDController m_shooterPidController;
    private SparkPIDController m_intakePidController;

    // color sensor
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public Limelight limelight;

    public Arm(Limelight limelight) {
        this.limelight = limelight;
        SmartDashboard.putBoolean("Note Detected", false);
    }

    private boolean isNoteDetected() {
        return SmartDashboard.getBoolean("Note Detected", false);
    }

    // testing neos
    public CANSparkMax shootMotor = new CANSparkMax(8, MotorType.kBrushless);
    // - make talon public CANSparkMax holdMotor = new CANSparkMax(3, MotorType.kBrushless); //probably a talon
    public CANSparkMax intakeMotor = new CANSparkMax(9, MotorType.kBrushless);
    //public WPI_TalonSRX holdMotor = 
    public WPI_TalonSRX holdMotor = new WPI_TalonSRX(10);

    public static XboxController aux = new XboxController(1); // 1 is the zux controller - oml "zux" can we rename aux to that

    public RelativeEncoder shooterEncoder = shootMotor.getEncoder();
    public RelativeEncoder intakeEncoder = intakeMotor.getEncoder();

    ArmAction state;

    enum ArmAction { STOPPED, INTAKE, HOLD, REV_UP, SHOOT
    }

    ArmAction[] armActions = {};

    public void armTeleopInit() {
        this.state = ArmAction.STOPPED;
    }

    public void armUpdate() {
        System.out.println(state.name());
        if (state == ArmAction.STOPPED) {
            holdMotor.set(0);
            
            if (aux.getAButtonPressed()) {
                this.state = ArmAction.INTAKE;
            }
        } else if (state == ArmAction.INTAKE) {
            //set intake motors
            if (aux.getAButtonPressed()) {
                this.state = ArmAction.STOPPED;
            } else if (isNoteDetected()) { //color is detected
                this.state = ArmAction.HOLD;
            }
        } else if (state == ArmAction.HOLD) {
            //stop motors
            if (aux.getRightBumper()) {
                this.state = ArmAction.REV_UP;
            }
        } else if (state == ArmAction.REV_UP) {
            //rev up shooter motor and intake motor
            if (aux.getRightBumperReleased()) {
                this.state = ArmAction.HOLD;
            } else if (aux.getBButton()) {
                this.state  = ArmAction.SHOOT;
            }
        } else if (state == ArmAction.SHOOT) {
            //set motors for shooting speed
            if (aux.getBButtonReleased()) {
                this.state = ArmAction.STOPPED;
                //possibly use time
                //driver needs to hold until note is released 
            }
        }
    }

    public void armPidValues() {

        m_shooterPidController = shootMotor.getPIDController();
        m_intakePidController = intakeMotor.getPIDController();

        // PID coefficients
        kP = 6e-5; 
        kI = 0;
        kD = 0; 
        kIz = 0; 
        kFF = 0.000015; 
        kMaxOutput = 1; 
        kMinOutput = -1;
        maxRPM = 5700;
    
        // set PID coefficients
        m_shooterPidController.setP(kP);
        m_shooterPidController.setI(kI);
        m_shooterPidController.setD(kD);
        m_shooterPidController.setIZone(kIz);
        m_shooterPidController.setFF(kFF);
        m_shooterPidController.setOutputRange(kMinOutput, kMaxOutput);
        m_intakePidController.setP(kP);
        m_intakePidController.setI(kI);
        m_intakePidController.setD(kD);
        m_intakePidController.setIZone(kIz);
        m_intakePidController.setFF(kFF);
        m_intakePidController.setOutputRange(kMinOutput, kMaxOutput);
    
        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("P Gain", kP);
        SmartDashboard.putNumber("I Gain", kI);
        SmartDashboard.putNumber("D Gain", kD);
        SmartDashboard.putNumber("I Zone", kIz);
        SmartDashboard.putNumber("Feed Forward", kFF);
        SmartDashboard.putNumber("Max Output", kMaxOutput);
        SmartDashboard.putNumber("Min Output", kMinOutput);

        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("P Gain", 0);
        double i = SmartDashboard.getNumber("I Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);
        double iz = SmartDashboard.getNumber("I Zone", 0);
        double ff = SmartDashboard.getNumber("Feed Forward", 0);
        double max = SmartDashboard.getNumber("Max Output", 0);
        double min = SmartDashboard.getNumber("Min Output", 0);
    
        // if PID coefficients on SmartDashboard have changed, write new values to controller
        if((p != kP)) { m_shooterPidController.setP(p); kP = p; }
        if((i != kI)) { m_shooterPidController.setI(i); kI = i; }
        if((d != kD)) { m_shooterPidController.setD(d); kD = d; }
        if((iz != kIz)) { m_shooterPidController.setIZone(iz); kIz = iz; }
        if((ff != kFF)) { m_shooterPidController.setFF(ff); kFF = ff; }
        if((max != kMaxOutput) || (min != kMinOutput)) { 
            m_shooterPidController.setOutputRange(min, max); 
            kMinOutput = min; kMaxOutput = max; 
        }
        if((p != kP)) { m_intakePidController.setP(p); kP = p; }
        if((i != kI)) { m_intakePidController.setI(i); kI = i; }
        if((d != kD)) { m_intakePidController.setD(d); kD = d; }
        if((iz != kIz)) { m_intakePidController.setIZone(iz); kIz = iz; }
        if((ff != kFF)) { m_intakePidController.setFF(ff); kFF = ff; }
        if((max != kMaxOutput) || (min != kMinOutput)) { 
            m_intakePidController.setOutputRange(min, max); 
            kMinOutput = min; kMaxOutput = max; 
        }

        double setPointShooter = -aux.getLeftY()*maxRPM;
        double setPointIntake = aux.getRightY()*maxRPM;
        m_shooterPidController.setReference(setPointShooter, CANSparkMax.ControlType.kVelocity);
        
        SmartDashboard.putNumber("SetPointShooter", setPointShooter);
        SmartDashboard.putNumber("SetPointIntake", setPointIntake);
        SmartDashboard.putNumber("ProcessVariableShooter", shooterEncoder.getVelocity());
        SmartDashboard.putNumber("ProcessVariableIntake", intakeEncoder.getVelocity());
    }
        /*if () {
            m_intakePidController.setReference(setPointIntake, CANSparkMax.ControlType.kVelocity);
        } else {
            //intakeMotor.set();
        }

        if (Math.abs(shootSpeed) < Constants.AUX_DEADZONE) {
            shootMotor.set(0);
        } else {
            //shootMotor.set();
        } */

        //SmartDashboard.putNumber("Trigger value 1:" , aux.getLeftY());
        //SmartDashboard.putNumber("Trigger value 2:", aux.getRightY());

        //System.out.println("Trigger value 1:" + aux.getLeftY());
        //System.out.println("Trigger value 2:"+ aux.getRightY());

        // limelight stuff to calculate distance with april tags

    public void shooterDistance() {
        double targetOffsetAngle_Vertical = limelight.ty.getDouble(0.0); // getting the vertical angle that the
                                                                         // limelight is off from the april tag
        double limelightMountAngleDegrees = -2; // will need to change
        double limelightLensHeight = 6; // in inches -- will need to change
        // distance from center of the limelight lens to the floor
        double goalHeight= 24; // might need to change // in inches //goal = april tag NOT speaker
        // this is for the speaker
        double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);
        double heightDistanceFromLimelightToAprilTag = goalHeight - limelightLensHeight;
        double distanceFromLimelightToGoal = (heightDistanceFromLimelightToAprilTag) / Math.tan(angleToGoalRadians); //will fl, WAS limelightLensHeight - goalHeight
        // calculates distance
        double groundDistanceToSpeaker = Math.sqrt(Math.pow(distanceFromLimelightToGoal, 2) - Math.pow( heightDistanceFromLimelightToAprilTag , 2));
        // double shooterAngle = something where distanceFromLimelightToGoal is the
        // independent variable
        System.out.println("Distance on ground from limelight to april tag:" + groundDistanceToSpeaker);

    }

    // mech will decide locking mechanism so arm can stop at multiple angles --> did not happen :((((((((((( only one angle allowed

    // add brake
}
