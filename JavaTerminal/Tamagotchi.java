public class Tamagotchi{
    int id;
    String nom;
    int age;
    Stats statsTama;
    public Tamagotchi(int id,String nom,int age, Stats statsTama){
        if (statsTama.getIdTama() != id) {
            throw new IllegalArgumentException("L'ID du Tamagotchi et l'ID des stats ne correspondent pas.");
        }
        this.id = id;
        this.nom = nom;
        this.age = age;
        this.statsTama = statsTama;
    }
    public void manger(int valeur){
        this.statsTama.augementerfaim(valeur);
        System.out.println("Votre faim a été augmentée de "+ valeur +"\nVotre faim vaut désormais " + this.statsTama.faim);
    }
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getAge() {
        return age;
    }

    public Stats getStatsTama() {
        return statsTama;
    }
}
