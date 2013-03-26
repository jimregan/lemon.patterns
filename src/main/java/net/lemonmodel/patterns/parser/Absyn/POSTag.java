package net.lemonmodel.patterns.parser.Absyn; // Java Package generated by the BNF Converter.

public abstract class POSTag implements java.io.Serializable {
  public abstract <R,A> R accept(POSTag.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(net.lemonmodel.patterns.parser.Absyn.EAdjectivePOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EAdpositionPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EAdverbPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EArticlePOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EBulletPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.ECircumpositionPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EColonPOSPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.ECommaPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EConjunctionPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.ECopulaPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EDeterminerPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EInterjectionPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.ENounPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.ENumeralPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EParticlePOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EPointPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EPostpositionPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EPrepositionPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EPronounPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EPunctuationPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.ESemiColonPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.ESlashPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EVerbPOS p, A arg);
    public R visit(net.lemonmodel.patterns.parser.Absyn.EAnyPOS p, A arg);

  }

}