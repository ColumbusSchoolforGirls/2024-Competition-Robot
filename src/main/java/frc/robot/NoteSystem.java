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

    enum NoteAction {
        STOPPED, INTAKE, HOLD, REV_UP, SHOOT, REVERSEINTAKE
    }

    enum ShootMode {
        NORMAL, SIDE, AMP, TRAP
    }

    NoteAction state = NoteAction.STOPPED;
    ShootMode shootMode = ShootMode.NORMAL;
    String noteState = "STOPPED";

    public SparkPIDController m_shooterPidController;
    public SparkPIDController m_intakePidController;

    // limit switch to detect the note (using instead of color sensor)
    private static DigitalInput intakeLimitSwitch = new DigitalInput(Constants.INTAKE_LIMIT_SWITCH_CHANNEL);

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public Limelight limelight;
    public Robot robot;
    public DriveTrain driveTrain;
    public AutoStep autoStep;

    double startTime;

    public CANSparkMax shootMotor = new CANSparkMax(8, MotorType.kBrushless);
    public CANSparkMax intakeMotor = new CANSparkMax(9, MotorType.kBrushless);
    public WPI_TalonSRX holdMotor = new WPI_TalonSRX(10);

    public static XboxController aux = new XboxController(1); // 1 is the zux controller - oml "zux"

    public RelativeEncoder shooterEncoder = shootMotor.getEncoder();
    public RelativeEncoder intakeEncoder = intakeMotor.getEncoder();

    public NoteSystem(Limelight limelight) {
        this.limelight = limelight;

        this.shootMotor.setSmartCurrentLimit(40);
        this.intakeMotor.setSmartCurrentLimit(40);
    }

    public boolean isNoteDetected() {
        return !intakeLimitSwitch.get(); // limit switch is wired opposite
    }

    public void teleopInit() {
        state = NoteAction.STOPPED;
        setMotors();
    }

    public void setCoastMode() {
        shootMotor.setIdleMode(IdleMode.kCoast);
        intakeMotor.setIdleMode(IdleMode.kCoast);
        holdMotor.setNeutralMode(NeutralMode.Coast);
    }

    public int getShootTargetSpeed() {
        if (state == NoteAction.INTAKE) {
            return Constants.INTAKING_RPM;
        }

        if (shootMode == ShootMode.NORMAL) {
            return Constants.SHOOTER_RPM;
        } else if (shootMode == ShootMode.SIDE) {
            return Constants.SIDE_SHOOTER_RPM;
        } else if (shootMode == ShootMode.AMP) {
            return Constants.AMP_SHOOTER_RPM;
        } else if (shootMode == ShootMode.TRAP) {
            return Constants.TRAP_SHOOTER_RPM;
        }

        return 0;
    }

    public int getIntakeTargetSpeed() {
        if (state == NoteAction.INTAKE) {
            return Constants.INTAKING_RPM;
        } else if (state == NoteAction.REVERSEINTAKE) {
            return Constants.REVERSE_INTAKING_RPM;
        }

        if (shootMode == ShootMode.NORMAL) {
            return Constants.INTAKE_RPM;
        } else if (shootMode == ShootMode.SIDE) {
            return Constants.SIDE_INTAKE_RPM;
        } else if (shootMode == ShootMode.AMP) {
            return Constants.AMP_INTAKE_RPM;
        } else if (shootMode == ShootMode.TRAP) {
            return Constants.TRAP_INTAKE_RPM;
        }

        return 0;
    }

    public double getHoldSpeed() {
        if (state == NoteAction.REV_UP) {
            return 0.5;
        } else if (state == NoteAction.SHOOT) {
            return -1.0;
        } else if (state == NoteAction.INTAKE) {
            return 1.0;
        } else if (state == NoteAction.REVERSEINTAKE) {
            return -0.5;
        }
        return 0.0;
    }

    public int getShooterThresholdSpeed() {
        if (shootMode == ShootMode.NORMAL) {
            return Constants.SHOOTING_THRESHOLD;
        } else if (shootMode == ShootMode.SIDE) {
            return Constants.SIDE_SHOOTING_THRESHOLD;
        } else if (shootMode == ShootMode.AMP) {
            return Constants.AMP_SHOOTING_THRESHOLD;
        } else if (shootMode == ShootMode.TRAP) {
            return Constants.TRAP_SHOOTING_THRESHOLD;
        }
        return 0;
    }

    public int getIntakeThresholdSpeed() {
        if (shootMode == ShootMode.NORMAL) {
            return Constants.SHOOTING_THRESHOLD;
        } else if (shootMode == ShootMode.SIDE) {
            return Constants.SIDE_SHOOTING_THRESHOLD;
        } else if (shootMode == ShootMode.AMP) {
            return Constants.AMP_INTAKE_THRESHOLD;
        } else if (shootMode == ShootMode.TRAP) {
            return Constants.TRAP_SHOOTER_RPM;
        }
        return 0;
    }

    public void startRevUp(ShootMode newMode) {
        shootMode = newMode;
        state = NoteAction.REV_UP;
        saveTime();
    }

    public void autoSoloRevUp() {
        holdMotor.set(0.5);
        m_shooterPidController.setReference(getShootTargetSpeed(), CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(getIntakeTargetSpeed(), CANSparkMax.ControlType.kVelocity);
    }

    public void startIntake(NoteAction action) {
        state = action;
        saveTime();
    }

    public void stopMotors() {
        holdMotor.set(0); // using percent output instead of velocity because it negates coast mode
        intakeMotor.set(0);
        shootMotor.set(0);
    }

    public void setMotors() {
        holdMotor.set(getHoldSpeed());
        m_shooterPidController.setReference(getShootTargetSpeed(), CANSparkMax.ControlType.kVelocity);
        m_intakePidController.setReference(getIntakeTargetSpeed(), CANSparkMax.ControlType.kVelocity);
    }

    public void saveTime() {
        startTime = Timer.getFPGATimestamp();
    }

    public boolean isAtSpeed() {
        return (state == NoteAction.REV_UP) && Math.abs(shooterEncoder.getVelocity()) > getShooterThresholdSpeed()
                && Math.abs(intakeEncoder.getVelocity()) > getIntakeThresholdSpeed();
    }

    public boolean isIntakeStalled() {
        boolean isStopped = intakeEncoder.getVelocity() < 100; // TODO: make constants
        boolean hasRunLong = Timer.getFPGATimestamp() - startTime > 0.5;
        return isStopped && hasRunLong;
    }

    public boolean isRevving() {
        return state == NoteAction.REV_UP;
    }

    public void update() {
        m_shooterPidController = shootMotor.getPIDController();
        m_intakePidController = intakeMotor.getPIDController();

        noteState = state.toString();

        if (aux.getYButtonPressed()) {
            state = NoteAction.STOPPED;
        }

        // System.out.println(state.name());

        // LIST OF BUTTONS AND THEIR ACTIONS:
        /*
         * Left bumper (hold): intake
         * Right bumper (press): revsup for normal shoot
         * Right trigger (press): shoots ONLY for normal & side
         * Left trigger (press): revs up AND shoots for trap
         * B button (press): revs up ONLY side shoot
         * A button (press): revs up AND shoots for amp
         * X button (hold): reverse intake
         * Y button (press): stops motors
         */

        if (state == NoteAction.STOPPED) {
            stopMotors();
            if (aux.getLeftBumper()) {
                startIntake(NoteAction.INTAKE);
            } else if (aux.getXButton()) {
                startIntake(NoteAction.REVERSEINTAKE);
            } else if (aux.getRightBumperPressed()) {
                startRevUp(ShootMode.NORMAL);
            } else if (aux.getBButtonPressed()) {
                startRevUp(ShootMode.SIDE);
            } else if (aux.getAButtonPressed()) {
                startRevUp(ShootMode.AMP);
            } else if (aux.getLeftTriggerAxis() > Constants.TRIGGER_DEADZONE) {
                startRevUp(ShootMode.TRAP);
            }
        } else if (state == NoteAction.INTAKE) {
            if (aux.getLeftBumperReleased()) {
                state = NoteAction.STOPPED;
            } else if (isNoteDetected()) { // limit switch is pressed ("is note detected", not "is not detected")
                state = NoteAction.HOLD;
            }
            if (isIntakeStalled()) {
                stopMotors();
            } else {
                setMotors();
            }
        } else if (state == NoteAction.REVERSEINTAKE) { // FROM STOPPED OR HOLD
            setMotors();
            if (aux.getXButtonReleased()) {
                state = NoteAction.STOPPED;
            }
        } else if (state == NoteAction.HOLD) {
            stopMotors();
            if (aux.getRightBumperPressed()) {
                startRevUp(ShootMode.NORMAL);
            } else if (aux.getBButtonPressed()) {
                startRevUp(ShootMode.SIDE);
            } else if (aux.getAButtonPressed()) {
                startRevUp(ShootMode.AMP);
            } else if (aux.getLeftTriggerAxis() > Constants.TRIGGER_DEADZONE) {
                startRevUp(ShootMode.TRAP);
            } else if (aux.getXButton()) {
                state = NoteAction.REVERSEINTAKE;
                saveTime();
            }
        } else if (state == NoteAction.REV_UP) {
            // robot = new Robot();
            setMotors();
            // boolean hasRunLongInAuto = DriverStation.isAutonomous() && Timer.getFPGATimestamp() - startTime > 3; // removed (is in auto) will shoot if running in teleop too long as well
            boolean hasRunLong = Timer.getFPGATimestamp() - startTime > 3;
            if (isAtSpeed() || hasRunLong) { // will shoot if it's run too long (will basically be at speed
                                                   // anyway)
                if (aux.getRightTriggerAxis() > Constants.TRIGGER_DEADZONE) {
                    state = NoteAction.SHOOT;
                    saveTime();
                }
                if (DriverStation.isAutonomous() || shootMode == ShootMode.AMP || shootMode == ShootMode.TRAP) {
                    state = NoteAction.SHOOT;
                    saveTime();
                    // } //TODO: check all of this
                }
            }
            // TODO: test if we want to be able to shoot not at speed in teleop
        } else if (state == NoteAction.SHOOT) {
            setMotors();
            if (Timer.getFPGATimestamp() - startTime > 0.5) { // do not set < 1
                state = NoteAction.STOPPED;
            }
        }

        if (isAtSpeed() && !DriverStation.isAutonomous() && state == NoteAction.REV_UP) {
            aux.setRumble(GenericHID.RumbleType.kLeftRumble, 1.0); // rumble rumble CSG!
        } else {
            aux.setRumble(GenericHID.RumbleType.kLeftRumble, 0.0);
        }
    }

    public void updateDashboard() {
        SmartDashboard.putBoolean("Note Detected?", isNoteDetected()); // green box if it is detected
        SmartDashboard.putBoolean("Stall Detected", isIntakeStalled());
        SmartDashboard.putBoolean("Ready to Shoot", isAtSpeed());
        SmartDashboard.putBoolean("Revving", isRevving());
        SmartDashboard.putString("NoteState", noteState);
    }

    public void setUpPid() {
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

    // limelight stuff to calculate distance with april tags - not actually
    // using...check gitHub if you want to use the code future csg coders - lila :(

    // TODO: tune pid
    // TODO: in auto rev up and drive at same time for speed
}