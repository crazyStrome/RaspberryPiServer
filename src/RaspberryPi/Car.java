package RaspberryPi;

import com.pi4j.io.gpio.*;

public class Car {

    private GpioController gpio;
    private Wheel left;
    private Wheel right;

    private static Car car;

    public static Car getInstance() {
        if (car != null) {
            return car;
        }
        car = new Car();
        return car;
    }

    private Car() {
        gpio = GpioFactory.getInstance();
        //控制左侧的两个轮子
        left = new Wheel(gpio, RaspiPin.GPIO_22, RaspiPin.GPIO_23, RaspiPin.GPIO_01);
        //控制右侧的两个轮子，不需要在输入pwm引脚，否则会报错，因为实例化了两个同一引脚的对象
        right = new Wheel(gpio, RaspiPin.GPIO_24, RaspiPin.GPIO_25);
    }

    //车子向前
    public void forward() {
        left.forward();
        right.forward();
    }
    //车子向后
    public void backward() {
        left.backward();
        right.backward();
    }

    //车子向左，左侧向后，右侧向前滚动
    public void left() {
        left.contrast();
    }
    //同上
    public void right() {
        right.contrast();
    }

    //车子停转
    public void stop() {
        left.stop();
        right.stop();
    }

    //车子调整速度
    public void setSpeed(double rate) {
        left.setPwnRate((int)(rate * left.getRange()));
    }

    //退出所有资源
    public void destory() {
        this.gpio.shutdown();
    }
}
