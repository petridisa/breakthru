public class Counter {
    int minutes;
    int seconds;
    public Counter(int minutes){
        this.minutes = minutes;
        this.seconds = 0;
    }

    public Counter(int minutes, int seconds){
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void decrease(){
        this.seconds--;
        if(seconds<0){
            seconds+=60;
            minutes--;
        }
    }

    public void increase(){
        this.seconds++;
        if(seconds>=60){
            seconds-=60;
            minutes++;
        }
    }

    public boolean isZero(){
        if(minutes == 0 && seconds == 0 ){
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        String time = "";
        if(minutes<10){
            time+="0";
        }
        time+=minutes;
        time+="  :  ";
        if(seconds<10){
            time+="0";
        }
        time+=seconds;
        return time;
    }
}
