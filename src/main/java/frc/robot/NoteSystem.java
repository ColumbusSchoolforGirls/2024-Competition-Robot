//salkjsaldfjlsakjflskjdflksjdflkjsdlfkjslkfjsldkfjlsdkfjlskjflskdjflkdjflksdjflskjdflsdjflsdkjf - from ophelia - hi ophelia (from lila)

package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.GenericHID.RumbleType;- see if the rumble still works with this commented out
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NoteSystem {
    public SparkPIDController m_shooterPidController;
    public SparkPIDController m_intakePidController;

    // limit switch to detect the note (using instead of color sensor)
    private static DigitalInput intakeLimitSwitch = new DigitalInput(Constants.INTAKE_LIMIT_SWITCH_CHANNEL);

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public Limelight limelight;

    NoteAction state = NoteAction.STOPPED;
    AutoAction autoActions;

    boolean ampShoot = false;
    boolean sideShoot = false;
    boolean trapShoot = false;
    boolean normalShoot = true;

    boolean isStall = false;
    boolean atSpeed = false;
    boolean isRevving = false;

    double startShootTime;
    double startRevTime;
    double startIntakeTime;

    public NoteSystem(Limelight limelight) {
        this.limelight = limelight;

        this.shootMotor.setSmartCurrentLimit(40);
        this.intakeMotor.setSmartCurrentLimit(40);
        // SmartDashboard.putBoolean("Note Detected", false);
    }

    public boolean isNoteDetected() {
        boolean note = !intakeLimitSwitch.get();
        return note;
    }

    // testing neos
    public CANSparkMax shootMotor = new CANSparkMax(8, MotorType.kBrushless);

    public CANSparkMax intakeMotor = new CANSparkMax(9, MotorType.kBrushless);

    public WPI_TalonSRX holdMotor = new WPI_TalonSRX(10);

    public static XboxController aux = new XboxController(1); // 1 is the zux controller - oml "zux" can we rename aux to that

    public RelativeEncoder shooterEncoder = shootMotor.getEncoder();
    public RelativeEncoder intakeEncoder = intakeMotor.getEncoder();

    enum NoteAction {
        STOPPED, INTAKE, HOLD, REV_UP, SHOOT, REVERSEINTAKE
    }

    NoteAction[] noteActions = {};

    public void noteSystemTeleopInit() { 
        state = NoteAction.STOPPED;
    }

    public void setCoastMode() {
        shootMotor.setIdleMode(IdleMode.kCoast);
        intakeMotor.setIdleMode(IdleMode.kCoast);
        holdMotor.setNeutralMode(NeutralMode.Coast);
    }

    public void setStopped() {
        holdMotor.set(0); //using percent output instead of velocity because it negates coast mode
        intakeMotor.set(0);
        shootMotor.set(0);
    }

    public void setIntake() {
        m_shooterPidController.setReference(Constants.INTAKE_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.INTAKE_RPM, CANSparkMax.ControlType.kVelocity);
        holdMotor.set(1.0);
        //System.out.println("setIntake Called");
    }

    public void setRevUp() {
        holdMotor.set(0.5);
        m_shooterPidController.setReference(-Constants.SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        //System.out.println("SetRevUp Called");
    }

    public void setShoot() {
        holdMotor.set(-1.0); 
        m_shooterPidController.setReference(-Constants.SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.SHOOTER_RPM, CANSparkMax.ControlType.kVelocity); 
        //System.out.println("SetShoot Called");
    }

    public void setAmpRevUp() {
        holdMotor.set(0.5);
        m_shooterPidController.setReference(Constants.AMP_SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.AMP_INTAKE_RPM, CANSparkMax.ControlType.kVelocity); // "other" - an unknown entity                                                                                            // value?
        //System.out.println("SetAmpRevUp Called");
    }

    public void setAmpShoot() {
        holdMotor.set(-1.0);
        m_shooterPidController.setReference(Constants.AMP_SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.AMP_INTAKE_RPM, CANSparkMax.ControlType.kVelocity); // other
        //System.out.println("SetAmpShoot Called");
    }

    public void setSideRevUp() {
        holdMotor.set(0.5);
        m_shooterPidController.setReference(-Constants.SIDE_SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.SIDE_SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
    }

    public void setSideShoot() {
        holdMotor.set(-1.0);
        m_shooterPidController.setReference(-Constants.SIDE_SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.SIDE_SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
    }

    public void setReverseIntake() {
        holdMotor.set(-1.0);
        m_shooterPidController.setReference(0, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.REVERSE_INTAKE_RPM, CANSparkMax.ControlType.kVelocity);
        //System.out.println("SetReverseIntake Called");
    }

    public void setTrapRevUp() {
        holdMotor.set(0.5);
        m_shooterPidController.setReference(Constants.TRAP_SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.TRAP_INTAKE_RPM, CANSparkMax.ControlType.kVelocity);
    }

    public void setTrapShoot() {
        holdMotor.set(-1.0);
        m_shooterPidController.setReference(Constants.TRAP_SHOOTER_RPM, CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(Constants.TRAP_INTAKE_RPM, CANSparkMax.ControlType.kVelocity);
    }

    public void noteSystemUpdate() {

        m_shooterPidController = shootMotor.getPIDController();
        m_intakePidController = intakeMotor.getPIDController();

        //System.out.println(state.name());
        if (state == NoteAction.STOPPED) {
            setStopped();
            if (aux.getLeftBumper()) {
                state = NoteAction.INTAKE;
                startIntakeTime = Timer.getFPGATimestamp();

            } else if (aux.getRightBumperPressed()) {
                normalShoot = true;
                ampShoot = false;
                sideShoot = false;
                trapShoot = false;
                state = NoteAction.REV_UP;
                startRevTime = Timer.getFPGATimestamp();
            } else if (aux.getAButtonPressed()) {
                normalShoot = false;
                ampShoot = true;
                sideShoot = false;
                trapShoot = false;
                state = NoteAction.REV_UP;
                startRevTime = Timer.getFPGATimestamp();
            } else if (aux.getBButtonPressed()) {
                normalShoot = false;
                ampShoot = false;
                sideShoot = true;
                trapShoot = false;
                state = NoteAction.REV_UP;
                startRevTime = Timer.getFPGATimestamp();
            } else if (aux.getXButton()) {
                state = NoteAction.REVERSEINTAKE;
                startIntakeTime = Timer.getFPGATimestamp();
            } else if (aux.getLeftTriggerAxis() > 0.3) {
                state = NoteAction.REV_UP;
                startRevTime = Timer.getFPGATimestamp();
                normalShoot = false;
                ampShoot = false;
                sideShoot = false;
                trapShoot = true;
            }
        } else if (state == NoteAction.INTAKE) {
            //System.out.println(intakeMotor.getOutputCurrent() + "      velocity       " + intakeEncoder.getVelocity());

            if (aux.getLeftBumperReleased() || aux.getYButtonPressed()) {
                state = NoteAction.STOPPED;
                atSpeed = false;
                isRevving = false;
            } else if (isNoteDetected()) { // limit switch is pressed ("is note detected", not "is not detected")
                state = NoteAction.HOLD;
            }
            boolean isStopped = intakeEncoder.getVelocity() < 100;
            boolean hasRunLong = Timer.getFPGATimestamp() - startIntakeTime > 0.5;
            if (isStopped && hasRunLong) {
                intakeMotor.set(0);
                shootMotor.set(0);
                isStall = true;
                // System.out.println("intake has stalled");
            } else {
                setIntake();
                isStall = false;
            }
        } else if (state == NoteAction.REVERSEINTAKE) { // FROM STOPPED OR HOLD
            setReverseIntake();
            if (aux.getXButtonReleased()) {
                state = NoteAction.STOPPED;
            }
        } else if (state == NoteAction.HOLD) {
            setStopped();
            if (aux.getRightBumperPressed()) {
                normalShoot = true;
                ampShoot = false;
                sideShoot = false;
                trapShoot = false;
                state = NoteAction.REV_UP;
                startRevTime = Timer.getFPGATimestamp();
            } else if (aux.getAButtonPressed()) {
                normalShoot = false;
                ampShoot = true;
                sideShoot = false;
                trapShoot = false;
                state = NoteAction.REV_UP;
                startRevTime = Timer.getFPGATimestamp();
            } else if (aux.getBButtonPressed()) {
                normalShoot = false;
                ampShoot = false;
                sideShoot = true;
                trapShoot = false;
                state = NoteAction.REV_UP;
                startRevTime = Timer.getFPGATimestamp();
            } else if (aux.getLeftTriggerAxis() > 0.3) {
                normalShoot = false;
                ampShoot = false;
                sideShoot = false;
                trapShoot = true;
                state = NoteAction.REV_UP;
                startRevTime = Timer.getFPGATimestamp();
            } else if (aux.getXButton()) {
                state = NoteAction.REVERSEINTAKE;
                startIntakeTime = Timer.getFPGATimestamp();
            }
        } else if (state == NoteAction.REV_UP) {
            isRevving = true;
            if (normalShoot) {
                setRevUp();
                if (Math.abs(shooterEncoder.getVelocity()) > Constants.SHOOTING_VELOCITY
                        && Math.abs(intakeEncoder.getVelocity()) > Constants.SHOOTING_VELOCITY) {
                    atSpeed = true;
                    startShootTime = Timer.getFPGATimestamp(); //broke last time it was uncommented
                    // rev if we're driving around revving up
                    if (aux.getRightTriggerAxis() > 0.3) {
                        state = NoteAction.SHOOT;
                    }
                    if (DriverStation.isAutonomous()) { // if you want something fun to do, go to the definition of
                                                        // isAutonomous try... it finally, you might catch something
                                                        // cough cough
                        state = NoteAction.SHOOT;
                    }
                }
            } else if (ampShoot) {
                setAmpRevUp();
                if (Math.abs(shooterEncoder.getVelocity()) > Constants.AMP_SHOOTING_VELOCITY
                        && Math.abs(intakeEncoder.getVelocity()) > Constants.AMP_INTAKE_VELOCITY) {
                    atSpeed = true;
                    startShootTime = Timer.getFPGATimestamp();
                    state = NoteAction.SHOOT;
                }
            } else if (sideShoot) {
                setSideRevUp();
                if (Math.abs(shooterEncoder.getVelocity()) > Constants.SIDE_SHOOTING_VELOCITY
                        && Math.abs(intakeEncoder.getVelocity()) > Constants.SIDE_SHOOTING_VELOCITY) {
                    atSpeed = true;
                    startShootTime = Timer.getFPGATimestamp(); // might remove time limits from rev if we're driving
                                                               // around revving up
                    if (aux.getRightTriggerAxis() > 0.3) {
                        state = NoteAction.SHOOT;
                    }
                    if (DriverStation.isAutonomous()) {
                        state = NoteAction.SHOOT;
                    }
                }
            } else if (trapShoot) {
                setTrapRevUp();
                if (Math.abs(shooterEncoder.getVelocity()) > Constants.TRAP_SHOOTING_VELOCITY
                        && Math.abs(intakeEncoder.getVelocity()) > Constants.TRAP_INTAKE_VELOCITY) {
                    atSpeed = true;
                    startShootTime = Timer.getFPGATimestamp(); // might remove time limits from rev if we're driving
                                                               // around revving up
                    state = NoteAction.SHOOT;
                }
            }
            if (aux.getYButtonPressed()) {
                state = NoteAction.STOPPED;
                atSpeed = false;
                isRevving = false;
            }
            if (DriverStation.isAutonomous()) {
                if (Timer.getFPGATimestamp() - startRevTime > 2.25) { // do not set < 2
                    state = NoteAction.STOPPED;
                }
            }

            //System.out.println(shooterEncoder.getVelocity() + "*******SHOOT***********");
            //System.out.println(intakeEncoder.getVelocity() + "==========INTAKE===========");

        } else if (state == NoteAction.SHOOT) {
            if (normalShoot) {
                setShoot();
            } else if (ampShoot) {
                setAmpShoot();
            } else if (sideShoot) {
                setSideShoot();
            } else if (trapShoot) {
                setTrapShoot();
            }
            if (Timer.getFPGATimestamp() - startShootTime > 1.5) { // could lower more NOT LESS THAN 1 (was 2.0)
                
                state = NoteAction.STOPPED;
                atSpeed = false;
                isRevving = false;
            }
        }

        if (atSpeed && !DriverStation.isAutonomous()) {
            aux.setRumble(GenericHID.RumbleType.kLeftRumble, 1.0); //rumble rumble CSG!
        } else {
            aux.setRumble(GenericHID.RumbleType.kLeftRumble, 0.0);
        }
    }

    public void updates() {
        SmartDashboard.putBoolean("Note Detected?",isNoteDetected()); //green box if it is detected
        SmartDashboard.putBoolean("Stall Detected", isStall);
        SmartDashboard.putBoolean("Ready to Shoot", atSpeed);
        SmartDashboard.putBoolean("Revving", isRevving);
    }

    public void noteSystemSetUpPid() {

        m_shooterPidController = shootMotor.getPIDController();
        m_intakePidController = intakeMotor.getPIDController();

        // PID coefficients
        kP = 0.00015; 
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0.000172; // FF is driving force - thx I was wondering what it stood for
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
    }

    // limelight stuff to calculate distance with april tags - not actually using...check gitHub if you want to use the code future csg coders - lila :()
}
