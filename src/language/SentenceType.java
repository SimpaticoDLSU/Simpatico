package language;

public enum SentenceType {
	RELATIVE_CLAUSE,
    COMPOUND;


    

    @Override
    public String toString() {
        switch (this) {
            case RELATIVE_CLAUSE:
                return "compound_complex";
            case COMPOUND:
                return "compound";
        }
        return super.toString();
    }

   
}
