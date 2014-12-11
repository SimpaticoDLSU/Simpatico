package language;

import edu.stanford.nlp.trees.TypedDependency;

public class DependentPairs {

	private String subject;
	private String pointsTo;
	private String relation;
	private TypedDependency td;
	
	/**
	 * Assigns a pair of Strings based on their typed dependency.
	 * Cleaned version.
	 * @param subject
	 * @param pointsTo
	 */
	public DependentPairs(String subject, String pointsTo)
	{
		this.subject	= subject;
		this.pointsTo 	= pointsTo;
	}
	
	public DependentPairs(String subject, String pointsTo, String relation, TypedDependency td)
	{
		this.subject 	= subject;
		this.pointsTo 	= pointsTo;
		this.relation 	= relation;
		this.td 		= td;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPointsTo() {
		return pointsTo;
	}
	public void setPointsTo(String pointsTo) {
		this.pointsTo = pointsTo;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public TypedDependency getTd() {
		return td;
	}

	public void setTd(TypedDependency td) {
		this.td = td;
	}
	
	
	
}
