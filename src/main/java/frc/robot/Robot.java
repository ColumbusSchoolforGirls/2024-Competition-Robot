// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
  public Arm arm;
  
  // possible auto actions
  enum AutoAction {
    DRIVE, ARM, SHOOT, TURN, SQUARE_AND_SHOOT
  };

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

  //place holders for chosen path
  AutoAction[] autoActions = {};
  int[] autoValues = {};

  //default
  AutoAction[] autoDefault= {};
  int[] defaultValues = {};

  //start middle -- probably need to make more paths, just a template -- need to edit
  AutoAction[] autoMiddle = {AutoAction.DRIVE, AutoAction.SQUARE_AND_SHOOT, AutoAction.DRIVE, AutoAction.ARM};
  int[] middleValues = {-50, 0, -30, -100}; //need to change these values

  //start right -- need to edit
  AutoAction[] autoRight = {AutoAction.DRIVE, AutoAction.TURN, AutoAction.DRIVE, AutoAction.TURN, AutoAction.SQUARE_AND_SHOOT, AutoAction.DRIVE, AutoAction.ARM};
  int[] rightValues = { -50, -90, 25, 90, 0, -30, -100}; //need to change these values

  //start left -- need to edit
  AutoAction[] autoLeft = {AutoAction.DRIVE, AutoAction.TURN, AutoAction.DRIVE, AutoAction.TURN, AutoAction.SQUARE_AND_SHOOT, AutoAction.DRIVE, AutoAction.ARM};
  int[] leftValues = { -50, 90, 25, -90, 0, -30, -100}; //need to change these values

  //tesitng sequence -- need to edit
  AutoAction[] autoTest= {AutoAction.DRIVE, AutoAction.DRIVE, AutoAction.ARM,AutoAction.ARM};
  int[] testValues = { 20, -20, 100, -100}; //need to change these values

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    driveTrain = new DriveTrain(); // PS this rests encoders at initialization
    limelight = new Limelight(driveTrain);
    arm = new Arm(); 

    //autopath options for dashboard
    m_chooser.setDefaultOption(kAutoDefault, kAutoDefault);
    m_chooser.addOption(kAutoPathMiddle, kAutoPathMiddle);
    m_chooser.addOption(kAutoPathRight, kAutoPathRight);
    m_chooser.addOption(kAutoPathLeft, kAutoPathLeft);
    m_chooser.addOption(kAutoPathTest, kAutoPathTest);

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
    //will need to add gyro
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
    //reset gyro
    driveTrain.resetEncoders();

    //assigns selected auto path into placeholder variables
    if (m_chooser.getSelected() == kAutoPathMiddle) {
      autoActions = autoMiddle;
      autoValues = middleValues;
    } else if (m_chooser.getSelected() == kAutoPathRight) {
      autoActions = autoRight;
      autoValues = rightValues;
    } else if (m_chooser.getSelected() == kAutoPathLeft) {
      autoActions = autoLeft;
      autoValues = leftValues;
    } else if (m_chooser.getSelected() == kAutoPathTest) {
      autoActions = autoTest;
      autoValues = testValues;
    } else { //default
      autoActions = autoDefault;
      autoValues = defaultValues;
    }

    //print selected auto path
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_chooser.getSelected());
    driveTrain.setAuto(); // sets drivetrain to break mode
    //reset arm encoders
    // set state to -1 (so that it moves to 0 when it starts)
    state = -1;
    goToNextState(); // runs intital/setup code for upcoming state (in this case the first one), and moves into it

  }

  public void goToNextState() {
    //moves into next state
    state++;
    // if at the end of the path, end
    if (state >= autoActions.length) {
      return;
    }
  

  //make a stop arm function maybe

  // sets current action/distance vlaues based on whatever state we're in
  AutoAction currentAction = autoActions[state];
  int currentValue = autoValues[state];

  // sets target angle/initial values/setup for the auto actions that need it
  /* if (currentAction == AutoAction.TURN) {
    driveTrain.startTurn(currentValue);
  } else if (currentAction == AutoAction.DRIVE) { 
    driveTrain.startDrive(currentValue);
  } else if (currentAction == AutoAction.ARM) {
    arm.startArm(currentValue);
  } 
  */
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    /*switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    } */

    SmartDashboard.putNumber("Current State", state);

    // stop robot and finish if at end of auto path
    if (state >= autoActions.length) {
      driveTrain.robotDrive.driveCartesian(0, 0 , 0);
      return;
    }

    //set current action to correct step of auto path
    AutoAction currentAction = autoActions[state];

    // behavior for each possible auto action
    /*if (currentAction == AutoAction.TURN) {
      driveTrain.gyroTurn()
    } */
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

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
