//include rev with shoot? TALK TO NOAH SATURDAY 2/10/24
//limelight ground distance print is commented
//limitswitch code (intake->hold) commented
//ABUTTON CHANGED TO LEFTBUMPER (me as aux i want it to be leftbumper, if shooter incorporates rev KEEP IT RIGHT BUMPER)
//leftBumperReleased for making hold->stopped STATE change easier (doesn't bug into INTAKE for a second)

//rev-> hold -> intake broken EDIT: maybe fixed?


//CHANGE MOTOR IDS BACKsalkjsaldfjlsakjflskjdflksjdflkjsdlfkjslkfjsldkfjlsdkfjlskjflskdjflkdjflksdjflskjdflsdjflsdkjf - from ophelia 


package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NoteSystem {
    public SparkPIDController m_shooterPidController;
    public SparkPIDController m_intakePidController;

    // limit switch not color sensor
    private static DigitalInput intakeLimitSwitch = new DigitalInput(Constants.INTAKE_LIMIT_SWITCH_CHANNEL);
    
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public Limelight limelight;

    NoteAction state;

    double startTime;

    public NoteSystem(Limelight limelight) {
        this.limelight = limelight;
        SmartDashboard.putBoolean("Note Detected", false);
    }

    public boolean isNoteDetected() {
        boolean note = !intakeLimitSwitch.get();
        SmartDashboard.putBoolean("Note Detected?", note); //green box if it is detected
        return note;
    }

    // testing neos
    public CANSparkMax shootMotor = new CANSparkMax(8, MotorType.kBrushless); // ID 8 CHANGE BACK

    public CANSparkMax intakeMotor = new CANSparkMax(9, MotorType.kBrushless); // ID 9 CHANGE BACK, 6 AND 7 FOR TEST

    public WPI_TalonSRX holdMotor = new WPI_TalonSRX(10);

    public static XboxController aux = new XboxController(1); // 1 is the zux controller - oml "zux" can we rename aux to that
    
    public RelativeEncoder shooterEncoder = shootMotor.getEncoder();
    public RelativeEncoder intakeEncoder = intakeMotor.getEncoder();


    enum NoteAction { 
        STOPPED, INTAKE, HOLD, REV_UP, SHOOT, REVERSEINTAKE 
    }

    NoteAction[] noteActions = {};

    public void noteSystemTeleopInit() { //not being used?
        state = NoteAction.STOPPED;
    }

    public void setCoastMode() {
        shootMotor.setIdleMode(IdleMode.kCoast);
        intakeMotor.setIdleMode(IdleMode.kCoast);
    }

    public void setStopped() {
        holdMotor.set(0);
        m_intakePidController.setReference(0, CANSparkMax.ControlType.kVelocity);
        m_shooterPidController.setReference(0, CANSparkMax.ControlType.kVelocity);
    }

    public void setIntake() {
        m_shooterPidController.setReference(0, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.INTAKE_RPM, CANSparkMax.ControlType.kVelocity);
        holdMotor.set(-0.2);
    }

    public void setRevUp() {
        holdMotor.set(0);
        m_shooterPidController.setReference(Constants.SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.INTAKE_RPM, CANSparkMax.ControlType.kVelocity);
    }

    public void setShoot() {
        holdMotor.set(0.2); //will probably need to change
        m_shooterPidController.setReference(Constants.SHOOTER_RPM, CANSparkMax.ControlType.kVelocity); //will change
        m_intakePidController.setReference(Constants.INTAKE_RPM, CANSparkMax.ControlType.kVelocity); //will change
            
    }

    public void setReverseIntake() {
        holdMotor.set(0.2);
        m_shooterPidController.setReference(0, CANSparkMax.ControlType.kVelocity); //will change
        m_intakePidController.setReference(-Constants.INTAKE_RPM, CANSparkMax.ControlType.kVelocity);
    }

    public void noteSystemUpdate() {
        // //FOR TESTING: CHANGE
        // if (aux.getLeftBumper()) {//intake
        //     intakeMotor.set(0.6);
        // } else if (aux.getRightBumper()) {//shoot
        //     intakeMotor.set(0.5);
        //     shootMotor.set(0.5);
        // } else if (aux.getAButtonPressed()) {
        //     intakeMotor.set(0);
        //     shootMotor.set(0);
        // } else if (aux.getXButton()) {
        //     intakeMotor.set(-0.75);
        // } else {
        //     intakeMotor.set(0);
        //     shootMotor.set(0);
        // } 

        m_shooterPidController = shootMotor.getPIDController();
        m_intakePidController = intakeMotor.getPIDController();

        System.out.println(state.name());
        if (state == NoteAction.STOPPED) {
            setStopped();
            if (aux.getLeftBumper()) {
                state = NoteAction.INTAKE;
            } else if (aux.getRightBumperPressed()) {
                state = NoteAction.REV_UP;
            } else if (aux.getXButton()) {
                state = NoteAction.REVERSEINTAKE;   
            }
        } else if (state == NoteAction.INTAKE) {
            setIntake();
            if (aux.getLeftBumperReleased()) {
                state = NoteAction.STOPPED;
            } else if (isNoteDetected() || aux.getYButtonPressed()) { //limit switch is pressed - need to comment out when testing until we get limit switch
                state = NoteAction.HOLD; 
        } else if (state == NoteAction.REVERSEINTAKE) { //FROM STOPPED OR HOLD
            setReverseIntake();
            if (aux.getXButtonReleased()) {
                state = NoteAction.STOPPED;
            }
        } else if (state == NoteAction.HOLD) {
            setStopped();
            if (aux.getRightBumperPressed()) {
                state = NoteAction.REV_UP;
            } //else if (aux.getLeftBumperReleased()) { //in case you rev-up w/o a note (notesystem: cant hold->intake so INSTEAD hold->stopped->intake to try again)
                //state = NoteAction.STOPPED; //feels a little broken please check - I dont think this is necessary because if you dont have a note you wont get to the hold state
            } else if (aux.getXButton()) {
                state = NoteAction.REVERSEINTAKE;
            } else if (aux.getLeftBumperPressed()) {
                state = NoteAction.INTAKE;
            }
        } else if (state == NoteAction.REV_UP) {
            setRevUp();
            if (Math.abs(shooterEncoder.getVelocity()) > 2800 && Math.abs(intakeEncoder.getVelocity()) > 2800) {
                state = NoteAction.SHOOT;
                startTime = Timer.getFPGATimestamp();
            }
            //else if (aux.getBButton()) {
                //state  = NoteAction.SHOOT; 
            //}
        } else if (state == NoteAction.SHOOT) {
            setShoot();
            if (Timer.getFPGATimestamp() - startTime > 2.0) {
                state = NoteAction.STOPPED;
            }
        }
    }
    
    public void noteSystemSetUpPid() {

        m_shooterPidController = shootMotor.getPIDController();
        m_intakePidController = intakeMotor.getPIDController();

        //PID coefficients
        kP = 0.00015; //needs to be a low number, was 1 that was probably the problem - slay!
        kI = 0;
        kD = 0; 
        kIz = 0; 
        kFF = 0.000172; //FF is driving force - thx I was wondering what it stood for
        kMaxOutput = 1; 
        kMinOutput = -1;
        maxRPM = 5700;
    
        //set PID coefficients
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

    // // mech will decide locking mechanism so arm can stop at multiple angles --> did not happen :((((((((((( only one angle allowed

    // // add brake -> brake????
}
