package language;

public enum SentenceType {
	RELATIVE_CLAUSE,
    COMPOUND,
    COMPOUND_COMPLEX,
    PASSIVE_ACTIVE;

    

    @Override
    public String toString() {
        switch (this) {
            case RELATIVE_CLAUSE:
                return "compound_complex";
            case COMPOUND:
                return "compound";
            case COMPOUND_COMPLEX:
                return "relative_clause";
            case PASSIVE_ACTIVE:
                return "passive_active";

        }
        return super.toString();
    }

   
}
