package mx.com.vialogika.mistclient.Utils;

public class APDetailsDataHolder {
    private String percent;
    private int guardsRequired;
    private int guardsCovered;
    private String apostamientoName;

    public APDetailsDataHolder() {
    }

    public APDetailsDataHolder(String apostamientoName,int guardsRequired, int guardsCovered) {
        this.guardsRequired = guardsRequired;
        this.guardsCovered = guardsCovered;
        this.apostamientoName = apostamientoName;
        calculatePercent();
    }

    private void calculatePercent(){
        int percent = (this.guardsCovered * 100) / this.guardsRequired;
        this.percent = String.valueOf(percent) + " %";
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public int getGuardsRequired() {
        return guardsRequired;
    }

    public void setGuardsRequired(int guardsRequired) {
        this.guardsRequired = guardsRequired;
    }

    public int getGuardsCovered() {
        return guardsCovered;
    }

    public void setGuardsCovered(int guardsCovered) {
        this.guardsCovered = guardsCovered;
    }

    public String getApostamientoName() {
        return apostamientoName;
    }

    public void setApostamientoName(String apostamientoName) {
        this.apostamientoName = apostamientoName;
    }
}
