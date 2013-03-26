package net.lemonmodel.patterns.parser.Absyn; // Java Package generated by the BNF Converter.

public class EDunnotelicEventVerb extends VerbPattern {
  public final VerbPattern verbpattern_;

  public EDunnotelicEventVerb(VerbPattern p1) { verbpattern_ = p1; }

  public <R,A> R accept(net.lemonmodel.patterns.parser.Absyn.VerbPattern.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof net.lemonmodel.patterns.parser.Absyn.EDunnotelicEventVerb) {
      net.lemonmodel.patterns.parser.Absyn.EDunnotelicEventVerb x = (net.lemonmodel.patterns.parser.Absyn.EDunnotelicEventVerb)o;
      return this.verbpattern_.equals(x.verbpattern_);
    }
    return false;
  }

  public int hashCode() {
    return this.verbpattern_.hashCode();
  }


}