package net.lemonmodel.patterns

import java.net.URI
import org.scalatest._
import org.scalatest.matchers._
import scala.xml._
import scala.xml.Utility._

class VerbTest extends FlatSpec with ShouldMatchers {
  
  val defaultNamer = new URINamer {
    private var dupes = new scala.collection.mutable.HashSet[String]()
    def apply(pos : String, form : String, element : Option[String] = None) = {
      val frag = (element match {
        case Some(elem) => {
          var f = "#" + elem
          var i = 2
          while(dupes contains f) {
            f = "#" + elem + i
            i = i + 1
          }
          dupes += f
          f
        }
        case None => ""
      })
      URI.create("file:example/"+form+"-"+pos+frag)
    }
    def auxiliaryEntry(form : String) = URI.create("file:example/"+form)
  }
  
  def xmlCheck(pattern : Pattern, xml : Elem, lang : String = "en") {
    trim(pattern.toXML(defaultNamer,lang)).toString() should equal (trim(xml).toString())
  }
  
  val ontology = Namespace("http://www.example.com/ontology#")
  
  "Tabling" should "generate correct number of elements" in {
    StateVerb("amare",ontology("loves")).withTable(lexinfo, Map(("tense","present") -> "amo")).forms.size should equal (1)
  }
  
  "The state verb pattern" should "produce valid lemon" in {
    xmlCheck(StateVerb("amare",ontology("loves")) withTable (lexinfo,Map(
      ("tense","present") -> Map(
        ("number","singular") -> Map(
          ("person","firstPerson") -> "amo",
          ("person","secondPerson") -> "amas",
          ("person","thirdPerson") -> "amat"
        ),
        ("number","plural") -> Map(
          ("person","firstPerson") -> "amamus",
          ("person","secondPerson") -> "amatis",
          ("person","thirdPerson") -> "amant"
        )
      )
    )),
    <lemon:LexicalEntry rdf:about="file:example/amare-verb">
         <lemon:canonicalForm>
           <lemon:LexicalForm rdf:about="file:example/amare-verb#canonicalForm">
             <lemon:writtenRep xml:lang="la">amare</lemon:writtenRep>
           </lemon:LexicalForm>
         </lemon:canonicalForm>
         <lexinfo:partOfSpeech rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#verb"></lexinfo:partOfSpeech>
         <lemon:otherForm>
           <lemon:LexicalForm rdf:about="file:example/amare-verb#form">
             <lemon:writtenRep xml:lang="la">amo</lemon:writtenRep>
             <lexinfo:tense rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#present" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:number rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#singular" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:person rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#firstPerson" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
           </lemon:LexicalForm>
         </lemon:otherForm>
         <lemon:otherForm>
           <lemon:LexicalForm rdf:about="file:example/amare-verb#form2">
             <lemon:writtenRep xml:lang="la">amas</lemon:writtenRep>
             <lexinfo:tense rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#present" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:number rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#singular" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:person rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#secondPerson" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
           </lemon:LexicalForm>
         </lemon:otherForm>
         <lemon:otherForm>
           <lemon:LexicalForm rdf:about="file:example/amare-verb#form3">
             <lemon:writtenRep xml:lang="la">amat</lemon:writtenRep>
             <lexinfo:tense rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#present" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:number rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#singular" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:person rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#thirdPerson" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
           </lemon:LexicalForm>
         </lemon:otherForm>
         <lemon:otherForm>
           <lemon:LexicalForm rdf:about="file:example/amare-verb#form4">
             <lemon:writtenRep xml:lang="la">amamus</lemon:writtenRep>
             <lexinfo:tense rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#present" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:number rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#plural" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:person rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#firstPerson" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
           </lemon:LexicalForm>
         </lemon:otherForm>
         <lemon:otherForm>
           <lemon:LexicalForm rdf:about="file:example/amare-verb#form5">
             <lemon:writtenRep xml:lang="la">amatis</lemon:writtenRep>
             <lexinfo:tense rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#present" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:number rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#plural" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:person rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#secondPerson" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
           </lemon:LexicalForm>
         </lemon:otherForm>
         <lemon:otherForm>
           <lemon:LexicalForm rdf:about="file:example/amare-verb#form6">
             <lemon:writtenRep xml:lang="la">amant</lemon:writtenRep>
             <lexinfo:tense rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#present" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:number rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#plural" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
             <lexinfo:person rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#thirdPerson" xmlns:lexinfo="http://lexinfo.net/ontology/2.0/lexinfo#"/>
           </lemon:LexicalForm>
         </lemon:otherForm>
         <lemon:sense>
           <lemon:LexicalSense rdf:about="file:example/amare-verb#sense">
             <lemon:reference>
               <rdf:Property rdf:about="http://www.example.com/ontology#loves"/>
             </lemon:reference>
             <lemon:semArg>
               <lemon:Argument rdf:about="file:example/amare-verb#subject"/>
               <lemon:Argument rdf:about="file:example/amare-verb#object"/>
             </lemon:semArg>
           </lemon:LexicalSense>
         </lemon:sense>
         <lemon:synBehavior>
           <lemon:Frame rdf:about="file:example/amare-verb#frame">
             <rdf:type rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame"/>
             <lexinfo:subject rdf:resource="file:example/amare-verb#subject"/>
             <lexinfo:directObject rdf:resource="file:example/amare-verb#object"/>
           </lemon:Frame>
         </lemon:synBehavior>
       </lemon:LexicalEntry>,"la")
  }
  
  "The event verb frame" should "produce valid lemon" in {
    
    xmlCheck(AchievementVerb("give",ontology("GivingEvent"),
         args=Seq(ontology("giver") as Subject,
                  ontology("recipient") as DirectObject,
                  ontology("givenObject") as IndirectObject)),
       <lemon:LexicalEntry rdf:about="file:example/give-verb">
         <lemon:canonicalForm>
           <lemon:LexicalForm rdf:about="file:example/give-verb#canonicalForm2">
             <lemon:writtenRep xml:lang="en">give</lemon:writtenRep>
           </lemon:LexicalForm>
         </lemon:canonicalForm>
         <lexinfo:partOfSpeech rdf:resource="http://lexinfo.net/ontology/2.0/lexinfo#verb"></lexinfo:partOfSpeech>
         <lemon:sense>
           <lemon:LexicalSense rdf:about="file:example/give-verb#sense2">
             <lemon:reference>
               <rdfs:Class rdf:about="http://www.example.com/ontology#GivingEvent">
                 <rdfs:subClassOf rdf:resource="http://www.lemon-model.net/oils#Achievement"/>
               </rdfs:Class>
             </lemon:reference>
             <lemon:subsense>
               <lemon:LexicalSense rdf:about="file:example/give-verb#subsense1">
                 <lemon:semArg>
                   <lemon:Argument rdf:about="file:example/give-verb#arg1"/>
                   <!-- Mandatory argument -->
                 </lemon:semArg>
                 <lemon:reference>
                   <rdf:Property rdf:about="http://www.example.com/ontology#giver"/>
                 </lemon:reference>
               </lemon:LexicalSense>
             </lemon:subsense>
             <lemon:subsense>
               <lemon:LexicalSense rdf:about="file:example/give-verb#subsense2">
                 <lemon:semArg>
                   <lemon:Argument rdf:about="file:example/give-verb#arg2"/>
                   <!-- Mandatory argument -->
                 </lemon:semArg>
                 <lemon:reference>
                   <rdf:Property rdf:about="http://www.example.com/ontology#recipient"/>
                 </lemon:reference>
               </lemon:LexicalSense>
             </lemon:subsense>
             <lemon:subsense>
               <lemon:LexicalSense rdf:about="file:example/give-verb#subsense3">
                 <lemon:semArg>
                   <lemon:Argument rdf:about="file:example/give-verb#arg3"/>
                   <!-- Mandatory argument -->
                 </lemon:semArg>
                 <lemon:reference>
                   <rdf:Property rdf:about="http://www.example.com/ontology#givenObject"/>
                 </lemon:reference>
               </lemon:LexicalSense>
             </lemon:subsense>
           </lemon:LexicalSense>
         </lemon:sense>
         <lemon:synBehavior>
           <lemon:Frame rdf:about="file:example/give-verb#frame2">
             <lexinfo:subject rdf:resource="file:example/give-verb#arg1"/>
             <lexinfo:directObject rdf:resource="file:example/give-verb#arg2"/>
             <lexinfo:indirectObject rdf:resource="file:example/give-verb#arg3"/>
           </lemon:Frame>
         </lemon:synBehavior>
       </lemon:LexicalEntry>)
  }
}
