package net.lemonmodel.patterns.parser.Absyn; // Java Package generated by the BNF Converter.

public class ENPComplex extends NP {
  public final ListPOSTaggedWord listpostaggedword_;

  public ENPComplex(ListPOSTaggedWord p1) { listpostaggedword_ = p1; }

  public <R,A> R accept(net.lemonmodel.patterns.parser.Absyn.NP.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof net.lemonmodel.patterns.parser.Absyn.ENPComplex) {
      net.lemonmodel.patterns.parser.Absyn.ENPComplex x = (net.lemonmodel.patterns.parser.Absyn.ENPComplex)o;
      return this.listpostaggedword_.equals(x.listpostaggedword_);
    }
    return false;
  }

  public int hashCode() {
    return this.listpostaggedword_.hashCode();
  }


}