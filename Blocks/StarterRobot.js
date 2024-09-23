// IDENTIFIERS_USED=armMotorAsDcMotor,gamepad1,intakeAsCRServo,leftMotorAsDcMotor,rightMotorAsDcMotor

var y, x, leftPower, rightPower, max;

/**
 * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
 * Comment Blocks show where to place Initialization code (runs once, after touching the
 * DS INIT button, and before touching the DS
 * Start arrow), Run code (runs once, after
 * touching Start), and Loop code (runs repeatedly
 * while the OpMode is active, namely not
 * Stopped).
 */
function runOpMode() {
  linearOpMode.waitForStart();
  leftMotorAsDcMotor.setDirection("REVERSE");
  leftMotorAsDcMotor.setZeroPowerBehavior("BRAKE");
  rightMotorAsDcMotor.setDirection("FORWARD");
  rightMotorAsDcMotor.setZeroPowerBehavior("BRAKE");
  armMotorAsDcMotor.setDirection("FORWARD");
  armMotorAsDcMotor.setZeroPowerBehavior("BRAKE");
  if (linearOpMode.opModeIsActive()) {
    while (linearOpMode.opModeIsActive()) {
      y = -gamepad1.getLeftStickY();
      x = gamepad1.getLeftStickX();
      leftPower = y + x;
      rightPower = y - x;
      max = Math.max.apply(null, [Math.abs(leftPower), Math.abs(rightPower)]);
      if (max >= 1) {
        leftPower = leftPower / max;
        rightPower = rightPower / max;
      }
      leftMotorAsDcMotor.setDualPower(leftPower, rightMotorAsDcMotor, rightPower);
      if (gamepad1.getA()) {
        armMotorAsDcMotor.setPower(0.5);
      } else if (gamepad1.getB()) {
        armMotorAsDcMotor.setPower(-0.5);
      } else {
        armMotorAsDcMotor.setPower(0);
      }
      if (gamepad1.getSquare()) {
        intakeAsCRServo.setPower(0.2);
      } else if (gamepad1.getTriangle()) {
        intakeAsCRServo.setPower(-0.2);
      } else {
        intakeAsCRServo.setPower(0);
      }
    }
  }
}
