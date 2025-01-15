package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SwerveConstants;

public class SwerveDriveSubsystem extends SubsystemBase{
    private SwerveDriveOdometry odometry;
  //private AHRS navx;
  private Pigeon2 pigeon2;
  private SwerveModule[] swerveModules;

  public SwerveDriveSubsystem() {
    swerveModules = new SwerveModule[]{
      new SwerveModule(0, SwerveConstants.FrontLeft.constants),
      new SwerveModule(1, SwerveConstants.BackLeft.constants),
      new SwerveModule(2, SwerveConstants.FrontRight.constants),
      new SwerveModule(3, SwerveConstants.BackRight.constants)
    };


    pigeon2 = new Pigeon2(13);
    pigeon2.setYaw(0);

    odometry = new SwerveDriveOdometry(
      SwerveConstants.kKinematics, 
      pigeon2.getRotation2d(),  
      getCurrentSwerveModulePositions());
  }


  public void resetPigeon2(){
    pigeon2.setYaw(0);
  }

  public Rotation2d getRotation2d(){
    return pigeon2.getRotation2d();
  }

  public Pose2d getPose(){
    return odometry.getPoseMeters();
  }

  public void setPose(Pose2d pose){
    odometry.resetPosition(getRotation2d(), getCurrentSwerveModulePositions(), pose);
  }

  public void resetOdometry(Pose2d pose){
    odometry.resetPosition(getRotation2d(), getCurrentSwerveModulePositions(), pose);
  }

  public SwerveModulePosition[] getCurrentSwerveModulePositions(){
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for(SwerveModule swerveModule : swerveModules){
      positions[swerveModule.moduleID] = swerveModule.getPosition();
    }
    return positions;
  }

  public void setModuleStates(SwerveModuleState[] desiredStates){
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConstants.kMaxSpeed);

    for(SwerveModule swerveModule : swerveModules){
      swerveModule.setState(desiredStates[swerveModule.moduleID]);
    }
  }

  public void stopModules(){
    for(SwerveModule swerveModule : swerveModules){
      swerveModule.stop();
    }
  }

 
  public void periodic() {
    odometry.update(getRotation2d().unaryMinus(), getCurrentSwerveModulePositions());
    for(SwerveModule swerveModule : swerveModules){
      swerveModule.print();
    }
    SmartDashboard.putNumber("Pigeon 2.0 Yaw", pigeon2.getRotation2d().getDegrees());
  }
}
