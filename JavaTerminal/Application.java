public class Application {
    public static void main(String[] args){
        Stats statsTama = new Stats(50,1,80,20,50,30,90);
        Tamagotchi tama = new Tamagotchi(1,"test",18,statsTama);
        System.out.println("niveau de faim avant de manger : "+tama.statsTama.faim);
        tama.manger(10);
    } 
}