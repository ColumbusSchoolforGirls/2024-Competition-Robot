//include rev with shoot? TALK TO NOAH SATURDAY 2/10/24
//limelight ground distance print is commented
//limitswitch code (intake->hold) commented
//ABUTTON CHANGED TO LEFTBUMPER (me as aux i want it to be leftbumper, if shooter incorporates rev KEEP IT RIGHT BUMPER)
//leftBumperReleased for making hold->stopped STATE change easier (doesn't bug into INTAKE for a second)

//rev-> hold -> intake broken EDIT: maybe fixed?

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NoteSystem {
    private SparkPIDController m_shooterPidController;
    private SparkPIDController m_intakePidController;

    // limit switch not color sensor
    private static DigitalInput intakeLimitSwitch = new DigitalInput(Constants.INTAKE_LIMIT_SWITCH_CHANNEL);
    
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public Limelight limelight;

    public NoteSystem(Limelight limelight) {
        this.limelight = limelight;
        this.state = state;
        SmartDashboard.putBoolean("Note Detected", false);
    }

    private boolean isNoteDetected() {
        // boolean note = intakeLimitSwitch.get();
        // SmartDashboard.putBoolean("Note Detected?", note); //green box if it is detected
        // return note;
        return SmartDashboard.putBoolean("Note Detected?", false);
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

    NoteAction state;

    enum NoteAction { 
        STOPPED, INTAKE, HOLD, REV_UP, SHOOT, REVERSEINTAKE 
    }

    NoteAction[] noteActions = {};

    public void noteSystemTeleopInit() { //not being used?
        this.state = NoteAction.STOPPED;
    }

    public void noteSystemUpdate() {
        System.out.println(state.name());
        if (state == NoteAction.STOPPED) {
            holdMotor.set(0);
            m_shooterPidController.setReference(0, CANSparkMax.ControlType.kVelocity);
            m_intakePidController.setReference(0, CANSparkMax.ControlType.kVelocity);
            if (aux.getLeftBumper()) {
                state = NoteAction.INTAKE;
            } else if (aux.getRightBumper()) {
                state = NoteAction.REV_UP;
            } else if (aux.getXButton()) {
                state = NoteAction.REVERSEINTAKE;   
            }
        } else if (state == NoteAction.INTAKE) {
            holdMotor.set(-0.5); //will probably change?
            m_shooterPidController.setReference(0, CANSparkMax.ControlType.kVelocity);
            m_intakePidController.setReference(120, CANSparkMax.ControlType.kVelocity); //will change with testing
            if (!aux.getLeftBumper()) {
                state = NoteAction.STOPPED;
            } //else if (isNoteDetected()) { //color is detected
                //this.state = NoteAction.HOLD;
            //} CHANGE BACK
        } else if (state == NoteAction.REVERSEINTAKE) { //FROM STOPPED OR HOLD
            holdMotor.set(0.5); //will probably change?
            m_shooterPidController.setReference(0, CANSparkMax.ControlType.kVelocity);
            m_intakePidController.setReference(-120, CANSparkMax.ControlType.kVelocity); //will change with testing
            if (!aux.getXButton()) {
                state = NoteAction.STOPPED;
            }
        } 
        else if (state == NoteAction.HOLD) {
            holdMotor.set(0);
            m_shooterPidController.setReference(0, CANSparkMax.ControlType.kVelocity);
            m_intakePidController.setReference(0, CANSparkMax.ControlType.kVelocity);
            if (aux.getRightBumper()) {
                state = NoteAction.REV_UP;
            } else if (aux.getLeftBumperReleased()) { //in case you rev-up w/o a note (notesystem: cant hold->intake so INSTEAD hold->stopped->intake to try again)
                state = NoteAction.STOPPED; //feels a little broken please check
            } else if (aux.getXButton()) {
                state = NoteAction.REVERSEINTAKE;
            }
        } else if (state == NoteAction.REV_UP) {
            holdMotor.set(0);
            m_shooterPidController.setReference(120, CANSparkMax.ControlType.kVelocity);
            m_intakePidController.setReference(120, CANSparkMax.ControlType.kVelocity);
            //rev up shooter motor and intake motor
            if (aux.getRightBumperReleased()) {
                this.state = NoteAction.HOLD;
            } else if (aux.getBButton()) {
                this.state  = NoteAction.SHOOT;
            }
        } else if (state == NoteAction.SHOOT) {
            holdMotor.set(0.5); //will probably need to change
            m_shooterPidController.setReference(-120, CANSparkMax.ControlType.kVelocity); //will change
            m_intakePidController.setReference(120, CANSparkMax.ControlType.kVelocity); //will change
            if (aux.getBButtonReleased()) {
                this.state = NoteAction.STOPPED;
                //possibly use time
                //driver needs to hold until note is released 
            }
        }
    }

    public void noteSystemSetUpPid() {

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
    
        // // display PID coefficients on SmartDashboard
        // SmartDashboard.putNumber("P Gain", kP);
        // SmartDashboard.putNumber("I Gain", kI);
        // SmartDashboard.putNumber("D Gain", kD);
        // SmartDashboard.putNumber("I Zone", kIz);
        // SmartDashboard.putNumber("Feed Forward", kFF);
        // SmartDashboard.putNumber("Max Output", kMaxOutput);
        // SmartDashboard.putNumber("Min Output", kMinOutput);

        // // read PID coefficients from SmartDashboard
        // double p = SmartDashboard.getNumber("P Gain", 0);
        // double i = SmartDashboard.getNumber("I Gain", 0);
        // double d = SmartDashboard.getNumber("D Gain", 0);
        // double iz = SmartDashboard.getNumber("I Zone", 0);
        // double ff = SmartDashboard.getNumber("Feed Forward", 0);
        // double max = SmartDashboard.getNumber("Max Output", 0);
        // double min = SmartDashboard.getNumber("Min Output", 0);
    
        // // if PID coefficients on SmartDashboard have changed, write new values to controller
        // if((p != kP)) { m_shooterPidController.setP(p); kP = p; }
        // if((i != kI)) { m_shooterPidController.setI(i); kI = i; }
        // if((d != kD)) { m_shooterPidController.setD(d); kD = d; }
        // if((iz != kIz)) { m_shooterPidController.setIZone(iz); kIz = iz; }
        // if((ff != kFF)) { m_shooterPidController.setFF(ff); kFF = ff; }
        // if((max != kMaxOutput) || (min != kMinOutput)) { 
        //     m_shooterPidController.setOutputRange(min, max); 
        //     kMinOutput = min; kMaxOutput = max; 
        // }
        // if((p != kP)) { m_intakePidController.setP(p); kP = p; }
        // if((i != kI)) { m_intakePidController.setI(i); kI = i; }
        // if((d != kD)) { m_intakePidController.setD(d); kD = d; }
        // if((iz != kIz)) { m_intakePidController.setIZone(iz); kIz = iz; }
        // if((ff != kFF)) { m_intakePidController.setFF(ff); kFF = ff; }
        // if((max != kMaxOutput) || (min != kMinOutput)) { 
        //     m_intakePidController.setOutputRange(min, max); 
        //     kMinOutput = min; kMaxOutput = max; 
        // }

        // double setPointShooter = -aux.getLeftY()*maxRPM;
        // double setPointIntake = aux.getRightY()*maxRPM;
        // m_shooterPidController.setReference(setPointShooter, CANSparkMax.ControlType.kVelocity);
        
        // SmartDashboard.putNumber("SetPointShooter", setPointShooter);
        // SmartDashboard.putNumber("SetPointIntake", setPointIntake);
        // SmartDashboard.putNumber("ProcessVariableShooter", shooterEncoder.getVelocity());
        // SmartDashboard.putNumber("ProcessVariableIntake", intakeEncoder.getVelocity());
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
        //System.out.println("Distance on ground from limelight to april tag:" + groundDistanceToSpeaker);

    }

    // mech will decide locking mechanism so arm can stop at multiple angles --> did not happen :((((((((((( only one angle allowed

    // add brake
}
