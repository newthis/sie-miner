package sie.parser.java;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.Document;

/**
 * This class contains the methods necessary for the parser;
 *
 * @author Fabio Palomba;
 */

	public class AstParser {
		private char[] charClass;
		private CompilationUnit unit;
		private ASTParser parser;
		@SuppressWarnings("unused")
		private Document document;

				public AstParser(){					
				}
				
				public AstParser (String pClassToAnalyze) { 
					this.charClass=pClassToAnalyze.toCharArray();
				}	


				/**
				 * This method allows to create a parser for the AST;
				 * 
				 * @param 
				 * 			a String representation of a Class;
				 * @return
				 * 			a CompilationUnit to work on;
				 */
		 		public CompilationUnit createParser() {
		 			//document=new Document("\n");
		 			parser = ASTParser.newParser(AST.JLS4);
		 			parser.setKind(ASTParser.K_COMPILATION_UNIT);
		 			parser.setSource(this.charClass); // set source
		 			parser.setResolveBindings(true); // we need bindings later on
		 			parser.setBindingsRecovery(true);
		 			return (CompilationUnit) parser.createAST(null);
		 		}
		 		
		 		public CompilationUnit createParser(ICompilationUnit icu) {
		 			//document=new Document("\n");
		 			parser = ASTParser.newParser(AST.JLS4);
		 			parser.setKind(ASTParser.K_COMPILATION_UNIT);
		 			parser.setSource(icu); // set source
		 			parser.setResolveBindings(true); // we need bindings later on
		 			parser.setBindingsRecovery(true);
		 			return (CompilationUnit) parser.createAST(null);
		 		}
		 		
		 		public TypeDeclaration createParser(String pMethod, int pType) {
		 			parser = ASTParser.newParser(AST.JLS4);
		 			parser.setKind(pType);
		 			parser.setSource(pMethod.toCharArray()); // set source
		 			
		 			return (TypeDeclaration) parser.createAST(null);
		 		}
		 		
		 		public CompilationUnit createEmptyParser(){
		 			parser = ASTParser.newParser(AST.JLS4);
		 			parser.setSource(" ".toCharArray());		
		 			unit = (CompilationUnit) parser.createAST(null);
		 			unit.recordModifications();
		 			//AST ast = unit.getAST(); 
		 			return unit;
		 		}
		 		
		 		public CompilationUnit getCompilationUnit(){
		 			return this.unit;
		 		}
		 		
		 		
	}