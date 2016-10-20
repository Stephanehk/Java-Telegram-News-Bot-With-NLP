package newsbot;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.*;
import edu.stanford.nlp.util.*;

import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.TreebankLangParserParams;
import edu.stanford.nlp.parser.lexparser.EnglishTreebankParserParams;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;


public class TreeExample {
	public static void main(String[] args) throws IOException {
	PrintWriter xmlOut = new PrintWriter("xmlOutput.xml");	
	Properties props = new Properties();
	props.setProperty("annotators","tokenize, ssplit, pos, lemma, ner, parse");
	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	Annotation annotation = new Annotation("Whats going on with the New York City Bombings");
	
	pipeline.annotate(annotation);
	pipeline.xmlPrint(annotation, xmlOut);
	
	List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	if (sentences != null && sentences.size() > 0) {
		CoreMap sentence = sentences.get(0);
		Tree tree = sentence.get(TreeAnnotation.class);
		PrintWriter out = new PrintWriter(System.out);
		out.println("The first sentence parsed is: ");
		tree.pennPrint(out);
		
		
		
		
		
	    Tree tree1 = Tree.valueOf(tree.pennString());

	    // This creates English uncollapsed dependencies as a
	    // SemanticGraph.  If you are creating many SemanticGraphs, you
	    // should use a GrammaticalStructureFactory and use it to generate
	    // the intermediate GrammaticalStructure instead
	    SemanticGraph graph = SemanticGraphFactory.generateUncollapsedDependencies(tree1);

	    // Alternatively, this could have been the Chinese params or any
	    // other language supported.  As of 2014, only English and Chinese
//	    TreebankLangParserParams params = new EnglishTreebankParserParams();
//	    GrammaticalStructureFactory gsf = params.treebankLanguagePack().grammaticalStructureFactory(params.treebankLanguagePack().punctuationWordRejectFilter(), params.typedDependencyHeadFinder());
//
//	    GrammaticalStructure gs = gsf.newGrammaticalStructure(tree1);

	    System.err.println(graph);

	    SemgrexPattern semgrex = SemgrexPattern.compile("{}=A <<nsubj {}=B");
	    SemgrexMatcher matcher = semgrex.matcher(graph);
	    // This will produce two results on the given tree: "likes" is an
	    // ancestor of both "dog" and "my" via the nsubj relation
	    while (matcher.find()) {
	      System.err.println(matcher.getNode("A") + " <<nsubj " + matcher.getNode("B"));
	    }
		
	}
	}

}
