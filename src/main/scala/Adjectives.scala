package net.lemonmodel.patterns

import java.net.URI
import scala.xml._
import net.lemonmodel.rdfutil.RDFUtil._


/**
 * An adjective
 */
trait Adjective[V <: Adjective[_]] extends Pattern {
  protected def makeWithForm(form : Form) : V
  protected def makeWithForms(forms : Seq[Form]) : V
  protected def senseXML(namer : URINamer) : NodeSeq
  def extractForms(namespace : Namespace,  table : Map[(String,String),Any], props : List[(URI,URI)]) : Seq[Form] = {
    (for(((prop,propVal),subtable) <- table) yield {
      val propURI = namespace(prop)
      val propValURI = namespace(propVal)
      subtable match {
        case form : String => Seq(Form(form,(props :+ (propURI,propValURI)).toMap))
        case st : Map[_,_] => extractForms(namespace,st.asInstanceOf[Map[(String,String),Any]],props :+ (propURI,propValURI))
        case st : (_,_) => extractForms(namespace,Map(st).asInstanceOf[Map[(String,String),Any]],props :+ (propURI,propValURI))
        case fail => throw new IllegalArgumentException("Invalid value in a table " + fail.toString())
      }
    }).flatten.toSeq
  }
  def withTable(namespace : Namespace, table : Map[(String,String),Any]) : V = {
    val forms = extractForms(namespace,table,Nil)
    makeWithForms(forms)
  }
  def withComparative(comparativeForm : String) = makeWithForm(Form(comparativeForm,Map(lexinfo("degree")->lexinfo("comparative"))))
  def withSuperlative(superlativeForm : String) = makeWithForm(Form(superlativeForm,Map(lexinfo("degree")->lexinfo("superlative"))))
  def lemma : String
  def forms : Seq[Form]
  def toXML(namer : URINamer, lang : String) = <lemon:LexicalEntry rdf:about={namer("adjective",lemma)}>
      <lemon:canonicalForm>
        <lemon:LexicalForm rdf:about={namer("adjective",lemma,Some("canonicalForm"))}>
          <lemon:writtenRep xml:lang={lang}>{lemma}</lemon:writtenRep>
        </lemon:LexicalForm>
      </lemon:canonicalForm> 
      <lexinfo:partOfSpeech rdf:resource={lexinfo("adjective")}/>
      {
        for(form <- forms) yield {
          <lemon:otherForm>
            <lemon:LexicalForm rdf:about={namer("adjective",lemma,Some("form"))}>
              <lemon:writtenRep xml:lang={lang}>{form.writtenRep}</lemon:writtenRep>
              {
                for((prop,propVal) <- form.props) yield {
                  val (prefixUri,prefix,suffix) = prefixURI(prop)
                  <lingonto:prop rdf:resource={propVal}/>.copy(prefix=prefix, label=suffix) %
                    Attribute("","xmlns:"+prefix,prefixUri,Null)
                }
              }
            </lemon:LexicalForm>
          </lemon:otherForm>
        }   
      }
      {senseXML(namer)}
    </lemon:LexicalEntry>
}

case class IntersectiveAdjective(val lemma : String,
                                      val sense : URI = null,
                                      val forms : Seq[Form] = Nil) extends Adjective[IntersectiveAdjective] {
  protected def makeWithForm(form : Form) = IntersectiveAdjective(lemma,sense,forms :+ form)
  protected def makeWithForms(otherForms : Seq[Form]) = IntersectiveAdjective(lemma,sense, forms ++ otherForms)
  protected def senseXML(namer : URINamer) = {
  val subjURI = namer("adjective",lemma,Some("subject"))
    <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("adjective",lemma,Some("sense"))}>
         <lemon:reference>
           <owl:Class rdf:about={sense}/>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectivePredicativeFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
      </lemon:Frame>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectiveAttributiveFrame")}/>
        <lexinfo:attributiveArg rdf:resource={subjURI}/>
      </lemon:Frame>
    </lemon:synBehavior>
  }
}
                                      
case class IntersectiveObjectPropertyAdjective(val lemma : String,
                                               val property : URI,
                                               val value : URI,
                                               val forms : Seq[Form] = Nil) extends Adjective[IntersectiveObjectPropertyAdjective] {
  protected def makeWithForm(form : Form) = IntersectiveObjectPropertyAdjective(lemma,property,value,forms :+ form)
  protected def makeWithForms(otherForms : Seq[Form]) = IntersectiveObjectPropertyAdjective(lemma,property,value, forms ++ otherForms)
  protected def senseXML(namer : URINamer) = {
  val subjURI = namer("adjective",lemma,Some("subject"))
    <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("adjective",lemma,Some("sense"))}>
         <lemon:reference>
           <owl:Restriction>
             <owl:onProperty rdf:resource={property}/>
             <owl:hasValue rdf:resource={value}/>
           </owl:Restriction>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectivePredicativeFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
      </lemon:Frame>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectiveAttributiveFrame")}/>
        <lexinfo:attributiveArg rdf:resource={subjURI}/>
      </lemon:Frame>
    </lemon:synBehavior>
  }
}
                                               
case class IntersectiveDataPropertyAdjective(val lemma : String,
                                             val property : URI,
                                             val value : String,
                                             val forms : Seq[Form] = Nil) extends Adjective[IntersectiveDataPropertyAdjective] {
  protected def makeWithForm(form : Form) = IntersectiveDataPropertyAdjective(lemma,property,value,forms :+ form)
  protected def makeWithForms(otherForms : Seq[Form]) = IntersectiveDataPropertyAdjective(lemma,property,value, forms ++ otherForms)
  protected def senseXML(namer : URINamer) = {
  val subjURI = namer("adjective",lemma,Some("subject"))
    <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("adjective",lemma,Some("sense"))}>
         <lemon:reference>
           <owl:Restriction>
             <owl:onProperty rdf:resource={property}/>
             <owl:hasValue>{value}</owl:hasValue>
           </owl:Restriction>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectivePredicativeFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
      </lemon:Frame>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectiveAttributiveFrame")}/>
        <lexinfo:attributiveArg rdf:resource={subjURI}/>
      </lemon:Frame>
    </lemon:synBehavior>
  }
}
                                             
case class PropertyModifyingAdjective(val lemma : String,
                                      val property : URI,
                                      val propObjIsAttr : Boolean,
                                      val forms : Seq[Form] = Nil) extends Adjective[PropertyModifyingAdjective] {
  protected def makeWithForm(form : Form) = PropertyModifyingAdjective(lemma,property,propObjIsAttr,forms :+ form)
  protected def makeWithForms(otherForms : Seq[Form]) = PropertyModifyingAdjective(lemma,property,propObjIsAttr,forms ++ otherForms)
  protected def senseXML(namer : URINamer) = {
    val subjURI = namer("adjective",lemma,Some("subject"))
    val objURI = namer("adjective",lemma,Some("attributive"))
    <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("adjective",lemma,Some("sense"))}>
         <lemon:reference>
           <rdf:Property rdf:about={property}/>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:semArg>
         <lemon:semArg>
            <lemon:Argument rdf:about={objURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectivePropertyModifyingFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
        <lexinfo:attributeArg rdf:resource={objURI}/>
      </lemon:Frame>
    </lemon:synBehavior>
  }
}
                                      
case class RelationalAdjective(val lemma : String,
                               val property : URI = null,
                               val relationalArg : Arg,
                               val forms : Seq[Form] = Nil) extends Adjective[RelationalAdjective] {
  protected def makeWithForm(form : Form) = RelationalAdjective(lemma,property,relationalArg,forms :+ form)
  protected def makeWithForms(otherForms : Seq[Form]) = RelationalAdjective(lemma,property,relationalArg,forms ++ otherForms)
  protected def senseXML(namer : URINamer) = {
    val subjURI = namer("adjective",lemma,Some("subject"))
    val objURI = namer("adjective",lemma,Some("attributive"))
    <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("adjective",lemma,Some("sense"))}>
         <lemon:reference>
           <rdf:Property rdf:about={property}/>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:semArg>
         <lemon:semArg>
            <lemon:Argument rdf:about={objURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectivePPFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
        { relationalArg.toXML(objURI,namer) }
      </lemon:Frame>
    </lemon:synBehavior>
  }
}

                               
case class ScalarAdjective(val lemma : String,
                           val scalarMemberships : Seq[ScalarMembership] = Nil,
                           val forms : Seq[Form] = Nil) extends Adjective[ScalarAdjective] {   
  protected def makeWithForm(form : Form) = ScalarAdjective(lemma,scalarMemberships,forms :+ form)
  protected def makeWithForms(otherForms : Seq[Form]) = ScalarAdjective(lemma,scalarMemberships,forms ++ otherForms)
  protected def senseXML(namer : URINamer) = {
    val subjURI = namer("adjective",lemma,Some("subject"))
    <lemon:sense>
    {
      for(ScalarMembership(property,forClass,boundary,direction) <- scalarMemberships) yield {
      <lemon:LexicalSense rdf:about={namer("adjective",lemma,Some("sense"))}>
         <lemon:reference>
            <owl:Class>
              <rdfs:subClassOf rdf:resource={forClass}/>
              <owl:equivalentClass>
                <owl:Restriction>
                  <owl:onProperty rdf:resource={property}/>
                  <owl:someValuesForm>
                    <rdfs:Datatype>
                      <owl:withRestrictions rdf:parseType="Collection">
                        <rdf:Description>{
                          if(direction == positive) {
                            <xsd:minExclusive>{boundary}</xsd:minExclusive>
                          } else {
                            <xsd:maxExclusive>{boundary}</xsd:maxExclusive> 
                          }
                        }</rdf:Description>
                      </owl:withRestrictions>
                    </rdfs:Datatype>
                  </owl:someValuesForm>
                </owl:Restriction>
              </owl:equivalentClass>
            </owl:Class>
         </lemon:reference>
         <lemon:isA>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:isA>
       </lemon:LexicalSense>
      }
    }
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectivePredicativeFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
      </lemon:Frame>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectiveAttributiveFrame")}/>
        <lexinfo:attributiveArg rdf:resource={subjURI}/>
      </lemon:Frame>
    </lemon:synBehavior>
  }
}
/*
case class ScalarParticleAdjective(val lemma : String,
                           val scalarMemberships : Seq[ScalarMembership] = Nil,
                           val forms : Seq[Form] = Nil) extends Adjective[ScalarParticleAdjective] {
                             
  protected def makeWithForm(form : Form) = ScalarAdjective(lemma,scalarMemberships,forms :+ form)
  protected def makeWithForms(otherForms : Seq[Form]) = ScalarAdjective(lemma,scalarMemberships,forms ++ otherForms)
  protected def senseXML(namer : URINamer) = {
    val subjURI = namer("adjective",lemma,Some("subject"))
    val subjURI = namer("adjective",lemma,Some("object"))
    <lemon:sense>
    {
      for(ScalarMembership(property,boundary,direction) <- scalarMemberships) yield {
      <lemon:LexicalSense rdf:about={namer("adjective",lemma,Some("sense"))}>
         <lemon:reference>
            <rdfs:Datatype>
                <owl:withRestrictions rdf:parseType="Collection">
                <rdf:Description>{
                  if(direction == positive) {
                    <xsd:minExclusive>{boundary}</xsd:minExclusive>
                  } else {
                    <xsd:maxExclusive>{boundary}</xsd:maxExclusive>
                  }
                }
                    </rdf:Description>
                </owl:withRestrictions>
            </rdfs:Datatype>
         </lemon:reference>
         <lemon:isA>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:isA>
       </lemon:LexicalSense>
      }
    }
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectivePredicativeFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
      </lemon:Frame>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectiveAttributiveFrame")}/>
        <lexinfo:attributiveArg rdf:resource={subjURI}/>
      </lemon:Frame>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectiveComparativeFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
        <lexinfo:comparativeAdjunct rdf:resource={objURI}/>
      </lemon:Frame>
      <lemon:Frame rdf:about={namer("adjective",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("AdjectiveSuperlativeFrame")}/>
        <lexinfo:superlativeAdjunct rdf:resource={objURI}/>
      </lemon:Frame>
    </lemon:synBehavior>
  }
}*/
