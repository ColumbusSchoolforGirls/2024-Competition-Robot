// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.HashMap;

import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.NoteSystem.NoteAction;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  //our different files
  public DriveTrain driveTrain;
  public Limelight limelight;
  public NoteSystem noteSystem;
  public Climber climber;
  public static double gyroAngle;
  //set arm brake?
  
  // possible auto actions


  // dashboard key for each auto path
  private static final String kAutoDefault = "Default";
  private static final String kAutoPathMiddle = "Middle";
  private static final String kAutoPathRight = "Right";
  private static final String kAutoPathLeft = "Left";
  private static final String kAutoPathTest = "Testing Sequence";
  private String m_autoSelected;

  //the drop down menu to choose a path on the dashboard
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  int state; //state for state machine
  int i; //inside of state machine for delaying end times on SHOOT



//NOTE EVERYTHING M UST BE DUPLICATED FOR OPPOSITE ALLIANCE BECAUSE ALLIANCES ARE MIRRORED NOT EXACT REPLICAS



  //place holders for chosen path
  AutoStep[] autoActions = {};

  //start middle -- probably need to make more paths, just a template -- need to edit
  HashMap<String,AutoStep[]> autoPaths = new HashMap<>();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    limelight = new Limelight(driveTrain);
    driveTrain = new DriveTrain(limelight); // PS this rests encoders at initialization
    noteSystem = new NoteSystem(limelight); 
    climber = new Climber(noteSystem);

    //autopath options for dashboard
    autoPaths.put("Red Left drive Aim", AutoPaths.autoREDLeftDriveAim);
    autoPaths.put("BlueLeft", AutoPaths.autoBLUELeftDriveAim );//rename
    autoPaths.put("RedMiddle", AutoPaths.autoREDMiddleDriveAim); //renmae
    autoPaths.put("BlueMiddle", AutoPaths.autoBLUEMiddleDriveAim);
    autoPaths.put("RedRight", AutoPaths.autoREDRightDriveAim);
    autoPaths.put("BlueRight", AutoPaths.autoBLUERightDriveAim );


    for(String autoPathName: autoPaths.keySet()){
      m_chooser.addOption(autoPathName, autoPathName);
    }


    SmartDashboard.putData("Auto choices", m_chooser); //actually puts them on the dashboard after they are added to m_chooser
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    //updating smartdashboard values
    driveTrain.update();
    gyroAngle = driveTrain.getFacingAngle();
    
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    //set arm brake
    driveTrain.resetGyro();
    driveTrain.resetEncoders();

    //print selected auto path
    autoActions = autoPaths.get(m_chooser.getSelected());
    System.out.println("Auto selected: " + m_chooser.getSelected());
    driveTrain.setAuto(); // sets drivetrain to break mode
    //reset arm encoders
    // set state to -1 (so that it moves to 0 when it starts)
    state = -1;
    goToNextState(); // runs intital/setup code for upcoming state (in this case the first one), and moves into it

  }

  public void goToNextState() {
    System.out.println("Next State");
    //moves into next state
    state++;
    // if at the end of the path, end
    if (state >= autoActions.length) {
      return;
    }
  

  //make a stop arm function maybe

  // sets current action/distance vlaues based on whatever state we're in
  AutoStep currentAction = autoActions[state];

  // sets target angle/initial values/setup for the auto actions that need it
  // Do this later
  if (currentAction.getAction() == AutoAction.TURN) {
      driveTrain.startTurn(currentAction.getValue());
  } else if (currentAction.getAction() == AutoAction.DRIVE) { 
      driveTrain.startDrive(currentAction.getValue());
  // } /* else if (currentAction == AutoAction.ARM) {
  //   arm.startArm(currentValue);
  // } */
  }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    noteSystem.noteSystemUpdate(); //using NoteSystem state machine in auto state machine

    /* switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    } */

    SmartDashboard.putNumber("Current State", state);
    System.out.println(state);
    // stop robot and finish if at end of auto path
    if (state >= autoActions.length) {
      driveTrain.robotDrive.driveCartesian(0, 0 , 0);
      return;
    }

    //set current action to correct step of auto path
    AutoStep currentStep = autoActions[state];
    System.out.println(currentStep);

    // behavior for each possible auto action
    if (currentStep.getAction() == AutoAction.TURN) {
      driveTrain.gyroTurn();
      if (driveTrain.turnComplete()) {
        goToNextState();
      }
    } else if (currentStep.getAction() == AutoAction.DRIVE) {
      driveTrain.autoDrive();
      if (driveTrain.driveComplete()) {
        goToNextState();
      }
    } else if (currentStep.getAction() == AutoAction.SQUARE) {
      driveTrain.square();
      if (driveTrain.squareComplete()) {
        goToNextState();
      }
    } else if (currentStep.getAction() == AutoAction.INTAKE) {
      //connect to noteaction state machine
      driveTrain.robotDrive.driveCartesian(0.01, 0, 0); //drives forward slightly while intaking
      noteSystem.state = NoteAction.INTAKE;
      if (noteSystem.isNoteDetected() || noteSystem.state == NoteAction.HOLD) { //rpm is set at 3000 in Constants.INTAKE_RPM
        noteSystem.state = NoteAction.HOLD;
        goToNextState(); //I am worried that since the noteSystem intake autoaction has an isNoteDetected function as well, an error may occur
      }  
    } else if (currentStep.getAction() == AutoAction.SHOOT) {
      //connect to noteaction state machine
      noteSystem.state = NoteAction.REV_UP;
      if (noteSystem.shooterEncoder.getVelocity() > 2800 && noteSystem.intakeEncoder.getVelocity() > 2800) { //is set at 3000 in NoteSystem
        noteSystem.state = NoteAction.SHOOT;
        double startTime = Timer.getFPGATimestamp();
      if (!noteSystem.isNoteDetected() && (Timer.getFPGATimestamp() - startTime) > 2.0) { //might stop too suddenly, how to delay - added delay - LJ :)
        noteSystem.state = NoteAction.STOPPED;
        goToNextState();
      }  
      }  
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    driveTrain.resetEncoders();
    noteSystem.noteSystemTeleopInit();
    //noteSystem.noteSystemSetUpPid();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (DriveTrain.driveController.getAButton()) {
      driveTrain.square();
      //if driveTrain.squareComplete() {
        //arm.aim();
      //}
      /*if arm.aimComplete() {
        arm.shoot();
      } */
    } else {
      driveTrain.drive(0.5, 0.25, false);
      driveTrain.setTeleop(); // switches between brake and coast when you press x button
    }

    noteSystem.noteSystemUpdate();
    //noteSystem.shooterDistance();
    climber.climb();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
