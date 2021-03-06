package net.lemonmodel.patterns

import java.net.URI
import scala.xml._
import net.lemonmodel.rdfutil.RDFUtil._
       
/**
 * A noun
 */
trait Noun[N <: Noun[_]] extends Pattern {
  protected def makeWithForm(form : Form) : N
  protected def senseXML(namer : URINamer) : NodeSeq
  protected def isProper = false
  def withPlural(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("plural"))))
  def withAccusative(form : String) = makeWithForm(Form(form,Map(lexinfo("case")->lexinfo("accusativeCase"))))
  def withDative(form : String) = makeWithForm(Form(form,Map(lexinfo("case")->lexinfo("dativeCase"))))
  def withGenetive(form : String) = makeWithForm(Form(form,Map(lexinfo("case")->lexinfo("genetiveCase"))))
  def withNominativeSingular(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("singular"),lexinfo("case")->lexinfo("nominativeCase"))))
  def withAccusativeSingular(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("singular"),lexinfo("case")->lexinfo("accusativeCase"))))
  def withGenetiveSingular(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("singular"),lexinfo("case")->lexinfo("genetiveCase"))))
  def withDativeSingular(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("singular"),lexinfo("case")->lexinfo("dativeCase"))))
  def withNominativePlural(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("plural"),lexinfo("case")->lexinfo("nominativeCase"))))
  def withAccusativePlural(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("plural"),lexinfo("case")->lexinfo("accusativeCase"))))
  def withGenetivePlural(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("plural"),lexinfo("case")->lexinfo("genetiveCase"))))
  def withDativePlural(form : String) = makeWithForm(Form(form,Map(lexinfo("number")->lexinfo("plural"),lexinfo("case")->lexinfo("dativeCase"))))
  def lemma : String
  def forms : Seq[Form]
  def toXML(namer : URINamer, lang : String) = <lemon:LexicalEntry rdf:about={namer("noun",lemma)}>
      <lemon:canonicalForm>
        <lemon:LexicalForm rdf:about={namer("noun",lemma,Some("canonicalForm"))}>
          <lemon:writtenRep xml:lang={lang}>{lemma}</lemon:writtenRep>
        </lemon:LexicalForm>
      </lemon:canonicalForm>
      { if(isProper) {
          <lexinfo:partOfSpeech rdf:resource={lexinfo("properNoun")}/>
        } else {
          <lexinfo:partOfSpeech rdf:resource={lexinfo("commonNoun")}/>
        }
      }
      {
        for(form <- forms) yield {
          <lemon:otherForm>
            <lemon:LexicalForm rdf:about={namer("noun",lemma,Some("form"))}>
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

/**
* A name (proper noun) associated to a named individual in the ontology
* @param lemma The canonical form of the name (required)
* @param sense The URI of the associated named individual
* @param forms The set of other forms
*/
case class Name(val lemma : String, 
                val sense : URI = null, 
                val forms : Seq[Form] = Nil) extends Noun[Name] {
  protected def makeWithForm(form : Form) = Name(lemma,sense, forms :+ form)
  protected def senseXML(namer : URINamer) = <lemon:sense>
       <lemon:LexicalSense rdf:about={namer("noun",lemma,Some("sense"))}>
         <lemon:reference>
           <owl:NamedIndividual rdf:about={sense}/>
         </lemon:reference>
       </lemon:LexicalSense>
    </lemon:sense>
   protected override def isProper = true
}

/**
* A noun representing a genus of object associated with an ontology class
* @param form The canonical form of the noun (required)
* @param sense The URI to be associated with
* @param forms The set of other forms
*/
case class ClassNoun(val lemma : String, 
                     val sense : URI = null, 
                     val forms : Seq[Form] = Nil) extends Noun[ClassNoun] {
  def makeWithForm(form : Form) = ClassNoun(lemma,sense,forms :+ form)
  def senseXML(namer : URINamer) = {
  val subjURI = namer("noun",lemma,Some("subject"))
    <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("noun",lemma,Some("sense"))}>
         <lemon:reference>
           <owl:Class rdf:about={sense}/>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("noun",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("NounPredicativeFrame")}/>
        <lexinfo:subject rdf:resource={subjURI}/>
      </lemon:Frame>
    </lemon:synBehavior>
  }
}

/**
 * A noun representing a bivalent relationship associated with an object property
 * in the ontology
 * @param form The canonical form of the noun (required)
 * @param sense The URI to be associated with
 * @param propSubj Indicates the argument that fills the subject (domain) of the object property
 * @param propObj Indicates the argument that fills the object (range) of the object property (required)
 * @param forms The set of other forms
 */
case class RelationalNoun(val lemma : String, 
                          val sense : URI = null, 
                          val propSubj : Arg = Subject, 
                          val propObj : Arg, 
                          val forms : Seq[Form] = Nil) extends Noun[RelationalNoun] {
   protected def makeWithForm(inflForm : Form) = RelationalNoun(lemma,sense,propSubj,propObj,forms :+ inflForm)
   protected def senseXML(namer : URINamer) = {
     val subjURI = namer("noun",lemma,Some("subject"))
     val objURI = namer("noun",lemma,Some("adpositionalObject"))
     <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("noun",lemma,Some("sense"))}>
         <lemon:reference>
           <rdf:Property rdf:about={sense}/>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
            <lemon:Argument rdf:about={objURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("noun",lemma,Some("frame"))}>
        { (propSubj,propObj) match {
          case (Subject,o : AdpositionalObject) => <rdf:type rdf:resource={lexinfo("NounPPFrame")}/>
            case (o : AdpositionalObject, Subject) => <rdf:type rdf:resource={lexinfo("NounPPFrame")}/>
            case _ => <!--Unrecognised frame-->
           }
        }
        { propSubj.toXML(subjURI,namer) }
        { propObj.toXML(objURI,namer) }
      </lemon:Frame>
    </lemon:synBehavior>
   }
}
                              
/**
 * A noun representing a relationship with multiple arguments
 * @param form The canonical form of the noun (required)
 * @param relationClass The class of relations
 * @param args The argument structure (required)
 * @param forms The set of other forms
 */
case class RelationalMultivalentNoun(val lemma : String,
                                     val relationClass : URI = null,
                                     val args : Seq[OntologyFrameElement],
                                     val forms : Seq[Form] = Nil)extends Noun[RelationalMultivalentNoun] {
   protected def makeWithForm(form : Form) = RelationalMultivalentNoun(lemma,relationClass,args,forms :+ form)
   protected def senseXML(namer : URINamer) = {
     val argURI = (for((arg,i) <- args.zipWithIndex) yield {
       arg -> namer("noun",lemma,Some("arg"+(i+1)))
     }).toMap
     <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("noun",lemma,Some("sense"))}>
         <lemon:reference>
           <rdfs:Class rdf:about={relationClass}>
             <rdfs:subClassOf rdf:resource="http://www.lemon-model.net/oils#Relationship"/>
           </rdfs:Class>
         </lemon:reference>
         {
           for((arg,i) <- args.zipWithIndex) yield {
            <lemon:subsense>
              <lemon:LexicalSense rdf:about={namer("noun",lemma,Some("subsense"+(i+1)))}>
                <lemon:semArg>
                  <lemon:Argument rdf:about={argURI(arg)}/>
                  {
                    if(arg.isOptional) {
                      <lemon:optional rdf:datatype="http://www.w3.org/2001/XMLSchema#boolean">true</lemon:optional>
                    } else {
                      <!-- Mandatory argument -->
                    }
                  }
                </lemon:semArg>
                <lemon:reference>
                  <rdf:Property rdf:about={arg.property}/>
                </lemon:reference>
              </lemon:LexicalSense>
            </lemon:subsense>
           }
         }
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("noun",lemma,Some("frame"))}>
        {
          for(arg <- args) yield {
            arg.arg.toXML(argURI(arg),namer)
          }
        }
      </lemon:Frame>
    </lemon:synBehavior>
   }
}

/**
 * A noun representing a relation that also defines a class
 * @param form The canonical form of the noun (required)
 * @param relationClass The class created by this relation
 * @param relation The property described by this relation
 * @param propSubj The subject of this property
 * @param propObj The object of the proprety (required)
 * @param forms The set of other forms
 **/
case class ClassRelationalNoun(val lemma : String,
                               val relationClass : URI = null,
                               val relation : URI = null,
                               val propSubj : Arg = Subject,
                               val propObj : Arg,
                               val forms : Seq[Form] = Nil) extends Noun[ClassRelationalNoun] {
   protected def makeWithForm(form : Form) = ClassRelationalNoun(lemma,relationClass,relation,propSubj,propObj,forms :+ form)
   protected def senseXML(namer : URINamer) = {
     val subjURI = namer("noun",lemma,Some("subject"))
     val objURI = namer("noun",lemma,Some("adpositionalObject"))
     <lemon:sense>
      <lemon:LexicalSense rdf:about={namer("noun",lemma,Some("senseRel"))}>
         <lemon:reference>
           <rdf:Property rdf:about={relation}/>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
            <lemon:Argument rdf:about={objURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
      <lemon:LexicalSense rdf:about={namer("noun",lemma,Some("senseClass"))}>
         <lemon:reference>
           <rdf:Property rdf:about={relationClass}/>
         </lemon:reference>
         <lemon:semArg>
            <lemon:Argument rdf:about={subjURI}/>
         </lemon:semArg>
       </lemon:LexicalSense>
    </lemon:sense> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("noun",lemma,Some("frame"))}>
        { (propSubj,propObj) match {
          case (Subject,o : AdpositionalObject) => <rdf:type rdf:resource={lexinfo("NounPPFrame")}/>
            case (o : AdpositionalObject, Subject) => <rdf:type rdf:resource={lexinfo("NounPPFrame")}/>
            case _ => <!--Unrecognised frame-->
           }
        }
        { propSubj.toXML(subjURI,namer) }
        { propObj.toXML(objURI,namer) }
      </lemon:Frame>
    </lemon:synBehavior> :+
    <lemon:synBehavior>
      <lemon:Frame rdf:about={namer("noun",lemma,Some("frame"))}>
        <rdf:type rdf:resource={lexinfo("NounPredicateFrame")}/>
        { propSubj.toXML(subjURI,namer) }
      </lemon:Frame>
    </lemon:synBehavior>
   }
} 
