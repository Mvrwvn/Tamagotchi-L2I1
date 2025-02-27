public class Stats {
    int id;
    int idTamagotchi;
    int faim;
    int soif;
    int sante;
    int energie;
    int hygiene;
    public Stats(int id, int idTamagotchi, int faim, int soif, int sante, int energie, int hygiene){
        this.id = id;
        this.idTamagotchi = idTamagotchi;
        this.faim = faim;
        this.soif = soif;
        this.sante = sante;
        this.energie = energie;
        this.hygiene = hygiene;
    }
    public void augementerfaim(int valeur){
        this.faim += valeur;
    }

    public int getIdStats() {
        return id;
    }

    public int getIdTama() {
        return idTamagotchi;
    }

    public int getFaim() {
        return faim;
    }

    public int getEnergie() {
        return energie;
    }
}
