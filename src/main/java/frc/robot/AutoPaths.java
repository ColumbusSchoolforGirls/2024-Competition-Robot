package frc.robot;

public class AutoPaths{

//ASHLEY QUESTIONS
//make red/blue versions
//see page 45 for measurements, 24 for zones
//shoot drive turn drive intake  turn drive shoot

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

// public static AutoStep[] autoBLUELeftMain = {
//   new AutoStep(AutoAction.SHOOT, 0),
//   new AutoStep(AutoAction.DRIVE, -10),
//   new AutoStep(AutoAction.TURN, -110),
//   new AutoStep(AutoAction.DRIVE, 100), //was 70
//   new AutoStep(AutoAction.DRIVEINTAKE, 30),
//   new AutoStep(AutoAction.TURN, 150),
//   //new AutoStep(AutoAction.DRIVE, 0),
//   //new AutoStep(AutoAction.SHOOT, 0)
// };

//   public static AutoStep[] autoBLUELeftDrive = {
//   new AutoStep(AutoAction.DRIVE, -8),
//   new AutoStep(AutoAction.TURN, -124),
//   new AutoStep(AutoAction.DRIVE, 48)//need to change.Runs over speaker!
// };

public static AutoStep[] autoMiddleMain = {
  new AutoStep(AutoAction.SHOOT, 0),
  new AutoStep(AutoAction.DRIVE, -29), //?
  new AutoStep(AutoAction.TURN, -180),
  new AutoStep(AutoAction.DRIVEINTAKE, 30), //need to change w testing 
  new AutoStep(AutoAction.TURN, 182),
  new AutoStep(AutoAction.DRIVE, 56), //changed match 22 from 80 to 75
  new AutoStep(AutoAction.SHOOT, 0)
};

  public static AutoStep[] autoMiddleDrive = { // figure out later
  new AutoStep(AutoAction.DRIVE, -75), //-65
};

// public static AutoStep[] autoBLUEMiddleMain = {
//   new AutoStep(AutoAction.SHOOT, 0),
//   new AutoStep(AutoAction.DRIVE, -27), //30
//   new AutoStep(AutoAction.TURN, -180), //165
//   new AutoStep(AutoAction.DRIVEINTAKE, 30), //need to change with testing
//   new AutoStep(AutoAction.TURN, 180), //170
//   new AutoStep(AutoAction.DRIVE, 66), //lower?
//   new AutoStep(AutoAction.SHOOT, 0)
// };

// public static AutoStep[] autoBLUEMiddleDrive = {
//   new AutoStep(AutoAction.DRIVE, -40),
// };

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


// public static AutoStep[] autoBLUERightMain = {
//   new AutoStep(AutoAction.SHOOT, 0),
//   new AutoStep(AutoAction.DRIVE, -10),
//   new AutoStep(AutoAction.TURN, 120), //was 90
//   new AutoStep(AutoAction.DRIVE, 100),
//   //new AutoStep(AutoAction.DRIVEINTAKE, 0),
//   //new AutoStep(AutoAction.TURN, -150),
//   //jnew AutoStep(AutoAction.SHOOT, 0)
// };

//   public static AutoStep[] autoBLUERightDrive = {
//   new AutoStep(AutoAction.DRIVE, -30),
//   new AutoStep(AutoAction.TURN, 130),
//   new AutoStep(AutoAction.DRIVE, 150)
// };

//old paths

//  public static AutoStep[] autoREDLeftDriveAim = {
//     new AutoStep(AutoAction.SHOOT, 0), 
//     new AutoStep(AutoAction.DRIVE, -10),
//     new AutoStep(AutoAction.TURN, -105),
//     new AutoStep(AutoAction.DRIVE, 80),
//     new AutoStep(AutoAction.DRIVEINTAKE, 30), //need to change
//     new AutoStep(AutoAction.TURN, 152),
//     new AutoStep(AutoAction.DRIVE, 40), //need to change
//     new AutoStep(AutoAction.SHOOT, 0),
//   };

//   public static AutoStep[] autoBLUELeftDriveAim = {
//     new AutoStep(AutoAction.SHOOT, 0), 
//     new AutoStep(AutoAction.DRIVE, 40),
//     new AutoStep(AutoAction.TURN, -50),
//     new AutoStep(AutoAction.DRIVE, -50),
//     new AutoStep(AutoAction.DRIVEINTAKE, 30),
//     new AutoStep(AutoAction.TURN, 180),
//     new AutoStep(AutoAction.DRIVE, 0),
//     new AutoStep(AutoAction.SHOOT, 0)
//   };
// public static AutoStep[] autoREDMiddleDriveAim = {
//     new AutoStep(AutoAction.SHOOT, 0), 
//     new AutoStep(AutoAction.DRIVE, 40),
//     new AutoStep(AutoAction.TURN, -50),
//     new AutoStep(AutoAction.DRIVE, -50),
//     new AutoStep(AutoAction.DRIVEINTAKE, 30),
//     new AutoStep(AutoAction.TURN, 180),
//     new AutoStep(AutoAction.DRIVE, 0),
//     new AutoStep(AutoAction.SHOOT, 0)
//   };

//   public static AutoStep[] autoBLUEMiddleDriveAim = {
//     new AutoStep(AutoAction.SHOOT, 0), 
//     new AutoStep(AutoAction.DRIVE, 40),
//     new AutoStep(AutoAction.TURN, -50),
//     new AutoStep(AutoAction.DRIVE, -50),
//     new AutoStep(AutoAction.DRIVEINTAKE, 30),
//     new AutoStep(AutoAction.TURN, 180),
//     new AutoStep(AutoAction.DRIVE, 0),
//     new AutoStep(AutoAction.SHOOT, 0)
//   };

//   public static AutoStep[] autoREDRightDriveAim = {
//     new AutoStep(AutoAction.SHOOT, 0), 
//     new AutoStep(AutoAction.DRIVE, 40),
//     new AutoStep(AutoAction.TURN, -50),
//     new AutoStep(AutoAction.DRIVE, -50),
//     new AutoStep(AutoAction.DRIVEINTAKE, 30),
//     new AutoStep(AutoAction.TURN, 180),
//     new AutoStep(AutoAction.DRIVE, 0),
//     new AutoStep(AutoAction.SHOOT, 0)
//   };

//   public static AutoStep[] autoBLUERightDriveAim = {
//     new AutoStep(AutoAction.SHOOT, 0), 
//     new AutoStep(AutoAction.DRIVE, -30),
//     new AutoStep(AutoAction.TURN, 90),
//     new AutoStep(AutoAction.DRIVE, 70),
//     new AutoStep(AutoAction.DRIVEINTAKE, 0),
//     new AutoStep(AutoAction.TURN, -100),
//     new AutoStep(AutoAction.DRIVE, 10),
//     new AutoStep(AutoAction.SHOOT, 0)
//   };


//   public static AutoStep[] autoLeft = {
//     new AutoStep(AutoAction.DRIVE, -50),
//     new AutoStep(AutoAction.TURN, 90),
//     new AutoStep(AutoAction.DRIVE, 25),
//     new AutoStep(AutoAction.TURN, -90),
//     new AutoStep(AutoAction.SQUARE, 0),
//     new AutoStep(AutoAction.AIM, 100),
//     new AutoStep(AutoAction.SHOOT, 0),
//     new AutoStep(AutoAction.DRIVE, -30),
//     new AutoStep(AutoAction.AIM, -100)
//   };

//   public static AutoStep[] autoTest = {
//     //new AutoStep(AutoAction.DRIVE, 20),
//     new AutoStep(AutoAction.DRIVE, 90),
//     //new AutoStep(AutoAction.AIM, 100),
//     //new AutoStep(AutoAction.SHOOT, 0),
//     //new AutoStep(AutoAction.AIM, -100)
//     new AutoStep(AutoAction.TURN, 90),
//   };

//    public static AutoStep[] autoLeftShootLeave= {
//     new AutoStep(AutoAction.TURN, 90),
//     new AutoStep(AutoAction.SQUARE, 0),
//     new AutoStep(AutoAction.AIM, 0),
//     new AutoStep(AutoAction.SHOOT, 0),
//     new AutoStep(AutoAction.DRIVE, -50)
//   };

//    public static AutoStep[] autoLeftLeaveIntake = {
//     new AutoStep(AutoAction.TURN, 90),
//     new AutoStep(AutoAction.DRIVE, -50),
  
//   };

//    public static AutoStep[] autoLeftLeave = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0),
//   };

//    public static AutoStep[] autoLeftShootLeaveIntake = {
//     new AutoStep(AutoAction.DRIVE, 0),
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.SQUARE, 0),
//     new AutoStep(AutoAction.AIM, 0),
//     new AutoStep(AutoAction.SHOOT, 0),
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0),
//   };
  
//   public static AutoStep[] autoMiddleShootLeave = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.SQUARE, 0),
//     new AutoStep(AutoAction.AIM, 0),
//     new AutoStep(AutoAction.SHOOT, 0),
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0),
//   };

//   public static AutoStep[] autoMiddleLeaveIntake = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0),

//   };
 
//   public static AutoStep[] autoMiddleLeave = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0)
//   };
  
//   public static AutoStep[] autoMiddleShootLeaveIntake = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.SQUARE, 0),
//     new AutoStep(AutoAction.AIM, 0),
//     new AutoStep(AutoAction.SHOOT, 0),
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0),
//   };

//   public static AutoStep[] autoRightShootLeave = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.SQUARE, 0),
//     new AutoStep(AutoAction.AIM, 0),
//     new AutoStep(AutoAction.SHOOT, 0),
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0)
//   };

//   public static AutoStep[] autoRightLeaveIntake = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0),
//   };

//   public static AutoStep[] autoRightLeave = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0)
//   };

//   public static AutoStep[] autoRightShootLeaveIntake = {
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.SQUARE, 0),
//     new AutoStep(AutoAction.AIM, 0),
//     new AutoStep(AutoAction.SHOOT, 0),
//     new AutoStep(AutoAction.TURN, 0),
//     new AutoStep(AutoAction.DRIVE, 0),

//   };
}