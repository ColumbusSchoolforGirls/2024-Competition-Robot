package frc.robot;

public class AutoPaths {

  // see page 45 for measurements, 24 for zones

  public static AutoStep[] autoLeftMain = {
      new AutoStep(AutoAction.SHOOT, 0),
      new AutoStep(AutoAction.DRIVE, -11),
      new AutoStep(AutoAction.TURN, -122), // was -105
      new AutoStep(AutoAction.DRIVEINTAKE, 88), // was 90
      new AutoStep(AutoAction.DRIVE, -65),
      new AutoStep(AutoAction.TURN, 122),
      new AutoStep(AutoAction.DRIVEREVUP, 12), 
      new AutoStep(AutoAction.SHOOT, 0)
  };
  
  public static AutoStep[] autoLeftShootONLY = {
    new AutoStep(AutoAction.SHOOT, 0),
};

  public static AutoStep[] autoLeftDrive = {
      new AutoStep(AutoAction.DRIVE, -35),
      new AutoStep(AutoAction.TURN, 56),
      new AutoStep(AutoAction.DRIVE, -80)
  };

  public static AutoStep[] autoLeftShoot = {
    new AutoStep(AutoAction.SHOOT, 0),
    new AutoStep(AutoAction.DRIVE, -72),
    //new AutoStep(AutoAction.TURN, -58),
    //new AutoStep(AutoAction.DRIVE, -130)
};

  public static AutoStep[] autoMiddleMain = {
      new AutoStep(AutoAction.SHOOT, 0),
      new AutoStep(AutoAction.DRIVE, -27), // ?
      new AutoStep(AutoAction.TURN, -180),
      new AutoStep(AutoAction.DRIVEINTAKE, 32),
      new AutoStep(AutoAction.TURN, 180),
      new AutoStep(AutoAction.DRIVEREVUP, 59),
      new AutoStep(AutoAction.SHOOT, 0)
  };

  public static AutoStep[] autoMiddleDrive = { // figure out later
      new AutoStep(AutoAction.DRIVE, -75), // -65
  };

  public static AutoStep[] autoRightMain = {
      new AutoStep(AutoAction.SHOOT, 0),
      new AutoStep(AutoAction.DRIVE, -11),
      new AutoStep(AutoAction.TURN, 125), // was -105
      new AutoStep(AutoAction.DRIVEINTAKE, 88), // was 90
      new AutoStep(AutoAction.DRIVE, -65),
      new AutoStep(AutoAction.TURN, -125),
      new AutoStep(AutoAction.DRIVEREVUP, 25),
      new AutoStep(AutoAction.SHOOT, 0)
  };

  public static AutoStep[] autoRightDrive = {
      new AutoStep(AutoAction.DRIVE, -35),
      new AutoStep(AutoAction.TURN, -56),
      new AutoStep(AutoAction.DRIVE, -80)
  };

}