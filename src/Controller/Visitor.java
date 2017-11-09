package Controller;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

public class Visitor extends ASTVisitor{

	ArrayList<ImportDeclaration> importlist=new ArrayList<>();
	ArrayList<CompilationUnit> classlist=new ArrayList<>();
	public Visitor() {}
	
	//imports
	@Override
	public boolean visit(ImportDeclaration node) {
		// TODO Auto-generated method stub
		importlist.add(node);
		return super.visit(node);
	}
	
	public ArrayList<ImportDeclaration> getImports(){
		return importlist;
	}
	
	//class declaration
	@Override
	public boolean visit(CompilationUnit node) {
		// TODO Auto-generated method stub
		classlist.add(node);
		return super.visit(node);
	}
	
	public ArrayList<CompilationUnit> getClasses(){
		return classlist;
	}

}
