package frc.robot;

public final class Constants {
    
    //drive train
    public static final double WHEEL_CIRCUMFERENCE = 8.0 * Math.PI; //diameter of our wheels
    public static final double DRIVE_CONTROLLER_DEADZONE = 0.1; //dead zone
    public static final int FRONT_LEFT_ID = 3;
    public static final int BACK_LEFT_ID = 2;
    public static final int FRONT_RIGHT_ID = 5;
    public static final int BACK_RIGHT_ID = 4;
    public static final double GEAR_RATIO = 12.75;
    public static final int SLOWING_DISTANCE = 10;

    // tolerance (deadzone) for auto
    public static final double TURN_TOLERANCE = 2.0; 
    public static final double DISTANCE_TOLERANCE = 0.075;
    public static final double SQUARE_TOLERANCE = 1.5;

    //arm stuff
    public static final double AUX_DEADZONE = 0.1;
    
}

