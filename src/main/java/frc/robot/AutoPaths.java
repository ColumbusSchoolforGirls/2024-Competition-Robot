package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoPaths{

//ASHLEY QUESTIONS
//make red/blue versions
//see page 45 for measurements, 24 for zones
//shoot drive turn drive intake  turn drive shoot

 public static AutoStep[] autoREDLeftDriveAim = {
    new AutoStep(AutoAction.SHOOT, 0), 
    new AutoStep(AutoAction.DRIVE, -22),
    new AutoStep(AutoAction.TURN, 100),
    new AutoStep(AutoAction.DRIVE, 20),
    new AutoStep(AutoAction.DRIVEINTAKE, 0),
    new AutoStep(AutoAction.TURN, 180),
    new AutoStep(AutoAction.DRIVE, 40),
    new AutoStep(AutoAction.SHOOT, 0),
  };

  public static AutoStep[] autoBLUELeftDriveAim = {
    new AutoStep(AutoAction.SHOOT, 0), 
    new AutoStep(AutoAction.DRIVE, 40),
    new AutoStep(AutoAction.TURN, -50),
    new AutoStep(AutoAction.DRIVE, -50),
    new AutoStep(AutoAction.DRIVEINTAKE, 30),
    new AutoStep(AutoAction.TURN, 180),
    new AutoStep(AutoAction.DRIVE, 0),
    new AutoStep(AutoAction.SHOOT, 0)
  };
public static AutoStep[] autoREDMiddleDriveAim = {
    new AutoStep(AutoAction.SHOOT, 0), 
    new AutoStep(AutoAction.DRIVE, 40),
    new AutoStep(AutoAction.TURN, -50),
    new AutoStep(AutoAction.DRIVE, -50),
    new AutoStep(AutoAction.DRIVEINTAKE, 30),
    new AutoStep(AutoAction.TURN, 180),
    new AutoStep(AutoAction.DRIVE, 0),
    new AutoStep(AutoAction.SHOOT, 0)
  };

  public static AutoStep[] autoBLUEMiddleDriveAim = {
    new AutoStep(AutoAction.SHOOT, 0), 
    new AutoStep(AutoAction.DRIVE, 40),
    new AutoStep(AutoAction.TURN, -50),
    new AutoStep(AutoAction.DRIVE, -50),
    new AutoStep(AutoAction.DRIVEINTAKE, 30),
    new AutoStep(AutoAction.TURN, 180),
    new AutoStep(AutoAction.DRIVE, 0),
    new AutoStep(AutoAction.SHOOT, 0)
  };

  public static AutoStep[] autoREDRightDriveAim = {
    new AutoStep(AutoAction.SHOOT, 0), 
    new AutoStep(AutoAction.DRIVE, 40),
    new AutoStep(AutoAction.TURN, -50),
    new AutoStep(AutoAction.DRIVE, -50),
    new AutoStep(AutoAction.DRIVEINTAKE, 30),
    new AutoStep(AutoAction.TURN, 180),
    new AutoStep(AutoAction.DRIVE, 0),
    new AutoStep(AutoAction.SHOOT, 0)
  };

  public static AutoStep[] autoBLUERightDriveAim = {
    new AutoStep(AutoAction.SHOOT, 0), 
    new AutoStep(AutoAction.DRIVE, -30),
    new AutoStep(AutoAction.TURN, 90),
    new AutoStep(AutoAction.DRIVE, 70),
    new AutoStep(AutoAction.DRIVEINTAKE, 0),
    new AutoStep(AutoAction.TURN, -100),
    new AutoStep(AutoAction.DRIVE, 10),
    new AutoStep(AutoAction.SHOOT, 0)
  };


  // public static AutoStep[] autoLeft = {
  //   new AutoStep(AutoAction.DRIVE, -50),
  //   new AutoStep(AutoAction.TURN, 90),
  //   new AutoStep(AutoAction.DRIVE, 25),
  //   new AutoStep(AutoAction.TURN, -90),
  //   new AutoStep(AutoAction.SQUARE, 0),
  //   new AutoStep(AutoAction.AIM, 100),
  //   new AutoStep(AutoAction.SHOOT, 0),
  //   new AutoStep(AutoAction.DRIVE, -30),
  //   new AutoStep(AutoAction.AIM, -100)
  // };

  // public static AutoStep[] autoTest = {
  //   //new AutoStep(AutoAction.DRIVE, 20),
  //   new AutoStep(AutoAction.DRIVE, 90),
  //   //new AutoStep(AutoAction.AIM, 100),
  //   //new AutoStep(AutoAction.SHOOT, 0),
  //   //new AutoStep(AutoAction.AIM, -100)
  //   new AutoStep(AutoAction.TURN, 90),
  // };

  //  public static AutoStep[] autoLeftShootLeave= {
  //   new AutoStep(AutoAction.TURN, 90),
  //   new AutoStep(AutoAction.SQUARE, 0),
  //   new AutoStep(AutoAction.AIM, 0),
  //   new AutoStep(AutoAction.SHOOT, 0),
  //   new AutoStep(AutoAction.DRIVE, -50)
  // };

  //  public static AutoStep[] autoLeftLeaveIntake = {
  //   new AutoStep(AutoAction.TURN, 90),
  //   new AutoStep(AutoAction.DRIVE, -50),
  
  // };

  //  public static AutoStep[] autoLeftLeave = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0),
  // };

  //  public static AutoStep[] autoLeftShootLeaveIntake = {
  //   new AutoStep(AutoAction.DRIVE, 0),
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.SQUARE, 0),
  //   new AutoStep(AutoAction.AIM, 0),
  //   new AutoStep(AutoAction.SHOOT, 0),
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0),
  // };
  
  // public static AutoStep[] autoMiddleShootLeave = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.SQUARE, 0),
  //   new AutoStep(AutoAction.AIM, 0),
  //   new AutoStep(AutoAction.SHOOT, 0),
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0),
  // };

  // public static AutoStep[] autoMiddleLeaveIntake = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0),

  // };
 
  // public static AutoStep[] autoMiddleLeave = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0)
  // };
  
  // public static AutoStep[] autoMiddleShootLeaveIntake = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.SQUARE, 0),
  //   new AutoStep(AutoAction.AIM, 0),
  //   new AutoStep(AutoAction.SHOOT, 0),
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0),
  // };

  // public static AutoStep[] autoRightShootLeave = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.SQUARE, 0),
  //   new AutoStep(AutoAction.AIM, 0),
  //   new AutoStep(AutoAction.SHOOT, 0),
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0)
  // };

  // public static AutoStep[] autoRightLeaveIntake = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0),
  // };

  // public static AutoStep[] autoRightLeave = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0)
  // };

  // public static AutoStep[] autoRightShootLeaveIntake = {
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.SQUARE, 0),
  //   new AutoStep(AutoAction.AIM, 0),
  //   new AutoStep(AutoAction.SHOOT, 0),
  //   new AutoStep(AutoAction.TURN, 0),
  //   new AutoStep(AutoAction.DRIVE, 0),

  // };
}