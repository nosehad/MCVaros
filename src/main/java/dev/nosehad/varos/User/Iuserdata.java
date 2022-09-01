package dev.nosehad.varos.User;
interface Iuserdata {

    void setProtection (boolean Protection);
    boolean hasProtection();
    void setCooldown (boolean value);
    boolean hasCooldown ();
    void setAdditionalString(String AdditionalString);
    String getAdditionalString();
    int getAdditionalInt ();
    void setAdditionalInt ( int additionalInt );
}
