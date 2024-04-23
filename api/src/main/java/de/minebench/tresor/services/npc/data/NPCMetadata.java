package de.minebench.tresor.services.npc.data;

public class NPCMetadata {

    private String displayName;
    private NPCSkinData skinData;

    public NPCMetadata(String displayName, NPCSkinData skinData) {
        this.displayName = displayName;
        this.skinData = skinData;
    }

    public String getDisplayName() {
        return displayName;
    }

    public NPCSkinData getSkinData() {
        return skinData;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSkinData(NPCSkinData skinData) {
        this.skinData = skinData;
    }

}
