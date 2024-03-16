package frc.robot;

public class AutoPaths {

  // see page 45 for measurements, 24 for zones

  public static AutoStep[] autoLeftMain = {
      new AutoStep(AutoAction.SHOOT, 0),
      new AutoStep(AutoAction.DRIVE, -11),
      new AutoStep(AutoAction.TURN, -124), // was -105
      new AutoStep(AutoAction.DRIVEINTAKE, 90),
      new AutoStep(AutoAction.DRIVE, -65),
      new AutoStep(AutoAction.TURN, 124),
      new AutoStep(AutoAction.DRIVE, 9),
      new AutoStep(AutoAction.SHOOT, 0)
  };

  public static AutoStep[] autoLeftDrive = {
      new AutoStep(AutoAction.DRIVE, -35),
      new AutoStep(AutoAction.TURN, 56),
      new AutoStep(AutoAction.DRIVE, -80)
  };

  public static AutoStep[] autoMiddleMain = {
      new AutoStep(AutoAction.SHOOT, 0),
      new AutoStep(AutoAction.DRIVE, -29), // ?
      new AutoStep(AutoAction.TURN, -180),
      new AutoStep(AutoAction.DRIVEINTAKE, 30), // need to change w testing
      new AutoStep(AutoAction.TURN, 182),
      new AutoStep(AutoAction.DRIVE, 56), // changed match 22 from 80 to 75
      new AutoStep(AutoAction.SHOOT, 0)
  };

  public static AutoStep[] autoMiddleDrive = { // figure out later
      new AutoStep(AutoAction.DRIVE, -75), // -65
  };

  public static AutoStep[] autoRightMain = {
      new AutoStep(AutoAction.SHOOT, 0),
      new AutoStep(AutoAction.DRIVE, -11),
      new AutoStep(AutoAction.TURN, 124), // was -105
      new AutoStep(AutoAction.DRIVEINTAKE, 90),
      new AutoStep(AutoAction.DRIVE, -65),
      new AutoStep(AutoAction.TURN, -124),
      new AutoStep(AutoAction.DRIVE, 9),
      new AutoStep(AutoAction.SHOOT, 0)
  };

  public static AutoStep[] autoRightDrive = {
      new AutoStep(AutoAction.DRIVE, -35),
      new AutoStep(AutoAction.TURN, -56),
      new AutoStep(AutoAction.DRIVE, -80)
  };

}