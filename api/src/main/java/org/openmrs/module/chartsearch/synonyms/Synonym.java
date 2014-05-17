package org.openmrs.module.chartsearch.synonyms;

/**
 * Created by Eli on 16/05/14.
 */
public class Synonym {


    private String synonymName;
    private int synonymId;
    private SynonymGroup group;


    public Synonym(){
    }

    public Synonym(String synonymName) {
        this.synonymName = synonymName;
    }

    public String getSynonymName() {
        return synonymName;
    }

    public void setSynonymName(String synonymName) {
        this.synonymName = synonymName;
    }


    public int getSynonymId() {
        return synonymId;
    }

    public void setSynonymId(int synonymId) {
        this.synonymId = synonymId;
    }

    public SynonymGroup getGroup() {
        return group;
    }

    public void setGroup(SynonymGroup group) {
        this.group = group;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof String)) return false;

        String synonym = (String) o;

        if (!synonymName.equals(synonym)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return synonymName.hashCode();
    }

    @Override
    public String toString() {
        return synonymName;
    }
}
